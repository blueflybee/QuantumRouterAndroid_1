package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-8
 *     desc   : presenter
 *     version: 1.0
 * </pre>
 */
public class UpdateLiteRequest {

  /**
   * routerSerialNo : lite网关序列号
   * downloadUrl : 更新地址
   */

  private String routerSerialNo;
  private String downloadUrl;

  public String getRouterSerialNo() {
    return routerSerialNo;
  }

  public void setRouterSerialNo(String routerSerialNo) {
    this.routerSerialNo = routerSerialNo;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }
}
