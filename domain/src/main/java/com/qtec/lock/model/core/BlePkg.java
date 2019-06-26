package com.qtec.lock.model.core;

import java.util.ArrayList;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/29
 *     desc   : 蓝牙通信数据包
 *     version: 1.0
 * </pre>
 */
public class BlePkg {
//  数据头    长度     密钥key    用户id     序列号    命令     内容
//   head    len      Key_num   Usr_id    Seq     Cmdid    payload
//    1       2        3-6      7-10       11      12      13-20

  private String head;
  private String length;
  private String keyId;
  private String userId;
  private String seq;
  private BleBody body;

  public String getHead() {
    return head;
  }

  public void setHead(String head) {
    this.head = head;
  }

  public String getLength() {
    return length;
  }

  public void setLength(String length) {
    this.length = length;
  }

  public String getKeyId() {
    return keyId;
  }

  public void setKeyId(String keyId) {
    this.keyId = keyId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getSeq() {
    return seq;
  }

  public void setSeq(String seq) {
    this.seq = seq;
  }

  public BleBody getBody() {
    return body;
  }

  public void setBody(BleBody body) {
    this.body = body;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BlePkg blePkg = (BlePkg) o;

    if (head != null ? !head.equals(blePkg.head) : blePkg.head != null) return false;
    if (length != null ? !length.equals(blePkg.length) : blePkg.length != null) return false;
    if (keyId != null ? !keyId.equals(blePkg.keyId) : blePkg.keyId != null) return false;
    if (userId != null ? !userId.equals(blePkg.userId) : blePkg.userId != null) return false;
    if (seq != null ? !seq.equals(blePkg.seq) : blePkg.seq != null) return false;
    return body != null ? body.equals(blePkg.body) : blePkg.body == null;

  }

  @Override
  public int hashCode() {
    int result = head != null ? head.hashCode() : 0;
    result = 31 * result + (length != null ? length.hashCode() : 0);
    result = 31 * result + (keyId != null ? keyId.hashCode() : 0);
    result = 31 * result + (userId != null ? userId.hashCode() : 0);
    result = 31 * result + (seq != null ? seq.hashCode() : 0);
    result = 31 * result + (body != null ? body.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "BlePkg{" +
        "head='" + head + '\'' +
        ", length='" + length + '\'' +
        ", keyId='" + keyId + '\'' +
        ", userId='" + userId + '\'' +
        ", seq='" + seq + '\'' +
        ", body=" + body +
        '}';
  }
}
