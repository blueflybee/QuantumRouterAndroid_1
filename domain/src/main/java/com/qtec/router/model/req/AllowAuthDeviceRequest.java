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
public class AllowAuthDeviceRequest {
  private String dev_mac;
  private String dev_name;
  private int auth;
  private int block;

  public String getDev_mac() {
    return dev_mac;
  }

  public void setDev_mac(String dev_mac) {
    this.dev_mac = dev_mac;
  }

  public int getAuth() {
    return auth;
  }

  public void setAuth(int auth) {
    this.auth = auth;
  }

  public int getBlock() {
    return block;
  }

  public void setBlock(int block) {
    this.block = block;
  }

  public String getDev_name() {
    return dev_name;
  }

  public void setDev_name(String dev_name) {
    this.dev_name = dev_name;
  }

  @Override
  public String toString() {
    return "AllowAuthDeviceRequest{" +
        "dev_mac='" + dev_mac + '\'' +
        ", dev_name='" + dev_name + '\'' +
        ", auth=" + auth +
        ", block=" + block +
        '}';
  }
}
