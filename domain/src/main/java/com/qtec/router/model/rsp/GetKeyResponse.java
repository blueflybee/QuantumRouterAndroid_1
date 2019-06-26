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
public class GetKeyResponse<T> {

  private int keynumber;
  private String devicename;

  private T keylist;

  public int getKeynumber() {
    return keynumber;
  }

  public void setKeynumber(int keynumber) {
    this.keynumber = keynumber;
  }

  public String getDevicename() {
    return devicename;
  }

  public void setDevicename(String devicename) {
    this.devicename = devicename;
  }

  public T getKeylist() {
    return keylist;
  }

  public void setKeylist(T keylist) {
    this.keylist = keylist;
  }

  public static class KeyBean {

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

  @Override
  public String toString() {
    return "GetKeyResponse{" +
        "keynumber=" + keynumber +
        ", devicename='" + devicename + '\'' +
        ", keylist=" + keylist +
        '}';
  }
}
