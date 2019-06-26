package com.fernandocejas.android10.sample.presentation.view.device.litegateway.data;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/05/31
 *     desc   : lite gateway设备类
 *     version: 1.0
 * </pre>
 */
public class LiteGateway implements Serializable{
  private String id;
  private boolean isBind;

  public static final String DEVICE_TYPE_LITE_GATEWAY = "4";

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDeviceType() {
    return DEVICE_TYPE_LITE_GATEWAY;
  }

  public boolean isBind() {
    return isBind;
  }

  public void setBind(boolean bind) {
    isBind = bind;
  }

  @Override
  public String toString() {
    return "LiteGateway{" +
        "id='" + id + '\'' +
        ", isBind=" + isBind +
        '}';
  }
}
