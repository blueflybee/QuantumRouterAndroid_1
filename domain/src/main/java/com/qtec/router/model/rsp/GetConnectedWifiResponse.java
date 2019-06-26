package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetConnectedWifiResponse {
  private int enable;
  private int auto_switch;
  private int status;
  private String ssid;
  private String mac;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public int getAuto_switch() {
    return auto_switch;
  }

  public void setAuto_switch(int auto_switch) {
    this.auto_switch = auto_switch;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getSsid() {
    return ssid;
  }

  public void setSsid(String ssid) {
    this.ssid = ssid;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  @Override
  public String toString() {
    return "GetConnectedWifiResponse{" +
        "enable=" + enable +
        ", auto_switch=" + auto_switch +
        ", status=" + status +
        ", ssid='" + ssid + '\'' +
        '}';
  }
}
