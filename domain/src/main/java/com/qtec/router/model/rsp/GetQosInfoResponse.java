package com.qtec.router.model.rsp;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetQosInfoResponse implements Serializable {
  private int qosmode;
  private int enabled;
  private int download;
  private int upload;

  public int getQosmode() {
    return qosmode;
  }

  public void setQosmode(int qosmode) {
    this.qosmode = qosmode;
  }

  public int getEnabled() {
    return enabled;
  }

  public void setEnabled(int enabled) {
    this.enabled = enabled;
  }

  public int getDownload() {
    return download;
  }

  public void setDownload(int download) {
    this.download = download;
  }

  public int getUpload() {
    return upload;
  }

  public void setUpload(int upload) {
    this.upload = upload;
  }

  @Override
  public String toString() {
    return "GetQosInfoResponse{" +
        "qosmode=" + qosmode +
        ", enabled=" + enabled +
        ", download=" + download +
        ", upload=" + upload +
        '}';
  }
}
