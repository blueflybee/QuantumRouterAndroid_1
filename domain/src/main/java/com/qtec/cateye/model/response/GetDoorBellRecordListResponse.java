package com.qtec.cateye.model.response;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *
 "messageUniqueKey":"用户的门铃消息唯一标识（取自message表）",
 "messageCode":"警告类型：201门铃告警，202红外感应告警，203低电量告警，其他待扩展（取自zhongwei_push表）",
 "videoPath":"视频地址，可能为空（取自zhongwei_push表）",
 "picturePath":"图片地址，可能为空（取自zhongwei_push表）",
 "isRead":"是否已读，0未读，1已读（取自message表）",
 "occurTime":"事件触发时间（取自zhongwei_push表）"
 *
 *     version: 1.0
 * </pre>
 */
public class GetDoorBellRecordListResponse<T> {

  private String deviceSerialNo;
  private String userUniqueKey;
  private T msgList;

  public static class MsgList {
    private String messageUniqueKey;
    private String messageCode;
    private String isRead;
    private String occurTime;
    private String videoPath;
    private String picturePath;
    private String recordUniqueKey;

    public String getMessageUniqueKey() {
      return messageUniqueKey;
    }

    public void setMessageUniqueKey(String messageUniqueKey) {
      this.messageUniqueKey = messageUniqueKey;
    }

    public String getRecordUniqueKey() {
      return recordUniqueKey;
    }

    public void setRecordUniqueKey(String recordUniqueKey) {
      this.recordUniqueKey = recordUniqueKey;
    }

    public String getMessageCode() {
      return messageCode;
    }

    public void setMessageCode(String messageCode) {
      this.messageCode = messageCode;
    }

    public String getIsRead() {
      return isRead;
    }

    public void setIsRead(String isRead) {
      this.isRead = isRead;
    }

    public String getOccurTime() {
      return occurTime;
    }

    public void setOccurTime(String occurTime) {
      this.occurTime = occurTime;
    }

    public String getVideoPath() {
      return videoPath;
    }

    public void setVideoPath(String videoPath) {
      this.videoPath = videoPath;
    }

    public String getPicturePath() {
      return picturePath;
    }

    public void setPicturePath(String picturePath) {
      this.picturePath = picturePath;
    }

    @Override
    public String toString() {
      return "MsgList{" +
          "messageUniqueKey='" + messageUniqueKey + '\'' +
          ", messageCode='" + messageCode + '\'' +
          ", isRead='" + isRead + '\'' +
          ", occurTime='" + occurTime + '\'' +
          ", videoPath='" + videoPath + '\'' +
          ", picturePath='" + picturePath + '\'' +
          '}';
    }
  }

  public String getDeviceSerialNo() {
    return deviceSerialNo;
  }

  public void setDeviceSerialNo(String deviceSerialNo) {
    this.deviceSerialNo = deviceSerialNo;
  }

  public String getUserUniqueKey() {
    return userUniqueKey;
  }

  public void setUserUniqueKey(String userUniqueKey) {
    this.userUniqueKey = userUniqueKey;
  }

  public T getMsgList() {
    return msgList;
  }

  public void setMsgList(T msgList) {
    this.msgList = msgList;
  }

  @Override
  public String toString() {
    return "GetDoorBellRecordListResponse{" +
        "deviceSerialNo='" + deviceSerialNo + '\'' +
        ", userUniqueKey='" + userUniqueKey + '\'' +
        ", msgList=" + msgList +
        '}';
  }
}
