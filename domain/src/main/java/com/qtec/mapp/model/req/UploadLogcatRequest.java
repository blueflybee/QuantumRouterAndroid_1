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
public class UploadLogcatRequest {

  private String androidlog;
  private String phone;
  private String time;
  private String brand;
  private String model;
  private String appversion;

  public String getAndroidlog() {
    return androidlog;
  }

  public void setAndroidlog(String androidlog) {
    this.androidlog = androidlog;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getAppversion() {
    return appversion;
  }

  public void setAppversion(String appversion) {
    this.appversion = appversion;
  }
}
