package com.qtec.mapp.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   : 中维版本号
 *     version: 1.0
 * </pre>
 */
public class QueryVersionResponse implements Serializable{

  private Data data;
  private String msg;
  private String result;
  private String errorCode;

  public Data getData() {
    return data;
  }

  public void setData(Data data) {
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

  public static class Data implements Serializable{
    private String version;

    public String getVersion() {
      return version;
    }

    public void setVersion(String version) {
      this.version = version;
    }

    @Override
    public String toString() {
      return "Data{" +
          "version='" + version + '\'' +
          '}';
    }
  }
}
