package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-8
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UpdateLiteResponse {

  /**
   * upgradeStatus : 升级状态,1代表升级中...
   */

  private String upgradeStatus;

  public String getUpgradeStatus() {
    return upgradeStatus;
  }

  public void setUpgradeStatus(String upgradeStatus) {
    this.upgradeStatus = upgradeStatus;
  }
}
