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
public class DeleteVpnRequest {
  private String ifname;

  public void setIfname(String ifname) {
    this.ifname = ifname;
  }

  public String getIfname() {
    return ifname;
  }

  @Override
  public String toString() {
    return "DeleteVpnRequest{" +
        "ifname='" + ifname + '\'' +
        '}';
  }
}
