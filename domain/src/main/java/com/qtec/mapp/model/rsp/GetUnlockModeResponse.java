package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetUnlockModeResponse {


  /**
   * passwordOpenConfig : 0，不需要开启蓝牙（默认）；1，需要开启蓝牙
   * openConfig : fingerprint 0，不需要开启蓝牙（默认）；1，需要开启蓝牙
   * doorcardOpenConfig : 0，不需要开启蓝牙（默认）；1，需要开启蓝牙
   */
  private String passwordOpenConfig;
  private String openConfig;
  private String doorcardOpenConfig;

  public String getPasswordOpenConfig() {
    return passwordOpenConfig;
  }

  public void setPasswordOpenConfig(String passwordOpenConfig) {
    this.passwordOpenConfig = passwordOpenConfig;
  }

  public String getOpenConfig() {
    return openConfig;
  }

  public void setOpenConfig(String openConfig) {
    this.openConfig = openConfig;
  }

  public String getDoorcardOpenConfig() {
    return doorcardOpenConfig;
  }

  public void setDoorcardOpenConfig(String doorcardOpenConfig) {
    this.doorcardOpenConfig = doorcardOpenConfig;
  }
}
