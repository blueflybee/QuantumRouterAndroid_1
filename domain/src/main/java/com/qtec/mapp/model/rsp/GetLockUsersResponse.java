package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetLockUsersResponse implements Serializable{

  /**
   * userId : 用户id，当管理员要删除用户时，用此id和门锁进行交互
   * userUniqueKey : 用户唯一标识
   * userNickName : 用户昵称
   * userHeadImage : 用户头像
   * userPhone : 用户手机号
   * userRole : 用户角色：0为管理员，1为普通用户
   * deviceSerialNo : 设备序列号
   */

  private String userId;
  private String userUniqueKey;
  private String userNickName;
  private String userHeadImage;
  private String userPhone;
  private String userRole;
  private String deviceSerialNo;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getUserNickName() {
    return userNickName;
  }

  public void setUserNickName(String userNickName) {
    this.userNickName = userNickName;
  }

  public String getUserHeadImage() {
    return userHeadImage;
  }

  public void setUserHeadImage(String userHeadImage) {
    this.userHeadImage = userHeadImage;
  }

  public String getUserPhone() {
    return userPhone;
  }

  public void setUserPhone(String userPhone) {
    this.userPhone = userPhone;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  @Override
  public String toString() {
    return "GetLockUsersResponse{" +
        "userId='" + userId + '\'' +
        ", userUniqueKey='" + userUniqueKey + '\'' +
        ", userNickName='" + userNickName + '\'' +
        ", userHeadImage='" + userHeadImage + '\'' +
        ", userPhone='" + userPhone + '\'' +
        ", userRole='" + userRole + '\'' +
        ", deviceSerialNo='" + deviceSerialNo + '\'' +
        '}';
  }
}
