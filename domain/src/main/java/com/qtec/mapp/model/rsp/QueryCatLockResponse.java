package com.qtec.mapp.model.rsp;

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
public class QueryCatLockResponse implements Serializable{
  private String deviceSerialNo;  //门锁Id
  private String deviceNickName;
  private String deviceModel;
  private String routerSerialNo;//门锁绑定的网关id

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDeviceNickName() {
    return deviceNickName;
  }

  public void setDeviceNickName(String deviceNickName) {
    this.deviceNickName = deviceNickName;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getRouterSerialNo() {
    return routerSerialNo;
  }

  public void setRouterSerialNo(String routerSerialNo) {
    this.routerSerialNo = routerSerialNo;
  }

  @Override
  public String toString() {
    return "QueryCatLockResponse{" +
        "deviceSerialNo='" + deviceSerialNo + '\'' +
        ", deviceNickName='" + deviceNickName + '\'' +
        ", deviceModel='" + deviceModel + '\'' +
        ", routerSerialNo='" + routerSerialNo + '\'' +
        '}';
  }
}
