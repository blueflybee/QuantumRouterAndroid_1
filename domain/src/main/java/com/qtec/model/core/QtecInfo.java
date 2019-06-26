package com.qtec.model.core;

import com.fernandocejas.android10.sample.domain.params.IRequest;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/10
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class QtecInfo implements IRequest {

  /**
   * 包含加密的报文，QtecEncryptInfo或QtecResult的json+加密格式
   */
  private String encryptinfo;
  /**
   *
   * 0 encryption
   * 1 no encryption
   */
  private int encryption;

  /**
   * 加密的keyid
   */
  private String keyid;

  /**
   * 用户账户
   */
  private String userid;

  /**
   * 设备ID
   */
  private String deviceid;


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
  private int keyinvalid;

  private String serialnumber;
  
  public String getEncryptinfo() {
    return encryptinfo;
  }

  public void setEncryptinfo(String encryptinfo) {
    this.encryptinfo = encryptinfo;
  }

  public int getEncryption() {
    return encryption;
  }

  public void setEncryption(int encryption) {
    this.encryption = encryption;
  }

  public String getKeyid() {
    return keyid;
  }

  public void setKeyid(String keyid) {
    this.keyid = keyid;
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

  public int getReuse() {
    return reuse;
  }

  public void setReuse(int reuse) {
    this.reuse = reuse;
  }

  public int getKeyinvalid() {
    return keyinvalid;
  }

  public void setKeyinvalid(int keyinvalid) {
    this.keyinvalid = keyinvalid;
  }

  public String getSerialnumber() {
    return serialnumber;
  }

  public void setSerialnumber(String serialnumber) {
    this.serialnumber = serialnumber;
  }
}
