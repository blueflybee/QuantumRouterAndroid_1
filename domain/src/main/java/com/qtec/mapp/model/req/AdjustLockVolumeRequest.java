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
public class AdjustLockVolumeRequest {

  /**
   * deviceSerialNo : 设备(门锁)序列号
   * volume : 门锁音量值
   */

  private String deviceSerialNo;
  private String volume;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }
}
