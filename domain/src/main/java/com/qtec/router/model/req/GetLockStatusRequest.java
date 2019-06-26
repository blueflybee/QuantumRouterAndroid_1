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
public class GetLockStatusRequest {
  private String devid;

  public void setDevid(String devid) {
    this.devid = devid;
  }

  public String getDevid() {
    return devid;
  }

  @Override
  public String toString() {
    return "GetLockStatusRequest{" +
        "devid='" + devid + '\'' +
        '}';
  }
}
