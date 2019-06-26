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

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityShowSettingBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeZoneBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class TimeZoneActivity extends BaseCatActivity{
  private ActivityTimeZoneBinding mBinding;
  private int REQUEST_CODE_FOR_TIME_ZONE_SET = 0x13;
  //intent
  private int window = 0;
  private static final String TAG = "TimeZoneActivity";
  private int timeZone;//时区-12~12 表示东12-西12
  private String timeZoneStr = "";

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_zone);

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
  }

  private void initView() {
    initTitleBar("时区设置");
  }

  /**
   * 点击事件处理
   *
   * @param
   * @return
   */
  public void onTimeZoneSetClick(View view) {
    Intent intent = new Intent(this,TimeZoneSettingActivity.class);
    intent.putExtra("TimeZoneSet",timeZoneStr);
    startActivityForResult(intent,REQUEST_CODE_FOR_TIME_ZONE_SET);
  }

  public void onTimeSetClick(View view) {
    Intent intent = new Intent(this,TimeSettingActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (data == null) {
      return;
    }

    if (requestCode == REQUEST_CODE_FOR_TIME_ZONE_SET) {
      timeZoneStr = data.getStringExtra("CHOOSE_TIME_ZONG_SET");
      mBinding.tvTimeZone.setText(timeZoneStr);
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
          Log.e(TAG, "nPacketType=" + nPacketType + "；nCmd=" + nCmd + "；obj=" + obj.toString());

          switch (nCmd) {
            //配置信息全的回调
            case JVNetConst.SRC_PARAM_ALL: {
              analyzeAll(JSONObject.parseObject(data));
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
   * 从配置信息全里面解析设置相关内容的状态
   *
   * @param data
   */
  private void analyzeAll(JSONObject data) {
    //2.时间时区

    if(AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())){
      timeZone = 12 - data.getInteger(AppConsts.TAG_TIME_ZONE_STREAM);
    }else if(AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())){
      timeZone = 12 - data.getInteger(AppConsts.TIME_ZONE);
    }

    refreshTimeZoneState(timeZone);
  }

  private void refreshTimeZoneState(int timeZone){
    switch (timeZone) {
      // //时区-12~12 表示东12-西12
      case 0:
        timeZoneStr = "东十二区";
        break;
      case 1:
        timeZoneStr = "东十一区";
        break;
      case 2:
        timeZoneStr = "东十区";
        break;
      case 3:
        timeZoneStr = "东九区";
        break;
      case 4:
        timeZoneStr = "东八区";
        break;
      case 5:
        timeZoneStr = "东七区";
        break;
      case 6:
        timeZoneStr = "东六区";
        break;
      case 7:
        timeZoneStr = "东五区";
        break;
      case 8:
        timeZoneStr = "东四区";
        break;
      case 9:
        timeZoneStr = "东三区";
        break;
      case 10:
        timeZoneStr = "东二区";
        break;
      case 11:
        timeZoneStr = "东一区";
        break;
      case 12:
        timeZoneStr = "中时区";
        break;
      case 13:
        timeZoneStr = "西一区";
        break;
      case 14:
        timeZoneStr = "西二区";
        break;
      case 15:
        timeZoneStr = "西三区";
        break;
      case 16:
        timeZoneStr = "西四区";
        break;
      case 17:
        timeZoneStr = "西五区";
        break;
      case 18:
        timeZoneStr = "西六区";
        break;
      case 19:
        timeZoneStr = "西七区";
        break;
      case 20:
        timeZoneStr = "西八区";
        break;
      case 21:
        timeZoneStr = "西九区";
        break;
      case 22:
        timeZoneStr = "西十区";
        break;
      case 23:
        timeZoneStr = "西十一区";
        break;
      case 24:
        timeZoneStr = "西十二区";
        break;
    }

    mBinding.tvTimeZone.setText(timeZoneStr);
  }
}
