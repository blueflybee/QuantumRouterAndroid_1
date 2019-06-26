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
public class GetVpnListResponse<T> {
  private int enable;
  private T vpn_list;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public T getVpn_list() {
    return vpn_list;
  }

  public void setVpn_list(T vpn_list) {
    this.vpn_list = vpn_list;
  }

  @Override
  public String toString() {
    return "GetVpnListResponse{" +
        "enable=" + enable +
        ", vpn_list=" + vpn_list +
        '}';
  }

  public static class VpnBean implements Serializable{
    private String description;
    private String mode;
    private String server_ip;
    private String username;
    private String ifname;
    private int enable;
    private String status;

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getMode() {
      return mode;
    }

    public void setMode(String mode) {
      this.mode = mode;
    }

    public String getServer_ip() {
      return server_ip;
    }

    public void setServer_ip(String server_ip) {
      this.server_ip = server_ip;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getIfname() {
      return ifname;
    }

    public void setIfname(String ifname) {
      this.ifname = ifname;
    }

    public int getEnable() {
      return enable;
    }

    public void setEnable(int enable) {
      this.enable = enable;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    @Override
    public String toString() {
      return "VpnBean{" +
          "description='" + description + '\'' +
          ", mode='" + mode + '\'' +
          ", server_ip='" + server_ip + '\'' +
          ", username='" + username + '\'' +
          ", ifname='" + ifname + '\'' +
          ", enable=" + enable +
          ", status='" + status + '\'' +
          '}';
    }
  }
}
