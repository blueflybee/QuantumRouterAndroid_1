package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UpdateLockVersionRequest {

  /**
   * deviceSerialNo : 设备序列号
   * deviceVersion : 版本号
   * deviceType : 0路由器，1门锁，其他待扩展
   */

  private String deviceSerialNo;
  private String deviceVersion;
  private String deviceType;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDeviceVersion() {
    return deviceVersion;
  }

  public void setDeviceVersion(String deviceVersion) {
    this.deviceVersion = deviceVersion;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }
}
