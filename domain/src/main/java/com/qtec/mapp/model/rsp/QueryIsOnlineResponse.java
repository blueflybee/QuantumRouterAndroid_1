package com.qtec.mapp.model.rsp;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 设备在线
 *     version: 1.0
 * </pre>
 */
public class QueryIsOnlineResponse implements Serializable{
  private List<Data> data;
  private String msg;
  private String result;
  private String errorCode;

  public List<Data> getData() {
    return data;
  }

  public void setData(List<Data> data) {
    this.data = data;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String toString() {
    return "QueryVersionResponse{" +
        "data='" + data + '\'' +
        ", msg='" + msg + '\'' +
        ", result='" + result + '\'' +
        ", errorCode='" + errorCode + '\'' +
        '}';
  }

  public class Data{
    private String deviceGuid;
    private int online;

    public String getDeviceGuid() {
      return deviceGuid;
    }

    public void setDeviceGuid(String deviceGuid) {
      this.deviceGuid = deviceGuid;
    }

    public int getOnline() {
      return online;
    }

    public void setOnline(int online) {
      this.online = online;
    }

    @Override
    public String toString() {
      return "Data{" +
          "deviceGuid='" + deviceGuid + '\'' +
          ", online=" + online +
          '}';
    }
  }
}
