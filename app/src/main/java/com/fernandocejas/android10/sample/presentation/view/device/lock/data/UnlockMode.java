package com.fernandocejas.android10.sample.presentation.view.device.lock.data;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UnlockMode {

  public static final String UNLOCK = "01";
  public static final String UNLOCK_BLE = "00";

  public static final String UNLOCK_CLOUD = "0";
  private String name;
  /**
   * 01:不需要蓝牙鉴权
   * 00:需要蓝牙鉴权
   */
  private String bleCode;

  /**
   * unlockModeCloud : 0，仅指纹开锁；1，手机连接蓝牙后指纹开锁（默认）
   */
  private String unlockModeCloud;

  public UnlockMode() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBleCode() {
    return bleCode;
  }

  public void setBleCode(String bleCode) {
    this.bleCode = bleCode;
  }

  public String getUnlockModeCloud() {
    return unlockModeCloud;
  }

  public void setUnlockModeCloud(String unlockModeCloud) {
    this.unlockModeCloud = unlockModeCloud;
  }

  public boolean unlockWithoutBle() {
    return UNLOCK.equals(getBleCode());
  }

  public boolean unlockWithoutBleCloud(String cloudConfig) {
    return UNLOCK_CLOUD.equals(cloudConfig);
  }

  @Override
  public String toString() {
    return "UnlockMode{" +
        "name='" + name + '\'' +
        ", bleCode='" + bleCode + '\'' +
        ", unlockModeCloud='" + unlockModeCloud + '\'' +
        '}';
  }
}
