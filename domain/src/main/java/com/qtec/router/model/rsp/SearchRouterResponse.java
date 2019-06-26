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
public class SearchRouterResponse implements Serializable {

  private String hostname;
  private String version;
  private String serialnum;
  private String devmodel;
  private int configured;


  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getSerialnum() {
    return serialnum;
  }

  public void setSerialnum(String serialnum) {
    this.serialnum = serialnum;
  }

  public String getDevmodel() {
    return devmodel;
  }

  public void setDevmodel(String devmodel) {
    this.devmodel = devmodel;
  }

  public int getConfigured() {
    return configured;
  }

  public void setConfigured(int configured) {
    this.configured = configured;
  }

  @Override
  public String toString() {
    return "SearchRouterResponse{" +
        "hostname='" + hostname + '\'' +
        ", version='" + version + '\'' +
        ", serialnum='" + serialnum + '\'' +
        ", devmodel='" + devmodel + '\'' +
        ", configured='" + configured + '\'' +
        '}';
  }
}
