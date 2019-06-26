package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-31
 *     desc   : "comment":"解除门锁与路由器的绑定关系"
 *     version: 1.0
 * </pre>
 */
public class CloudUnbindRouterLockRequest {

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
