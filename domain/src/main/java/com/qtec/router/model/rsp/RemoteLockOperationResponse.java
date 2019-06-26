package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RemoteLockOperationResponse {

  private int len;
  private String encrypdata;

  public int getLen() {
    return len;
  }

  public void setLen(int len) {
    this.len = len;
  }

  public String getEncrypdata() {
    return encrypdata;
  }

  public void setEncrypdata(String encrypdata) {
    this.encrypdata = encrypdata;
  }
}
