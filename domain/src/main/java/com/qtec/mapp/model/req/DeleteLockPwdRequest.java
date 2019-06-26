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
public class DeleteLockPwdRequest {

  /**
   * deviceSerialNo : 门锁唯一标识
   * passwordSerialNo : 密码标识
   */

  private String deviceSerialNo;
  private String passwordSerialNo;

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
}
