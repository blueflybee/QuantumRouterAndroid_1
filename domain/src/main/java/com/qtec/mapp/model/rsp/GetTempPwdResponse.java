package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-9-11
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetTempPwdResponse {

  /**
   * tempPassword : 一个临时密码
   */

  private String tempPassword;

  public String getTempPassword() {
    return tempPassword;
  }

  public void setTempPassword(String tempPassword) {
    this.tempPassword = tempPassword;
  }
}
