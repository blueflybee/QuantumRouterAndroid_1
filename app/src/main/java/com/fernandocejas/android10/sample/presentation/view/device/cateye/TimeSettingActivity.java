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

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeSettingBinding;
import com.fernandocejas.android10.sample.presentation.utils.CustomDatePicker;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class TimeSettingActivity extends BaseCatActivity{
  private int window = 0;
  private static final String TAG = "TimeSettingActivity";
  private int sntp;//网络校时开关：0关，1开

  private ActivityTimeSettingBinding mBinding;

  private CustomDatePicker customDatePicker;

  private String mSettingTime = "";

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_setting);

    initTitleBar("时间设置");

    initData();
  }

  private void initDatePicker() {
    String now = mBinding.tvTimeSetting.getText().toString();

    customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
      @Override
      public void handle(String time) { // 回调接口，获得选中的时间

        if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {

          mSettingTime = time;

          System.out.println("mSettingTime = " + mSettingTime);

          //点击完成 设置时间
          DialogUtil.showProgress(TimeSettingActivity.this);
          SovUtil.setStreamCatTime(window,mSettingTime);
        } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {

          mSettingTime = time+":00";

          System.out.println("mSettingTime = " + mSettingTime);

          //点击完成 设置时间
          DialogUtil.showProgress(TimeSettingActivity.this);
          SovUtil.setStreamTime(window, mSettingTime.replace(" ", "-"));
        }

      }
    }, "2000-01-01 00:00", "2100-01-01 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
    customDatePicker.showSpecificTime(true); // 显示时和分
    customDatePicker.setIsLoop(true); // 允许循环滚动

    customDatePicker.show(mBinding.tvTimeSetting.getText().toString());
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private void refreshCheckBtnState(){
    if(sntp == 0){  //关
      mBinding.switchBtnNetTime.setChecked(false);
      mBinding.rlTimeSet.setClickable(true);
      mBinding.tvTimeSet.setTextColor(getResources().getColor(R.color.black_424242));
    }else if(sntp == 1){
      mBinding.switchBtnNetTime.setChecked(true);
      mBinding.rlTimeSet.setClickable(false);
      mBinding.tvTimeSet.setTextColor(getResources().getColor(R.color.gray_999999));
    }
  }

  private void initData() {
    //获取所有配置信息
    SovUtil.getStreamCatDataAll(window);
    //单独获取系统时间
    SovUtil.getStreamCatTime(window);

    refreshCheckBtnState();

    mBinding.switchBtnNetTime.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(TimeSettingActivity.this);
        SovUtil.setStreamCatSntp(window,
            String.format(AppConsts.FORMATTER_BSNTP, (sntp + 1) % 2));
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
              analyzeAll(JSONObject.parseObject(data));
              break;
            }
            //2.设备时间相关
            case JVNetConst.SRC_TIME: {
              switch (nPacketType) {
                //单独获取系统时间
                case JVNetConst.SRC_EX_GETSYSTIME: {
                  mBinding.tvTimeSetting.setText(data);
                  break;
                }

                //设置设备网络校时开关回调
                case JVNetConst.SRC_EX_SETTIME_SNTP: {
                  if (result == 0) {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(this,"设置成功", Toast.LENGTH_SHORT).show();
                    sntp = (sntp + result) % 2;
                    /*sntpBtn.setText(sntp == 0 ? "关" : "开");*/
                    refreshCheckBtnState();

                    mBinding.tvTimeSetting.setText(CommonUtil.getCurrentTime());

                    /*SovUtil.getStreamCatTime(window);*/

                    /*SovUtil.getStreamCatTime(window);*/
                  }
                  break;
                }

                //修改设备时间回调
                case JVNetConst.SRC_EX_SETSYSTIME: {
                  if (result == 0) {
                    Toast.makeText(this,"设置失败", Toast.LENGTH_SHORT).show();
                    break;
                  } else {
                    Toast.makeText(this,"设置成功", Toast.LENGTH_SHORT).show();
                    mBinding.tvTimeSetting.setText(mSettingTime);
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
          makeText(this,"Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }

  }

  public void onTimeSetClick(View view) {
    initDatePicker();

  }

  /**
   * 从配置信息全里面解析设置相关内容的状态
   *
   * @param data
   */
  private void analyzeAll(JSONObject data) {
    //2.时间时区
    sntp = data.getInteger(AppConsts.TAG_BSNTP);
    refreshCheckBtnState();
  }
}
