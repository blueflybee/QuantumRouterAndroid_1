package com.qtec.cateye.model.response;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/07
 *     desc   :
 *
 * "messageUniqueKey":"用户的门铃消息唯一标识（取自message表）",
 * "messageCode":"警告类型：201门铃告警，202人体感应告警，其他待扩展（取自record表）",
 * "message":"视频地址（取自record表）",
 * "isRead":"是否已读，0未读，1已读（取自message表）",
 * "occurTime":"事件触发时间（取自record表）"
 *
 *     version: 1.0
 * </pre>
 */
public class GetDoorBellRecordListRequest {

  /*       "deviceSerialNo":"1(必传参数)",
             "userUniqueKey":"",
             "isAlarm":"(非必传参数：0正常记录，1告警记录；不传则查询所有，传值则查询目标类型)",
             "messageCode":"非必传参数：当isAlarm为1时，若不为空，则查询特定告警类型的记录"
             "pageSize":"(必传参数)",
             "recordUniqueKey":"当获取第一页时，传-1，当获取第一页以后的，该值取服务端上次返回的列表中最后一个记录的recordUniqueKey"*/

  private String deviceSerialNo;
  private String userUniqueKey;
  private String isAlarm;
  private String messageCode;
  private String pageSize;
  private String recordUniqueKey;

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

  public String getIsAlarm() {
    return isAlarm;
  }

  public void setIsAlarm(String isAlarm) {
    this.isAlarm = isAlarm;
  }

  public String getMessageCode() {
    return messageCode;
  }

  public void setMessageCode(String messageCode) {
    this.messageCode = messageCode;
  }

  public String getPageSize() {
    return pageSize;
  }

  public void setPageSize(String pageSize) {
    this.pageSize = pageSize;
  }

  public String getRecordUniqueKey() {
    return recordUniqueKey;
  }

  public void setRecordUniqueKey(String recordUniqueKey) {
    this.recordUniqueKey = recordUniqueKey;
  }

  @Override
  public String toString() {
    return "GetDoorBellRecordListRequest{" +
        "deviceSerialNo='" + deviceSerialNo + '\'' +
        ", userUniqueKey='" + userUniqueKey + '\'' +
        ", isAlarm='" + isAlarm + '\'' +
        ", messageCode='" + messageCode + '\'' +
        ", pageSize='" + pageSize + '\'' +
        ", recordUniqueKey='" + recordUniqueKey + '\'' +
        '}';
  }
}
