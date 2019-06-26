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
public class GetNetModeResponse implements Serializable{

  /**
   * connectiontype : pppoe
   * username : wjj
   * password : xxx
   * ipaddr : 192.168.90.127
   * netmask : 255.255.255.0
   * gateway : 192.168.90.1
   * dns : 192.168.90.1
   */

  private String connectiontype; //dhcp,pppoe,static
  private String username;
  private String password;
  private String ipaddr;
  private String netmask;
  private String gateway;
  private String dns;

  public String getConnectiontype() {
    return connectiontype;
  }

  public void setConnectiontype(String connectiontype) {
    this.connectiontype = connectiontype;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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
}
