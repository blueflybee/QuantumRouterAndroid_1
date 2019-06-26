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
public class SetGuestWifiSwitchRequest {
  private int enable;
  private String name;
  private int isHide;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getIsHide() {
    return isHide;
  }

  public void setIsHide(int isHide) {
    this.isHide = isHide;
  }

  @Override
  public String toString() {
    return "SetGuestWifiSwitchResponse{" +
        "enable=" + enable +
        ", name='" + name + '\'' +
        ", isHide=" + isHide +
        '}';
  }
}
