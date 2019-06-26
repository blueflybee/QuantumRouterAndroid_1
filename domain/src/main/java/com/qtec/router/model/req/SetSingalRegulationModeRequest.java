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
public class SetSingalRegulationModeRequest {
  private int mode;

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  @Override
  public String toString() {
    return "SetSingalRegulationModeRequest{" +
        "mode=" + mode +
        '}';
  }
}
