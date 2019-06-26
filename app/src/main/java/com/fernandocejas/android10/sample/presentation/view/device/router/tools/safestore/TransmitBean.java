package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

/**
 * 传输消息 存入数据库
 * 
 * @author way
 * 
 */
public class TransmitBean {
	private String key;//
	private long fileSize;
	private long complete;
	private String state;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public long getFileSize() {
    return fileSize;
  }

  public void setFileSize(long fileSize) {
    this.fileSize = fileSize;
  }

  public long getComplete() {
    return complete;
  }

  public void setComplete(long complete) {
    this.complete = complete;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "TransmitBean{" +
        "key='" + key + '\'' +
        ", fileSize=" + fileSize +
        ", complete=" + complete +
        ", state='" + state + '\'' +
        '}';
  }
}
