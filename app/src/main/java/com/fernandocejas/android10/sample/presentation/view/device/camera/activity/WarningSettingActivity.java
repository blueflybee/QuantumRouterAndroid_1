/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.fernandocejas.android10.sample.presentation.view.device.camera.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWarningSettingBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.camera.util.CameraPopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class WarningSettingActivity extends BaseCatActivity{

  //intent
  private int window = 0;
  private int ringAndLCD;//按门铃亮屏开关：0关，1开
  private static final String TAG = "ShowSettingActivity";
  private int wakeTime = -1;//唤醒时长
  private String wakeTimeStr = "";
  private int language;//语言：0：中文，1：英文
  private String languageStr = "";
  private int safeGuardState = 0,bMDEnable = 0,alarmSound = 0,bMDEnableSensity = 0;
  //云台转到速度 云台速度速度范围3-255，速度设置后转动一下云台就能保存，不用单独保存速度
  private int REQUEST_FOR_SAFE_PERIOD = 0x88;

  private ActivityWarningSettingBinding mBinding;

  private CameraPopupWindow mPopWin;

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_warning_setting);

    initTitleBar("报警设置");

    initData();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private void initData() {

    //获取所有配置信息
    SovUtil.getStreamCatDataAll(window);

    mBinding.switchBtnWarningPush.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(safeGuardState == 0){
          safeGuardState = 1;//true
        }else {
          safeGuardState = 0;
        }

        DialogUtil.showProgress(WarningSettingActivity.this);

        SovUtil.setStreamAlarmEnable(window,
            String.format(AppConsts.FORMATTER_SET_DEV_SAFE_STATE, safeGuardState));

      }
    });

    mBinding.switchBtnMoveWatch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(bMDEnable == 0){
          bMDEnable = 1;//true
        }else {
          bMDEnable = 0;
        }

        DialogUtil.showProgress(WarningSettingActivity.this);

        SovUtil.setStreamAlarmMdetectSwicth(window,
            String.format(AppConsts.FORMATTER_SET_MDENABLE_STATE, bMDEnable));
      }
    });

    mBinding.switchBtnMoveVolume.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(alarmSound == 0){
          alarmSound = 1;//true
        }else {
          alarmSound = 0;
        }

        DialogUtil.showProgress(WarningSettingActivity.this);

        SovUtil.setStreamAlarmSound(window,
            String.format(AppConsts.FORMATTER_SET_ALARM_SOUND,alarmSound));
      }
    });

  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    DialogUtil.hideProgress();
    switch (what) {
      //连接结果,视频异常断开时关闭此界面
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        if(arg2!= JVNetConst.CCONNECTTYPE_CONNOK){
          this.finish();
        }
        break;
      }
      //流媒体猫眼，设置协议回调
      case JVNetConst.CALL_CATEYE_SENDDATA: {
        try {
          org.json.JSONObject object = new org.json.JSONObject(obj.toString());
          String data = object.getString("data");
          int nCmd = object.getInt("nCmd");
          int nPacketType = object.getInt("nPacketType");
          int result = object.getInt("result");
          String reason = object.getString("reason");
          Log.e(TAG, "nPacketType=" + nPacketType + "；nCmd=" + nCmd + "；obj=" + obj.toString());

          switch (nCmd) {
            //配置信息全的回调
            case JVNetConst.SRC_PARAM_ALL: {
              refreshSetUi(JSONObject.parseObject(data));
              break;
            }

            //1.报警设置相关回调
            case JVNetConst.SRC_INTELLIGENCE: {

              switch (nPacketType) {
                case JVNetConst.SRC_EX_INTELLIGENCE_REFRESH://报警设置模块全部参数
                  break;
                case JVNetConst.SRC_EX_INTELLIGENCE_ALARM_ENABLE://报警开关
                  if (result == 1) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    mBinding.llWarningRelative.setVisibility(View.VISIBLE);
                    refreshWarningPushBtn();
                  } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                    mBinding.llWarningRelative.setVisibility(View.GONE);
                  }
                  break;

                case JVNetConst.SRC_EX_INTELLIGENCE_MDETECT_SWITCH://移动侦测开关
                  if (result == 1) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    refreshMoveAccuracyBtn();
                  } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                  }
                  break;
                case JVNetConst.SRC_EX_INTELLIGENCE_MDETECT_SENS://移动侦测灵敏度
                  if (result == 1) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    //获取所有配置信息
                    SovUtil.getStreamCatDataAll(window);
                  } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                  }
                  break;
                case JVNetConst.SRC_EX_INTELLIGENCE_ALARM_SOUND://报警声音开关
                  if (result == 1) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    refreshVolumeBtn();
                  } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                  }
                  break;
                default:
                  break;
              }
              break;
            }

            default: {
              break;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          makeText(this,"Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }
  }

  /**
   * 根据配置信息刷新UI
   *
   * @param streamJson
   */

  public void refreshSetUi(JSONObject streamJson) {

    // 1. 安全防护开关
    safeGuardState = streamJson.containsKey(AppConsts.TAG_SET_DEV_SAFE_STATE) ?
        streamJson.getInteger(AppConsts.TAG_SET_DEV_SAFE_STATE) : -1;//报警开关
    refreshWarningPushBtn();

    // 2. 移动侦测开关
    bMDEnable = streamJson.containsKey(AppConsts.TAG_SET_MDENABLE_STATE) ?
        streamJson.getInteger(AppConsts.TAG_SET_MDENABLE_STATE) : -1;//移动侦测开关
    refreshMoveAccuracyBtn();

    // 3. 移动侦测灵敏度
    bMDEnableSensity = streamJson.containsKey(AppConsts.TAG_NMDSENSITIVITY) ?
        streamJson.getInteger(AppConsts.TAG_NMDSENSITIVITY) : -1;//移动侦测灵敏度

    // 4. 报警声音开关
    alarmSound = streamJson.containsKey(AppConsts.TAG_SET_ALARM_SOUND) ?
        streamJson.getInteger(AppConsts.TAG_SET_ALARM_SOUND) : -1;//报警声音开关
    refreshVolumeBtn();

  }

  private void refreshWarningPushBtn(){
    if (safeGuardState == 1) {
      mBinding.switchBtnWarningPush.setChecked(true);
      mBinding.llWarningRelative.setVisibility(View.VISIBLE);
    } else {
      mBinding.switchBtnWarningPush.setChecked(false);
      mBinding.llWarningRelative.setVisibility(View.GONE);
    }
  }

  private void refreshMoveAccuracyBtn(){
    if (bMDEnable == 1) {
      mBinding.switchBtnMoveWatch.setChecked(true);
    } else {
      mBinding.switchBtnMoveWatch.setChecked(false);
    }
  }

  private void refreshVolumeBtn(){
    if (alarmSound == 1) {
      mBinding.switchBtnMoveVolume.setChecked(true);
    } else {
      mBinding.switchBtnMoveVolume.setChecked(false);
    }
  }

  /**
  * 点击事件
  *
  * @param
  * @return
  */
  public void onSafePeriodClick(View view) {
    Intent intent = new Intent(this,SafePeriodSetingActivity.class);
    startActivityForResult(intent,REQUEST_FOR_SAFE_PERIOD);
  }

  public void onMoveAccuracy(View view) {
    mPopWin = new CameraPopupWindow(this,"0",bMDEnableSensity);
    mPopWin.setFocusable(true);
    mPopWin.showAtLocation(mPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);
    
    mPopWin.setOnPositiveClickListener(new CameraPopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int progress,View dialogPos) {

        DialogUtil.showProgress(WarningSettingActivity.this);
        System.out.println("progress = " + progress);
        SovUtil.setStreamAlarmMDSens(window,
            String.format(AppConsts.FORMATTER_NMDSENSITIVITY, progress));

      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if(data == null){
      return;
    }

    //获取所有配置信息
    SovUtil.getStreamCatDataAll(window);
  }
}
