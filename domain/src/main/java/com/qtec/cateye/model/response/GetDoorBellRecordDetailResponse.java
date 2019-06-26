package com.qtec.cateye.model.response;

/**
 * <pre>
 *     author :
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *
 "messageCode":"警告类型：201门铃告警，202人体感应告警，其他待扩展",
 "videoPath":"视频地址",
 "picturePath":"图片地址",
 "occurTime":"事件触发时间"
 *
 *     version: 1.0
 * </pre>
 */
public class GetDoorBellRecordDetailResponse {

  private String messageCode;
  private String videoPath;
  private String picturePath;
  private String occurTime;

  public String getMessageCode() {
    return messageCode;
  }

  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
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

  public String getOccurTime() {
    return occurTime;
  }

  public void setOccurTime(String occurTime) {
    this.occurTime = occurTime;
  }

  @Override
  public String toString() {
    return "GetDoorBellRecordListResponse{" +
        "messageCode='" + messageCode + '\'' +
        ", videoPath='" + videoPath + '\'' +
        ", picturePath='" + picturePath + '\'' +
        ", occurTime='" + occurTime + '\'' +
        '}';
  }
}

