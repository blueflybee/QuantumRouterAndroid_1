package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author shaojun
 * @name VersionUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-27
 */
public class VersionUtil {

  public static int getVersionCode(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public static String getVersionName(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return "";
    }
  }
}
