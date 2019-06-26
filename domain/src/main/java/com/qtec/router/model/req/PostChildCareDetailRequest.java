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
public class PostChildCareDetailRequest {
  private String macaddr;
  private String start_time;
  private String stop_time;
  private String weekdays;
  private int enabled;

  public String getMacaddr() {
    return macaddr;
  }

  public void setMacaddr(String macaddr) {
    this.macaddr = macaddr;
  }

  public String getStart_time() {
    return start_time;
  }

  public void setStart_time(String start_time) {
    this.start_time = start_time;
  }

  public String getStop_time() {
    return stop_time;
  }

  public void setStop_time(String stop_time) {
    this.stop_time = stop_time;
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
    return "PostChildCareDetailRequest{" +
        "macaddr='" + macaddr + '\'' +
        ", start_time='" + start_time + '\'' +
        ", stop_time='" + stop_time + '\'' +
        ", weekdays='" + weekdays + '\'' +
        ", enabled=" + enabled +
        '}';
  }
}
