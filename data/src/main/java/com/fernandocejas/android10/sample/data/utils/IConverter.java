package com.fernandocejas.android10.sample.data.utils;

import android.support.annotation.NonNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/10
 *     desc   : 对报文进行加解密操作的转换器
 *     version: 1.0
 * </pre>
 */
public interface IConverter {

  String EXP_KEY_INVALID = "exp_key_invalid";
  String EXP_ACCESS_DEVICE_NOT_CONNECTED = "exp_access_device_not_connected";

  String convertTo(String initText, int encryption);

  String convertFrom(String encryptText, String requestUrl);

  String convertToForFake(@NonNull String rspJson, int encryption);


  /**
   * 测试用封装返回报文
   * @param resultJson
   * @param encryption
   * @return
   */
  String createEncryptResponse(String resultJson, int encryption);

  /**
   * 测试用解密和解析返回报文
   * @param encryptResult
   * @param path
   * @return
   */
  String convertFromForTestResponse(String encryptResult, String path);
}
