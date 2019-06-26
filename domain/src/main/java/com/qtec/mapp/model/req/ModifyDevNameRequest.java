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
public class ModifyDevNameRequest {


  /**
   * userUniqueKey : 13557321203
   * deviceSerialNo : 设备序列号
   * deviceNickName : 设备昵称
   */

  private String userUniqueKey;
  private String deviceSerialNo;
  private String deviceNickName;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDeviceNickName() {
    return deviceNickName;
  }

  public void setDeviceNickName(String deviceNickName) {
    this.deviceNickName = deviceNickName;
  }
}
