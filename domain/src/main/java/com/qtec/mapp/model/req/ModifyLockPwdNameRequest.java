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
public class ModifyLockPwdNameRequest {

  /**
   * deviceSerialNo : 门锁唯一标识
   * passwordSerialNo : 密码标识
   * passwordName : 密码名称
   */

  private String deviceSerialNo;
  private String passwordSerialNo;
  private String passwordName;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
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
}
