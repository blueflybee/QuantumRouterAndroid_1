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
public class GetUserRoleResponse {

  public static final String ADMIN = "0";
  public static final String NO_ADMIN = "1";

  /**
   * userRole : 0管理员，1非管理员
   */

  private String userRole;

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }
}
