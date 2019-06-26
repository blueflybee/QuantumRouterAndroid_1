package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-9-19
 *     desc   : presenter
 *     version: 1.0
 * </pre>
 */
public class GetLockVolumeRequest {

  /**
   * deviceSerialNo : 设备(门锁)序列号
   */

  private String deviceSerialNo;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }
}
