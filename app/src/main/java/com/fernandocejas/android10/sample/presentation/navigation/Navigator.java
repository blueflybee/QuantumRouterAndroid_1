/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

  public static final String EXTR_APP_MENUS = "app_menus";
  public static final String EXTR_REGISTER_PHONE = "extr_register_phone";
  public static final String EXTR_RESET_PWD_PHONE = "extr_reset_pwd_phone";
  public static final String EXTR_AGREEMENT_CONTENT = "extr_agreement_content";
  public static final String EXTR_SERECT_AGREEMENT_CONTENT = "extr_serect_agreement_content";
  public static final String EXTR_QUESTION_LIST_CONTENT = "extr_question_list_content";
  public static final String EXTR_QUESTION_DETAIL_CONTENT = "extr_question_detail_content";
  public static final String EXTR_MY_ADVICE = "extr_my_advice";
  public static final String EXTR_ADVICE_DETAIL = "extr_advice_detail";
  public static final String EXTR_ADVICE_POTISION = "extr_advice_position";
  public static final String EXTR_IS_FORGET_PWD = "extr_is_forget_pwd";
  public static final String EXTR_ROUTER_LIST = "extr_router_list";
  public static final String EXTR_SHARED_MEM_LIST = "extr_shared_mem_list";
  public static final String EXTR_ROUTER_SERIAL_NUM = "extr_router_serial_num";
  public static final String EXTR_INTEL_DEVICE_ID = "extr_intel_device_id";
  public static final String EXTRA_ROUTER_ID = "extra_router_id";
  public static final String EXTRA_ADD_ROUTER_INFO = "extra_add_router_info";
  public static final String EXTRA_ADD_INTEL_DEV_INFO = "extra_add_intel_dev_info";
  public static final String EXTRA_FINGER_LIST = "extra_finger_list";
  public static final String EXTRA_FINGER_EDIT_FLAG = "extra_finger_edit_flag";
  public static final String EXTRA_DEVICE_SERIAL_NO = "extra_device_serial_no";
  public static final String EXTRA_DEVICE_NAME = "extra_device_name";
  public static final String EXTRA_FINGER_ID = "extra_finger_id";

  public static final int EDIT_FINGER_NAME = 1;
  public static final int ADD_FINGER_NAME = 2;
  public static final int DELETE_FINGER = 3;
  public static final int OPEN_REMOTE_DOOR = 5;
  public static final String EXTRA_LZKEYINFO = "extra_lzkeyinfo";
  public static final String EXTRA_ROUTER_NAME = "extra_router_name";
  public static final String EXTRA_ROUTER_TYPE = "extra_router_type";
  public static final String EXTRA_ROUTER_MODEL = "extra_router_model";
  public static final String EXTRA_MEM_DETAIL = "extra_mem_detail";
  public static final String EXTRA_MODIFY_PROPERTY = "extra_modify_property";
  public static final String EXTRA_MODIFY_HINT = "extra_modify_hint";
  public static final String EXTRA_ROUTER_DESCRIPTION = "extra_router_description";
  public static final String EXTRA_ROUTER_GROUP = "extra_router_group";
  public static final String EXTRA_NET_MODE_DATA = "extra_net_mode_data";
  public static final String EXTRA_TO_FRAGMENT = "extra_to_fragment";
  public static final String EXTRA_WIRELESS_RELAY_WIFI_NAME = "extra_wireless_relay_wifi_name";
  public static final String EXTRA_CONNECTED_LOCK = "extra_connected_lock";
  public static final String EXTRA_OWNED_ROUTER = "extra_owned_router";
  public static final String EXTRA_LOCK_PWD = "extra_lock_pwd";
  public static final String EXTRA_LOCK_FP = "extra_lock_fp";
  public static final String EXTRA_LOCK_DOOR_CARD = "extra_lock_door_card";
  public static final String EXTRA_ADD_LOCK_PWD = "extra_add_lock_pwd";
  public static final String EXTRA_LOCK_PAGE = "extra_lock_page";
  public static final String EXTRA_ADD_FP_ONLY = "extra_add_fp_only";
  public static final String EXTRA_ADD_PWD_ONLY = "extra_add_pwd_only";
  public static final String EXTRA_OLD_ADMIN_PWD = "extra_old_admin_pwd";
  public static final String EXTRA_LOCK_FP_ID = "extra_lock_fp_id";
  public static final String EXTRA_LOCK_DOOR_CARD_ID = "extra_lock_door_card_id";
  public static final String EXTRA_ONLY_INJECT_KEY = "extra_only_inject_key";
  public static final String EXTRA_LOCK_BIND_ROUTER_ID = "extra_lock_bind_router_id";
  public static final String EXTRA_BIND_ROUTER_TO_LOCK_ONLY = "extra_bind_router_to_lock_only";
  public static final String EXTRA_BINDED_ROUTER_ID = "extra_binded_router_id";
  public static final String EXTRA_LOCK_USER = "extra_lock_user";
  public static final String EXTRA_FACTORY_RESET_LOCK_NAME = "extra_factory_reset_lock_name";


  //ble
  public static final String EXTRA_BLE_DEVICE_NAME = "ble_device_name";
  public static final String EXTRA_BLE_DEVICE_ADDRESS = "ble_device_address";
  public static final String EXTRA_LOCK_PIN_MD5 = "extra_pin_md5";
  public static final String EXTRA_BLE_LOCK = "extra_ble_lock";
  public static final String EXTRA_NEW_LOCK_NAME = "new_lock_name";
  public static final String EXTRA_KEY_INVALID_TYPE = "extra_key_invalid_type";
  public static final String EXTRA_LOCK_FIRMWARE_DOWNLOAD_URL = "extra_lock_firmware_download_url";
  public static final String EXTRA_LOCK_UPDATE_FIRMWARE_MESSENGER = "extra_lock_update_firmware_messenger";
  public static final String EXTRA_LOCK_FIRMWARE_PATH = "extra_lock_firmware_path";
  public static final String EXTRA_NEW_ROUTER_NAME = "extra_new_router_name";
  public static final String EXTRA_LOCK_MANAGE_TIPS_TYPE = "extra_lock_manage_tips_type";
  public static final String EXTRA_LOCK_MANAGE_TIPS_DEVICE_ID = "extra_lock_manage_tips_device_id";
  public static final String EXTRA_CAT_EYE_DOORBELL_PUSH_DATA = "extra_cat_eye_doorbell_push_data";
  public static final String EXTRA_LOCK_TIPS_TITLE = "extra_lock_tips_title";
  public static final String EXTRA_LOCK_TIPS_CONTENT = "extra_lock_tips_content";
  public static final String EXTRA_BIND_LITE_GATEWAY = "extra_bind_lite_gateway";
  public static final String EXTRA_LITE_GATEWAY = "extra_lite_gateway";
  public static final String EXTRA_FOR_RESULT_DATA = "extra_result_data";
  public static final String EXTRA_CAT_WAVE_CONFIG = "extra_cat_wave_config";
  public static final String EXTRA_PLAY_PATH = "extra_play_path";
  public static final String EXTRA_VIDEO_TIME = "extra_video_time";

  @Inject
  public void Navigator() {
    //empty
  }

  /**
   * 跳转到已有的界面
   * 并且清除它之后的所有已经启动的界面
   */
  public void navigateExistAndClearTop(Context context, Class<?> toClazz) {
    navigateExistAndClearTop(context, toClazz, null);
  }

  public void navigateExistAndClearTop(Context context, Class<?> toClazz, Intent intent) {
    if (context == null) return;
    if (intent == null) {
      intent = new Intent();
    }
    intent.setClass(context, toClazz);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(intent);
  }

  /**
   * 启动新界面到新栈
   * 并且清除其它所有界面
   */
  public void navigateNewAndClearTask(Context context, Class<?> toClazz) {
    navigateNewAndClearTask(context, toClazz, null);
  }

  public void navigateNewAndClearTask(Context context, Class<?> toClazz, Intent intent) {
    if (context == null) return;
    if (intent == null) {
      intent = new Intent();
    }
    intent.setClass(context, toClazz);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);
  }

  /**
   * 正常启动界面
   * @param context
   * @param toClazz
   */
  public void navigateTo(Context context, Class<?> toClazz) {
    navigateTo(context, toClazz, null);
  }

  public void navigateTo(Context context, Class<?> toClazz, Intent intent) {
    if (context == null) return;
    if (intent == null) {
      intent = new Intent();
    }
    intent.setClass(context, toClazz);
    context.startActivity(intent);
  }

}
