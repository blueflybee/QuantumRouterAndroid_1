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
public class ModifyLockFpNameRequest {

  /**
   * deviceSerialNo : 门锁唯一标识
   * fingerprintSerialNo : 指纹标识
   * fingerprintName : 指纹名称
   */

  private String deviceSerialNo;
  private String fingerprintSerialNo;
  private String fingerprintName;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getFingerprintSerialNo() {
    return fingerprintSerialNo;
  }

  public void setFingerprintSerialNo(String fingerprintSerialNo) {
    this.fingerprintSerialNo = fingerprintSerialNo;
  }

  public String getFingerprintName() {
    return fingerprintName;
  }

  public void setFingerprintName(String fingerprintName) {
    this.fingerprintName = fingerprintName;
  }
}
