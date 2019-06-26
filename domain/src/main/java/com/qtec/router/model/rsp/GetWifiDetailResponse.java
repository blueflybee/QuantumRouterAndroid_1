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
public class GetWifiDetailResponse {
  private String ipaddr;
  private String netmask;
  private String gateway;
  private String dns;

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
    return "GetWifiDetailResponse{" +
        "ipaddr='" + ipaddr + '\'' +
        ", netmask='" + netmask + '\'' +
        ", gateway='" + gateway + '\'' +
        ", dns='" + dns + '\'' +
        '}';
  }
}
