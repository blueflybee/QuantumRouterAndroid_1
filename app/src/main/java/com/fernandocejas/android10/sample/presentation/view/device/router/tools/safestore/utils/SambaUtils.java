package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.utils.DownloadCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.Downloader;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileUploadBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.LoadInfo;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.MyProgressDialog;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.MyProgressDialogForPreview;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;
import com.google.android.sambadocumentsprovider.provider.ByteBufferPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 作者：Jole on 2017/6/5
 * 邮箱：18826125375@163.com
 */

public class SambaUtils {
  public static SmbClient mClient;
  public static boolean mUserPause = false,mIOException = false;//判断是否发生IO异常
  //public static int mUploadIOException = 0;//记录发生异常的次数
  public static int mUploadingIndex = 0,mDownloadingIndex = 0; //索引传输下标 最大支持三个，完成之后启动下一个
  public static int errorNum;
  public static String mUserPauseUploadUrl = null,mUserPauseDownloadUrl = null; //记录用户操作时暂停的第一个url


  public static void putCacheCount(int count){
    //存到缓存中
    SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
    spUtils.put(PrefConstant.SAMBA_CACHE_NUM, count);
  }

  public static int getCacheCount(){
    //获取
    return new SPUtils(PrefConstant.SP_NAME).getInt(PrefConstant.SAMBA_CACHE_NUM);
  }

  public SambaUtils() {
  }

  /**
   * 功能: 删除文件
   *
   * @param:
   * @return:
   */
  public static boolean deleteSmbFiles(SmbClient mClient, final String path, Boolean isFolder) {
    try {
      // 删除文件夹：空文件夹可直接删除，非空文件夹先遍历删除子项之后再进行文件夹删除

      if (isFolder) {
        mClient.rmdir(path.toString());
      } else {
        mClient.unlink(path.toString());
      }
      return true;
    } catch (FileNotFoundException e) {
      SambaUtils.errorNum = 0x01;//文件不存在
      e.printStackTrace();
    } catch (IOException e) {
      SambaUtils.errorNum = 0x02;//文件被操作
      e.printStackTrace();
    }
    return false;
  }

  public static String getSambaErrorMsg(){
    String errorMsg = "";
    switch (SambaUtils.errorNum){
      case 0x01:
        errorMsg = "文件已被删除";
        break;

      case 0x02:
        errorMsg = "文件正在被操作，无法删除";
        break;

      default:
        errorMsg = "操作发生异常，请重试";
        break;
    }
    return errorMsg;
  }

  /**
   * 文件预览
   *
   * @param
   * @return
   */
  public static class DownloadTaskForPreview extends AsyncTask<String, Integer, Integer> {
    private MyProgressDialogForPreview mDialog;
    private SmbClient smbClient;
    private Context context;
    private String remotePath;
    Boolean isSuccessed = false;

    public DownloadTaskForPreview(SmbClient smbClient, String remotePath,Context context) {
      this.smbClient = smbClient;
      this.remotePath = remotePath;
      this.context = context;
    }

    @Override
    protected void onPreExecute() {
      mDialog = new MyProgressDialogForPreview(context, this);
      mDialog.setMessage("正在加载中……");
      mDialog.show();
    }

    @Override
    protected Integer doInBackground(String... params) {

      try {
        //smb://192.168.1.1/qtec/xxx.mp4
        DocumentMetadata documentMetadata = DocumentMetadata.fromUri(Uri.parse(remotePath),smbClient);
        String fileName = documentMetadata.getDisplayName();
        SambaUtils.download(smbClient,remotePath,UploadUtils.genLocalPath(fileName, AppConstant.SAMBA_DOWNLOAD_CACHE_PATH), context, mDialog);
        isSuccessed = true;
      } catch (Exception e) {
        isSuccessed = false;
        e.printStackTrace();
      }

      return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
      if(isSuccessed){
        mDialog.dismiss();
        int count = SambaUtils.getCacheCount();
        count++;
        //存到缓存中
        SambaUtils.putCacheCount(count);

        System.out.println("文件预览: = " + "文件预览成功!");
        System.out.println("缓存文件 cacheCount= "+SambaUtils.getCacheCount());
      }else {
        if(mDialog != null){
          mDialog.dismiss();
        }
        Toast.makeText(context, "预览失败，请检查文件是否存在", Toast.LENGTH_SHORT).show();
      }
    }
  }

