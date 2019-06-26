package com.fernandocejas.android10.sample.presentation.view.device.router.tools.data;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/21
 *      desc: wifi时段
 *      version: 1.0
 * </pre>
 */

public class SpecialAttentionBean implements Serializable{
  private String mac;
  private String mode;
  private String name;
  private int type;
  private Boolean onLine;
  private Boolean offLine;

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Boolean getOnLine() {
    return onLine;
  }

  public void setOnLine(Boolean onLine) {
    this.onLine = onLine;
  }

  public Boolean getOffLine() {
    return offLine;
  }

  public void setOffLine(Boolean offLine) {
    this.offLine = offLine;
  }

  @Override
  public String toString() {
    return "SpecialAttentionBean{" +
        "mac='" + mac + '\'' +
        ", mode='" + mode + '\'' +
        ", name='" + name + '\'' +
        ", type=" + type +
        ", onLine=" + onLine +
        ", offLine=" + offLine +
        '}';
  }
}
