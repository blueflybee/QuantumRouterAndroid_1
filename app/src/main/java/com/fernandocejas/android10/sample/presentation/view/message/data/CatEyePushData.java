package com.fernandocejas.android10.sample.presentation.view.message.data;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/03/27
 *     desc   : 猫眼模块推送信息json解析类， alert部分数据
 *     version: 1.0
 * </pre>
 */
public class CatEyePushData implements Serializable {
//  {
//    "aps": {
//    "alert": {
//      "body": "{
//        \"businessData\":{
//              \"picturePath\":\"https://qtec-route-headimg.oss-cn-shanghai.aliyuncs.com/Default_avatar@3x.png\",
//              \"lockSerialNo\":\"\",
//              \"videoPath\":\"\",
//              \"routerSerialNo\":\"\",
//              \"messageUniqueKey\":\"b940c0c78dad48498ea6e2effd645fd0\",
//              \"occurTime\":\"2018-03-27 13:47:00\",
//              \"catSerialNo\":\"C200275881\",
//              \"messageCode\":\"201\"
//    },
//        \"businessId\":\"03\"
//  }",
//      "title": "猫眼设备电子猫眼(C200275881)门铃告警"
//  }
//}
//}




//  {
// "aps": {
//    "alert": {
//      "body": "{
//        \"businessData\":{
//            \"messageUniqueKey\":\"a041b3ca6736441c9e634391a641d042\",
//            \"deviceSerialNo\":\"C200275881\"
//        },
//      \"businessId\":\"04\"
//      }", "title": "猫眼设备电子猫眼(C200275881)门铃告警" } } }
  /**
   * 门铃告警
   */
  public static final String CAT_EYE_DOORBELL = "201";

  /**
   * 人体感应告警
   */
  public static final String CAT_EYE_INFRARED = "202";

  /**
   * 低电量告警
   */
  public static final String CAT_EYE_LOW_POWER = "203";

  /**
   * 移动侦测报警
   */
  public static final String CAT_EYE_MOTION_DETECTION = "204";

  private static final String JSON_ALERT = "alert";
  private static final String JSON_APS = "aps";
  private static final String JSON_TITLE = "title";
  private static final String JSON_BODY = "body";

  private String title;
  private DataBody body;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public DataBody getBody() {
    return body;
  }

  public void setBody(DataBody body) {
    this.body = body;
  }

  public class DataBody implements Serializable {
    private String businessId;
    private BusinessData businessData;

    public String getBusinessId() {
      return businessId;
    }

    public void setBusinessId(String businessId) {
      this.businessId = businessId;
    }

    public BusinessData getBusinessData() {
      return businessData;
    }

    public void setBusinessData(BusinessData businessData) {
      this.businessData = businessData;
    }

    @Override
    public String toString() {
      return "DataBody{" +
          "businessId='" + businessId + '\'' +
          ", businessData=" + businessData +
          '}';
    }

    public class BusinessData implements Serializable {
      private String picturePath;
      private String lockSerialNo;
      private String videoPath;
      private String routerSerialNo;
      private String messageUniqueKey;
      private String occurTime;
      private String catSerialNo;
      private String messageCode;

      public String getPicturePath() {
        return picturePath;
      }

      public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
      }

      public String getLockSerialNo() {
        return lockSerialNo;
      }

      public void setLockSerialNo(String lockSerialNo) {
        this.lockSerialNo = lockSerialNo;
      }

      public String getVideoPath() {
        return videoPath;
      }

      public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
      }

      public String getRouterSerialNo() {
        return routerSerialNo;
      }

      public void setRouterSerialNo(String routerSerialNo) {
        this.routerSerialNo = routerSerialNo;
      }

      public String getMessageUniqueKey() {
        return messageUniqueKey;
      }

      public void setMessageUniqueKey(String messageUniqueKey) {
        this.messageUniqueKey = messageUniqueKey;
      }

      public String getOccurTime() {
        return occurTime;
      }

      public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
      }

      public String getCatSerialNo() {
        return catSerialNo;
      }

      public void setCatSerialNo(String catSerialNo) {
        this.catSerialNo = catSerialNo;
      }

      public String getMessageCode() {
        return messageCode;
      }

      public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
      }

      @Override
      public String toString() {
        return "BusinessData{" +
            "picturePath='" + picturePath + '\'' +
            ", lockSerialNo='" + lockSerialNo + '\'' +
            ", videoPath='" + videoPath + '\'' +
            ", routerSerialNo='" + routerSerialNo + '\'' +
            ", messageUniqueKey='" + messageUniqueKey + '\'' +
            ", occurTime='" + occurTime + '\'' +
            ", catSerialNo='" + catSerialNo + '\'' +
            ", messageCode='" + messageCode + '\'' +
            '}';
      }
    }


  }

  @Override
  public String toString() {
    return "CatEyePushData{" +
        "title='" + title + '\'' +
        ", body=" + body +
        '}';
  }

  public static CatEyePushData parseFromJson(String message) {
    try {
      JSONObject jsonObject = new JSONObject(message);
      JSONObject jsonAps = jsonObject.getJSONObject(JSON_APS);
      JSONObject jsonAlert = jsonAps.getJSONObject(JSON_ALERT);
      CatEyePushData catEyePushData = new CatEyePushData();
      String title = jsonAlert.getString(JSON_TITLE);
      catEyePushData.setTitle(title);
      String jsonBody = jsonAlert.getString(JSON_BODY);
      DataBody dataBody = new Gson().fromJson(jsonBody, DataBody.class);
      catEyePushData.setBody(dataBody);
      return catEyePushData;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getMsgByType(String type) {
    if (CAT_EYE_DOORBELL.equals(type)) {
      return "门铃响啦！";
    } else if (CAT_EYE_INFRARED.equals(type)) {
      return "人体感应告警";
    } else if (CAT_EYE_LOW_POWER.equals(type)) {
      return "低电量告警";
    } else if (CAT_EYE_MOTION_DETECTION.equals(type)) {
      return "移动侦测报警";
    } else {
      return "未知类型";
    }

  }
}
