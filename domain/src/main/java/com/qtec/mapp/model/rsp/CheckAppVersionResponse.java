package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CheckAppVersionResponse {

  /**
   * versionNo : 版本号如v.xx.xx
   * versionNum : 25
   * versionStatement : 版本说明
   * createTime : 更新时间
   * minVersion : 最小支持版本
   * downloadUrl : 下载地址
   */

  private String versionNo;
  private int versionNum;
  private String versionStatement;
  private String createTime;
  private int minVersion;
  private String downloadUrl;

  public String getVersionNo() {
    return versionNo;
  }

  public void setVersionNo(String versionNo) {
    this.versionNo = versionNo;
  }

  public int getVersionNum() {
    return versionNum;
  }

  public void setVersionNum(int versionNum) {
    this.versionNum = versionNum;
  }

  public String getVersionStatement() {
    return versionStatement;
  }

  public void setVersionStatement(String versionStatement) {
    this.versionStatement = versionStatement;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public int getMinVersion() {
    return minVersion;
  }

  public void setMinVersion(int minVersion) {
    this.minVersion = minVersion;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
