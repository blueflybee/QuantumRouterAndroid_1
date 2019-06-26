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

package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityIntelligentSettingBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import java.util.Locale;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class IntelligentSettingActivity extends BaseCatActivity{
  private ActivityIntelligentSettingBinding mBinding;
  private int REQUEST_CODE_FOR_TRIGGER_TIME = 0x15;
  //intent
  private int window = 0;
  private int pirEnable;//红外感应开关：0关，1开
  private int mPirTime;//红外触发时间

  private int alarmSoundOutEnable;//门外报警音：0关，1开
  private static final String TAG = "IntelligentSettingActivity";

  private String mTriggerTime = "";

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intelligent_setting);

    initView();

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

    mBinding.switchBtnTriggerTouch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(IntelligentSettingActivity.this);
        SovUtil.setStreamPirEnable(window,
            String.format(Locale.CHINA, "bPirEnable=%d", (pirEnable + 1) % 2));
      }
    });

    mBinding.switchBtnWarningPrompt.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(IntelligentSettingActivity.this);
        SovUtil.setStreamFriendAlarmEnable(window,
            String.format(AppConsts.FORMATTER_BFRIENDALARMENABLE, (alarmSoundOutEnable + 1) % 2));
      }
    });

  }

  private void initView() {
    initTitleBar("智能设置");
  }

  /**
  * 触发时间
  *
  * @param
  * @return
  */
  public void onTriggerTimeClick(View view) {
    Intent intent = new Intent(this,InfraredTriggerTimeActivity.class);
    intent.putExtra("TrigerTime",mTriggerTime);
    startActivityForResult(intent,REQUEST_CODE_FOR_TRIGGER_TIME);
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (data == null) {
      return;
    }

    if (requestCode == REQUEST_CODE_FOR_TRIGGER_TIME) {
      mTriggerTime = data.getStringExtra("CHOOSE_TRIGGER_TIME");
      mBinding.tvTriggerTime.setText(mTriggerTime);
    }

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

          switch (nCmd) {
            //配置信息全的回调
            case JVNetConst.SRC_PARAM_ALL: {
              analyzeAll(JSONObject.parseObject(data));
              break;
            }

            //3.智能设置相关
            case JVNetConst.SRC_INTELLIGENCE: {
              switch (nPacketType) {
                //红外感应开关设置回调
                case JVNetConst.SRC_EX_INTELLIGENCE_PIR:
                  if (result == 0) {
                    makeText(this, "设置失败", LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(this,"设置成功", Toast.LENGTH_SHORT).show();
                    pirEnable = (pirEnable + result) % 2;
                    /*pirBtn.setText(pirEnable == 0 ? "关" : "开");*/
                    refreshTriggerBtnState();
                  }

                  break;

                //门外报警音开关设置回调
                case JVNetConst.SRC_EX_INTELLIGENCE_FRIEND_ALARM:
                  if (result == 0) {
                    makeText(this, "设置失败", LENGTH_SHORT).show();
                    break;
                  } else {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    alarmSoundOutEnable = (alarmSoundOutEnable + result) % 2;
                    refreshWarnBtnState();
                  }
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
          makeText(this, "Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }
  }

  /**
   * 从配置信息全里面解析设置相关内容的状态
   *
   * @param data
   */
  private void analyzeAll(JSONObject data) {
    //3.智能设置
    pirEnable = data.getInteger(AppConsts.TAG_BPIRENABLE);
    refreshTriggerBtnState();

    mPirTime = data.containsKey(AppConsts.TAG_BPIRTIME) ?
        data.getInteger(AppConsts.TAG_BPIRTIME) : -1;
    refreshTriggerTimeState((mPirTime + 1) * 5);

    alarmSoundOutEnable = data.containsKey(AppConsts.TAG_BFRIENDALARMENABLE) ?
        data.getInteger(AppConsts.TAG_BFRIENDALARMENABLE) : -1;
    refreshWarnBtnState();
  }

  private void refreshTriggerBtnState(){
    if(pirEnable == 0){
      mBinding.switchBtnTriggerTouch.setChecked(false);
    }else {
      mBinding.switchBtnTriggerTouch.setChecked(true);
    }
  }

  private void refreshWarnBtnState(){
    if(alarmSoundOutEnable == 0){
      mBinding.switchBtnWarningPrompt.setChecked(false);
    }else {
      mBinding.switchBtnWarningPrompt.setChecked(true);
    }
  }

  private void refreshTriggerTimeState(int time){
    switch (time) {
      case 5:
        mTriggerTime = "5s";
        break;
      case 10:
        mTriggerTime = "10s";
        break;
      case 15:
        mTriggerTime = "15s";
        break;
      default:
        mTriggerTime = "实时";
        break;
    }

    mBinding.tvTriggerTime.setText(mTriggerTime);
  }
}
