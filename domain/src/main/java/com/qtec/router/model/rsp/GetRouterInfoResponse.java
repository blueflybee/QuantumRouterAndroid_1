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
public class GetRouterInfoResponse {

  private String lfwifissid;
  private String hfwifissid;
  private String lanipaddress;
  private String wanipaddress;
  private String wanmac;
  private String cputype;
  private String cpubrand;
  private String cpufactory;

  public String getLfwifissid() {
    return lfwifissid;
  }

  public void setLfwifissid(String lfwifissid) {
    this.lfwifissid = lfwifissid;
  }

  public String getHfwifissid() {
    return hfwifissid;
  }

  public void setHfwifissid(String hfwifissid) {
    this.hfwifissid = hfwifissid;
  }

  public String getLanipaddress() {
    return lanipaddress;
  }

  public void setLanipaddress(String lanipaddress) {
    this.lanipaddress = lanipaddress;
  }

  public String getWanipaddress() {
    return wanipaddress;
  }

  public void setWanipaddress(String wanipaddress) {
    this.wanipaddress = wanipaddress;
  }

  public String getCputype() {
    return cputype;
  }

  public void setCputype(String cputype) {
    this.cputype = cputype;
  }

  public String getCpubrand() {
    return cpubrand;
  }

  public void setCpubrand(String cpubrand) {
    this.cpubrand = cpubrand;
  }

  public String getCpufactory() {
    return cpufactory;
  }

  public void setCpufactory(String cpufactory) {
    this.cpufactory = cpufactory;
  }

  public void setWanmac(String wanmac) {
    this.wanmac = wanmac;
  }

  public String getWanmac() {
    return wanmac;
  }

  @Override
  public String toString() {
    return "GetRouterInfoResponse{" +
        "lfwifissid='" + lfwifissid + '\'' +
        ", hfwifissid='" + hfwifissid + '\'' +
        ", lanipaddress='" + lanipaddress + '\'' +
        ", wanipaddress='" + wanipaddress + '\'' +
        ", wanmac='" + wanmac + '\'' +
        ", cputype='" + cputype + '\'' +
        ", cpubrand='" + cpubrand + '\'' +
        ", cpufactory='" + cpufactory + '\'' +
        '}';
  }
}
