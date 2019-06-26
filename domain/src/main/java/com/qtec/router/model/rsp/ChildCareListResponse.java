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
public class ChildCareListResponse{
  private String macaddr;
  private String starttime;
  private String stoptime;
  private String weekdays;
  private int enabled;

  public String getMacaddr() {
    return macaddr;
  }

  public void setMacaddr(String macaddr) {
    this.macaddr = macaddr;
  }

  public String getStarttime() {
    return starttime;
  }

  public void setStarttime(String starttime) {
    this.starttime = starttime;
  }

  public String getStoptime() {
    return stoptime;
  }

  public void setStoptime(String stoptime) {
    this.stoptime = stoptime;
  }

  public String getWeekdays() {
    return weekdays;
  }

  public void setWeekdays(String weekdays) {
    this.weekdays = weekdays;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  @Override
  public String toString() {
    return "ChildCareListResponse{" +
        "macaddr='" + macaddr + '\'' +
        ", starttime='" + starttime + '\'' +
        ", stoptime='" + stoptime + '\'' +
        ", weekdays='" + weekdays + '\'' +
        ", enabled=" + enabled +
        '}';
  }
}
