package com.qtec.mapp.model.req;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/29
 *     version: 1.0
 * </pre>
 */
//  {
//          "method":"post",
//          "requestUrl":"/route/route/dispatch",
//          "data":{"encryptInfo":"加密信息","routerSerialNo":"xxx"},
//          "token":"",
//          "response":{
//          "code":0,
//          "msg":"",
//          "data":{"encryptInfo":"加密信息"}
//          },
//          "comment":"转发消息到网关"
//          },
public class TransmitRequest<T> {

  /**
   * encryptInfo :
   * routerSerialNo : xxx
   */
  private T encryptInfo;
  private String routerSerialNo;
  private String keyId;
  private String encryption;
  /**
   * 用户账户
   */
  private String userId;

  /**
   * 设备ID
   */
  private String deviceId;

  /**
   * 1 reuse
   * 0 delete
   */
  private int reuse;


  /**
   * 1 密钥库失效，需要调用首次获取密钥接口重新同步密钥
   * 0 无需操作
   * 优先校验该字段
   */
  private int keyInvalid;

  public T getEncryptInfo() {
    return encryptInfo;
  }

  public void setEncryptInfo(T encryptInfo) {
    this.encryptInfo = encryptInfo;
  }

  public String getRouterSerialNo() {
    return routerSerialNo;
  }

  public void setRouterSerialNo(String routerSerialNo) {
    this.routerSerialNo = routerSerialNo;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public String getEncryption() {
    return encryption;
  }

  public void setEncryption(String encryption) {
    this.encryption = encryption;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public int getReuse() {
    return reuse;
  }

  public void setReuse(int reuse) {
    this.reuse = reuse;
  }

  public int getKeyInvalid() {
    return keyInvalid;
  }

  public void setKeyInvalid(int keyInvalid) {
    this.keyInvalid = keyInvalid;
  }

  @Override
  public String toString() {
    return "TransmitRequest{" +
        "encryptInfo=" + encryptInfo +
        ", routerSerialNo='" + routerSerialNo + '\'' +
        ", keyId='" + keyId + '\'' +
        ", encryption='" + encryption + '\'' +
        ", userId='" + userId + '\'' +
        ", deviceId='" + deviceId + '\'' +
        '}';
  }
}
