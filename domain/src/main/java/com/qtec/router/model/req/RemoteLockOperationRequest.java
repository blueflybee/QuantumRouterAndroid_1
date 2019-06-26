package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RemoteLockOperationRequest {
  private String devid;
  private int len;
  private String  encrypdata;

  private String cmdType;

  public String getDevid() {
    return devid;
  }

  public void setDevid(String devid) {
    this.devid = devid;
  }

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

  public String getCmdType() {
    return cmdType;
  }

  public void setCmdType(String cmdType) {
    this.cmdType = cmdType;
  }
}
