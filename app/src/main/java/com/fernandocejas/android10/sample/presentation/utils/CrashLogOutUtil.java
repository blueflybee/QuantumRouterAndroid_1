package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/11/02
 *      desc: 将日志打印到本地文件
 *      version: 1.0
 * </pre>
 */

public class CrashLogOutUtil implements Thread.UncaughtExceptionHandler {

  private static CrashLogOutUtil instance;

  public static CrashLogOutUtil getInstance() {
    if (instance == null) {
      instance = new CrashLogOutUtil();
    }
    return instance;
  }

  public void init(Context ctx) {
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  /**
   * 核心方法，当程序crash 会回调此方法， Throwable中存放这错误日志
   */
  @Override
  public void uncaughtException(Thread t, Throwable arg1) {

    String logPath;
    if (Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED)) {
      logPath = Environment.getExternalStorageDirectory()
          .getAbsolutePath()
          + File.separator
          + File.separator
          + "QtecLog";

      File file = new File(logPath);
      if (!file.exists()) {
        file.mkdirs();
      }
      try {
        FileWriter fw = new FileWriter(logPath + File.separator
            + "errorlog.log", true);
        fw.write(new Date() + "Bug日志如下：\n");
        // 错误信息
        // 这里还可以加上当前的系统版本，机型型号 等等信息
        fw.write("======================Bug Start============================="+"\n");
        StackTraceElement[] stackTrace = arg1.getStackTrace();
        fw.write(arg1.getMessage() + "\n");
        for (int i = 0; i < stackTrace.length; i++) {
          fw.write("file:" + stackTrace[i].getFileName()+ "\n"
              + " class:" + stackTrace[i].getClassName() + "\n"
              + " method:" + stackTrace[i].getMethodName() + "\n"
              + " line:" + stackTrace[i].getLineNumber() + "\n");
        }
        fw.write("======================Bug End============================="+"\n");
        fw.write("=========================================================="+"\n");

        fw.close();
        // uploadToServer();
        // 通过友盟上报奔溃日志信息
        MobclickAgent.reportError(AndroidApplication.getApplication(),arg1.getMessage());
      } catch (IOException e) {
        Log.e("crash handler", "load file failed...", e.getCause());
      }
    }
    arg1.printStackTrace();
    android.os.Process.killProcess(android.os.Process.myPid());
  }
}
