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
public class QueryCatLockRequest implements Serializable{
  private String slaveSerialNo;
  private String userUniqueKey;

  public String getSlaveSerialNo() {
    return slaveSerialNo;
  }

  public void setSlaveSerialNo(String slaveSerialNo) {
    this.slaveSerialNo = slaveSerialNo;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  @Override
  public String toString() {
    return "QueryCatLockRequest{" +
        "slaveSerialNo='" + slaveSerialNo + '\'' +
        ", userUniqueKey='" + userUniqueKey + '\'' +
        '}';
  }
}
