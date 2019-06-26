package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CommitAddRouterInfoRequest {

  /**
   * userUniqueKey : 13557321203
   * deviceName : 设备名称
   * deviceSerialNo : 设备序列号
   * deviceVersion : 设备版本
   * deviceModel : 设备型号
   * deviceType : 设备类型:0是网关,1是门锁
   * mac : 设备mac地址(可选)
   * bluetoothName : 蓝牙名称(可选)
   */

  private String userUniqueKey;
  private String deviceName;
  private String deviceSerialNo;
  private String deviceVersion;
  private String deviceModel;
  private String deviceType;
  private String mac;
  private String bluetoothName;
  //{"devicePass":"Yk14k4m7etSj2RoVcggw4kN0+sNEkxvilE1NEDoy64Z8jov/ZJP8oe8YJQG1cg25UOUHEN2ppBxpRYtL3gQHLQ=="}
  private String deviceDetail;

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDeviceVersion() {
    return deviceVersion;
  }

  public void setDeviceVersion(String deviceVersion) {
    this.deviceVersion = deviceVersion;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getBluetoothName() {
    return bluetoothName;
  }

  public void setBluetoothName(String bluetoothName) {
    this.bluetoothName = bluetoothName;
  }

  public String getDeviceDetail() {
    return deviceDetail;
  }

  public void setDeviceDetail(String deviceDetail) {
    this.deviceDetail = deviceDetail;
  }
}
