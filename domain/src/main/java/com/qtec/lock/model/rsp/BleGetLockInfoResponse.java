package com.qtec.lock.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BleGetLockInfoResponse {

  private String rspCode;
  private String deviceId;
  private String version;
  private String model;
  private String type;
  private String name;

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRspCode() {
    return rspCode;
  }

  public void setRspCode(String rspCode) {
    this.rspCode = rspCode;
  }

  @Override
  public String toString() {
    return "BleGetLockInfoResponse{" +
        "rspCode='" + rspCode + '\'' +
        ", deviceId='" + deviceId + '\'' +
        ", version='" + version + '\'' +
        ", model='" + model + '\'' +
        ", type='" + type + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
