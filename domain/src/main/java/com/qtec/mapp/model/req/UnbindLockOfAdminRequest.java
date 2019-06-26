package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :管理员解绑门锁且转让管理员
 *     version: 1.0
 * </pre>
 */
public class UnbindLockOfAdminRequest {

  /**
   * userUniqueKey : 管理员用户唯一标识
   * generalUserUniqueKey : 被转让用户唯一标识
   * deviceSerialNo : 设备序列号
   * isUnBind : 是否解绑：0否1是
   */

  private String userUniqueKey;
  private String generalUserUniqueKey;
  private String deviceSerialNo;
  private String isUnBind;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getGeneralUserUniqueKey() {
    return generalUserUniqueKey;
  }

  public void setGeneralUserUniqueKey(String generalUserUniqueKey) {
    this.generalUserUniqueKey = generalUserUniqueKey;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getIsUnBind() {
    return isUnBind;
  }

  public void setIsUnBind(String isUnBind) {
    this.isUnBind = isUnBind;
  }
}