  /**
   * 功能: 重命名
   *
   * @param:
   * @return:
   */
  public static boolean renameSmbFiles(SmbClient mClient, String path, String newName, Boolean isFolder) throws Exception {
    String fileSuffix = "";
    String newPath = "";
    try {
      if (isFolder) {
        //文件夹
        newPath = path.substring(0, path.lastIndexOf("/")) + File.separator + newName;
      } else {
        fileSuffix = path.substring(path.lastIndexOf("."));//获取文件的后缀名,带. 如.png
        newPath = path.substring(0, path.lastIndexOf("/")) + File.separator + newName + fileSuffix;
      }

      mClient.rename(path, newPath);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 功能: 移动文件
   *
   * @param:
   * @return:
   */
  public static boolean removeSmbFiles(final SmbClient mClient,final String movePath, final String currentPath) {
    try {
      System.out.println("移动文件 movePath = " + movePath);
      System.out.println("移动文件 currentPath = " + currentPath);

      /*String moveFileName = DocumentMetadata.fromUri(Uri.parse(movePath),mClient).getDisplayName();*/

      String moveFileName = movePath.substring(movePath.lastIndexOf("/")+1,movePath.length());

      String newPath = currentPath + moveFileName;

      System.out.println("移动文件 newPath = " + newPath);

      mClient.rename(movePath, newPath);

      return true;
    } catch (FileNotFoundException e) {
      System.out.println("移动文件11 文件不存在");
      SambaUtils.errorNum = 0x01;//文件不存在
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("移动文件11 发生IO异常");
    }
    return false;
  }

  /**
   * 功能: 下载文件
   *
   * @param:
   * @return:
   */
  public static boolean download(SmbClient mClient, final String remotePath, final String localPath,final Context context,MyProgressDialogForPreview dialog) throws Exception {
    final File localFile = new File(localPath);
    int length;
    long uploaded = 0;
    FileOutputStream outS = null;
    com.google.android.sambadocumentsprovider.nativefacade.SmbFile file = null;
    ByteBuffer mBuffer = null;
    try {
      outS = new FileOutputStream(localFile);
      file = mClient.openFile(remotePath, "r");
      mBuffer = new ByteBufferPool().obtainBuffer();

      byte[] buf = new byte[mBuffer.capacity()];

      while ((length = file.read(mBuffer, Integer.MAX_VALUE)) > 0) {
        mBuffer.get(buf, 0, length);
        outS.write(buf, 0, length);
        outS.flush();
        uploaded += length;
        mBuffer.clear();
        System.out.println("下载 downloaded = " + uploaded+" fileSize = "+localFile.length());
      }

      //下载完打开文件
      openFile(context, localFile);

      return true;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (outS != null) {
          outS.close();
        }
        if(file != null){
          file.close();
        }
        if(mBuffer != null){
          mBuffer = null;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  public final static void writeAndCloseStream(InputStream ins, OutputStream outs, final long totalSize, File localFile, Boolean isDownLoad,String localPath,Context context) throws IOException {
    byte[] tmp = new byte[2 * 1024 * 1024];//默认传输1M
    int length;
    long uploaded = 0;
    long mLastCompleteSize = 0;
    long mLastTime = 0;
    Boolean isLast = true;

    if (PrefConstant.getPictureRestoreState() && !isDownLoad) {
      GlobleConstant.gPictureTotalSize = totalSize;
    }

    try {
      while ((length = ins.read(tmp)) != -1) {
        outs.write(tmp, 0, length);
        outs.flush();
        uploaded += length;

        System.out.println("预览下载 totalSize = " + totalSize);
        System.out.println("预览下载 uploaded = " + uploaded);

        if (PrefConstant.getPictureRestoreState() && !isDownLoad) {
          GlobleConstant.gPictureUploadedSize = uploaded;

          //计算网速
          if (isLast) {
            GlobleConstant.gPictureUploadSpeed = SambaUtils.bytes2kb((uploaded - mLastCompleteSize) * 1000 / (System.currentTimeMillis() - mLastTime));

            isLast = false;
            mLastTime = System.currentTimeMillis();
            mLastCompleteSize = uploaded;
          }else {
            GlobleConstant.gPictureUploadSpeed = SambaUtils.bytes2kb((uploaded - mLastCompleteSize) * 1000 / (System.currentTimeMillis() - mLastTime));

            isLast = true;
            mLastCompleteSize = uploaded;
            mLastTime = System.currentTimeMillis();
          }

          System.out.println("网速 图片备份网速 GlobleConstant.gPictureUploadSpeed = " + GlobleConstant.gPictureUploadSpeed);

        }
      }

      if((ins.read(tmp)) == -1){
        if (PrefConstant.getPictureRestoreState() && !isDownLoad) {
          //把已上传完的Url备份到缓存中

          int count = GlobleConstant.getgPictureRestoredCount() + 1;
          GlobleConstant.setgPictureRestoredCount(count);

          System.out.println("图片备份：已上传照片数=" + GlobleConstant.getgPictureRestoredCount()+"  url:"+localPath);

          int index = GlobleConstant.gPictureRestoreIndex + 1;
          GlobleConstant.gPictureRestoreIndex = index;

          //上传成功的url记录下来
          new DatabaseUtil(context).saveRestoredPath(localPath);

          GlobleConstant.isLastPicRestored = true;

        }
      }

    } catch (Exception e) {

      if(PrefConstant.getPictureRestoreState() && !isDownLoad){
        System.out.println("图片备份11：上传发生异常 导致暂停 isStopRestorePicture = true");
        GlobleConstant.isLastPicRestored = true;
        GlobleConstant.isStopRestorePicture = false;

        UploadUtils.stopPicRestoreTimer();
      }

      throw e;
    } finally {
      if (ins != null) {
        ins.close();
      }

      if (outs != null) {
        outs.flush();
        outs.close();
      }

    }
  }

  /**
   * 判断是否是samba路径
   *
   * @param
   * @return
   */
  public final static String wrapSmbFileUrl(String parent, String name) {
    if (TextUtils.isEmpty(parent) || TextUtils.isEmpty(name)) {
      return null;
    }
    StringBuilder builder = new StringBuilder(parent);
    if (!parent.endsWith("/")) {
      builder.append("/");
    }
    if (name.endsWith("/")) {
      int index = name.length() - 1;
      name = name.substring(0, index);
    }
    builder.append(name);
    return builder.toString();
  }

  /**
   * 按名称排序
   *
   * @param
   * @return
   */
  public static void FileSortByName(List<FileBean> fileBeans) throws Exception {
    Collections.sort(fileBeans, new Comparator<FileBean>() {
      @Override
      public int compare(FileBean o1, FileBean o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
      }
    });
  }

  /**
   * 按时间排序
   *StructStat stat = smbClient.stat(directoryFile.get(i).toString());
   * @param
   * @return
   */
  public static void FileSortByTime(List<FileBean> fileBeans) throws Exception {

    Collections.sort(fileBeans, new Comparator<FileBean>() {

      public int compare(FileBean bean1, FileBean bean2) {
        long diff = 0;
        try {
          diff = bean1.getLongDate() - bean2.getLongDate();
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (diff > 0)
          return -1;
        else if (diff == 0)
          /*return 0;*/
          return bean1.getName().compareToIgnoreCase(bean2.getName());
        else
          return 1;
      }

      public boolean equals(Object obj) {
        return true;
      }

    });
  }

  public static void FileSortDefault(List<FileBean> fileBeans) throws Exception {
    Collections.sort(fileBeans, new Comparator<FileBean>() {
      @Override
      public int compare(FileBean o1, FileBean o2) {
        Boolean o1Type = false,o2Type = false;

        o1Type = o1.getFileType();
        o2Type = o2.getFileType();

        int result = o1Type.compareTo(o2Type);

        if(result == 0){
          return o1.getName().compareToIgnoreCase(o2.getName());
        }else if(result > 0){
          return -1;
        }else {
          return 1;
        }

   /*     try {
          o1Type = o1.getFileType();
          o2Type = o2.getFileType();

          if (o1Type && !o2Type)
            return -1;

          if (!o1Type && o2Type)
            return 1;
        } catch (Exception e) {
          e.printStackTrace();
        }*/
        /*return 0;*/
      }
    });
  }

  /**
   * @param bytes
   * @return
   * @Description long转文件大小M单位方法
   * @author temdy
   */
  public final static String bytes2kb(long bytes) {
    DecimalFormat df = new DecimalFormat("#.00");
    String fileSizeString = "";
    if (bytes < 1024) {
      fileSizeString = df.format((double) bytes) + "B";
    } else if (bytes < 1048576) {
      fileSizeString = df.format((double) bytes / 1024) + "K";
    } else if (bytes < 1073741824) {
      fileSizeString = df.format((double) bytes / 1048576) + "M";
    } else {
      fileSizeString = df.format((double) bytes / 1073741824) + "G";
    }
    return fileSizeString;
  }

  public static void openFile(Context context, File file) {
/*    if (file.isDirectory()) {
      initFileListInfo(file.getPath());
    } else */
    {
      if (file.getName().lastIndexOf(".") == -1) {// 无后缀文件不可读
        //OtherHelp.ErrorProcess(ctx);
        Toast.makeText(context, "文件不可读", Toast.LENGTH_SHORT).show();
      } else {
        InputUtil.requestPermissions(context);//获取权限

        String type = getMIMEType(file.getName());
        if (type.length() == 0) {
          Toast.makeText(context, "不支持该格式", Toast.LENGTH_SHORT).show();
        } else {
          //适配andorid N > 7.0
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            // 设置当前文件类型
            System.out.println("context 打开 = " + context);
            System.out.println("file 打开 = " + file.getName());
            intent.setDataAndType(FileProvider.getUriForFile(context.getApplicationContext(), context.getApplicationContext().getPackageName() + ".fileprovider", file), type);
            context.startActivity(intent);
          } else {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            // 设置当前文件类型
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
          }
        }
      }
    }
  }

  /**
   * 获得MIME类型的方法
   */
  private static String getMIMEType(String fileName) {
    String type = "";
    // 取出文件后缀名并转成小写
    String fileEnds = fileName.substring(fileName.lastIndexOf(".") + 1,
        fileName.length()).toLowerCase();
    for (int i = 0; i < Const.MIME_MapTable.length; i++) {
      if (fileEnds.equals(Const.MIME_MapTable[i][0]))
        type = Const.MIME_MapTable[i][1];
    }
    return type;
  }

  /**
   * 新建文件夹
   *
   * @param
   * @return
   */
  public static boolean createSambaFolder(SmbClient mClient, final String path, final String name) {
    try {
      if("/".equals(path.substring(path.length()-1))){
        //判断path是否以/结尾
        System.out.println("新建文件夹 = 以/结尾");
        mClient.mkdir(path + name);
      }else {
        System.out.println("新建文件夹 = 不以/结尾");
        mClient.mkdir(path + File.separator + name);
      }

      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 下载
   *
   * @param
   * @return
   */
  public static class DownloadTask extends AsyncTask<String, Integer, LoadInfo> {
    Downloader downloader = null;
    View v = null;
    String urlstr = null;
    String localfile = null;
    Map<String, Downloader> downloaders = null;
    static Context mContext;
    SmbClient mClient;
    Handler mHandler;

    public DownloadTask( Map<String, Downloader> downloaders, Context context,SmbClient mClient,Handler mHandler) {
      this.downloaders = downloaders;
      this.mContext = context;
      this.mClient = mClient;
      this.mHandler = mHandler;
    }

    @Override
    protected void onPreExecute() {
      SambaUtils.mDownloadingIndex++;

      System.out.println("SambaUtils.mDownloadingIndex = " + SambaUtils.mDownloadingIndex);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      System.out.println("values = " + values);
      super.onProgressUpdate(values);
    }

    @Override
    protected LoadInfo doInBackground(String... params) {
      urlstr = params[0];
      localfile = params[1];
      int threadcount = Integer.parseInt(params[2]);
      // 初始化一个downloader下载器
      downloader = downloaders.get(urlstr);
      if (downloader == null) {
        downloader = new Downloader(urlstr,localfile, threadcount);
        downloaders.put(urlstr, downloader);
        DownloadCacheManager.putDownloader(mContext,urlstr,downloader);//写到文件
        /*((AndroidApplication) mContext.getApplicationContext()).getDBUtil().insertloadersKey(urlstr);*/
      }else {
        downloader.setState(Downloader.DOWNLOADING);
      }
      // 得到下载信息类的个数组成集合
      return downloader.getDownloaderInfors(downloader,mContext,mClient);
    }

    @Override
    protected void onPostExecute(LoadInfo loadInfo) {
      if (loadInfo != null) {
        // 调用方法开始下载
        downloader.download(loadInfo.getFileSize(), downloaders,mContext,mClient,mHandler);
      }
    }
  }

  /**
   * 上传
   *
   * @param
   * @return
   */
  public static class UploadTask extends AsyncTask<String, Integer, LoadInfo> {
    Downloader downloader = null;
    String urlstr = null;
    Map<String, Downloader> downloaders = null;
    static Context mContext;
    SmbClient mClient;
    Handler mHandler;

    public UploadTask(Map<String, Downloader> downloaders,Context context, SmbClient mClient,Handler mHandler) {
      this.downloaders = downloaders;
      this.mContext = context;
      this.mClient = mClient;
      this.mHandler = mHandler;
    }

    @Override
    protected void onPreExecute() {
      SambaUtils.mUploadingIndex++;

      System.out.println("SambaUtils.mUploadingIndex = " + SambaUtils.mUploadingIndex);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
    }

    @Override
    protected LoadInfo doInBackground(String... params) {
      urlstr = params[0];
      String localfile = params[1];//为了首页不显示还未传输完的文件，加一个标志
      System.out.println("上传 UploadTask  urlstr = " + urlstr+" localfile = "+localfile);
      int threadcount = Integer.parseInt(params[2]);
      // 初始化一个downloader下载器
      downloader = downloaders.get(urlstr);
      if (downloader == null) {
        downloader = new Downloader(urlstr,localfile, threadcount);
        downloaders.put(urlstr, downloader);
        UploadCacheManager.putUploader(mContext,urlstr,downloader);//写到文件
      }else {
        downloader.setState(Downloader.DOWNLOADING);
      }

      return downloader.getUploaderInfors(downloader,mContext,mClient);
    }

    @Override
    protected void onPostExecute(LoadInfo loadInfo) {
      if (loadInfo != null) {
        // 调用方法开始上传
        downloader.upload(loadInfo.getFileSize(), downloaders,mContext,mClient,mHandler);
      }
    }
  }

  /**
   * 响应暂停下载按钮的点击事件
   */
  public static void pauseDownload(String url, Map<String, Downloader> downloaders) {
    if(downloaders.get(url) != null){
      downloaders.get(url).pause();
    }
  }

  public static void waitingDownload(String url, Map<String, Downloader> downloaders) {
    if(downloaders.get(url) != null){
      downloaders.get(url).waiting();
    }
  }

  /**
   * 获取传输状态
   *
   * @param
   * @return
   */
  public static int getTransmitingState(String url, Map<String, Downloader> downloaders) {
    return downloaders.get(url).getState();
  }

  public final static String getSystemDate() {
    // 完整显示日期时间
    String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(new Date());
    return str;
  }

  /**
   * 去除重复的元素 冒泡排序，后续待优化，备选方案：hashSet
   *
   * @param
   * @return
   */
  public static List<FileUploadBean> removeDuplicateData(List<FileUploadBean> beans) {
    for (int i = 0; i < beans.size()-1; i++) {
      for (int j = beans.size()-1; j > i; j--) {
        if (beans.get(j).getPath().equals(beans.get(i).getPath())) {
          beans.remove(j);
        }
      }
    }
    return beans;
  }

  public static MyProgressDialog showDialog(Context context, String str){
    MyProgressDialog mDialog = null;
    if(mDialog == null){
      mDialog = new MyProgressDialog(context);
    }

    mDialog.setMessage(str);
    mDialog.show();

    return mDialog;
  }

  public static void hideDialog(MyProgressDialog mDialog){
    if(mDialog != null){
      mDialog.dismiss();
    }
  }

}
