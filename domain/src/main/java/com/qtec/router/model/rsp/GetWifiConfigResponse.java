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
public class GetWifiConfigResponse {

  /**
   * lfssid : xxx
   * lfkey : xxxxxxxx
   * lfdisabled : 1
   * lfhiden : 1
   * hfssid : xxx
   * hfkey : xxxxxxxx
   * hfdisabled : 1
   * hfhiden : 1
   */

  private String lfssid;
  private String lfkey;
  private String lfdisabled;
  private String lfhiden;
  private String hfssid;
  private String hfkey;
  private String hfdisabled;
  private String hfhiden;

  public String getLfssid() {
    return lfssid;
  }

  public void setLfssid(String lfssid) {
    this.lfssid = lfssid;
  }

  public String getLfkey() {
    return lfkey;
  }

  public void setLfkey(String lfkey) {
    this.lfkey = lfkey;
  }

  public String getLfdisabled() {
    return lfdisabled;
  }

  public void setLfdisabled(String lfdisabled) {
    this.lfdisabled = lfdisabled;
  }

  public String getLfhiden() {
    return lfhiden;
  }

  public void setLfhiden(String lfhiden) {
    this.lfhiden = lfhiden;
  }

  public String getHfssid() {
    return hfssid;
  }

  public void setHfssid(String hfssid) {
    this.hfssid = hfssid;
  }

  public String getHfkey() {
    return hfkey;
  }

  public void setHfkey(String hfkey) {
    this.hfkey = hfkey;
  }

  public String getHfdisabled() {
    return hfdisabled;
  }

  public void setHfdisabled(String hfdisabled) {
    this.hfdisabled = hfdisabled;
  }

  public String getHfhiden() {
    return hfhiden;
  }

  public void setHfhiden(String hfhiden) {
    this.hfhiden = hfhiden;
  }
}
