package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetBlackListResponse {
  private String macaddr;
  private String name;
  private int enabled;

  public String getMacaddr() {
    return macaddr;
  }

  public void setMacaddr(String macaddr) {
    this.macaddr = macaddr;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "GetBlackListResponse{" +
        "macaddr='" + macaddr + '\'' +
        ", name='" + name + '\'' +
        ", enabled=" + enabled +
        '}';
  }
}
