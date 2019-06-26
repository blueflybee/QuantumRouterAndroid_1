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
public class DeleteLockUserRequest {


  /**
   * userUniqueKey : 管理员用户唯一标识
   * deviceSerialNo : 门锁唯一标识
   * generalUserUniqueKey : 被删除用户唯一标识
   */

  private String userUniqueKey;
  private String deviceSerialNo;
  private String generalUserUniqueKey;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getGeneralUserUniqueKey() {
    return generalUserUniqueKey;
  }

  public void setGeneralUserUniqueKey(String generalUserUniqueKey) {
    this.generalUserUniqueKey = generalUserUniqueKey;
  }
}
