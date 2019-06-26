package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CommitAddRouterInfoResponse {
  private String catBindStatus;
  private String availableLockStatus;

  public String getCatBindStatus() {
    return catBindStatus;
  }

  public void setCatBindStatus(String catBindStatus) {
    this.catBindStatus = catBindStatus;
  }

  public String getAvailableLockStatus() {
    return availableLockStatus;
  }

  public void setAvailableLockStatus(String availableLockStatus) {
    this.availableLockStatus = availableLockStatus;
  }

  @Override
  public String toString() {
    return "CommitAddRouterInfoResponse{" +
        "catBindStatus='" + catBindStatus + '\'' +
        ", availableLockStatus='" + availableLockStatus + '\'' +
        '}';
  }
}
