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

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAboutCameraBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.AboutCatEyePopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UploadDevicePwdPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UploadDevicePwdView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CloudUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import javax.inject.Inject;

public class AboutCameraActivity extends BaseCatActivity implements UploadDevicePwdView {
  private ActivityAboutCameraBinding mBinding;
  private AboutCatEyePopupWindow mPopWindow;
  //intent
  private int window = 0;
  private static final String TAG = "AboutCatEyeActivity";
  private int effectFlag = 0;
  private boolean isDownPosition = false;//标记是否是倒着放
  @Inject
  UploadDevicePwdPresenter uploadPwdPresenter;

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_camera);

    initView();

    initData();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private void initPresenter() {
    uploadPwdPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initData() {
    //获取所有配置信息
    SovUtil.getStreamCatDataAll(window);

    initializeInjector();

    initPresenter();
  }

  private void initView() {
    initTitleBar("关于摄像机");
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
      //设置协议回调
      case JVNetConst.CALL_CATEYE_SENDDATA: {
        try {
          JSONObject root = JSON.parseObject(obj.toString());
          int nCmd = root.getInteger(AppConsts.TAG_NCMD);
          int nPacketType = root.getInteger(AppConsts.TAG_NPACKETTYPE);
          int result = root.getInteger(AppConsts.TAG_RESULT);
          String reason = root.getString(AppConsts.TAG_REASON);
          String data = root.getString(AppConsts.TAG_DATA);
          JSONObject dataJson = JSON.parseObject(data);
          if (result == 0) {
            Toast.makeText(this, "设置协议回调，错误码：" + reason, Toast.LENGTH_LONG).show();
            return;
          }
          Log.e(TAG, "nPacketType=" + nPacketType + "；nCmd=" + nCmd + "；obj=" + obj.toString());
          switch (nCmd) {
            //配置信息全的回调
            case JVNetConst.SRC_PARAM_ALL: {
              refreshSetUi(dataJson);
              break;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    }
  }

  /**
   * 根据配置信息刷新UI
   *
   * @param streamJson
   */

  public void refreshSetUi(JSONObject streamJson) {

    effectFlag = streamJson.getInteger(AppConsts.TAG_NEFFECT_FLAG);

    if (AppConsts.SCREEN_OVERTURN == CloudUtil.getVideoDirection(effectFlag)) {
      isDownPosition = true;
      mBinding.tvCameraPosition.setText("摄像机倒着放");
      //倒装
    } else {
      //正装
      isDownPosition = false;
      mBinding.tvCameraPosition.setText("摄像机正着放");
    }
  }


  public void onRestartCameraClick(View view) {
    //弹窗
    mPopWindow = new AboutCatEyePopupWindow(this, 0, false);
    mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int index) {
        restartCamera();
      }
    });
  }

  private void restartCamera() {
    boolean restart = SovUtil.streamRestart(window);
    DialogUtil.showRestartCatEyeDialog(getContext(), "正在重启摄像机", "摄像机正在重启中，请稍候",
        R.drawable.logo_video_restart, 4000, new DialogUtil.OnCatEyeRestartListener() {
          @Override
          public void onCatEyeRestart() {
            ToastUtils.showShort(restart ? "摄像机重启成功" : "摄像机重启失败，请重试");
          }
        });
  }

  public void onResetCameraClick(View view) {
    //弹窗
    mPopWindow = new AboutCatEyePopupWindow(this, 1, false);
    mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int index) {

        Boolean isResetSuccessed = false;

        isResetSuccessed = SovUtil.streamReset(window);

        if (isResetSuccessed) {
          Toast.makeText(AboutCameraActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
          //设备密码上报服务器
          GlobleConstant.setgCameraPwd("");
          uploadPwdPresenter.uploadDevicePwd(GlobleConstant.getgCameraId(),"");
        } else {
          Toast.makeText(AboutCameraActivity.this, "重置失败", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  public void onCameraLocationClick(View view) {
    //弹窗
    mPopWindow = new AboutCatEyePopupWindow(this, 3, isDownPosition);
    mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int index) {
        //1 0 正 倒
        if (index == 0) {
          effectFlag = 0;// demo yaoqiu
        } else if (index == 1) {
          effectFlag = 6;
        }

        SovUtil.streamRotateVideo(window,
            CloudUtil.getVideoDirection(effectFlag), effectFlag);

        Toast.makeText(AboutCameraActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

        //获取所有配置信息
        SovUtil.getStreamCatDataAll(window);
      }
    });
  }

  @Override
  public void uploadDevicePwdSuccessed() {
    System.out.println("设备密码 摄像头 重置成功 上传服务器成功");
  }
}
