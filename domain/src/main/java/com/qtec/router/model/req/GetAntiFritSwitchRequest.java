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
public class GetAntiFritSwitchRequest {
  private int router_access;
  private int lan_dev_access;

  public int getRouter_access() {
    return router_access;
  }

  public void setRouter_access(int router_access) {
    this.router_access = router_access;
  }

  public int getLan_dev_access() {
    return lan_dev_access;
  }

  public void setLan_dev_access(int lan_dev_access) {
    this.lan_dev_access = lan_dev_access;
  }

  @Override
  public String toString() {
    return "GetAntiFritSwitchRequest{" +
        "router_access=" + router_access +
        ", lan_dev_access=" + lan_dev_access +
        '}';
  }
}
