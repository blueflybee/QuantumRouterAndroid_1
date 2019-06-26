package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetUsersOfLockResponse<T> implements Serializable {

  private String deviceSerialNo;

  private T users;

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public T getUsers() {
    return users;
  }

  public void setUsers(T users) {
    this.users = users;
  }

  @Override
  public String toString() {
    return "GetUsersOfLockResponse{" +
        "deviceSerialNo='" + deviceSerialNo + '\'' +
        ", users=" + users +
        '}';
  }
}
