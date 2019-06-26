package com.qtec.cateye.model.response;

/**
 * <pre>
 *     author :
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *
 *
 *     version: 1.0
 * </pre>
 */
public class SetAllDoorBellRecordReadRequest {
  private String userUniqueKey;
  private String deviceSerialNo;

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

  @Override
  public String toString() {
    return "SetAllDoorBellRecordReadRequest{" +
        "userUniqueKey='" + userUniqueKey + '\'' +
        ", deviceSerialNo='" + deviceSerialNo + '\'' +
        '}';
  }
}

