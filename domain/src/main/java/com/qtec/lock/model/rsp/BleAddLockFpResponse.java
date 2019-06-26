package com.qtec.lock.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BleAddLockFpResponse {

  private String code;
  private String fpId;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getFpId() {
    return fpId;
  }

  public void setFpId(String fpId) {
    this.fpId = fpId;
  }

  @Override
  public String toString() {
    return "BleAddLockFpResponse{" +
        "code='" + code + '\'' +
        ", fpId='" + fpId + '\'' +
        '}';
  }
}
