package com.fernandocejas.android10.sample.presentation.utils;

import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/07/28
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class Base64Util {

  public Base64Util(){

  }
  //将 s 进行 BASE64 编码
  public static String encodeBase64(String s) {
    if (s == null) return null;
    try {
      return (new sun.misc.BASE64Encoder()).encode( s.getBytes("UTF-8") );
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }
  }

  //将 BASE64 编码的字符串 s 进行解码
  public static String decodeBase64(String s) {
    if (s == null) return null;
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      byte[] b = decoder.decodeBuffer(s);
      return new String(b, "UTF-8");
    } catch (Exception e) {
      return null;
    }
  }
}
