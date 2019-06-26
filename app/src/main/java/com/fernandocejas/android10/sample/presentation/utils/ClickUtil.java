package com.fernandocejas.android10.sample.presentation.utils;

/**
 * @author shaojun
 * @name VersionUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-27
 */
public class ClickUtil {

  private static final int MIN_DELAY_TIME = 100;  // 两次点击间隔不能少于500ms
  private static long lastClickTime;

  public static boolean isFastClick() {
    boolean flag = true;
    long currentClickTime = System.currentTimeMillis();
    if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
      flag = false;
    }
    lastClickTime = currentClickTime;
    return flag;
  }

}
