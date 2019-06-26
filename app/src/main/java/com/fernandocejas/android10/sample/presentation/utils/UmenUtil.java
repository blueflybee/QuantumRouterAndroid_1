package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.text.TextUtils;

/**
 * @author shaojun
 * @name UmenUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-12
 */
public class UmenUtil {

  public static final String EVENT_REAL_PLAY = "real_play";

  public static String getDeviceInfo(Context context) {
    try {
      org.json.JSONObject json = new org.json.JSONObject();
      android.telephony.TelephonyManager tm =
          (android.telephony.TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

      String device_id = tm.getDeviceId();

      android.net.wifi.WifiManager wifi =
          (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

      String mac = wifi.getConnectionInfo().getMacAddress();
      json.put("mac", mac);

      if (TextUtils.isEmpty(device_id)) {
        device_id = mac;
      }

      if (TextUtils.isEmpty(device_id)) {
        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
            android.provider.Settings.Secure.ANDROID_ID);
      }

      json.put("device_id", device_id);

      return json.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


}
