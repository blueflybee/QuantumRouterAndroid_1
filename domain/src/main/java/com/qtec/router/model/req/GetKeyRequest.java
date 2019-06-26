package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 后续补充密钥接口
 *     version: 1.0
 * </pre>
 */
public class GetKeyRequest {

  private int keytype;
  private int keynumber;
  private int requestid;
  private String devicename;

  public int getKeytype() {
    return keytype;
  }

  public void setKeytype(int keytype) {
    this.keytype = keytype;
  }

  public int getKeynumber() {
    return keynumber;
  }

  public void setKeynumber(int keynumber) {
    this.keynumber = keynumber;
  }

  public int getRequestid() {
    return requestid;
  }

  public void setRequestid(int requestid) {
    this.requestid = requestid;
  }

  public String getDevicename() {
    return devicename;
  }

  public void setDevicename(String devicename) {
    this.devicename = devicename;
  }
}
