package com.fernandocejas.android10.sample.presentation.utils;

import android.app.Activity;

/**
 * @author shaojun
 * @name VersionUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-27
 */
public class IntentUtil {

  public static Object getSerialExtra(Activity activity, String name) {
    return activity.getIntent().getSerializableExtra(name);
  }

}
