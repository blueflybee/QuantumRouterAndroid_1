package com.qtec.mapp.model.req;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 设备共享成员
 *     version: 1.0
 * </pre>
 */
public class UnbindCatLockRequest implements Serializable{
  // "masterSerialNo":"门锁序列号","slaveSerialNo":"猫眼序列号"},
  private String masterSerialNo;
  private String slaveSerialNo;

  public String getMasterSerialNo() {
    return masterSerialNo;
  }

  public void setMasterSerialNo(String masterSerialNo) {
    this.masterSerialNo = masterSerialNo;
  }

  public String getSlaveSerialNo() {
    return slaveSerialNo;
  }

  public void setSlaveSerialNo(String slaveSerialNo) {
    this.slaveSerialNo = slaveSerialNo;
  }

  @Override
  public String toString() {
    return "UnbindCatLockRequest{" +
        "masterSerialNo='" + masterSerialNo + '\'' +
        ", slaveSerialNo='" + slaveSerialNo + '\'' +
        '}';
  }
}
