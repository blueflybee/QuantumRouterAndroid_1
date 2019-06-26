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
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCameraSettingBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.TimeZoneActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UbindCatPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UnbindCatView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;

import javax.inject.Inject;

public class CameraSettingActivity extends BaseCatActivity implements UnbindCatView {
  private ActivityCameraSettingBinding mBinding;
  private String group = "";
  private int ystNo = 0;
  SurfaceHolder surfaceHolder;//
  private boolean isConnected = false;//是否已连接
  private int window = 0;

  @Inject
  UbindCatPresenter mUbindCatPresenter;

  @Override
  protected void initSettings() {
    group = CommonUtil.getGroup(GlobleConstant.getgCameraId());// "C200474135"
    ystNo = CommonUtil.getYST(GlobleConstant.getgCameraId());
  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_camera_setting);

    initView();

    surfaceHolder = new SurfaceView(this).getHolder();



    if(isConnected){
      //
    }else {
      connectToVideo(); //首次进入不加载loading图标 影响体验
    }

    initializeInjector();

    initPresenter();

  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private void initView() {
    initTitleBar("设置");

    isConnected = getConnectFlag();

    System.out.println("GlobleConstant.getgCameraId() = " + GlobleConstant.getgCameraId());

    System.out.println("摄像头 isConnected = " + isConnected);
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mUbindCatPresenter.setView(this);
  }

  private void connectToVideo() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("设备密码 摄像头 设置 = "+GlobleConstant.getgCameraPwd());
        SovUtil.connect(group, ystNo, "admin", GlobleConstant.getgCameraPwd(), false, surfaceHolder.getSurface());
      }
    }).start();

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public void unbindCatSuccess(UnbindRouterResponse response) {
    Toast.makeText(this, "摄像机解绑成功", Toast.LENGTH_SHORT).show();
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
  }

  /**
  * 解绑
  *
  * @param
  * @return
  */
  public void onUnbindCatClick(View view) {
    mUbindCatPresenter.unbindCat(GlobleConstant.getgCameraId());
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    System.out.println("catEye: onHandler what=" + what);
    switch (what) {
      //连接结果
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        switch (arg2) {
          //连接成功
          case JVNetConst.CCONNECTTYPE_CONNOK: {
            System.out.println("catEye: 连接成功，正在接收数据");
            break;
          }
          //连接异常断开
          case JVNetConst.CCONNECTTYPE_DISCONNE: {
            isConnected = false;
            SovUtil.disconnect(window);
            System.out.println("catEye: 连接断开");
            break;
          }
          //其他都是连接失败
          default: {
            isConnected = false;
            System.out.println("catEye: 连接失败");
            break;
          }

        }

        break;
      }
      case JVNetConst.CALL_NORMAL_DATA: {
        isConnected = true;
        break;
      }

      default:
        break;
    }
  }

  private Boolean getConnectFlag(){
    return getIntent().getBooleanExtra("isConnected",false);
  }

  @Override
  protected void onResume() {
    SovUtil.enableStreamCatVideoData(window, true);
    System.out.println("catEye: enableStreamCatVideoData = true");
    super.onResume();
  }

  @Override
  protected void onPause() {
    if (isConnected) {
      SovUtil.enableStreamCatVideoData(window, false);
      System.out.println("catEye: enableStreamCatVideoData = false");
      JniUtil.pauseSurface(window);
    }
    super.onPause();
  }

  @Override
  public void onBackPressed() {
    /*SovUtil.disconnect(window);*/
    super.onBackPressed();
  }

  /**
  * 点击事件
  *
  * @param
  * @return
  */
  public void onAboutCameraClick(View view) {
    mNavigator.navigateTo(getContext(), AboutCameraActivity.class);
  }

  public void onStorageManagerClick(View view) {
    if(isConnected){
      mNavigator.navigateTo(getContext(), StorageManageActivity.class);
    }else {
      Toast.makeText(this, "正在与摄像机建立连接，请稍候重试", Toast.LENGTH_SHORT).show();
        /*SovUtil.connect(group, ystNo, "admin", "", null);*/
      DialogUtil.showProgress(this);
      connectToVideo();
    }
  }

  public void onWarningClick(View view) {
    if(isConnected){
      mNavigator.navigateTo(getContext(), WarningSettingActivity.class);
    }else {
      Toast.makeText(this, "正在与摄像机建立连接，请稍候重试", Toast.LENGTH_SHORT).show();
        /*SovUtil.connect(group, ystNo, "admin", "", null);*/
      DialogUtil.showProgress(this);
      connectToVideo();
    }
  }

  public void onTimeZoneOnClick(View view) {
    if(isConnected){
      mNavigator.navigateTo(getContext(), TimeZoneActivity.class);
    }else {
      Toast.makeText(this, "正在与摄像机建立连接，请稍候重试", Toast.LENGTH_SHORT).show();
        /*SovUtil.connect(group, ystNo, "admin", "", null);*/
      DialogUtil.showProgress(this);
      connectToVideo();
    }

  }
}
