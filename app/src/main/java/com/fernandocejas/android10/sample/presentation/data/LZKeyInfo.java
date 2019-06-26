package com.fernandocejas.android10.sample.presentation.data;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LZKeyInfo<T> implements Serializable{

  private int keytype;
  private int keynumber;
  private int requestid;
  private String userid;
  private String deviceid;
  private String devicename;

  private List<T> keylist;

  public int getKeytype() {
    return keytype;
  }

  public void setKeytype(int keytype) {
    this.keytype = keytype;
  }

  public int getKeynumber() {
    return keynumber;
  }

  public void setKeynumber(int keynumber) {
    this.keynumber = keynumber;
  }

  public int getRequestid() {
    return requestid;
  }

  public void setRequestid(int requestid) {
    this.requestid = requestid;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getDeviceid() {
    return deviceid;
  }

  public void setDeviceid(String deviceid) {
    this.deviceid = deviceid;
  }

  public String getDevicename() {
    return devicename;
  }

  public void setDevicename(String devicename) {
    this.devicename = devicename;
  }

  public List<T> getKeylist() {
    return keylist;
  }

  public void setKeylist(List<T> keylist) {
    this.keylist = keylist;
  }

  public static class KeyBean implements Serializable{

    private String keyid;
    private String key;

    public String getKeyid() {
      return keyid;
    }

    public void setKeyid(String keyid) {
      this.keyid = keyid;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

    @Override
    public String toString() {
      return "KeyBean{" +
          "keyid='" + keyid + '\'' +
          ", key='" + key + '\'' +
          '}';
    }
  }


}
