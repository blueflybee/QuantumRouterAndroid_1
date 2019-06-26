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
public class AddLockFpRequest {

  /**
   * userUniqueKey : 13557321203
   * fingerprintSerialNo : 指纹序列号
   * deviceSerialNo : 设备序列号
   * fingerprintName : 指纹名称
   */

  private String userUniqueKey;
  private String fingerprintSerialNo;
  private String deviceSerialNo;
  private String fingerprintName;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getFingerprintSerialNo() {
    return fingerprintSerialNo;
  }

  public void setFingerprintSerialNo(String fingerprintSerialNo) {
    this.fingerprintSerialNo = fingerprintSerialNo;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getFingerprintName() {
    return fingerprintName;
  }

  public void setFingerprintName(String fingerprintName) {
    this.fingerprintName = fingerprintName;
  }
}
