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
public class BlockAuthedRequest {
  private String dev_mac;

  public String getDev_mac() {
    return dev_mac;
  }

  public void setDev_mac(String dev_mac) {
    this.dev_mac = dev_mac;
  }

  @Override
  public String toString() {
    return "BlockAuthedRequest{" +
        "dev_mac='" + dev_mac + '\'' +
        '}';
  }
}
