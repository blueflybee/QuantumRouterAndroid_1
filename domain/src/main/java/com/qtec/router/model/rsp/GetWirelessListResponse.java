package com.qtec.router.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetWirelessListResponse<T> {
  private T wifi;

  public T getWifi() {
    return wifi;
  }

  public void setWifi(T wifi) {
    this.wifi = wifi;
  }

  @Override
  public String toString() {
    return "GetWirelessListResponse{" +
        ", wifi=" + wifi +
        '}';
  }

  public static class WirelessBean implements Serializable{
    private String ssid;
    private int power;
    private int status;
    private int mode;
    private String mac;
    private int channel;
    private String  encrypt;

    public String getSsid() {
      return ssid;
    }

    public void setSsid(String ssid) {
      this.ssid = ssid;
    }

    public int getPower() {
      return power;
    }

    public void setPower(int power) {
      this.power = power;
    }

    public int getStatus() {
      return status;
    }

    public void setStatus(int status) {
      this.status = status;
    }

    public int getMode() {
      return mode;
    }

    public void setMode(int mode) {
      this.mode = mode;
    }

    public String getMac() {
      return mac;
    }

    public void setMac(String mac) {
      this.mac = mac;
    }

    public int getChannel() {
      return channel;
    }

    public void setChannel(int channel) {
      this.channel = channel;
    }

    public String getEncrypt() {
      return encrypt;
    }

    public void setEncrypt(String encrypt) {
      this.encrypt = encrypt;
    }

    @Override
    public String toString() {
      return "WirelessBean{" +
          "ssid='" + ssid + '\'' +
          ", power=" + power +
          ", status=" + status +
          ", mode=" + mode +
          ", mac='" + mac + '\'' +
          ", channel=" + channel +
          ", encrypt='" + encrypt + '\'' +
          '}';
    }
  }
}
