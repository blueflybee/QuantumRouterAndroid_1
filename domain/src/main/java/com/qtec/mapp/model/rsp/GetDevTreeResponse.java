package com.qtec.mapp.model.rsp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/16
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetDevTreeResponse<T> implements Serializable {

  public static final String ADMIN = "0";
  public static final String NO_ADMIN = "1";

  private String deviceName;
  private String deviceVersion;
  private String deviceType;
  private String deviceSerialNo;
  private String routerSerialNo;
  private String deviceModel;
  private String deviceShareNum;
  private String userRole;
  private String mac;
  //"deviceDetail": "{\"bluetoothName\":\"dddddd\"}",
  private String deviceDetail;

  private T deviceList;

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public String getDeviceVersion() {
    return deviceVersion;
  }

  public void setDeviceVersion(String deviceVersion) {
    this.deviceVersion = deviceVersion;
  }

  public String getDeviceType() {
    return deviceType;
  }

  public void setDeviceType(String deviceType) {
    this.deviceType = deviceType;
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getDeviceShareNum() {
    return deviceShareNum;
  }

  public void setDeviceShareNum(String deviceShareNum) {
    this.deviceShareNum = deviceShareNum;
  }

  public String getUserRole() {
    return userRole;
  }

  public void setUserRole(String userRole) {
    this.userRole = userRole;
  }

  public String getMac() {
    return mac;
  }

  public void setMac(String mac) {
    this.mac = mac;
  }

  public String getDeviceDetail() {
    return deviceDetail;
  }

  public void setDeviceDetail(String deviceDetail) {
    this.deviceDetail = deviceDetail;
  }

  public T getDeviceList() {
    return deviceList;
  }

  public void setDeviceList(T deviceList) {
    this.deviceList = deviceList;
  }

  public String getRouterSerialNo() {
    return routerSerialNo;
  }

  public void setRouterSerialNo(String routerSerialNo) {
    this.routerSerialNo = routerSerialNo;
  }

  public String parseDeviceDetail(String key) {
    if ("".equals(key) || key == null) return "";
    if ("".equals(getDeviceDetail()) || getDeviceDetail() == null) return "";
    try {
      JsonElement jsonElement = new JsonParser().parse(getDeviceDetail());
      JsonObject asJsonObject = jsonElement.getAsJsonObject();
      return asJsonObject.get(key).getAsString();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  public static class DeviceBean implements Serializable {

    private String deviceSerialNo;
    private String deviceVersion;
    private String deviceModel;
    private String deviceType;
    private String deviceName;
    private String deviceStatus;

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

    public String getDeviceName() {
      return deviceName;
    }

    public void setDeviceName(String deviceName) {
      this.deviceName = deviceName;
    }

    public String getDeviceStatus() {
      return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
      this.deviceStatus = deviceStatus;
    }

    @Override
    public String toString() {
      return "DeviceBean{" +
          "deviceSerialNo='" + deviceSerialNo + '\'' +
          ", deviceVersion='" + deviceVersion + '\'' +
          ", deviceModel='" + deviceModel + '\'' +
          ", deviceType='" + deviceType + '\'' +
          ", deviceName='" + deviceName + '\'' +
          ", deviceStatus='" + deviceStatus + '\'' +
          '}';
    }
  }

  @Override
  public String toString() {
    return "GetDevTreeResponse{" +
        "deviceName='" + deviceName + '\'' +
        ", deviceVersion='" + deviceVersion + '\'' +
        ", deviceType='" + deviceType + '\'' +
        ", deviceSerialNo='" + deviceSerialNo + '\'' +
        ", routerSerialNo='" + routerSerialNo + '\'' +
        ", deviceModel='" + deviceModel + '\'' +
        ", deviceShareNum='" + deviceShareNum + '\'' +
        ", userRole='" + userRole + '\'' +
        ", mac='" + mac + '\'' +
        ", deviceDetail='" + deviceDetail + '\'' +
        ", deviceList=" + deviceList +
        '}';
  }
}
