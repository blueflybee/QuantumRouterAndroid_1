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
public class UpdateChildCareDetailRequest {
  private String oldmacaddr;
  private String oldstarttime;
  private String oldstoptime;
  private String oldweekdays;
  private int oldenabled;
  private String newmacaddr;
  private String newstarttime;
  private String newstoptime;
  private String newweekdays;
  private int newenabled;

  public String getOldmacaddr() {
    return oldmacaddr;
  }

  public void setOldmacaddr(String oldmacaddr) {
    this.oldmacaddr = oldmacaddr;
  }

  public String getOldstarttime() {
    return oldstarttime;
  }

  public void setOldstarttime(String oldstarttime) {
    this.oldstarttime = oldstarttime;
  }

  public String getOldstoptime() {
    return oldstoptime;
  }

  public void setOldstoptime(String oldstoptime) {
    this.oldstoptime = oldstoptime;
  }

  public String getOldweekdays() {
    return oldweekdays;
  }

  public void setOldweekdays(String oldweekdays) {
    this.oldweekdays = oldweekdays;
  }

  public int getOldenabled() {
    return oldenabled;
  }

  public void setOldenabled(int oldenabled) {
    this.oldenabled = oldenabled;
  }

  public String getNewmacaddr() {
    return newmacaddr;
  }

  public void setNewmacaddr(String newmacaddr) {
    this.newmacaddr = newmacaddr;
  }

  public String getNewstarttime() {
    return newstarttime;
  }

  public void setNewstarttime(String newstarttime) {
    this.newstarttime = newstarttime;
  }

  public String getNewstoptime() {
    return newstoptime;
  }

  public void setNewstoptime(String newstoptime) {
    this.newstoptime = newstoptime;
  }

  public String getNewweekdays() {
    return newweekdays;
  }

  public void setNewweekdays(String newweekdays) {
    this.newweekdays = newweekdays;
  }

  public int getNewenabled() {
    return newenabled;
  }

  public void setNewenabled(int newenabled) {
    this.newenabled = newenabled;
  }

  @Override
  public String toString() {
    return "UpdateChildCareDetailRequest{" +
        "oldmacaddr='" + oldmacaddr + '\'' +
        ", oldstarttime='" + oldstarttime + '\'' +
        ", oldstoptime='" + oldstoptime + '\'' +
        ", oldweekdays='" + oldweekdays + '\'' +
        ", oldenabled='" + oldenabled + '\'' +
        ", newmacaddr='" + newmacaddr + '\'' +
        ", newstarttime='" + newstarttime + '\'' +
        ", newstoptime='" + newstoptime + '\'' +
        ", newweekdays='" + newweekdays + '\'' +
        ", newenabled='" + newenabled + '\'' +
        '}';
  }
}
