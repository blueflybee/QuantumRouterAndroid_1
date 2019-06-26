package com.fernandocejas.android10.sample.data.constant;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.utils.MD5Util;

import java.io.File;
import java.util.Map;

/**
 * Preferences 常量
 *
 * @author shaojun
 * @name PrefConstant
 * @package com.fernandocejas.android10.sample.presentation.comm
 * @date 15-10-10
 */
public class PrefConstant {

  public static final String SP_NAME = "sp_router";
  public static String MSG_DEVICE_ID = "msg_device_id";  //消息推送设备ID
  public static String SAMBA_CACHE_NUM = "samba_cache_count";  //samba缓存数量
  public static final String SAMBA_DOWNLOADERS = "samba_downloaders";  //samba下载器
  public static String ROUTER_ID_CONNECTED_DIRECT = "FFFF"; //判断网络是不是直连网关

  public static Boolean IS_AUTI_DEVICE_AUTHED = false; //防蹭网设备是否认证

  public static Boolean IS_ANTI_SWITCH = false; //判断防蹭网是否开启
  public static Boolean IS_ANTI_LIMIT_NET = false; //判断防蹭网是否禁止访问网关

  public static final String SP_USER_HEAD_IMG = "user_head_img";
  public static final String SP_USER_NICK_NAME = "user_nick_name";
  public static final String SP_APP_VERSION_CODE = "app_version_code";

  private static final String SP_APP_TOKEN = "app_token";
  private static final String SP_CLOUD_URL = "cloud_url";

  public static final String SP_TOKEN = "app_token";
  private static final String SP_USER_ID = "sp_user_id";
  public static final String SP_USER_UNIQUE_KEY = "sp_user_unique_key";
  public static final String SP_USER_PHONE = "sp_user_phone";
  public static final String SP_USER_PWD = "sp_user_pwd";
  public static final String SP_PICTURE_RESTORE_STATE = "sp_picture_restore_state";
  public static final String SP_PICTURE_RESTORE_ROUTER_ID = "sp_picture_restore_router_id";
  public static final String SP_SAMBA_ACCOUNT = "sp_samba_account";
  public static final String SP_SAMBA_BATCH_UPLOAD_PATH = "sp_samba_upload_path";
  public static final String SP_SAMBA_PWD = "sp_samba_pwd";

  public static final String SP_PICTURE_RESTORED_COUNT = "sp_picture_restored_count";//已备份图片数
  public static final String SP_PICTURE_RESTORED_SUM_COUNT = "sp_picture_restored_sum_count";//待备份的总数


  public static final String SP_BLE_STOP_SCAN = "sp_ble_stop_scan";//待备份的总数

  //public static final String SAMBA_CACHE_PATH = String.valueOf(Environment.getExternalStorageDirectory()) + File.separator + "sbsamba/";//samba缓存的本地路径
  private static SPUtils sSpUtils = new SPUtils(SP_NAME);


  public static void setUserId(int userId) {
    new SPUtils(SP_NAME).put(SP_USER_ID, userId);
  }

  /**
   * 获取门锁用户id
   */
  public static int getUserId() {
    int userId = new SPUtils(SP_NAME).getInt(SP_USER_ID);
    return userId == -1 ? 0 : userId;
  }

  public static String getUserIdInHexString() {
    String userId = Integer.toHexString(PrefConstant.getUserId());
    StringBuilder sb = new StringBuilder();
    sb.append(userId);
    if (userId.length() < 8) {
      int supplyZeroLength = 8 - userId.length();
      for (int i = 0; i < supplyZeroLength; i++) {
        sb.insert(0, "0");
      }
    }
    return sb.toString();
  }

  public static String getUserIdInHexString(int lockUserId) {
    String userId = Integer.toHexString(lockUserId);
    StringBuilder sb = new StringBuilder();
    sb.append(userId);
    if (userId.length() < 8) {
      int supplyZeroLength = 8 - userId.length();
      for (int i = 0; i < supplyZeroLength; i++) {
        sb.insert(0, "0");
      }
    }
    return sb.toString();
  }

  /**
   * 根据设备类型获取网关或门锁的用户id
   */
  public static String getUserUniqueKey(String deviceType) {
    return "0".equals(deviceType) ? new SPUtils(SP_NAME).getString(SP_USER_UNIQUE_KEY) : String.valueOf(PrefConstant.getUserId());
  }

