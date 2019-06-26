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
public class AddTempPwdRequest {

  /**
   * deviceSerialNo : 设备(门锁)序列号
   * tempPasswords : 临时密码串，中间以','隔开
   */

  private String deviceSerialNo;
  private String tempPasswords;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getTempPasswords() {
    return tempPasswords;
  }

  public void setTempPasswords(String tempPasswords) {
    this.tempPasswords = tempPasswords;
  }
}
