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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityShowSettingBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class ShowSettingActivity extends BaseCatActivity {

  private int REQUEST_CODE_FOR_SCREEN_TIME = 0x11;
  private int window = 0;
  private int ringAndLCD;//按门铃亮屏开关：0关，1开
  private static final String TAG = "ShowSettingActivity";
  private int wakeTime = -1;//唤醒时长
  private String wakeTimeStr = "";
  private int language;//语言：0：中文，1：英文
  private String languageStr = "";

  private ActivityShowSettingBinding mBinding;

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_show_setting);

    initTitleBar("显示设置");

    //获取所有配置信息
    SovUtil.getStreamCatDataAll(window);

    initData();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private void refreshCheckBtnState() {
    if (ringAndLCD == 0) {  //关
      mBinding.switchBtnClickDoor.setChecked(false);
    } else if (ringAndLCD == 1) {
      mBinding.switchBtnClickDoor.setChecked(true);
    }

    System.out.println("ringAndLCD refreshCheckBtnState设置返回 = " + ringAndLCD);
  }

  private void refreshWakeTimeState(int wakeTime) {
    switch (wakeTime) {
      case 15:
        wakeTimeStr = "15秒后息屏";
        break;
      case 60:
        wakeTimeStr = "1分钟后息屏";
        break;
      case 120:
        wakeTimeStr = "2分钟息屏";
        break;
      case 180:
        wakeTimeStr = "3分钟后息屏";
        break;
      default:
        wakeTimeStr = "永不息屏";
        break;
    }

    mBinding.tvWakeTime.setText(wakeTimeStr);
  }

  private void refreshLanguageState(int language) {
    switch (language) {
      case 0:
        languageStr = "简体中文";
        break;
      case 1:
        languageStr = "英文";
        break;
    }

    mBinding.tvLanguage.setText(languageStr);
  }

  private void initData() {

    mBinding.switchBtnClickDoor.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        System.out.println("ringAndLCD switchBtnClickDoor = " + ringAndLCD);

        DialogUtil.showProgress(ShowSettingActivity.this);
        SovUtil.setStreamRingAndLCD(window,
            String.format(AppConsts.FORMATTER_RINGANDLCD, (ringAndLCD + 1) % 2));
        System.out.println("猫眼 设置完成 亮屏 onClick ");
      }
    });

  }

  /**
   * 点击事件处理
   *
   * @param
   * @return
   */
  public void onScreenOffClick(View view) {
    Intent intent = new Intent(this, ScreenOffTimeActivity.class);
    intent.putExtra("ScreenOffTime", wakeTimeStr);
    /*intent.putExtra("ringAndLCD", ringAndLCD);*/
    startActivity(intent);

    System.out.println("ringAndLCD onScreenOffClick = " + ringAndLCD);
  }

/*  public void onLanguageTypeClick(View view) {
    Intent intent = new Intent(this, LanguageTypeActivity.class);
    intent.putExtra("LanguageType", languageStr);
    startActivityForResult(intent, REQUEST_CODE_FOR_LANGUAGE_TYPE);
  }*/

/*  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    System.out.println("ringAndLCD onActivityResult = " + ringAndLCD);

    if (requestCode == REQUEST_CODE_FOR_SCREEN_TIME) {
      wakeTimeStr = data.getStringExtra("CHOOSE_SCEEN_OFF_TIME");
      mBinding.tvWakeTime.setText(wakeTimeStr);
    }

    *//*if (requestCode == REQUEST_CODE_FOR_LANGUAGE_TYPE) {
      languageStr = data.getStringExtra("CHOOSE_LANGUAGE_TYPE");
      mBinding.tvLanguage.setText(languageStr);
    }*//*
  }*/

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    System.out.println("猫眼 设置完成回来 onHandler ");
    DialogUtil.hideProgress();
    switch (what) {
      //连接结果,视频异常断开时关闭此界面
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        if (arg2 != JVNetConst.CCONNECTTYPE_CONNOK) {
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
            //1.显示设置回调 7
            case JVNetConst.SRC_DISPLAY: {
              switch (nPacketType) {
                //按门铃亮屏设置回调 5
                case JVNetConst.SRC_EX_DISPLAY_RINGANDLCD: {
                  if (result == 0) {
                    makeText(this, "设置失败", LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    ringAndLCD = (ringAndLCD + result) % 2;
                    /*ringAndLCDBtn.setText(ringAndLCD == 0 ? "关" : "开");*/
                    refreshCheckBtnState();
                  }

                  break;
                }
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
    //1.显示设置
    ringAndLCD = data.containsKey(AppConsts.TAG_RINGANDLCD) ?
        data.getInteger(AppConsts.TAG_RINGANDLCD) : -1;
    refreshCheckBtnState();

    wakeTime = data.containsKey(AppConsts.TAG_NLCDSHOWTIME) ?
        data.getInteger(AppConsts.TAG_NLCDSHOWTIME) : -1;
    refreshWakeTimeState(wakeTime);

   /* language = data.containsKey(AppConsts.TAG_LANGUAGE) ?
        data.getInteger(AppConsts.TAG_LANGUAGE) : -1;
    refreshLanguageState(language);*/
  }
  @Override
  protected void onResume() {
    SovUtil.enableStreamCatVideoData(window, true);
    super.onResume();
  }

  @Override
  protected void onPause() {
    SovUtil.enableStreamCatVideoData(window, false);
    super.onPause();
  }

}
