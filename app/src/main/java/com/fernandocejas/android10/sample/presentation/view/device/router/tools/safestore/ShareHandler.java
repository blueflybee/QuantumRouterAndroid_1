package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/04
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ShareHandler extends Handler {
  public static Handler mHandler;
  public static Bundle mBundle;
  public static Map<String,Bundle> bundles;
  public static Map<String,Handler> handlers = new HashMap<>() ;

  public static Handler setHandler(String url) {
    int i = 0;
    mHandler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what == 1){
          mBundle = msg.getData();
          System.out.println("handler:"+(i+1)+"  "+url);
        }
      }
    };
    handlers.put(url,mHandler);
    return mHandler;
  }

  public static Handler getHandler() {
    return mHandler;
  }

  public static Bundle getBundleData(){
    return mBundle;
  }

}
