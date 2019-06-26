package com.fernandocejas.android10.sample.presentation.view.device.router.tools.data;

import java.io.Serializable;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/21
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class ChildCareBean implements Serializable{
  private String staname;
  private String macaddr;
  private int devicetype;
  private Boolean isAdd;//判断新增还是更新
  private int isEnable;
  private String start_time;
  private String stop_time;
  private String weekdays;

  public String getStaname() {
    return staname;
  }

  public void setStaname(String staname) {
    this.staname = staname;
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

  public Boolean getAdd() {
    return isAdd;
  }

  public void setAdd(Boolean add) {
    isAdd = add;
  }

  public int getIsEnable() {
    return isEnable;
  }

  public void setIsEnable(int isEnable) {
    this.isEnable = isEnable;
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

  @Override
  public String toString() {
    return "ChildCareBean{" +
        "staname='" + staname + '\'' +
        ", macaddr='" + macaddr + '\'' +
        ", devicetype='" + devicetype + '\'' +
        ", isAdd=" + isAdd +
        ", isEnable='" + isEnable + '\'' +
        ", start_time='" + start_time + '\'' +
        ", stop_time='" + stop_time + '\'' +
        ", weekdays='" + weekdays + '\'' +
        '}';
  }
}

