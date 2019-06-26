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
public class SetDHCPRequest {

  private String connectiontype;
  /**
   * 0 未配置
   * 1 已配置
   */
  private int configed;


  public String getConnectiontype() {
    return connectiontype;
  }

  public void setConnectiontype(String connectiontype) {
    this.connectiontype = connectiontype;
  }

  public int getConfiged() {
    return configed;
  }

  public void setConfiged(int configed) {
    this.configed = configed;
  }
}
