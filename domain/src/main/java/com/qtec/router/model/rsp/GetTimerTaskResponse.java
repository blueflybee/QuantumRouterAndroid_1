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
public class GetTimerTaskResponse {
  /**
   * enable : 0
   * minute : 24
   * hour : 15
   * day : 2,4,6
   */
//"data": {
//    "enable":0,// (0代表去使能，1代表使能)
//        "minute":24,
//        "hour":15,
//        "day":"2,4,6"//（定时器周，0表示周日，1表示周一，依次类推, ）
//  },
  private int enable;
  private int minute;
  private int hour;
  private String day;

  public int getEnable() {
    return enable;
  }

  public void setEnable(int enable) {
    this.enable = enable;
  }

  public int getMinute() {
    return minute;
  }

  public void setMinute(int minute) {
    this.minute = minute;
  }

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }
}
