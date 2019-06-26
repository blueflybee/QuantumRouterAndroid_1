package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象存储工具类
 * @author shaojun
 * @name ObjectUtil
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class ObjectUtil {

  public static final String LOCK_FILE_NAME = "blelocks.dat";
  public static final String USER_DEVICES = "devices.dat";
  public static final String SAMBA_PICTURES = "pictures.dat";
  public static final String SAMBA_URLS = "urls.dat";
  public static final String SAMBA_DOWNLOADERS = "downloaders.dat";
  public static final String SAMBA_UPLOADERS = "uploaders.dat";
  public static final String SAMBA_BATCH_UPLOAD = "batchuploads.dat";
  public static final String SAMBA_BATCH_DOWNLOAD = "batchdownloads.dat";

  public static void saveObject(Context context, Object obj, String fileName) {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(obj);
      System.out.println("write object success!");
    } catch (Exception e) {
      System.out.println("write object failed");
      e.printStackTrace();
      //这里是保存文件产生异常
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e) {
          //fos流关闭异常
          e.printStackTrace();
        }
      }
      if (oos != null) {
        try {
          oos.close();
        } catch (IOException e) {
          //oos流关闭异常
          e.printStackTrace();
        }
      }
    }
  }

  public synchronized static Object readObject(Context context, String fileName){
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      fis = context.openFileInput(fileName);
      ois = new ObjectInputStream(fis);
      Object result = ois.readObject();
      System.out.println("read object success!");
      return result;
    } catch (Exception e) {
      System.out.println("read object failed");
      e.printStackTrace();
      //这里是读取文件产生异常
    } finally {
      if (fis != null){
        try {
          fis.close();
        } catch (IOException e) {
          //fis流关闭异常
          e.printStackTrace();
        }
      }
      if (ois != null){
        try {
          ois.close();
        } catch (IOException e) {
          //ois流关闭异常
          e.printStackTrace();
        }
      }
    }
    //读取产生异常，返回null
    return null;
  }

}
