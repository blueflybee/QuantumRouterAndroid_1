package com.fernandocejas.android10.sample.presentation.view.device.router.tools.data;

import java.util.Arrays;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/21
 *      desc: wifi时段
 *      version: 1.0
 * </pre>
 */

public class WifiTimeIntervalBean {
  private String startTime;
  private String endTime;
  private String name;
  private String[] timeIntervalArr;

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String[] getTimeIntervalArr() {
    return timeIntervalArr;
  }

  public void setTimeIntervalArr(String[] timeIntervalArr) {
    this.timeIntervalArr = timeIntervalArr;
  }

  @Override
  public String toString() {
    return "WifiTimeIntervalBean{" +
        "startTime='" + startTime + '\'' +
        ", endTime='" + endTime + '\'' +
        ", name='" + name + '\'' +
        ", timeIntervalArr=" + Arrays.toString(timeIntervalArr) +
        '}';
  }
}
