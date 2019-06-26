package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class QueryBindRouterToLockResponse {

  /**
   * 门锁绑定安全路由器的查询字段
   */
  private int contained;
  /**
   * LITE网关绑定门锁的查询字段
   */
  private String encrypdata;

  public int getContained() {
    return contained;
  }

  public void setContained(int contained) {
    this.contained = contained;
  }

  public String getEncrypdata() {
    return encrypdata;
  }

  public void setEncrypdata(String encrypdata) {
    this.encrypdata = encrypdata;
  }
}
