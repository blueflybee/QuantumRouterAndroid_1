package com.qtec.mapp.model.req;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 设备在线
 *     version: 1.0
 * </pre>
 */
public class QueryIsOnlineRequest implements Serializable{
  private String companyName;
  private String deviceGuidArr;
  private int sig;
  private int type;

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getDeviceGuidArr() {
    return deviceGuidArr;
  }

  public void setDeviceGuidArr(String deviceGuidArr) {
    this.deviceGuidArr = deviceGuidArr;
  }

  public int getSig() {
    return sig;
  }

  public void setSig(int sig) {
    this.sig = sig;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "QueryIsOnlineResponse{" +
        "companyName='" + companyName + '\'' +
        ", deviceGuidArr='" + deviceGuidArr + '\'' +
        ", sig=" + sig +
        ", type=" + type +
        '}';
  }
}
