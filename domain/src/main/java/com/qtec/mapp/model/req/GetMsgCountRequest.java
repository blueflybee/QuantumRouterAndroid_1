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
public class GetMsgCountRequest {
  private String userUniqueKey;

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  @Override
  public String toString() {
    return "GetMsgCountResponse{" +
        "userUniqueKey='" + userUniqueKey + '\'' +
        '}';
  }
}
