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
 *     desc   : 门锁解锁方式配置推送信息json解析类， alert部分数据
 *     version: 1.0
 * </pre>
 */
public class UnlockModePushData implements Serializable {
//{
//  "aps": {
//    "alert": {
//      "body": "{
//        \"businessData\":{
//            \"openconfig\":\"0\",
//            \"deviceSerialNo\":\"TESTLOCK02\",
//            \"messageUniqueKey\":\"42919367bbb9417d83103386c55a6543\",
//            \"bluetoothName\":\"TESTLOCK01 lanya2.0\"
//        },
//        \"businessId\":\"04\"
//      }",
//      "title": "设备测试门锁(TESTLOCK02)指纹开锁方式修改为仅指纹开锁"
//    },
//    "content-available": 1
//  }
// }
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
      private String openconfig;
      private String deviceSerialNo;
      private String messageUniqueKey;
      private String bluetoothName;

      public String getDeviceSerialNo() {
        return deviceSerialNo;
      }

      public void setDeviceSerialNo(String deviceSerialNo) {
        this.deviceSerialNo = deviceSerialNo;
      }

      public String getMessageUniqueKey() {
        return messageUniqueKey;
      }

      public void setMessageUniqueKey(String messageUniqueKey) {
        this.messageUniqueKey = messageUniqueKey;
      }

      public String getOpenconfig() {
        return openconfig;
      }

      public void setOpenconfig(String openconfig) {
        this.openconfig = openconfig;
      }

      public String getBluetoothName() {
        return bluetoothName;
      }

      public void setBluetoothName(String bluetoothName) {
        this.bluetoothName = bluetoothName;
      }

      @Override
      public String toString() {
        return "BusinessData{" +
            "openconfig='" + openconfig + '\'' +
            ", deviceSerialNo='" + deviceSerialNo + '\'' +
            ", messageUniqueKey='" + messageUniqueKey + '\'' +
            ", bluetoothName='" + bluetoothName + '\'' +
            '}';
      }
    }
  }

  @Override
  public String toString() {
    return "UnlockModePushData{" +
        "title='" + title + '\'' +
        ", body=" + body +
        '}';
  }

  public static UnlockModePushData parseFromJson(String message) {
    try {
      JSONObject jsonObject = new JSONObject(message);
      JSONObject jsonAps = jsonObject.getJSONObject(JSON_APS);
      JSONObject jsonAlert = jsonAps.getJSONObject(JSON_ALERT);
      UnlockModePushData pushData = new UnlockModePushData();
      pushData.setTitle(jsonAlert.getString(JSON_TITLE));
      String jsonBody = jsonAlert.getString(JSON_BODY);
      DataBody dataBody = new Gson().fromJson(jsonBody, DataBody.class);
      pushData.setBody(dataBody);
      return pushData;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

}
