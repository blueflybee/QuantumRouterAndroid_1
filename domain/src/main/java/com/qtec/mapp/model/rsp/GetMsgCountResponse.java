package com.qtec.mapp.model.rsp;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetMsgCountResponse{
  private String unHandleNum;
  private String unReadMsg;
  private String totalMsgTipNum;

  public String getUnHandleNum() {
    return unHandleNum;
  }

  public void setUnHandleNum(String unHandleNum) {
    this.unHandleNum = unHandleNum;
  }

  public String getUnReadMsg() {
    return unReadMsg;
  }

  public void setUnReadMsg(String unReadMsg) {
    this.unReadMsg = unReadMsg;
  }

  public String getTotalMsgTipNum() {
    return totalMsgTipNum;
  }

  public void setTotalMsgTipNum(String totalMsgTipNum) {
    this.totalMsgTipNum = totalMsgTipNum;
  }

  @Override
  public String toString() {
    return "GetMsgCountResponse{" +
        "unHandleNum='" + unHandleNum + '\'' +
        ", unReadMsg='" + unReadMsg + '\'' +
        ", totalMsgTipNum='" + totalMsgTipNum + '\'' +
        '}';
  }
}
