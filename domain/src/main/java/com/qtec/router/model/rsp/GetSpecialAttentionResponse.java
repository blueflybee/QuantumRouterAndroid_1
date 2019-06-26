package com.qtec.router.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetSpecialAttentionResponse {
  private String specialcare;

  public void setSpecialcare(String specialcare) {
    this.specialcare = specialcare;
  }

  public String getSpecialcare() {
    return specialcare;
  }

  @Override
  public String toString() {
    return "GetSpecialAttentionResponse{" +
        "specialcare='" + specialcare + '\'' +
        '}';
  }
}
