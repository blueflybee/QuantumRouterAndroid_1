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
public class QueryDiskStateResponse {
  private int status_errorcode;
  private String status_message;
  private int process_statuscode;
  private String process_message;
  private String disk_size;
  private String disk_used;
  private String disk_capacity;

  public int getStatus_errorcode() {
    return status_errorcode;
  }

  public void setStatus_errorcode(int status_errorcode) {
    this.status_errorcode = status_errorcode;
  }

  public String getStatus_message() {
    return status_message;
  }

  public void setStatus_message(String status_message) {
    this.status_message = status_message;
  }

  public int getProcess_statuscode() {
    return process_statuscode;
  }

  public void setProcess_statuscode(int process_statuscode) {
    this.process_statuscode = process_statuscode;
  }

  public String getProcess_message() {
    return process_message;
  }

  public void setProcess_message(String process_message) {
    this.process_message = process_message;
  }

  public String getDisk_size() {
    return disk_size;
  }

  public void setDisk_size(String disk_size) {
    this.disk_size = disk_size;
  }

  public String getDisk_used() {
    return disk_used;
  }

  public void setDisk_used(String disk_used) {
    this.disk_used = disk_used;
  }

  public String getDisk_capacity() {
    return disk_capacity;
  }

  public void setDisk_capacity(String disk_capacity) {
    this.disk_capacity = disk_capacity;
  }

  @Override
  public String toString() {
    return "QueryDiskStateResponse{" +
        "status_errorcode=" + status_errorcode +
        ", status_message='" + status_message + '\'' +
        ", process_statuscode=" + process_statuscode +
        ", process_message='" + process_message + '\'' +
        ", disk_size='" + disk_size + '\'' +
        ", disk_used='" + disk_used + '\'' +
        ", disk_capacity='" + disk_capacity + '\'' +
        '}';
  }
}
