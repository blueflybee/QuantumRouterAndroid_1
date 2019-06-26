package com.qtec.router.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyWifiDataRequest {
  private int id;
  private String week_day;
  private String name;
  private int rule_enable;
  private int start_hour;
  private int start_min;
  private int stop_hour;
  private int stop_min;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getWeek_day() {
    return week_day;
  }

  public void setWeek_day(String week_day) {
    this.week_day = week_day;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getRule_enable() {
    return rule_enable;
  }

  public void setRule_enable(int rule_enable) {
    this.rule_enable = rule_enable;
  }

  public int getStart_hour() {
    return start_hour;
  }

  public void setStart_hour(int start_hour) {
    this.start_hour = start_hour;
  }

  public int getStart_min() {
    return start_min;
  }

  public void setStart_min(int start_min) {
    this.start_min = start_min;
  }

  public int getStop_hour() {
    return stop_hour;
  }

  public void setStop_hour(int stop_hour) {
    this.stop_hour = stop_hour;
  }

  public int getStop_min() {
    return stop_min;
  }

  public void setStop_min(int stop_min) {
    this.stop_min = stop_min;
  }

  @Override
  public String toString() {
    return "ModifyWifiDataRequest{" +
        "id=" + id +
        ", week_day='" + week_day + '\'' +
        ", name='" + name + '\'' +
        ", rule_enable=" + rule_enable +
        ", start_hour=" + start_hour +
        ", start_min=" + start_min +
        ", stop_hour=" + stop_hour +
        ", stop_min=" + stop_min +
        '}';
  }
}
