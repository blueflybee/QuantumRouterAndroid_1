package com.qtec.cateye.model.response;

/**
 * <pre>
 *     author :
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *
 *     version: 1.0
 * </pre>
 */
public class GetDoorBellRecordDetailRequest {

  private String messageUniqueKey;

  public String getMessageUniqueKey() {
    return messageUniqueKey;
  }

  public void setMessageUniqueKey(String messageUniqueKey) {
    this.messageUniqueKey = messageUniqueKey;
  }

  @Override
  public String toString() {
    return "GetDoorBellRecordDetailRequest{" +
        "messageUniqueKey='" + messageUniqueKey + '\'' +
        '}';
  }
}

