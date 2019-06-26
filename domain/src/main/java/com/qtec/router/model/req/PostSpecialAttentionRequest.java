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
public class PostSpecialAttentionRequest {
 private String specialcare;

  public String getSpecialcare() {
    return specialcare;
  }

  public void setSpecialcare(String specialcare) {
    this.specialcare = specialcare;
  }

  @Override
  public String toString() {
    return "PostSpecialAttentionRequest{" +
        "specialcare='" + specialcare + '\'' +
        '}';
  }
}
