package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetDoorCardsResponse implements Serializable{


  /**
   * deviceSerialNo : 门锁唯一标识
   * doorcardSerialNo : 门卡序列号
   * doorcardUniqueKey : 门卡唯一标识
   * doorcardName : 门卡名称
   */

  private String deviceSerialNo;
  private String doorcardSerialNo;
  private String doorcardUniqueKey;
  private String doorcardName;

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

  public String getDoorcardUniqueKey() {
    return doorcardUniqueKey;
  }

  public void setDoorcardUniqueKey(String doorcardUniqueKey) {
    this.doorcardUniqueKey = doorcardUniqueKey;
  }

  public String getDoorcardName() {
    return doorcardName;
  }

  public void setDoorcardName(String doorcardName) {
    this.doorcardName = doorcardName;
  }
}
