package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyRouterDescRequest {


  /**
   * routerSerialNo : 13557321203
   * description :
   * userUniqueKey : 13557321203
   */

  private String routerSerialNo;
  private String description;
  private String userUniqueKey;

  public String getRouterSerialNo() {
    return routerSerialNo;
  }

  public void setRouterSerialNo(String routerSerialNo) {
    this.routerSerialNo = routerSerialNo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }
}
