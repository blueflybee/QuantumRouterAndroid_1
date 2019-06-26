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
public class DeleteLockDoorCardRequest {

  /**
   * deviceSerialNo : 门锁唯一标识
   * doorcardSerialNo : 门卡序列号
   */

  private String deviceSerialNo;
  private String doorcardSerialNo;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDoorcardSerialNo() {
    return doorcardSerialNo;
  }

  public void setDoorcardSerialNo(String doorcardSerialNo) {
    this.doorcardSerialNo = doorcardSerialNo;
  }
}
