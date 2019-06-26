package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 设备共享成员
 *     version: 1.0
 * </pre>
 */
public class QueryLockedCatResponse implements Serializable{
  private String bindStatus;

  public void setBindStatus(String bindStatus) {
    this.bindStatus = bindStatus;
  }

  public String getBindStatus() {
    return bindStatus;
  }

  @Override
  public String toString() {
    return "QueryLockedCatResponse{" +
        "bindStatus='" + bindStatus + '\'' +
        '}';
  }
}
