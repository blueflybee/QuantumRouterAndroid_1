package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-8
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CheckLiteVersionResponse {

  /**
   * currentVersionNo : 当前版本号,比如:V0.0.1
   * isLatest : int型(是否为最新版,1表示是最新版,0表示不是)
   * latestVersionNo : 最新版本号,比如:V0.0.2
   * downloadUrl : 更新地址
   */

  private String currentVersionNo;
  private int isLatest;
  private String latestVersionNo;
  private String downloadUrl;

  public String getCurrentVersionNo() {
    return currentVersionNo;
  }

  public void setCurrentVersionNo(String currentVersionNo) {
    this.currentVersionNo = currentVersionNo;
  }

  public int getIsLatest() {
    return isLatest;
  }

  public void setIsLatest(int isLatest) {
    this.isLatest = isLatest;
  }

  public String getLatestVersionNo() {
    return latestVersionNo;
  }

  public void setLatestVersionNo(String latestVersionNo) {
    this.latestVersionNo = latestVersionNo;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
