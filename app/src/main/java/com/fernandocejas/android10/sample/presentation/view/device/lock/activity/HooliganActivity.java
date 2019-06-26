package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/10/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HooliganActivity extends Activity {
  private static HooliganActivity instance;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    instance = this;
    Window window = getWindow();
    window.setGravity(Gravity.LEFT | Gravity.TOP);
    WindowManager.LayoutParams params = window.getAttributes();
    params.x = 0;
    params.y = 0;
    params.height = 1;
    params.width = 1;
    window.setAttributes(params);
  }

  @Override
  protected void onResume() {
    super.onResume();
    System.out.println("HooliganActivity.onResume");
    ((AndroidApplication) getApplication()).bindAndStartBleService();
  }

  /**
   * 开启保活页面
   */
  public static void startHooligan() {
    System.out.println("HooliganActivity.startHooligan");
    Intent intent = new Intent(AndroidApplication.mApplicationContext, HooliganActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    AndroidApplication.mApplicationContext.startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    instance = null;
  }

  /**
   * 关闭保活页面
   */
  public static void killHooligan() {
    System.out.println("HooliganActivity.killHooligan");
    if (instance != null) {
      instance.finish();
    }
  }

}
