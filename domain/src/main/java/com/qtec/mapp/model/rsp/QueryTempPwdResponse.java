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
public class QueryTempPwdResponse {

  /**
   * tempSize : 剩余临时密码数量
   */

  private long tempSize;

  public long getTempSize() {
    return tempSize;
  }

  public void setTempSize(long tempSize) {
    this.tempSize = tempSize;
  }
}
