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
public class GetRouterInfoCloudResponse {


  /**
   * routerNickName : 13557321203
   * groupName : 123456
   * description :
   */

  private String routerNickName;
  private String groupName;
  private String description;

  public String getRouterNickName() {
    return routerNickName;
  }

  public void setRouterNickName(String routerNickName) {
    this.routerNickName = routerNickName;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "GetRouterInfoCloudResponse{" +
        "routerNickName='" + routerNickName + '\'' +
        ", groupName=" + groupName +
        ", description='" + description + '\'' +
        '}';
  }
}
