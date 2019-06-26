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
public class GetLiteUpdateResponse {

  /**
   * upgradeStatus : 升级结果,0表示升级成功,-1表示升级失败,1表示升级中
   */

  private String upgradeStatus;

  public String getUpgradeStatus() {
    return upgradeStatus;
  }

  public void setUpgradeStatus(String upgradeStatus) {
    this.upgradeStatus = upgradeStatus;
  }
}
