package com.qtec.router.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RouterStatusResponse<T> {
  private String ownip;
  private String lfssid;
  private String hfssid;
  private int devnum;
  private T stalist;
  private int routertx;
  private int routerrx;

  public String getOwnip() {
    return ownip;
  }

  public void setOwnip(String ownip) {
    this.ownip = ownip;
  }

  public String getLfssid() {
    return lfssid;
  }

  public void setLfssid(String lfssid) {
    this.lfssid = lfssid;
  }

  public String getHfssid() {
    return hfssid;
  }

  public void setHfssid(String hfssid) {
    this.hfssid = hfssid;
  }

  public int getDevnum() {
    return devnum;
  }

  public void setDevnum(int devnum) {
    this.devnum = devnum;
  }

  public T getStalist() {
    return stalist;
  }

  public void setStalist(T stalist) {
    this.stalist = stalist;
  }

  public int getRoutertx() {
    return routertx;
  }

  public void setRoutertx(int routertx) {
    this.routertx = routertx;
  }

  public int getRouterrx() {
    return routerrx;
  }

  public void setRouterrx(int routerrx) {
    this.routerrx = routerrx;
  }

  @Override
  public String toString() {
    return "RouterStatusResponse{" +
        "ownip='" + ownip + '\'' +
        ", lfssid='" + lfssid + '\'' +
        ", hfssid='" + hfssid + '\'' +
        ", devnum=" + devnum +
        ", stalist=" + stalist +
        ", routertx=" + routertx +
        ", routerrx=" + routerrx +
        '}';
  }

  public static class Status implements Serializable{

    private String staname;
    private int stastatus;
    private String ipaddr;
    private String macaddr;
    private int devicetype;
    private String accesstype;
    private int tx;
    private int rx;

    public String getStaname() {
      return staname;
    }

    public void setStaname(String staname) {
      this.staname = staname;
    }

    public int getStastatus() {
      return stastatus;
    }

    public void setStastatus(int stastatus) {
      this.stastatus = stastatus;
    }

    public String getIpaddr() {
      return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
      this.ipaddr = ipaddr;
    }

    public String getMacaddr() {
      return macaddr;
    }

    public void setMacaddr(String macaddr) {
      this.macaddr = macaddr;
    }

    public int getDevicetype() {
      return devicetype;
    }

    public void setDevicetype(int devicetype) {
      this.devicetype = devicetype;
    }

    public String getAccesstype() {
      return accesstype;
    }

    public void setAccesstype(String accesstype) {
      this.accesstype = accesstype;
    }

    public int getTx() {
      return tx;
    }

    public void setTx(int tx) {
      this.tx = tx;
    }

    public int getRx() {
      return rx;
    }

    public void setRx(int rx) {
      this.rx = rx;
    }

    @Override
    public String toString() {
      return "Status{" +
          "staname='" + staname + '\'' +
          ", stastatus=" + stastatus +
          ", ipaddr='" + ipaddr + '\'' +
          ", macaddr='" + macaddr + '\'' +
          ", devicetype=" + devicetype +
          ", accesstype='" + accesstype + '\'' +
          ", tx=" + tx +
          ", rx=" + rx +
          '}';
    }
  }

}
