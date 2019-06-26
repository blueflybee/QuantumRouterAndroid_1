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
public class SetWifiSwitchRequest {
  private int enable;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  @Override
  public String toString() {
    return "SetWifiSwitchRequest{" +
        "enable=" + enable +
        '}';
  }
}
