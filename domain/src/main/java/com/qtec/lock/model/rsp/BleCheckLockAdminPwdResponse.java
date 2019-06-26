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
public class BleCheckLockAdminPwdResponse {

  private String rspCode;
  private boolean clearFactoryResetFlag;

  public static final String FACTORY_RESET = "01";
  public static final String NO_FACTORY_RESET = "00";

  public String getRspCode() {
    return rspCode;
  }

  public void setRspCode(String rspCode) {
    this.rspCode = rspCode;
  }

  public boolean isClearFactoryResetFlag() {
    return clearFactoryResetFlag;
  }

  public void setClearFactoryResetFlag(String clearFactoryResetFlag) {
    System.out.println("clearFactoryResetFlag = " + clearFactoryResetFlag);
    this.clearFactoryResetFlag = FACTORY_RESET.equals(clearFactoryResetFlag);
  }

  @Override
  public String toString() {
    return "BleCheckLockAdminPwdResponse{" +
        "rspCode='" + rspCode + '\'' +
        ", clearFactoryResetFlag=" + clearFactoryResetFlag +
        '}';
  }
}
