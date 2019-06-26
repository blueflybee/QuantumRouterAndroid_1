package com.qtec.router.model.req;

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
public class SetVpnRequest<T>{
  private int enable;
  private T vpn;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public T getVpn() {
    return vpn;
  }

  public void setVpn(T vpn) {
    this.vpn = vpn;
  }

  @Override
  public String toString() {
    return "SetVpnResponse{" +
        "enable=" + enable +
        ", vpn=" + vpn +
        '}';
  }

  public static class VpnBean implements Serializable {

    private int enable;
    private String ifname;

    public int getEnable() {
      return enable;
    }

    public void setEnable(int enable) {
      this.enable = enable;
    }

    public String getIfname() {
      return ifname;
    }

    public void setIfname(String ifname) {
      this.ifname = ifname;
    }

    @Override
    public String toString() {
      return "VpnBean{" +
          "enable=" + enable +
          ", ifname='" + ifname + '\'' +
          '}';
    }
  }

}
