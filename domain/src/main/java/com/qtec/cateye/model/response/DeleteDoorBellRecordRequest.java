package com.qtec.cateye.model.response;

import java.util.Arrays;

/**
 * <pre>
 *     author :
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *    "data":{"messageUniqueKeys":"[\"门铃消息记录uuid\",\"门铃消息记录uuid\"]"}
 *
 *     version: 1.0
 * </pre>
 */
public class DeleteDoorBellRecordRequest {
  private String[] messageUniqueKeys;

  public String[] getMessageUniqueKeys() {
    return messageUniqueKeys;
  }

  public void setMessageUniqueKeys(String[] messageUniqueKeys) {
    this.messageUniqueKeys = messageUniqueKeys;
  }

  @Override
  public String toString() {
    return "DeleteDoorBellRecordRequest{" +
        "messageUniqueKeys=" + Arrays.toString(messageUniqueKeys) +
        '}';
  }
}

