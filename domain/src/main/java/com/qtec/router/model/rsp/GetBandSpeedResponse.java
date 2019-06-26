package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetBandSpeedResponse {
  private int speedtest;
  private String downspeed;
  private String upspeed;
  private String download;
  private String upload;

  public int getSpeedtest() {
    return speedtest;
  }

  public void setSpeedtest(int speedtest) {
    this.speedtest = speedtest;
  }

  public String getDownspeed() {
    return downspeed;
  }

  public void setDownspeed(String downspeed) {
    this.downspeed = downspeed;
  }

  public String getUpspeed() {
    return upspeed;
  }

  public void setUpspeed(String upspeed) {
    this.upspeed = upspeed;
  }

  public String getDownload() {
    return download;
  }

  public void setDownload(String download) {
    this.download = download;
  }

  public String getUpload() {
    return upload;
  }

  public void setUpload(String upload) {
    this.upload = upload;
  }

  @Override
  public String toString() {
    return "GetBandSpeedResponse{" +
        "speedtest=" + speedtest +
        ", downspeed='" + downspeed + '\'' +
        ", upspeed='" + upspeed + '\'' +
        ", download='" + download + '\'' +
        ", upload='" + upload + '\'' +
        '}';
  }
}
