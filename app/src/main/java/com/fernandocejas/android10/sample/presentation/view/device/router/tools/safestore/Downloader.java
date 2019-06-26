package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.utils.DownloadCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.NotifyPictureSystemUtil;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;
import com.google.android.sambadocumentsprovider.nativefacade.SmbFile;
import com.google.android.sambadocumentsprovider.provider.ByteBufferPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Downloader implements Serializable {
  public static final int DOWNLOAD_FINISHED = 1;
  public static final int DOWNLOAD_LOADING = 2;
  public static final int DOWNLOAD_USER_PAUSE = 3;
  private String urlstr;// 下载的地址
  private String localfile;// 保存路径
  private String name;
  private int threadcount;// 线程数
  private long fileSize;// 所要下载的文件的大小
  private long compeleteSize;//完成度
  private List<DownloadInfo> infos;// 存放下载信息类的集合
  public static final int INIT = 1;//定义三种下载的状态：初始化状态1，正在下载状态2，暂停状态3
  public static final int DOWNLOADING = 2;
  public static final int PAUSE = 3;
  public static final int WAITING = 4; // 等待
  public static final int FAILED = 5; // 传输失败
  public int state = INIT;

  public Downloader(String urlstr, String localfile, int threadcount) {
    this.urlstr = urlstr;
    this.localfile = localfile;
    this.threadcount = threadcount;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setUrlstr(String urlstr) {
    this.urlstr = urlstr;
  }

  public void setLocalfile(String localfile) {
    this.localfile = localfile;
  }

  public void setState(int state) {
    this.state = state;
  }

  public void setCompeleteSize(long compeleteSize) {
    this.compeleteSize = compeleteSize;
  }

  public long getCompeleteSize() {
    return compeleteSize;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }

  public String getUrlstr() {
    return urlstr;
  }

  public String getLocalfile() {
    return localfile;
  }

  public void setInfos(List<DownloadInfo> infos) {
    this.infos = infos;
  }

  public List<DownloadInfo> getInfos() {
    return infos;
  }



  /**
   * 判断是否正在下载
   */
  public boolean isdownloading() {
    return state == DOWNLOADING;
  }

  public boolean iswaiting() {
    return state == WAITING;
  }

  /**
   * 得到downloader里的信息
   * 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
   * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
   */
  public LoadInfo getDownloaderInfors(Downloader downloader, Context context, SmbClient mClient) {
    if (isFirst(urlstr, context)) {
      Log.v("TAG", "isFirst");
      init(downloader, context, mClient);
      int range = (int) (fileSize / threadcount);
      infos = new ArrayList<DownloadInfo>();
      for (int i = 0; i < threadcount - 1; i++) {
        DownloadInfo info = new DownloadInfo(i, i * range, (i + 1) * range - 1, 0, urlstr);
        infos.add(info);
      }
      DownloadInfo info = new DownloadInfo(threadcount - 1, (threadcount - 1) * range, (int) (fileSize - 1), 0, urlstr);
      infos.add(info);
      //保存infos中的数据到数据库
      Dao.getInstance(context).saveInfos(infos);

      //创建一个LoadInfo对象记载下载器的具体信息
      LoadInfo loadInfo = new LoadInfo(fileSize, 0, urlstr);
      return loadInfo;
    } else {
      //得到数据库中已有的urlstr的下载器的具体信息
      infos = Dao.getInstance(context).getInfos(urlstr);
      Log.v("TAG", "not isFirst size=" + infos.size());
      int size = 0;
      long compeleteSize = 0;
      for (DownloadInfo info : infos) {
        compeleteSize += info.getCompeleteSize();
        size += info.getEndPos() - info.getStartPos() + 1;
      }

      downloader.setCompeleteSize(compeleteSize);
      DownloadCacheManager.updateDownloaderInfos(context, urlstr, downloader);

      return new LoadInfo(size, compeleteSize, urlstr);
    }
  }

  public LoadInfo getUploaderInfors(Downloader downloader, Context context, SmbClient mClient) {
    if (isFirst(urlstr, context)) {
      Log.v("TAG", "isFirst");
      initUpload(downloader, mClient, context);
      int range = (int) (fileSize / threadcount);
      infos = new ArrayList<DownloadInfo>();
      for (int i = 0; i < threadcount - 1; i++) {
        DownloadInfo info = new DownloadInfo(i, i * range, (i + 1) * range - 1, 0, urlstr);
        infos.add(info);
      }
      DownloadInfo info = new DownloadInfo(threadcount - 1, (threadcount - 1) * range, (int) (fileSize - 1), 0, urlstr);
      infos.add(info);
      //保存infos中的数据到数据库
      Dao.getInstance(context).saveInfos(infos);
      //创建一个LoadInfo对象记载下载器的具体信息
      LoadInfo loadInfo = new LoadInfo(fileSize, 0, urlstr);
      return loadInfo;
    } else {
      //得到数据库中已有的urlstr的下载器的具体信息
      infos = Dao.getInstance(context).getInfos(urlstr);
      Log.v("TAG", "not isFirst size=" + infos.size());
      int size = 0;
      long compeleteSize = 0;
      for (DownloadInfo info : infos) {
        compeleteSize += info.getCompeleteSize();
        size += info.getEndPos() - info.getStartPos() + 1;
      }

      downloader.setCompeleteSize(compeleteSize);
      UploadCacheManager.updateUploaderInfos(context, urlstr, downloader);

      return new LoadInfo(size, compeleteSize, urlstr);
    }
  }

  /**
   * 初始化
   */
  private void init(Downloader downloader, Context context, SmbClient mClient) {
    try {

      fileSize = DocumentMetadata.fromUri(Uri.parse(urlstr), mClient).getSize();

      System.out.println("fileSizeDownload = " + fileSize);

      downloader.setCompeleteSize(0);
      downloader.setFileSize(fileSize);

      System.out.println("下载 初始化 fileSize = " + fileSize);

      DownloadCacheManager.updateDownloaderInfos(context, urlstr, downloader);

      File file = new File(localfile);
      if (!file.exists()) {
        file.createNewFile();
      }
      // 本地访问文件
      RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
      accessFile.setLength(fileSize);
      accessFile.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initUpload(Downloader downloader, SmbClient mClient, Context context) {
    try {
      File file = new File(urlstr);
      fileSize = file.length();

      mClient.createFile(localfile);

      downloader.setCompeleteSize(0);
      downloader.setFileSize(fileSize);

      UploadCacheManager.updateUploaderInfos(context, urlstr, downloader);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 判断是否是第一次 下载
   */
  private boolean isFirst(String urlstr, Context context) {
    return Dao.getInstance(context).isHasInfors(urlstr);
  }

  /**
   * 利用线程开始下载数据
   */
  public void download(long fileSize, Map<String, Downloader> downloaders, Context context, SmbClient mClient, Handler mHandler) {
    if (infos != null) {
 /*     if (state == DOWNLOADING)
        return;*/
      state = DOWNLOADING;

      for (DownloadInfo info : infos) {
        new MyThread(info.getThreadId(), info.getCompeleteSize(), fileSize,
            info.getUrl(), downloaders, context, mClient, mHandler).start();
      }
    }
  }

  public void upload(long fileSize, Map<String, Downloader> downloaders, Context context, SmbClient mClient, Handler mHandler) {
    if (infos != null) {

     /* if (state == DOWNLOADING)
        return;*/

      state = DOWNLOADING;

      for (DownloadInfo info : infos) {
        new UploadThread(info.getThreadId(), info.getCompeleteSize(), fileSize,
            info.getUrl(), downloaders, context, mClient, mHandler).start();
      }
    }
  }

  public class MyThread extends Thread {
    private int threadId;
    private long compeleteSize;
    private long fileSize;
    private String urlstr;
    private Context context;
    private Map<String, Downloader> downloaders;
    private SmbClient mClient;
    private Handler mHandler;
    private long lastCompleteSize,lastCompleteTime;
    private String loadingSpeed;

    public MyThread(int threadId, long compeleteSize, long fileSize, String urlstr, Map<String, Downloader> downloaders, Context context, SmbClient mClient, Handler mHandler) {
      this.threadId = threadId;
      this.fileSize = fileSize;
      this.compeleteSize = compeleteSize;
      this.urlstr = urlstr;
      this.downloaders = downloaders;
      this.context = context;
      this.mClient = mClient;
      this.mHandler = mHandler;
    }

    @Override
    public void run() {
      RandomAccessFile randomAccessFile = null;
      int length;
      ByteBuffer mBuffer = null;
      SmbFile remoteFile = null;

      try {
        mBuffer = new ByteBufferPool().obtainBuffer();
        byte[] buf = new byte[mBuffer.capacity()];

        remoteFile = mClient.openFile(urlstr, "rw");
        final File localFile1 = new File(localfile);

        randomAccessFile = new RandomAccessFile(localFile1, "rwd");

        randomAccessFile.seek(compeleteSize);
        remoteFile.seek(compeleteSize);

        if (downloaders.get(urlstr) != null) {
          downloaders.get(urlstr).setFileSize(fileSize);
          DownloadCacheManager.updateDownloaderInfos(context, urlstr, downloaders.get(urlstr));
        }

        while ((length = remoteFile.read(mBuffer, Integer.MAX_VALUE)) > 0) {

          if (SambaUtils.mUserPause) {
            System.out.println("SambaUtils.mUserPause 下载：用户操作暂停中 = " + SambaUtils.mUserPause);
            SambaUtils.mDownloadingIndex = 0;
            if (downloaders.get(urlstr) != null) {
              System.out.println("下载操作中 暂停 urlstr = " + urlstr);
              downloaders.get(urlstr).setState(Downloader.WAITING);
            }
            return;

            //用户暂停操作
            //设计思路：当用户操作时，保留第一个任务continue while循环，其他的任务都暂停掉，这样可解决卡顿问题
            //当用户停止操作时保留的循环操作完成之后会启动三个新的任务继续传输
   /*         if(TextUtils.isEmpty(SambaUtils.mUserPauseDownloadUrl )){
              SambaUtils.mUserPauseDownloadUrl = urlstr;
              SambaUtils.mDownloadingIndex = 1;
              System.out.println("下载操作中 保留 urlstr = " + urlstr);
            }else {
              if(!SambaUtils.mUserPauseDownloadUrl.equals(urlstr)){
                if (downloaders.get(urlstr) != null) {
                  System.out.println("下载操作中 暂停 urlstr = " + urlstr);
                  downloaders.get(urlstr).setState(Downloader.WAITING);
                }
                return;
              }
            }

            sleep(3000);
            mBuffer.clear();
            remoteFile.seek(compeleteSize);
            continue;*/
          }

          mBuffer.get(buf, 0, length);
          randomAccessFile.write(buf, 0, length);
          compeleteSize += length;

          mBuffer.clear();

          System.out.println("下载：进行中" + "size= " + fileSize + "  complete= " + compeleteSize + " url= " + urlstr + " localfile = " + localfile);

          if (downloaders.get(urlstr) != null) {
            downloaders.get(urlstr).setCompeleteSize(compeleteSize);
          }


          //计算网速
          loadingSpeed = SambaUtils.bytes2kb((compeleteSize - lastCompleteSize) * 1000 / (System.currentTimeMillis() - lastCompleteTime));
          lastCompleteSize = compeleteSize;
          lastCompleteTime = System.currentTimeMillis();

          sendHandMsg(mHandler, DOWNLOAD_LOADING, null, null,loadingSpeed,compeleteSize);

          // 更新数据库中的下载信息
          Dao.getInstance(context).updataInfos(threadId, compeleteSize, urlstr);

          // 用消息将下载信息传给进度条，对进度条进行更新
          DownloadCacheManager.updateDownloaderInfos(context, urlstr, downloaders.get(urlstr));

          if (state == PAUSE) {
            System.out.println("下载：暂停" + "size= " + fileSize + "  complete= " + compeleteSize + " url= " + urlstr);
            return;
          }
        }

        if(compeleteSize == fileSize){
          // 文件下载完成后先存入数据库，然后删除下载器的数据库记录
          compeleteSize = 0;

          FileUploadBean bean = new FileUploadBean();
          String fileName = DocumentMetadata.fromUri(Uri.parse(urlstr), mClient).getDisplayName();
          System.out.println("fileName = " + fileName + " urlStr = " + urlstr);
          bean.setName(fileName);
          bean.setDate(SambaUtils.getSystemDate());
          bean.setPath(localfile);
          AndroidApplication.getDBUtil().insert(bean);

          //通知相册
          NotifyPictureSystemUtil.sendNotifyToPictureSystem(context, localfile);

          System.out.println(urlstr + ":下载 = :" + "完成!");

          //下载完就删除key
          sendHandMsg(mHandler, DOWNLOAD_FINISHED, bean, null,null,0);
        }

        //下载成功后直接打开:可配置
        //openFile(context,localFile1);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        SambaUtils.errorNum = 0x01;
        sendHandMsg(mHandler, DOWNLOAD_FINISHED, null, "LOADING_EXCEPTION",null,0);
      }catch (IOException e){
        e.printStackTrace();
        sendHandMsg(mHandler, DOWNLOAD_FINISHED, null, "LOADING_EXCEPTION",null,0);
      } finally {
        try {
          if (randomAccessFile != null) {
            randomAccessFile.close();
            randomAccessFile = null;
          }
          if (remoteFile != null) {
            remoteFile.close();
            remoteFile = null;
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  public class UploadThread extends Thread {
    private int threadId;
    private long compeleteSize;
    private long fileSize;
    private String urlstr;
    private Handler mHandler;
    private Map<String, Downloader> downloaders;
    private Context context;
    private SmbClient mClient;
    private long lastCompleteSize,lastCompleteTime;
    private String loadingSpeed;
    Boolean isLoadingFinished = false;//判读是否下载完成

    public UploadThread(int threadId, long compeleteSize, long fileSize, String urlstr, Map<String, Downloader> downloaders, Context context, SmbClient mClient, Handler mHandler) {
      this.threadId = threadId;
      this.fileSize = fileSize;
      this.compeleteSize = compeleteSize;
      this.urlstr = urlstr;
      this.downloaders = downloaders;
      this.mHandler = mHandler;
      this.context = context;
      this.mClient = mClient;
    }

    @Override
    public void run() {
      RandomAccessFile randomAccessFile = null;
      SmbFile remoteFile1 = null;

      ByteBuffer mBuffer = new ByteBufferPool().obtainBuffer();
      byte[] buf = new byte[mBuffer.capacity()];

      try {
        // url 源地址 localfile 目标地址
        remoteFile1 = mClient.openFile(localfile, "rw");
        System.out.println("remoteFile1 = " + remoteFile1.getClass().getName());
        final File localFile = new File(urlstr);

        randomAccessFile = new RandomAccessFile(localFile, "r");

        randomAccessFile.seek(compeleteSize);
        remoteFile1.seek(compeleteSize);

        if (downloaders.get(urlstr) != null) {
          downloaders.get(urlstr).setFileSize(fileSize);
          UploadCacheManager.updateUploaderInfos(context, urlstr, downloaders.get(urlstr));
        }

        int length;
        while ((length = randomAccessFile.read(buf)) > 0) {

          if (SambaUtils.mUserPause) {

            SambaUtils.mUploadingIndex = 0;

            if (downloaders.get(urlstr) != null) {
              System.out.println("上传操作中 暂停 urlstr = " + urlstr);
              downloaders.get(urlstr).setState(Downloader.WAITING);
            }
            return;

           /* System.out.println("SambaUtils.mUserPause 上传：用户操作暂停中 = " + SambaUtils.mUserPause+" url = "+urlstr);

            if(TextUtils.isEmpty(SambaUtils.mUserPauseUploadUrl )){
              SambaUtils.mUserPauseUploadUrl = urlstr;
              SambaUtils.mUploadingIndex = 1;
              System.out.println("上传操作中 保留 urlstr = " + urlstr);
            }else {
              if(!SambaUtils.mUserPauseUploadUrl.equals(urlstr)){
                if (downloaders.get(urlstr) != null) {
                  System.out.println("上传操作中 暂停 urlstr = " + urlstr);
                  downloaders.get(urlstr).setState(Downloader.WAITING);
                }
                return;
              }
            }

            sleep(3000);
            randomAccessFile.seek(compeleteSize);
            lastCompleteSize = 0;
            lastCompleteTime = 0;
            continue;*/
          }

          System.out.println("SambaUtils.mUserPause 上传：用户操作暂停完成 继续传输 = " + SambaUtils.mUserPause+" url = "+urlstr);

          mBuffer.put(buf, 0, length);
          remoteFile1.write(mBuffer, length);
          compeleteSize += length;
          mBuffer.clear();

          System.out.println("上传：进行中" + "size= " + fileSize + "  complete= " + compeleteSize + " url= " + urlstr);

          // 用消息将下载信息传给进度条，对进度条进行更新
          if (downloaders.get(urlstr) != null) {
            downloaders.get(urlstr).setCompeleteSize(compeleteSize);
          }

          //计算网速
          loadingSpeed = SambaUtils.bytes2kb((compeleteSize - lastCompleteSize) * 1000 / (System.currentTimeMillis() - lastCompleteTime));
          lastCompleteSize = compeleteSize;
          lastCompleteTime = System.currentTimeMillis();

          sendHandMsg(mHandler, DOWNLOAD_LOADING, null, null,loadingSpeed,compeleteSize);

          // 更新数据库中的下载信息
          Dao.getInstance(context).updataInfos(threadId, compeleteSize, urlstr);

          UploadCacheManager.updateUploaderInfos(context, urlstr, downloaders.get(urlstr));

          if (state == PAUSE) {
            System.out.println("上传：暂停" + "size= " + fileSize + "  complete= " + compeleteSize + " url= " + urlstr);
            return;
          }

          if (state == WAITING) {
            System.out.println("上传：等待" + "size= " + fileSize + "  complete= " + compeleteSize + " url= " + urlstr);
            return;
          }
        }

        // 文件上传完成后先存入数据库，然后删除上传器的数据库记录
        if(compeleteSize == fileSize){

          compeleteSize = 0;
          isLoadingFinished = true;

          FileUploadBean bean = new FileUploadBean();
          String newName = DocumentMetadata.fromUri(Uri.parse(localfile), mClient).getDisplayName().replaceAll(".upload", "");//去掉.upload
          bean.setName(newName);
          bean.setDate(SambaUtils.getSystemDate());
          bean.setPath(localfile);

          AndroidApplication.getUploadDBUtil().insertUploadData(bean);

          System.out.println("上传文件 = :" + "完成!"+ "url = " + urlstr + " localfile = " + localfile+" completeSize = "+compeleteSize+" fileSize = "+fileSize);

          //上传完就删除key
          sendHandMsg(mHandler, DOWNLOAD_FINISHED, bean, null,null,0);
        }

        //下载成功后直接打开:可配置
        //openFile(context,localFile1);
      } catch (Exception e) {
        e.printStackTrace();

        if(e != null){
          System.out.println("上传文件异常类型 e.getClass().getName() = " + e.getClass().getName());
          if("java.io.IOException".equals(e.getClass().getName())){
            //发生IO异常 可能性为文件正在被操作或者磁盘空间已满
            SambaUtils.mIOException = true;
          }
        }
        System.out.println("上传文件 上传发生异常 url = " + urlstr + " localfile = " + localfile+" completeSize = "+compeleteSize+" fileSize = "+fileSize);
        System.out.println("上传文件 ====================================================================================================");
        sendHandMsg(mHandler, DOWNLOAD_FINISHED, null, "LOADING_EXCEPTION",null,0);
      } finally {
        try {
          if (remoteFile1 != null) {
            remoteFile1.close();
            remoteFile1 = null;
          }

          if (randomAccessFile != null) {
            randomAccessFile.close();
            randomAccessFile = null;
          }

          if (remoteFile1 == null && (isLoadingFinished)) {
            //上传完成把.upload重命名去掉 (path,newPath)
            isLoadingFinished = false;
            System.out.println("上传 UploadTask  before urlstr = " + urlstr + " localfile = " + localfile);
            if (localfile.indexOf(".upload") != -1) {
              //包含
              sleep(10);
              String newPath = localfile.replaceAll(".upload", "");//去掉.upload
              System.out.println("上传 UploadTask  arter urlstr = " + urlstr + " localfile newPath = " + newPath);
              mClient.rename(localfile, newPath);
            }
          }
        }catch (FileNotFoundException e){
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

  }

  private void sendHandMsg(Handler mHandler, int what, FileUploadBean bean, String str,String speed,long mCompleteSize) {
    if (mHandler != null) {
      Message message = Message.obtain();
      message.what = what;
      message.obj = urlstr;

      Bundle bundle = new Bundle();
      bundle.putSerializable("bean", bean);
      bundle.putString("LOADING_EXCEPTION", str);
      bundle.putString("LOADING_SPEED", speed);
      bundle.putLong("LOADING_COMPLETESIZE", mCompleteSize);
      message.setData(bundle);

      mHandler.sendMessage(message);
    }
  }

  //删除数据库中urlstr对应的下载器信息
  public void delete(String urlstr, Context context) {
    if (context != null) {
      Dao.getInstance(context).delete(urlstr);
    }
  }

  //设置暂停
  public void pause() {
    state = PAUSE;
  }

  //设置等待
  public void waiting() {
    state = WAITING;
  }

  //重置下载状态
  public void reset() {
    state = INIT;
  }

  public int getState() {
    return state;
  }
}