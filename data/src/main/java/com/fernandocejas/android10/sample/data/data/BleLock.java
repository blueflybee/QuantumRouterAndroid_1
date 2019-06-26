package com.fernandocejas.android10.sample.data.data;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BleLock implements Serializable {
  private String id;
  private String deviceName;
  private String type;
  private String bindRouterId;
  private String model;
  private String mac;
  private String version;
  private String bleName;
  private String keyRepoId;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getBindRouterId() {
    return bindRouterId;
  }

  public void setBindRouterId(String bindRouterId) {
    this.bindRouterId = bindRouterId;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getBleName() {
    return bleName;
  }

  public void setBleName(String bleName) {
    this.bleName = bleName;
  }

  public String getKeyRepoId() {
    return keyRepoId;
  }

  public void setKeyRepoId(String keyRepoId) {
    this.keyRepoId = keyRepoId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BleLock bleLock = (BleLock) o;

    if (id != null ? !id.equals(bleLock.id) : bleLock.id != null) return false;
    return mac != null ? mac.equals(bleLock.mac) : bleLock.mac == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (mac != null ? mac.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "BleLock{" +
        "id='" + id + '\'' +
        ", deviceName='" + deviceName + '\'' +
        ", type='" + type + '\'' +
        ", bindRouterId='" + bindRouterId + '\'' +
        ", model='" + model + '\'' +
        ", mac='" + mac + '\'' +
        ", version='" + version + '\'' +
        ", bleName='" + bleName + '\'' +
        ", keyRepoId='" + keyRepoId + '\'' +
        '}';
  }
}
