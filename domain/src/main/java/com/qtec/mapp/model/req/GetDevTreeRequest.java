package com.qtec.mapp.model.req;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      version: 1.0
 * </pre>
 */

public class GetDevTreeRequest {

  /**
   * userUniqueKey : 13557321203
   * deviceType : 0是网关,1是门锁,空表示全部
   */

  private String userUniqueKey;
  private String deviceType;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }
}
