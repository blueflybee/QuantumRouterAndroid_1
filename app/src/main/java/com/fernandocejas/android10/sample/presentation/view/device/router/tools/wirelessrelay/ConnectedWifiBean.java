package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import java.io.Serializable;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/15
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ConnectedWifiBean implements Serializable{
  private int enable;
  private int auto_switch;
  private String ssid;
  private int power;
  private int status;
  private int mode;
  private String mac;
  private int channel;
  private String  encrypt;
  private String ipaddr;
  private String netmask;
  private String gateway;
  private String dns;

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

  public String getIpaddr() {
    return ipaddr;
  }

  public void setIpaddr(String ipaddr) {
    this.ipaddr = ipaddr;
  }

  public String getNetmask() {
    return netmask;
  }

  public void setNetmask(String netmask) {
    this.netmask = netmask;
  }

  public String getGateway() {
    return gateway;
  }

  public void setGateway(String gateway) {
    this.gateway = gateway;
  }

  public String getDns() {
    return dns;
  }

  public void setDns(String dns) {
    this.dns = dns;
  }

  @Override
  public String toString() {
    return "ConnectedWifiBean{" +
        "enable=" + enable +
        ", auto_switch=" + auto_switch +
        ", ssid='" + ssid + '\'' +
        ", power=" + power +
        ", status=" + status +
        ", mode=" + mode +
        ", mac='" + mac + '\'' +
        ", channel=" + channel +
        ", encrypt='" + encrypt + '\'' +
        ", ipaddr='" + ipaddr + '\'' +
        ", netmask='" + netmask + '\'' +
        ", gateway='" + gateway + '\'' +
        ", dns='" + dns + '\'' +
        '}';
  }
}
