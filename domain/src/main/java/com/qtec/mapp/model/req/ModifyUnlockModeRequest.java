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
public class ModifyUnlockModeRequest {

  /**
   * deviceSerialNo : 门锁序列号
   * userUniqueKey : 用户唯一标识
   * passwordOpenConfig : 0，不需要开启蓝牙（默认）；1，需要开启蓝牙
   * fingerprintOpenConfig : 0，不需要开启蓝牙（默认）；1，需要开启蓝牙
   * doorcardOpenConfig : 0，不需要开启蓝牙（默认）；1，需要开启蓝牙
   */

  private String deviceSerialNo;
  private String userUniqueKey;
  private String passwordOpenConfig;
  private String openConfig;
  private String doorcardOpenConfig;

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

  public String getPasswordOpenConfig() {
    return passwordOpenConfig;
  }

  public void setPasswordOpenConfig(String passwordOpenConfig) {
    this.passwordOpenConfig = passwordOpenConfig;
  }

  public String getOpenConfig() {
    return openConfig;
  }

  public void setOpenConfig(String openConfig) {
    this.openConfig = openConfig;
  }

  public String getDoorcardOpenConfig() {
    return doorcardOpenConfig;
  }

  public void setDoorcardOpenConfig(String doorcardOpenConfig) {
    this.doorcardOpenConfig = doorcardOpenConfig;
  }
}
