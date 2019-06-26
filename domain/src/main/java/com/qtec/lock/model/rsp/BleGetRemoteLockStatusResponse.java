package com.qtec.lock.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BleGetRemoteLockStatusResponse {

  private String code;
  private boolean lock;
  private boolean ble;
  private boolean zigBee;
  private boolean battery;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public boolean isLock() {
    return lock;
  }

  public void setLock(boolean lock) {
    this.lock = lock;
  }

  public boolean isBle() {
    return ble;
  }

  public void setBle(boolean ble) {
    this.ble = ble;
  }

  public boolean isZigBee() {
    return zigBee;
  }

  public void setZigBee(boolean zigBee) {
    this.zigBee = zigBee;
  }

  public boolean isBattery() {
    return battery;
  }

  public void setBattery(boolean battery) {
    this.battery = battery;
  }

  @Override
  public String toString() {
    return "BleGetRemoteLockStatusResponse{" +
        "code='" + code + '\'' +
        ", lock=" + lock +
        ", ble=" + ble +
        ", zigBee=" + zigBee +
        ", battery=" + battery +
        '}';
  }
}
