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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityStorageManageBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.MyProgressDialog;
import com.fernandocejas.android10.sample.presentation.view.device.camera.util.CameraPopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import java.text.DecimalFormat;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class StorageManageActivity extends BaseCatActivity {
  private ActivityStorageManageBinding mBinding;
  private int REQUEST_CODE_FOR_STORAGE = 0x55;
  //intent
  private int window = 0;
  private static final String TAG = "TimeZoneActivity";
  private int timeZone;//时区-12~12 表示东12-西12
  private CameraPopupWindow mPopWin;
  private int recordModeSelectIndex = 0;//录像模式index

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_storage_manage);

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
    // 获取设备的存储状态
    SovUtil.getStreamSDCard(window);
  }

  private void initView() {
    initTitleBar("存储管理");
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (data == null) {
      return;
    }

    if (requestCode == REQUEST_CODE_FOR_STORAGE) {
      recordModeSelectIndex = data.getIntExtra("VideoModeIndex",0);
      mBinding.tvVideoMode.setText(convertToVideoMode(recordModeSelectIndex));
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
              break;
            }

            //4.存储管理回调
            case JVNetConst.SRC_STORAGE: {
              switch (nPacketType) {
                case JVNetConst.EX_STORAGE_REFRESH://存储卡相关信息
                  refreshSDCardUi(JSON.parseObject(data));
                  break;
                case JVNetConst.SRC_EX_STORAGE_CMD_FORMAT://格式化SD卡回调
                  Boolean isFormatSuccessed = false;

                  if (result == 1) {
                    // 获取设备的存储状态
                    isFormatSuccessed = SovUtil.getStreamSDCard(window);
                  } else {
                    isFormatSuccessed = false;
                  }

                  if (isFormatSuccessed) {
                    DialogUtil.showConectLockDialog(getContext(), "格式化成功", () -> {
                      finish();
                    });
                  } else {
                    Toast.makeText(this, "格式化失败", Toast.LENGTH_SHORT).show();
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

  public void onFormatClick(View view) {
    
    if(recordModeSelectIndex == 1){
      //全天录像模式下不允许格式化
      Toast.makeText(this, "全天录像模式下不支持格式化", Toast.LENGTH_SHORT).show();
      return;
    }
    
    mPopWin = new CameraPopupWindow(this, "1",0);
    mPopWin.setFocusable(true);
    mPopWin.showAtLocation(mPopWin.getOuterLayout(), Gravity.CENTER, 0, 0);

    mPopWin.setOnPositiveClickListener(new CameraPopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int progress,View dialogPos) {
        DialogUtil.showProgress(StorageManageActivity.this);

        SovUtil.setStreamSDFormat(window);
      }
    });
  }

  public void onVideoModeClick(View view) {
    Intent intent = new Intent(this, VideoModeChooseActivity.class);
    intent.putExtra("VideoMode", recordModeSelectIndex);
    startActivityForResult(intent,REQUEST_CODE_FOR_STORAGE);
  }

  /**
   * 设置录像信息及存储卡信息
   */
  private void refreshSDCardUi(JSONObject streamJSON) {
    Log.e(TAG, "refreshSDCardUi: " + streamJSON);
    if (streamJSON == null) {
      return;
    }
    int nStatus = streamJSON.getInteger(AppConsts.TAG_NSTATUS);
    float total, used;
    switch (nStatus) {
      case 0://无卡
        Toast.makeText(this, "设备没插卡", Toast.LENGTH_SHORT).show();
        return;
      case 1://未格式化
      case 2://已满
      case 4://空闲
      case 3://使用中
        total = streamJSON.getInteger(AppConsts.TAG_NTOTALSIZE);
        used = streamJSON.getInteger(AppConsts.TAG_NUSEDSIZE);

        total = (float) (total / 1024.0);
        used = (float) (used / 1024.0);
        //10.SD使用情况
        DecimalFormat fnum2 = new DecimalFormat("##0.00");
        DecimalFormat fnum4 = new DecimalFormat("##0.00");

        mBinding.tvDeviceList.setText(fnum2.format(total)
            + "GB存储卡");
        mBinding.tvDeviceState.setText(getResources().getStringArray(R.array.array_sdcard_state)[nStatus]);
        mBinding.tvUseCondition.setText(fnum2.format(used)
            + "GB/"
            + fnum2.format(total) + "GB");
        break;
    }

    // 录像模式
    recordModeSelectIndex = streamJSON.containsKey(AppConsts.N_RECORD_TYPE) ?
        streamJSON.getInteger(AppConsts.N_RECORD_TYPE) : -1;

    mBinding.tvVideoMode.setText(convertToVideoMode(recordModeSelectIndex));

  }

  public String convertToVideoMode(int index) {
    String str = "";
    switch (index) {
      case 0:
        str = "停止录像";
        break;
      case 1:
        str = "全天录像";
        break;
      case 2:
        str = "报警录像";
        break;
      case 3:
        str = "缩时录像";
        break;
    }
    return str;
  }

}
