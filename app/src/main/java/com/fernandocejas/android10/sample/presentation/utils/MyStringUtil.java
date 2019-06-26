package com.fernandocejas.android10.sample.presentation.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shaojun
 * @name CharUtil
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-10-30
 */
public class MyStringUtil {

  /**
   * 对输入string的3到7位之间打*号
   *
   * @param origin
   * @return
   */
  public static String addStar(String origin) {
    if (TextUtils.isEmpty(origin)) return "";
    if (origin.length() <= 3) return origin;
    if (origin.length() > 3 && origin.length() <= 7) {
      String head = origin.substring(0, 3);
      return head + "****";
    }
    if (origin.length() > 7) {
      String head = origin.substring(0, 3);
      String tail = origin.substring(7);
      return head + "****" + tail;
    }
    return origin;
  }

  public static byte[] strToBytes(String str, String charsetName) {
    if (TextUtils.isEmpty(str)) return null;
    try {
      return str.getBytes(charsetName);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String bytesToStr(byte[] bytes, String charsetName) {
    if (bytes == null) return "";
    try {
      return new String(bytes, charsetName);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }
  }


}


