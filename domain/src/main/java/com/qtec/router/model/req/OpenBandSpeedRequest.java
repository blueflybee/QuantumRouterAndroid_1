package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class OpenBandSpeedRequest {
  private int action;

  public int getAction() {
    return action;
  }

  public void setAction(int action) {
    this.action = action;
  }

  @Override
  public String toString() {
    return "OpenBandSpeedRequest{" +
        "action=" + action +
        '}';
  }
}
