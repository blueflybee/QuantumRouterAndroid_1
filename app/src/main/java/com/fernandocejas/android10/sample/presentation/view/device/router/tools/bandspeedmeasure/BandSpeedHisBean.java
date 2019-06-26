package com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/11
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class BandSpeedHisBean {
  private String date;
  private String speed;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getSpeed() {
    return speed;
  }

  public void setSpeed(String speed) {
    this.speed = speed;
  }

  @Override
  public String toString() {
    return "BandSpeedHisBean{" +
        "date='" + date + '\'' +
        ", speed='" + speed + '\'' +
        '}';
  }
}
