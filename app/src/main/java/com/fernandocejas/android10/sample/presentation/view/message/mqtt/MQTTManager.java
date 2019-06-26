package com.fernandocejas.android10.sample.presentation.view.message.mqtt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;

import com.blueflybee.mqttdroidlibrary.MQQTUtils;
import com.blueflybee.mqttdroidlibrary.data.MQMessage;
import com.blueflybee.mqttdroidlibrary.service.MQTTService;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.NotificationUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CatEyeDoorbellActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.IBleOperable;
import com.fernandocejas.android10.sample.presentation.view.message.constant.PushConstant;
import com.fernandocejas.android10.sample.presentation.view.message.data.CatEyePushData;
import com.fernandocejas.android10.sample.presentation.view.message.data.UnlockModePushData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * mqtt消息推送处理类
 */
public class MQTTManager {

  private static final MQTTManager INSTANCE = new MQTTManager();

  private static final String JSON_ALERT = "alert";
  private static final String JSON_APS = "aps";
  private static final String JSON_BODY = "body";
  private static final String JSON_BUSINESS_ID = "businessId";


  private Context mContext;

  @SuppressLint("HandlerLeak")
  private Handler mMQTTHandler = new Handler() {
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case MQTTService.MSG_RECEIVE_SUCCESS:
          MQMessage mqMessage = (MQMessage) msg.obj;
            /*ToastUtils.showShort(mqMessage.toString());*/
          handlePushMessage(mqMessage);
          break;

        case MQTTService.MSG_MQTT_STATUS:
          MQMessage mqStatusMessage = (MQMessage) msg.obj;
            /*ToastUtils.showShort(mqStatusMessage.getMessage());*/
          break;
        default:
          break;
      }
    }
  };

  private MQTTManager() {
  }

  public static MQTTManager instance() {
    System.out.println("MQTTService INSTANCE = " + INSTANCE);
    return INSTANCE;
  }

  public void init(Context context) {
    this.mContext = context;
    String androidID = MQQTUtils.getAndroidID(context);
    String[] topics = new String[]{
        "smarthome.server.s." + androidID,
        "smarthome.server.s.caretec.topic"
    };
    Intent intent = new Intent(context, MQTTService.class);
    intent.putExtra(MQTTService.EXTRA_MQTT_MESSENGER, new Messenger(mMQTTHandler));
    intent.putExtra(MQTTService.EXTRA_CLIENT_ID, androidID);
    intent.putExtra(MQTTService.EXTRA_TOPICS, topics);
    context.startService(intent);
  }

  public void stop() {
    if (mContext != null) {
      mContext.stopService(new Intent(mContext, MQTTService.class));
    }
  }

  private void handlePushMessage(MQMessage mqMessage) {
    System.out.println("mqMessage = " + mqMessage);
    if (mqMessage == null || TextUtils.isEmpty(mqMessage.getMessage())) return;
    String busId = parseBusId(mqMessage.getMessage());
    if (TextUtils.isEmpty(busId)) return;
    System.out.println("busId = " + busId);
    if (PushConstant.BUSI_CAT_PUSH.equals(busId)) {
      handleCatEyePush(mqMessage);
    } else if (PushConstant.BUSI_UNLOCK_MODE_PUSH.equals(busId)) {
      handleUnlockModePush(mqMessage);
    }
  }

  private void handleUnlockModePush(MQMessage mqMessage) {
    IBleOperable bleLockService = ((AndroidApplication) mContext.getApplicationContext()).getBleLockService();
    if (bleLockService == null) return;
    UnlockModePushData pushData = UnlockModePushData.parseFromJson(mqMessage.getMessage());
    System.out.println("pushData = " + pushData);
    if (pushData == null || pushData.getBody() == null || pushData.getBody().getBusinessData() == null) return;
    UnlockModePushData.DataBody.BusinessData businessData = pushData.getBody().getBusinessData();
    String openconfig = businessData.getOpenconfig();
    if (PushConstant.CLOSE_BLE.equals(openconfig)) {
      bleLockService.setUnlockWithoutBle(true);
    } else if (PushConstant.OPEN_BLE.equals(openconfig)){
      bleLockService.setUnlockWithoutBle(false);
    }
  }

  private void handleCatEyePush(MQMessage mqMessage) {
    CatEyePushData catEyePushData = CatEyePushData.parseFromJson(mqMessage.getMessage());
    if (catEyePushData == null || catEyePushData.getBody() == null) return;
    boolean appBackground = ((AndroidApplication) mContext.getApplicationContext()).isAppBackground();
    Intent intent = new Intent(mContext, CatEyeDoorbellActivity.class);
    intent.putExtra(Navigator.EXTRA_CAT_EYE_DOORBELL_PUSH_DATA, catEyePushData);
    if (!appBackground) {
      new Navigator().navigateTo(mContext, CatEyeDoorbellActivity.class, intent);
    } else {
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      NotificationUtil.showNotification(mContext, catEyePushData.getTitle(), catEyePushData.getTitle(), intent);
    }
  }

  private String parseBusId(String message) {
    try {
      JSONObject jsonAps = new JSONObject(message).getJSONObject(JSON_APS);
      JSONObject jsonAlert = jsonAps.getJSONObject(JSON_ALERT);
      System.out.println("jsonAlert.toString() = " + jsonAlert.toString());
      String jsonBody = jsonAlert.getString(JSON_BODY);
      System.out.println("jsonBody = " + jsonBody);
      return new JSONObject(jsonBody).getString(JSON_BUSINESS_ID);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

}