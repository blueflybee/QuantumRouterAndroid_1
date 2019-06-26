package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyPassagewayModeRequest {


  /**
   * deviceSerialNo : 门锁序列号
   * userUniqueKey : 用户唯一标识
   * channelConfig : 0，关闭（默认）；1，开启
   */

  private String deviceSerialNo;
  private String userUniqueKey;
  private String channelConfig;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getChannelConfig() {
    return channelConfig;
  }

  public void setChannelConfig(String channelConfig) {
    this.channelConfig = channelConfig;
  }
}
