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
public class SetStaticIPRequest {


  /**
   * connectiontype : static
   * ipaddr : 192.168.90.127
   * netmask : 255.255.255.0
   * gateway : 192.168.90.1
   * dns : 192.168.90.1
   */
  private String connectiontype;
  private String ipaddr;
  private String netmask;
  private String gateway;
  private String dns;
  /**
   * 0 未配置
   * 1 已配置
   */
  private int configed;


  public String getConnectiontype() {
    return connectiontype;
  }

  public void setConnectiontype(String connectiontype) {
    this.connectiontype = connectiontype;
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

  public int getConfiged() {
    return configed;
  }

  public void setConfiged(int configed) {
    this.configed = configed;
  }
}
