package com.fernandocejas.android10.sample.presentation.view.mine.data;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class UpdateData {

  /**
   * url : http://192.168.205.33:8080/Hello/app_v3.0.1_Other_20150116.apk
   * versionCode : 2
   * updateMessage : 版本更新信息
   */

  private String url;
  private int versionCode;
  private String updateMessage;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getVersionCode() {
    return versionCode;
  }

  public void setVersionCode(int versionCode) {
    this.versionCode = versionCode;
  }

  public String getUpdateMessage() {
    return updateMessage;
  }

  public void setUpdateMessage(String updateMessage) {
    this.updateMessage = updateMessage;
  }
}
