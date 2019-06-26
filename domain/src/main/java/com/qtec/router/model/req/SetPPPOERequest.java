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
public class SetPPPOERequest {


  /**
   * connectiontype : pppoe
   * username : wjj
   * password : xxx
   */

  private String connectiontype;
  private String username;
  private String password;

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

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getConfiged() {
    return configed;
  }

  public void setConfiged(int configed) {
    this.configed = configed;
  }
}
