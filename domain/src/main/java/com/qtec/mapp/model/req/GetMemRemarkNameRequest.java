package com.qtec.mapp.model.req;

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
public class GetMemRemarkNameRequest implements Serializable{
  private String historyUniqueKey;
  private String remark;

  public String getHistoryUniqueKey() {
    return historyUniqueKey;
  }

  public void setHistoryUniqueKey(String historyUniqueKey) {
    this.historyUniqueKey = historyUniqueKey;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return "GetMemRemarkNameRequest{" +
        "historyUniqueKey='" + historyUniqueKey + '\'' +
        ", remark='" + remark + '\'' +
        '}';
  }
}
