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
public class LoginResponse {

  private int id;
  private String userHeadImg;
  private String userNickName;
  private String userPhone;
  private String token;
  private String userUniqueKey;

  public String getUserHeadImg() {
    return userHeadImg;
  }

  public void setUserHeadImg(String userHeadImg) {
    this.userHeadImg = userHeadImg;
  }

  public String getUserNickName() {
    return userNickName;
  }

  public void setUserNickName(String userNickName) {
    this.userNickName = userNickName;
  }

  public String getUserPhone() {
    return userPhone;
  }

  public void setUserPhone(String userPhone) {
    this.userPhone = userPhone;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    LoginResponse response = (LoginResponse) o;

    if (id != response.id) return false;
    if (userHeadImg != null ? !userHeadImg.equals(response.userHeadImg) : response.userHeadImg != null)
      return false;
    if (userNickName != null ? !userNickName.equals(response.userNickName) : response.userNickName != null)
      return false;
    if (userPhone != null ? !userPhone.equals(response.userPhone) : response.userPhone != null)
      return false;
    if (token != null ? !token.equals(response.token) : response.token != null) return false;
    return userUniqueKey != null ? userUniqueKey.equals(response.userUniqueKey) : response.userUniqueKey == null;

  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (userHeadImg != null ? userHeadImg.hashCode() : 0);
    result = 31 * result + (userNickName != null ? userNickName.hashCode() : 0);
    result = 31 * result + (userPhone != null ? userPhone.hashCode() : 0);
    result = 31 * result + (token != null ? token.hashCode() : 0);
    result = 31 * result + (userUniqueKey != null ? userUniqueKey.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "LoginResponse{" +
        "id=" + id +
        ", userHeadImg='" + userHeadImg + '\'' +
        ", userNickName='" + userNickName + '\'' +
        ", userPhone='" + userPhone + '\'' +
        ", token='" + token + '\'' +
        ", userUniqueKey='" + userUniqueKey + '\'' +
        '}';
  }
}
