package com.qtec.router.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SearchIntelDevResponse<T> implements Serializable {

  private T devlist;

  public T getDevlist() {
    return devlist;
  }

  public void setDevlist(T devlist) {
    this.devlist = devlist;
  }

  public static class IntelDev implements Serializable {

    private String devid;
    private String devname;

    public String getDevid() {
      return devid;
    }

    public void setDevid(String devid) {
      this.devid = devid;
    }

    public String getDevname() {
      return devname;
    }

    public void setDevname(String devname) {
      this.devname = devname;
    }

    @Override
    public String toString() {
      return "DeviceBean{" +
          "devid=" + devid +
          ", devname='" + devname + '\'' +
          '}';
    }
  }


}
