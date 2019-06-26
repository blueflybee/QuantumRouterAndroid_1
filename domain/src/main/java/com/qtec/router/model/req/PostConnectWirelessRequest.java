package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PostConnectWirelessRequest {
  private String ssid;
  private String mac;
  private String password;
  private String encrypt;
  private int mode;

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEncrypt() {
    return encrypt;
  }

  public void setEncrypt(String encrypt) {
    this.encrypt = encrypt;
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  @Override
  public String toString() {
    return "PostConnectWirelessRequest{" +
        "ssid='" + ssid + '\'' +
        ", mac='" + mac + '\'' +
        ", password='" + password + '\'' +
        ", encrypt='" + encrypt + '\'' +
        ", mode=" + mode +
        '}';
  }
}
