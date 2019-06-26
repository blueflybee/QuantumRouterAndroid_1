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
public class UpdateFirmwareRequest {

  /**
   * keepconfig : 1（当前配置是否保存）
   */
  private int keepconfig;

  public int getKeepconfig() {
    return keepconfig;
  }

  public void setKeepconfig(int keepconfig) {
    this.keepconfig = keepconfig;
  }

}
