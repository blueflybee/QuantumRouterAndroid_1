package com.fernandocejas.android10.sample.data.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.w3c.dom.Text;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppStringUtil {
  @NonNull
  public static String cutTail(String decryptJson) {
    if (TextUtils.isEmpty(decryptJson)) return "";
    if (!decryptJson.contains("}")) return "";
    try {
      return decryptJson.substring(0, decryptJson.lastIndexOf("}") + 1);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
