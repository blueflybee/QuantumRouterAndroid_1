package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/12/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LogcatUtil {
  private static final long FILE_SIZE = 6000 * 1000;

  private boolean running = false;

  private static int fileIndex = 0;
  private BufferedReader mBufferedReader;

  public LogcatUtil() {
  }

  public void start(Context context, String dirName, String fileName) {
    createLogCollector();
    String dirPath;
    if (Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
      dirPath = Environment.getExternalStorageDirectory()
          .getAbsolutePath() + File.separator + dirName;

    } else {// 如果SD卡不存在，就保存到本应用的目录下
      dirPath = context.getFilesDir().getAbsolutePath()

          + File.separator + dirName;
    }

    File dir = new File(dirPath);

    if (!dir.exists()) {
      dir.mkdirs();
    }

    File file = createNewFile(fileName, dir);
    if (file == null) return;
    running = true;
    new WriteLogThread(file).start();
  }

  @Nullable
  private static File createNewFile(String fileName, File dir) {
    File file = new File(dir, fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        Log.d("writelog", "read logcat process failed. message: " + e.getMessage());
//        e.printStackTrace();
        return null;
      }
    }
    return file;
  }

  public void stop() {
    running = false;
  }

  private class WriteLogThread extends Thread {
    private File file;
    private String fileName;

    public WriteLogThread(File file) {
      this.file = file;
      this.fileName = file.getName();
    }

    @Override
    public void run() {
//      FileOutputStream os = null;
//      try {
//        java.lang.Process p = Runtime.getRuntime().exec("logcat -d");
//        final InputStream is = p.getInputStream();
//        os = new FileOutputStream(file);
//        int len = 0;
//        byte[] buf = new byte[256];


        String line;
        try {
          while ((line = mBufferedReader.readLine()) != null) {
            //读出每行log信息
//                System.out.println("log==== " + line);
            if (!running) {
              break;
            }

            clearLogCache();
//            logcatRequest.setAndroidlog(line);
            FileUtils.writeFileFromString(file, line + "\n", true);
//            if (line.contains("FIRST_GET_KEYS")) {
//              ToastUtils.showLong("请再等待20秒");
//            }
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
//        while (-1 != (len = is.read(buf))) {
//          os.write(buf, 0, len);
//          os.flush();
//          if (!running) break;
//          if (file.length() >= FILE_SIZE) {
//            os.close();
//            os = null;
//            fileIndex++;
//            File parentFile = file.getParentFile();
//            file = null;
//            file = createNewFile(fileIndex + fileName, parentFile);
//            if (file == null) return;
//            os = new FileOutputStream(file);
//            Thread.sleep(2000);
//          }
//        }
//      } catch (Exception e) {
//        Log.d("writelog", "read logcat process failed. message: " + e.getMessage());
//      } finally {
//        if (null != os) {
//          try {
//            os.close();
//            os = null;
//          } catch (IOException e) {
//            // Do nothing
//          }
//        }
//      }
    }
  }
  /**
   * 每次记录日志之前先清除日志的缓存, 不然会在两个日志文件中记录重复的日志
   */
  private void clearLogCache() {
    List<String> commandList = new ArrayList<>();
    commandList.add("logcat");
    commandList.add("-c");
    try {
      Runtime.getRuntime().exec(commandList.toArray(new String[commandList.size()]));

    } catch (Exception e) {

    }
  }
  /**
   * 开始收集日志信息
   */
  public void createLogCollector() {
//    String logFileName = sdf.format(new Date()) + ".log";// 日志文件名称
    List<String> commandList = new ArrayList<>();
    commandList.add("logcat");
//    commandList.add("-d");
    //commandList.add(LOG_PATH_INSTALL_DIR + File.separator + logFileName);
//    commandList.add(getLogPath());
    commandList.add("-v");
    commandList.add("time");
    commandList.add("*:D");

    //commandList.add("*:E");// 过滤所有的错误信息

    // 过滤指定TAG的信息
    // commandList.add("MyAPP:V");
    // commandList.add("*:S");
    try {
      Process process = Runtime.getRuntime().exec(commandList.toArray(new String[commandList.size()]));
      mBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//      recordLogServiceLog("start collecting the log,and log name is:"+logFileName);
      // process.waitFor();
    } catch (Exception e) {
//      Log.e(TAG, "CollectorThread == >" + e.getMessage(), e);
//      recordLogServiceLog("CollectorThread == >" + e.getMessage());
    }
  }

}
