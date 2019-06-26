package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.utils.NotificationUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CatEyeDoorbellActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ExceptionWarnMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockManageTipActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockTipsActivity;
import com.fernandocejas.android10.sample.presentation.view.message.constant.PushConstant;
import com.fernandocejas.android10.sample.presentation.view.message.data.CatEyePushData;
import com.fernandocejas.android10.sample.presentation.view.message.data.GetAcceptContent;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockManageTipActivity.LOCK_TIPS_TYPE_ADMIN_TRANSFER;
import static com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockManageTipActivity.LOCK_TIPS_TYPE_DELETED_BY_ADMIN;
import static com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockManageTipActivity.LOCK_TIPS_TYPE_RESET_LOCK;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/20
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class YunOsMessageReceiver extends MessageReceiver {
  // 消息接收部分的LOG_TAG
  public static final String REC_TAG = "receiver";
  public static final String KEY_NOTIFICATION_TYPE = "NotificationType";
  public static final String KEY_DATA = "data";
  public static final String DELETED_BY_ADMIN = "deletedbyadmin";
  public static final String ADMIN_TRANSFER = "admintransfer";
  public static final String RESETLOCK = "resetlock";
  public static final String LOCK_ALARM = "lockalarm";
  public static final String JSON_DEVICE_SERIAL_NO = "deviceSerialNo";
  public String mLockId = "", mRouterId = "";
  static OnRefreshMsgListener mOnRefreshMsgListener;

  @Override
  public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
    System.out.println("onNotification context = [" + context + "], title = [" + title + "], summary = [" + summary + "], extraMap = [" + extraMap + "]");
//    context = [android.app.ReceiverRestrictedContext@d252ef], title = [设备管理权限移除通知], summary = [管理员已移除您对设备-九州智能门锁(B9CA7714004B1200)的控制权限], extraMap = [{_ALIYUN_NOTIFICATION_ID_=36567, NotificationType=deletedbyadmin, data={"deviceSerialNo":"B9CA7714004B1200"}}]
//    通知的业务标识： 1、管理员删除某个用户->deletedbyadmin； 2、管理员转让兼解绑->admintransfer； 3、门锁恢复出厂设置->resetlock

//    onNotification context = [android.app.ReceiverRestrictedContext@2f8f96b], title = [告警], summary = [多次错误（密码，指纹等）报警], extraMap = [{_ALIYUN_NOTIFICATION_ID_=353546, NotificationType=lockalarm, data={"deviceSerialNo":"FCCA7714004B1200"}}]

    if (!isNotificationEnabled(context)) {
      //通知没有打开
      System.out.println("onNotification 通知没有打开，跳至设置界面");
      Toast.makeText(context, "请开启系统通知管理权限", Toast.LENGTH_SHORT).show();
      toSetting(context);
    }
//
    boolean appBackground = ((AndroidApplication) context.getApplicationContext()).isAppBackground();
//    System.out.println("appBackground = " + appBackground);
    if (extraMap.size() != 0) {
            /*Intent intent = new Intent(context,DealWarningInfoActivity.class);
            intent.putExtra("UNLOCK_WARNING_INFO",summary);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);*/
      //获取告警记录的lockID
      AndroidApplication.IS_HAS_NEW_MSG = true;

      String notificationType = extraMap.get(KEY_NOTIFICATION_TYPE);
      String jsonData = extraMap.get(KEY_DATA);

      if ("specialFocus".equals(notificationType)) {
        //特别关注
        System.out.println("onNotification 特别关注 收到通知了 请及时处理！");

      } else if (DELETED_BY_ADMIN.equals(notificationType)) {
        clearLockData(context, parseDeviceSerialNo(jsonData));
        if (!appBackground) {
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_TYPE, LOCK_TIPS_TYPE_DELETED_BY_ADMIN);
          intent.setAction(LockManageTipActivity.ACTION_LOCK_MANAGE_TIPS);
          context.sendBroadcast(intent);
        }

      } else if (ADMIN_TRANSFER.equals(notificationType)) {
        DeviceCacheManager.setDeviceUserRole(context, parseDeviceSerialNo(jsonData), GetDevTreeResponse.ADMIN);
        if (!appBackground) {
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_TYPE, LOCK_TIPS_TYPE_ADMIN_TRANSFER);
          intent.setAction(LockManageTipActivity.ACTION_LOCK_MANAGE_TIPS);
          context.sendBroadcast(intent);
        }
      } else if (RESETLOCK.equals(notificationType)) {
        clearLockData(context, parseDeviceSerialNo(jsonData));
        if (!appBackground) {
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_TYPE, LOCK_TIPS_TYPE_RESET_LOCK);
          intent.setAction(LockManageTipActivity.ACTION_LOCK_MANAGE_TIPS);
          context.sendBroadcast(intent);
        }
      } else if (LOCK_ALARM.equals(notificationType)) {
        if (!appBackground) {
          Intent intent = new Intent(context, LockTipsActivity.class);
          intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, parseDeviceSerialNo(jsonData));
          intent.putExtra(Navigator.EXTRA_LOCK_TIPS_TITLE, title);
          intent.putExtra(Navigator.EXTRA_LOCK_TIPS_CONTENT, summary);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.startActivity(intent);
        }
      } else {
        //告警记录
        mLockId = extraMap.get("");
        mRouterId = extraMap.get("");
      }

    }
  }

  private String parseDeviceSerialNo(String json) {
    try {
      if (TextUtils.isEmpty(json)) return null;
      return new JSONObject(json).getString(JSON_DEVICE_SERIAL_NO);
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  private void clearLockData(Context context, String lockId) {
    BleLock bleLock = LockManager.getLockById(context, lockId);
    if (bleLock == null) return;
    KeystoreRepertory.getInstance().clear(bleLock.getKeyRepoId());
    LockManager.delete(context, bleLock.getMac());
    DeviceCacheManager.delete(context, lockId);
  }

  @Override
  public void onMessage(Context context, CPushMessage cPushMessage) {
    if (cPushMessage == null) return;
    System.out.println("onMessage messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());

    // 01 分享  00 多端登录  02 消息刷新  03猫眼设备
    String flagAndTitle = cPushMessage.getTitle();
    if (TextUtils.isEmpty(cPushMessage.getContent()) || TextUtils.isEmpty(flagAndTitle) || flagAndTitle.length() < 2)
      return;
    String flag = flagAndTitle.substring(0, 2);
    String title = flagAndTitle.substring(2);

    if (flag.equals("02")) {
      mOnRefreshMsgListener.refreshMsg();
                /*   context.sendBroadcast(intent);//发送消息广播
          System.out.println("发送刷新消息广播");*/
      System.out.println("消息 刷新");
      AndroidApplication.IS_HAS_NEW_MSG = true;
      System.out.println("消息标志 = " + AndroidApplication.IS_HAS_NEW_MSG);
    } else if (flag.equals(PushConstant.BUSI_CAT_PUSH)) {
      //cat eye
      CatEyePushData catEyePushData = CatEyePushData.parseFromJson(cPushMessage.getContent());
      if (catEyePushData == null || catEyePushData.getBody() == null) return;
      boolean appBackground = ((AndroidApplication) context.getApplicationContext()).isAppBackground();
      Intent intent = new Intent(context, CatEyeDoorbellActivity.class);
      intent.putExtra(Navigator.EXTRA_CAT_EYE_DOORBELL_PUSH_DATA, catEyePushData);
      if (!appBackground) {
        context.startActivity(intent);
      } else {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotificationUtil.showNotification(context, catEyePushData.getTitle(), catEyePushData.getTitle(), intent);
      }

    } else if (flag.equals("01")) {
      boolean appBackground = ((AndroidApplication) context.getApplicationContext()).isAppBackground();
      if (!appBackground) {
        context.startActivity(createPushMessageIntent(context, cPushMessage));
      } else {
        try {
          Gson gson = new Gson();
          GetAcceptContent acceptContent = gson.fromJson(cPushMessage.getContent(), GetAcceptContent.class);
          NotificationUtil.showNotification(context, title, acceptContent.getBodyContent(), createPushMessageIntent(context, cPushMessage));
        } catch (JsonSyntaxException e) {
          e.printStackTrace();
        }
      }

    } else if (flag.equals("00")) {
      if (isLogout()) return;
      boolean appBackground = ((AndroidApplication) context.getApplicationContext()).isAppBackground();
      if (!appBackground) {
        context.startActivity(createPushMessageIntent(context, cPushMessage));
      } else {
        NotificationUtil.showNotification(context, title, cPushMessage.getContent(), createPushMessageIntent(context, cPushMessage));
      }
    }

  }

  private boolean isLogout() {
    return TextUtils.isEmpty(PrefConstant.getAppToken());
  }

  @NonNull
  private Intent createPushMessageIntent(Context context, CPushMessage cPushMessage) {
    Intent intent = new Intent(context, AcceptInvitationActivity.class);
    intent.putExtra("ACCEPT_INVITATION_FLAG", cPushMessage);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  @Override
  public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
    System.out.println("onNotificationOpened context = [" + context + "], title = [" + title + "], summary = [" + summary + "], extraMap = [" + extraMap + "]");
//    onNotificationOpened context = [android.app.ReceiverRestrictedContext@c134896], title = [告警], summary = [多次错误（密码，指纹等）报警], extraMap = [{"_ALIYUN_NOTIFICATION_ID_":"184842","NotificationType":"lockalarm","data":"{\"deviceSerialNo\":\"FCCA7714004B1200\"}"}]
    if (TextUtils.isEmpty(extraMap)) return;

    // 点开通知
    if (extraMap.contains(LOCK_ALARM)) {

      Intent intent = new Intent(context, ExceptionWarnMoreActivity.class);
      intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, parseDeviceIdFormString(extraMap));
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    } else {
      Intent intent = new Intent(context, OtherMessageListActivity.class);
          /*  intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, mLockId);
            intent.putExtra(Navigator.EXTR_ROUTER_SERIAL_NUM, mRouterId);*/

      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }

  }

  private String parseDeviceIdFormString(String extraMap) {
    try {
      if (TextUtils.isEmpty(extraMap)) return "";
      String data = new JSONObject(extraMap).getString(KEY_DATA);
      return parseDeviceSerialNo(data);
    } catch (JSONException e) {
      e.printStackTrace();
      return "";
    }
  }

  @Override
  protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
    Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
  }

  @Override
  protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
    Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
  }

  @Override
  protected void onNotificationRemoved(Context context, String messageId) {
    Log.e("MyMessageReceiver", "onNotificationRemoved");
  }

  public interface OnRefreshMsgListener {
    void refreshMsg();
  }

  /**
   * 刷新消息 回调
   *
   * @param
   * @return
   */
  public static void setOnRefreshMsgListener(OnRefreshMsgListener mOnRefresgMsgListener) {
    mOnRefreshMsgListener = mOnRefresgMsgListener;
  }

  /**
   * 判断通知是否打开
   *
   * @param
   * @return
   */
  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private boolean isNotificationEnabled(Context context) {

    String CHECK_OP_NO_THROW = "checkOpNoThrow";
    String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
    ApplicationInfo appInfo = context.getApplicationInfo();
    String pkg = context.getApplicationContext().getPackageName();
    int uid = appInfo.uid;

    Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
    try {
      appOpsClass = Class.forName(AppOpsManager.class.getName());
      Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
          String.class);
      Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

      int value = (Integer) opPostNotificationValue.get(Integer.class);
      return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 系统的设置界面
   *
   * @param
   * @return
   */
  private void toSetting(Context context) {
    Intent localIntent = new Intent();
    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Build.VERSION.SDK_INT >= 9) {
      localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
      localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
    } else if (Build.VERSION.SDK_INT <= 8) {
      localIntent.setAction(Intent.ACTION_VIEW);
      localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
      localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
    }
    context.startActivity(localIntent);
  }

}