  public static String getUserHeadImg() {
    return new SPUtils(SP_NAME).getString(SP_USER_HEAD_IMG);
  }

  public static void putUserHeadImg(String imageUrl) {
    new SPUtils(SP_NAME).put(SP_USER_HEAD_IMG, imageUrl);
  }

  public static String getUserNickName() {
    return new SPUtils(SP_NAME).getString(SP_USER_NICK_NAME);
  }

  public static void putUserNickName(@NonNull String nickName) {
    new SPUtils(SP_NAME).put(SP_USER_NICK_NAME, nickName);
  }

  public static String getAppToken() {
    return new SPUtils(SP_NAME).getString(SP_APP_TOKEN);
  }

  public static void putAppToken(@NonNull String token) {
    new SPUtils(SP_NAME).put(SP_APP_TOKEN, token);
  }

  public static String getCloudUrl() {
    return new SPUtils(SP_NAME).getString(SP_CLOUD_URL);
  }

  public static void putCloudUrl(@NonNull String url) {
    new SPUtils(SP_NAME).put(SP_CLOUD_URL, url);
  }

  public static String getUserPwd() {
    return new SPUtils(SP_NAME).getString(SP_USER_PWD);
  }

  public static void setUserPwd(String pwd) {
    new SPUtils(SP_NAME).put(PrefConstant.SP_USER_PWD, MD5Util.encryption(pwd));
  }


  public static String getUserPhone() {
    return new SPUtils(SP_NAME).getString(SP_USER_PHONE);
  }

  /**
   * samba 图片备份总开关
   *
   * @param
   * @return
   */
  public static Boolean getPictureRestoreState() {
    return new SPUtils(SP_NAME).getBoolean(SP_PICTURE_RESTORE_STATE);
  }

  public static void putPictureRestoreState(Boolean gPictureRestore) {
    new SPUtils(SP_NAME).put(PrefConstant.SP_PICTURE_RESTORE_STATE, gPictureRestore);
  }

  public static String getPictureRestoreRouterId() {
    return new SPUtils(SP_NAME).getString(SP_PICTURE_RESTORE_ROUTER_ID);
  }

  public static void putPictureRestoreRouterId(String routerId) {
    new SPUtils(SP_NAME).put(SP_PICTURE_RESTORE_ROUTER_ID, routerId);
  }

  public static void putSambaAccount(String account) {
    new SPUtils(SP_NAME).put(SP_SAMBA_ACCOUNT, account);
  }

  public static String getSambaAccount() {
    return new SPUtils(SP_NAME).getString(SP_SAMBA_ACCOUNT);
  }

  /**
  * smb批量上传的地址
  *
  * @param
  * @return
  */
  public static void putBatchUploadCurrentPath(String account) {
    new SPUtils(SP_NAME).put(SP_SAMBA_BATCH_UPLOAD_PATH, account);
  }

  public static String getBatchUploadCurrentPath() {
    return new SPUtils(SP_NAME).getString(SP_SAMBA_BATCH_UPLOAD_PATH);
  }

  public static void putSambaPwd(String pwd) {
    new SPUtils(SP_NAME).put(SP_SAMBA_PWD, pwd);
  }

  public static String getSambaPwd() {
    return new SPUtils(SP_NAME).getString(SP_SAMBA_PWD);
  }

  public static String getMsgDeviceID() {
    return new SPUtils(SP_NAME).getString(MSG_DEVICE_ID);
  }

  public static int getAppVersionCode() {
    return new SPUtils(SP_NAME).getInt(SP_APP_VERSION_CODE, 0);
  }

  public static void saveAppVersionCode(int versionCode) {
    new SPUtils(SP_NAME).put(SP_APP_VERSION_CODE, versionCode);
  }
//
//  /**
//   * 保存是否停止后台蓝牙扫描设置
//   *
//   * @param stopScan
//   */
//  public static void setBleStopScan(boolean stopScan) {
//    sSpUtils.put(SP_BLE_STOP_SCAN, stopScan);
//  }
//
//  public static boolean getBleStopScan() {
//    return sSpUtils.getBoolean(SP_BLE_STOP_SCAN, false);
//  }
}
