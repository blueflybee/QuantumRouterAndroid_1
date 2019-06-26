package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetSignalRegulationResponse {
  private int mode;

  public void setMode(int mode) {
    this.mode = mode;
  }

  public int getMode() {
    return mode;
  }

  @Override
  public String toString() {
    return "GetSignalRegulationResponse{" +
        "mode=" + mode +
        '}';
  }
}
