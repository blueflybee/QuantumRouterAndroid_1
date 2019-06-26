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
public class PostInspectionResponse {
  private int ddos;
  private int telent;
  private int ftp;
  private int samba;
  private int virtualservice;
  private int dmz;

  public int getDdos() {
    return ddos;
  }

  public void setDdos(int ddos) {
    this.ddos = ddos;
  }

  public int getTelent() {
    return telent;
  }

  public void setTelent(int telent) {
    this.telent = telent;
  }

  public int getFtp() {
    return ftp;
  }

  public void setFtp(int ftp) {
    this.ftp = ftp;
  }

  public int getSamba() {
    return samba;
  }

  public void setSamba(int samba) {
    this.samba = samba;
  }

  public int getVirtualservice() {
    return virtualservice;
  }

  public void setVirtualservice(int virtualservice) {
    this.virtualservice = virtualservice;
  }

  public int getDmz() {
    return dmz;
  }

  public void setDmz(int dmz) {
    this.dmz = dmz;
  }

  @Override
  public String toString() {
    return "PostInspectionResponse{" +
        "ddos=" + ddos +
        ", telent=" + telent +
        ", ftp=" + ftp +
        ", samba=" + samba +
        ", virtualservice=" + virtualservice +
        ", dmz=" + dmz +
        '}';
  }
}
