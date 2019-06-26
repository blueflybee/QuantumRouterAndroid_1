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
public class ModifyLockDoorCardNameRequest {

  /**
   * doorcardSerialNo : 门卡序列号
   * deviceSerialNo : 设备序列号
   * doorcardName : 门卡名称
   */

  private String doorcardSerialNo;
  private String deviceSerialNo;
  private String doorcardName;

  public String getDoorcardSerialNo() {
    return doorcardSerialNo;
  }

  public void setDoorcardSerialNo(String doorcardSerialNo) {
    this.doorcardSerialNo = doorcardSerialNo;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDoorcardName() {
    return doorcardName;
  }

  public void setDoorcardName(String doorcardName) {
    this.doorcardName = doorcardName;
  }
}
