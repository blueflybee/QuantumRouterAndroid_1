package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileBean;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.LoginConfig;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;
import com.google.android.sambadocumentsprovider.nativefacade.SmbFile;
import com.google.android.sambadocumentsprovider.provider.ByteBufferPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/17
 *      desc: 本地文件上传工具包
 *      version: 1.0
 * </pre>
 */

public class UploadUtils {

  protected static Timer mTimer;
  protected static TimerTask mTimerTask;

  /**
   * 获取备份路径
   *
   * @param
   * @return
   */
  public static String getRestorePath() {
    return LoginConfig.LAN_REMOTE_ROOT_IP + "来自：" + DeviceUtils.getModel() + "/";
  }

  /**
   * 图片自动备份 定时器
   *
   * @param
   * @return
   */
  public final static void startPicRestoreTimer(SmbClient mClient, Context context) {
    if (mTimer == null) {
      mTimer = new Timer();
    }

    if (mTimerTask == null) {
      mTimerTask = new TimerTask() {
        @Override
        public void run() {
          System.out.println("图片备份定时器: startPicRestoreTimer");

          if (PrefConstant.getPictureRestoreState()) {

            if (GlobleConstant.isSambaExtranetAccess) {
              //外网
              stopPicRestoreTimer();//关掉定时器
              GlobleConstant.isLastPicRestored = true;
              GlobleConstant.isFinishedPicRestored = true;
              return;
            }

            if (GlobleConstant.isFinishedPicRestored) {
              // 下载完成
              stopPicRestoreTimer();//关掉定时器
              GlobleConstant.isLastPicRestored = true;
              GlobleConstant.isFinishedPicRestored = true;
              return;
            }

            if (GlobleConstant.getgPictureRestoredCount() <= GlobleConstant.getgPictureRestoredSumCount()) {
              GlobleConstant.isFinishedPicRestored = false;
              if (GlobleConstant.isLastPicRestored) {
                //上一次备份完成
                GlobleConstant.isLastPicRestored = false;

                new Thread(new Runnable() {
                  @Override
                  public void run() {
                    try {

                      if (GlobleConstant.gPictureRestoreIndex < GlobleConstant.getPicturePathList().size()) {
                        GlobleConstant.isFinishedPicRestored = false;
                        UploadUtils.upload(mClient, GlobleConstant.getPicturePathList().get(GlobleConstant.gPictureRestoreIndex), SambaUtils.wrapSmbFileUrl(getRestorePath(), new File(GlobleConstant.getPicturePathList().get(GlobleConstant.gPictureRestoreIndex)).getName()), context);
                      } else {
                        GlobleConstant.isFinishedPicRestored = true;
                      }

                    } catch (Exception e) {
                      e.printStackTrace();
                      System.out.println("图片备份11 new Run 异常 ");
                      if (PrefConstant.getPictureRestoreState()) {
                        GlobleConstant.isLastPicRestored = true;
                        GlobleConstant.isStopRestorePicture = false;
                        //跳过
                        // PrefConstant.putPictureRestoredCount(PrefConstant.getPictureRestoredCount()+1);//统计已备份的照片数
                      }
                    }
                  }
                }).start();
              }
            } else {
              stopPicRestoreTimer();//关掉定时器

              System.out.println("图片备份 定时器关闭  getgPictureRestoredCount()= " + GlobleConstant.getgPictureRestoredCount() + "  >  " + "getgPictureRestoredSumCount=" + GlobleConstant.getgPictureRestoredSumCount());

              GlobleConstant.isLastPicRestored = true;
              GlobleConstant.isFinishedPicRestored = true;
            }
          }
        }
      };
    }

    if (mTimer != null && mTimerTask != null) {

      try {
        mTimer.schedule(mTimerTask, 50, 200);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * 图片自动备份 定时器
   *
   * @param
   * @return
   */
  public final static void stopPicRestoreTimer() {
    if (mTimer != null) {
      mTimer.cancel();
      mTimer = null;
    }
    if (mTimerTask != null) {
      mTimerTask.cancel();
      mTimerTask = null;
    }

    System.out.println("图片备份定时器: stopPicRestoreTimer");
  }

  /**
   * 上传视频
   *
   * @param
   * @return
   */
  public final static boolean pickupVideo(Activity activity, int requestCode) {
    if (activity == null) {
      return false;
    }
    Intent intent = new Intent(Intent.ACTION_PICK);
    // Uri uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    // intent.setDataAndType(uri, "image/*");
    intent.setType("video/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    boolean available = isIntentAvailable(activity, intent);
    if (available) {
      activity.startActivityForResult(intent, requestCode);
    }
    return available;
  }

  /**
   * 上传音乐
   *
   * @param
   * @return
   */
  public final static boolean pickupMusic(Activity activity, int requestCode) {
    if (activity == null) {
      return false;
    }
    Intent intent = new Intent(Intent.ACTION_PICK);
    // Uri uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    // intent.setDataAndType(uri, "image/*");
    intent.setType("audio/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    boolean available = isIntentAvailable(activity, intent);
    if (available) {
      activity.startActivityForResult(intent, requestCode);
    }
    return available;
  }

  /**
   * 上传图片
   *
   * @param
   * @return
   */
  public final static boolean pickupImages(Activity activity, int requestCode) {
    if (activity == null) {
      return false;
    }
    Intent intent = new Intent(Intent.ACTION_PICK);
    // Uri uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    // intent.setDataAndType(uri, "image/*");
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    boolean available = isIntentAvailable(activity, intent);
    if (available) {
      activity.startActivityForResult(intent, requestCode);
    }
    return available;
  }

  public static final String genLocalPath(String smbFilePath, String pathType) {

    //File folder = new File(String.valueOf(Environment.getExternalStorageDirectory()) + File.separator + "samba/");
    /**
     * AppConstant.SAMBA_DOWNLOAD_CACHE_PATH 预览路径
     * AppConstant.SAMBA_DOWNLOAD_PATH 下载路径
     */

    File folder = new File(pathType);
    if (!folder.exists()) {
      folder.mkdirs();
    }
    String name = getFileName(smbFilePath);
    if (name == null) {
      name = String.valueOf(System.currentTimeMillis());
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append(folder.getAbsolutePath()).append("/").append(name).toString();
    File f = new File(buffer.toString());
    try {
      f.createNewFile();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return f.getAbsolutePath();
  }

  /**
   * 获取本地图片进行上传备份
   *
   * @param
   * @return
   */
  public static List<FileBean> getSystemPhotoList(Context context, List<FileBean> fileBean) {
    Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    ContentResolver contentResolver = context.getContentResolver();
    Cursor cursor = contentResolver.query(uri, null, null, null, null);
    if (cursor == null || cursor.getCount() <= 0) return null; // 没有图片
    while (cursor.moveToNext()) {
      int index = cursor
          .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      String path = cursor.getString(index); // 文件地址
      File file = new File(path);
      if (file.exists()) {
        FileBean bean = new FileBean();

        bean.setLastModifyTime(TimeUtils.millis2String(file.lastModified()));
        bean.setLongDate(file.lastModified());

        if (file.length() == 0) {
          //已损害的文件大小为0
          bean.setSize("0K");
        } else {
          bean.setSize(bytes2kb(file.length()));
        }

        bean.setPath(file.getPath());
        bean.setName(file.getName());
        bean.setFileType(false);

        fileBean.add(bean);
      }
    }

    return fileBean;
  }

  public final static String getFileName(String path) {
    if (path == null) {
      return path;
    }
    if (!path.contains("/")) {
      return path;
    }
    int index = path.lastIndexOf("/");
    if (index < 0 || index + 1 >= path.length()) {
      return path;
    }
    return path.substring(index + 1);
  }

  public final static boolean isIntentAvailable(Context context, Intent intent) {
    if (context == null || intent == null) {
      return false;
    }
    try {
      return listResolves(context, intent) != null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public final static List<ResolveInfo> listResolves(Context context, Intent intent) {
    PackageManager packageManager = context.getPackageManager();
    return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
  }

  // 对文件,文件夹进行排序，去掉.*,分成文件夹和文件两种，按名字排序
  public static File[] FileSort(File[] mFiles, List<FileBean> mLocalFileBeans) {

    File[] directoryFile = new File[mFiles.length];
    File[] otherFile = new File[mFiles.length];

    int directoryFileCount = 0; //目录文件
    int otherFileCount = 0;

    for (int i = 0; i < mFiles.length; i++) {
      //indexof String 匹配索引
      if (mFiles[i].getName().indexOf(".") != 0) {
        if (mFiles[i].isDirectory()) {//目录文件
          directoryFile[directoryFileCount] = mFiles[i];
          directoryFileCount++;
        } else {
          //过滤以.开头的无效文件
          if (!(mFiles[i].getName()).startsWith(".")) {
            otherFile[otherFileCount] = mFiles[i];
            otherFileCount++;
          }
        }
      }
    }

    //目录文件 复制
    File[] directoryFile2 = new File[directoryFileCount];
    System.arraycopy(directoryFile, 0, directoryFile2, 0,
        directoryFileCount);
    //文件 复制
    File[] otherFile2 = new File[otherFileCount];
    System.arraycopy(otherFile, 0, otherFile2, 0, otherFileCount);

    Arrays.sort(directoryFile2);
    Arrays.sort(otherFile2);

    File[] newFile = new File[directoryFileCount + otherFileCount];

    for (int i = 0; i < directoryFileCount + otherFileCount; i++) {
      FileBean bean = new FileBean();
      if (i < directoryFileCount) {
        newFile[i] = directoryFile2[i];

        bean.setLastModifyTime(TimeUtils.millis2String(newFile[i].lastModified()));
        bean.setPath(newFile[i].getPath());
        bean.setName(newFile[i].getName());
        bean.setSize(bytes2kb(newFile[i].length()));
        bean.setFileType(true);
        mLocalFileBeans.add(bean);
      } else {
        newFile[i] = otherFile2[i - directoryFileCount];

        bean.setLastModifyTime(TimeUtils.millis2String(newFile[i].lastModified()));

        if (newFile[i].length() == 0) {
          //已损害的文件大小为0
          bean.setSize("0K");
        } else {
          bean.setSize(bytes2kb(newFile[i].length()));
        }

        bean.setPath(newFile[i].getPath());
        bean.setName(newFile[i].getName());
        bean.setFileType(false);
        mLocalFileBeans.add(bean);
      }

    }
    return newFile;
  }

  /**
   * 删除本地文件
   *
   * @param
   * @return
   */
  public static void deleteLocalFile(String path) {
    File file = new File(path);

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      for (int i = 0; i < files.length; i++) {
        files[i].delete();
        System.out.println("\"删除文件!\" = " + "删除文件!");
      }

      SambaUtils.putCacheCount(0);
      System.out.println("缓存文件 cacheCount= "+SambaUtils.getCacheCount());
    }
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

  /**
   * 过滤出最近六个月的照片并进行排序
   *
   * @param
   * @return
   */
  public static void getLocalPhoneList(Context context) {
    if (PrefConstant.getPictureRestoreState()) {

      List<FileBean> picInfoList = new ArrayList<>();

      List<FileBean> picInfoListSort = new ArrayList<>();

      picInfoList.clear();

      picInfoListSort.clear();

      //过滤出最近六个月的图片
      UploadUtils.getSystemPhotoList(context, picInfoList);

      if (picInfoList.size() > 0) {
        System.out.println("图片备份 扫描总数 过滤前 = " + picInfoList.size());
        for (int i = 0; i < picInfoList.size(); i++) {
          //备份六个月之前的图片,损坏的图片除外
          if (picInfoList.get(i).getLongDate() >= UploadUtils.getDayMillis(6) && (!"0K".equals(picInfoList.get(i).getSize()))) {
            picInfoListSort.add(picInfoList.get(i));
          }
        }
      }

      //遍历sd卡图片信息
      if (picInfoListSort.size() > 0) {
        //将获取的对象按照时间最新排序(默认为最新在后)
        Collections.sort(picInfoListSort, new Comparator<FileBean>() {
          @Override
          public int compare(FileBean bean1, FileBean bean2) {
            int result = 0;
            if (bean1.getLongDate() - bean2.getLongDate() < 0) {
              result = 1;
            } else if (bean1.getLongDate() - bean2.getLongDate() > 0) {
              result = -1;
            }
            return result;
          }
        });

        List<String> restoredPaths = new DatabaseUtil(context).queryAllRestoredPaths();
        System.out.println("图片备份 已备份历史总数（数据库） = " + restoredPaths.size());
        GlobleConstant.setgPictureRestoredCount(0);//已备份
        System.out.println("图片备份 扫描总数 已备份总数 = " + GlobleConstant.getgPictureRestoredCount());

        for (int i = 0; i < restoredPaths.size(); i++) {
          for (int j = 0; j < picInfoListSort.size(); j++) {
            if (restoredPaths.get(i).equals(picInfoListSort.get(j).getPath())) {
              picInfoListSort.remove(j);
              break;
            }
          }
        }

        GlobleConstant.setgPictureRestoredSumCount(picInfoListSort.size());//备份总数
        System.out.println("图片备份 扫描总数 待备份总数 = " + GlobleConstant.getgPictureRestoredSumCount());

        if (picInfoListSort.size() == 0) {
          GlobleConstant.isFinishedPicRestored = true;//备份完成
          System.out.println("图片备份 扫描总数 已备份完成 ");
        } else {
          //保存到Gloable中
          GlobleConstant.isFinishedPicRestored = false;
          GlobleConstant.getGlobleArraylist().clear();
          for (int i = 0; i < picInfoListSort.size(); i++) {
            GlobleConstant.getGlobleArraylist().add(picInfoListSort.get(i).getPath());//待备份 list
          }

          GlobleConstant.gPictureRestoreIndex = 0;

          System.out.println("图片备份 扫描总数 过滤后 移除已备份记录 待备份数 list = " + GlobleConstant.getPicturePathList().size());
        }

      }
    }
  }

  /**
   * 文件上传（不支持断点续传）
   *
   * @param
   * @return
   */
  public static boolean upload(SmbClient mClient, String localPath, String remotePath, Context context) throws Exception {
    int length;
    long uploaded = 0;
    FileInputStream inS = null;
    SmbFile outFile = null;
    ByteBuffer mBuffer = null;
    long mLastCompleteSize = 0;
    long mLastTime = 0;
    Boolean isLast = true;
    try {
      final File localFile = new File(localPath);
      mClient.createFile(remotePath);

      inS = new FileInputStream(localFile);

      outFile = mClient.openFile(remotePath, "w");

      mBuffer = new ByteBufferPool().obtainBuffer();

      byte[] buf = new byte[mBuffer.capacity()];

      if (PrefConstant.getPictureRestoreState()) {
        GlobleConstant.gPictureTotalSize = localFile.length();
      }

      while ((length = inS.read(buf)) > 0) {
        mBuffer.put(buf, 0, length);
        outFile.write(mBuffer, length);
        uploaded += length;
        mBuffer.clear();

        System.out.println("预览下载 totalSize = " + localFile.length());
        System.out.println("预览下载 uploaded = " + uploaded);

        if (PrefConstant.getPictureRestoreState()) {

          GlobleConstant.gPictureUploadedSize = uploaded;

          //计算网速
          if (isLast) {
            GlobleConstant.gPictureUploadSpeed = SambaUtils.bytes2kb((uploaded - mLastCompleteSize) * 1000 / (System.currentTimeMillis() - mLastTime));

            isLast = false;
            mLastTime = System.currentTimeMillis();
            mLastCompleteSize = uploaded;
          } else {
            GlobleConstant.gPictureUploadSpeed = SambaUtils.bytes2kb((uploaded - mLastCompleteSize) * 1000 / (System.currentTimeMillis() - mLastTime));

            isLast = true;
            mLastCompleteSize = uploaded;
            mLastTime = System.currentTimeMillis();
          }

          System.out.println("网速 图片备份网速 GlobleConstant.gPictureUploadSpeed = " + GlobleConstant.gPictureUploadSpeed);

        }
      }

      if ((inS.read(buf)) == -1) {
        if (PrefConstant.getPictureRestoreState()) {
          //把已上传完的Url备份到缓存中

          int count = GlobleConstant.getgPictureRestoredCount() + 1;
          GlobleConstant.setgPictureRestoredCount(count);

          System.out.println("图片备份：已上传照片数=" + GlobleConstant.getgPictureRestoredCount() + "  url:" + localPath);

          int index = GlobleConstant.gPictureRestoreIndex + 1;
          GlobleConstant.gPictureRestoreIndex = index;

          //上传成功的url记录下来
          new DatabaseUtil(context).saveRestoredPath(localPath);

          GlobleConstant.isLastPicRestored = true;

        }
      }

      return true;
    } catch (Exception e) {
      e.printStackTrace();

      if (PrefConstant.getPictureRestoreState()) {
        System.out.println("图片备份11：上传发生异常 跳过一张图片");
        GlobleConstant.isLastPicRestored = true;
        GlobleConstant.isStopRestorePicture = false;

        /*UploadUtils.stopPicRestoreTimer();*/

        // TODO: 2018/3/17
        int count = GlobleConstant.getgPictureRestoredCount() + 1;
        GlobleConstant.setgPictureRestoredCount(count);
        System.out.println("图片备份：已上传照片数 (发生异常 跳过一张图片)=" + GlobleConstant.getgPictureRestoredCount() + "  url:" + localPath);
        int index = GlobleConstant.gPictureRestoreIndex + 1;
        GlobleConstant.gPictureRestoreIndex = index;
        GlobleConstant.isLastPicRestored = true;
        // TODO: 2018/3/17

      }

    } finally {
      try {
        if (outFile != null) {
          outFile.close();
        }
        if (inS != null) {
          inS.close();
        }
        if (mBuffer != null) {
          mBuffer = null;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  /**
   * 获取n个月之前的时间戳
   *
   * @param
   * @return
   */
  public static long getDayMillis(int num) {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MONTH, -num);
    return calendar.getTimeInMillis();//六个月之前的时间戳
  }

}
