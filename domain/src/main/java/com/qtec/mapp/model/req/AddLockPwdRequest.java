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
public class AddLockPwdRequest {


  /**
   * userUniqueKey : 13557321203
   * passwordSerialNo : 密码序列号
   * passwordName : 密码名称
   * deviceSerialNo : 设备序列号
   */

  private String userUniqueKey;
  private String passwordSerialNo;
  private String passwordName;
  private String deviceSerialNo;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getPasswordSerialNo() {
    return passwordSerialNo;
  }

  public void setPasswordSerialNo(String passwordSerialNo) {
    this.passwordSerialNo = passwordSerialNo;
  }

  public String getPasswordName() {
    return passwordName;
  }

  public void setPasswordName(String passwordName) {
    this.passwordName = passwordName;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }
}
