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
public class BleAddLockPwdResponse {

  private String code;
  private String pwdId;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPwdId() {
    return pwdId;
  }

  public void setPwdId(String pwdId) {
    this.pwdId = pwdId;
  }

  @Override
  public String toString() {
    return "BleAddLockPwdResponse{" +
        "code='" + code + '\'' +
        ", pwdId='" + pwdId + '\'' +
        '}';
  }
}
