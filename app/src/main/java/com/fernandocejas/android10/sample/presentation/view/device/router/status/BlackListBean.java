package com.fernandocejas.android10.sample.presentation.view.device.router.status;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/14
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class BlackListBean {
  private String name;
  private String mac;
  private int type;
  private int enable;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "BlackListBean{" +
        "name='" + name + '\'' +
        ", mac='" + mac + '\'' +
        ", type=" + type +
        ", enable=" + enable +
        '}';
  }
}
