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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAboutCatEyeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import java.text.DecimalFormat;

import javax.inject.Inject;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class AboutCatEyeActivity extends BaseCatActivity implements UploadDevicePwdView {
  private ActivityAboutCatEyeBinding mBinding;
  private AboutCatEyePopupWindow mPopWindow;
  //intent
  private int window = 0;
  private static final String TAG = "AboutCatEyeActivity";
  @Inject
  UploadDevicePwdPresenter uploadPwdPresenter;

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_cat_eye);

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
    initializeInjector();

    initPresenter();
  }

  private void initView() {
    initTitleBar("关于猫眼");
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

  /**
   * 点击事件处理
   *
   * @param
   * @return
   */
  public void onRestartCatEyeClick(View view) {
    //弹窗
    mPopWindow = new AboutCatEyePopupWindow(this, 0, false);
    mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int index) {
        restartCatEye();
      }
    });
  }

  private void restartCatEye() {
    boolean restart = SovUtil.streamRestart(window);
    DialogUtil.showRestartCatEyeDialog(getContext(), "正在重启猫眼", "猫眼正在重启中，请稍候",
        R.drawable.logo_cat_restart, 4000, new DialogUtil.OnCatEyeRestartListener() {
          @Override
          public void onCatEyeRestart() {
            ToastUtils.showShort(restart ? "猫眼重启成功" : "猫眼重启失败，请重试");
          }
        });
  }

  public void onResetCatEyeClick(View view) {
    //弹窗
    mPopWindow = new AboutCatEyePopupWindow(this, 1, false);
    mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
    mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
      @Override
      public void onPositiveClick(int index) {

        Boolean isResetSuccessed = false;

        isResetSuccessed = SovUtil.streamReset(window);

        if (isResetSuccessed) {
          Toast.makeText(AboutCatEyeActivity.this, "重置成功", Toast.LENGTH_SHORT).show();
          //设备密码上报服务器
          GlobleConstant.setgCatPwd("");
          uploadPwdPresenter.uploadDevicePwd(GlobleConstant.getgCatEyeId(),"");
        } else {
          Toast.makeText(AboutCatEyeActivity.this, "重置失败", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }


  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {

  }

  @Override
  public void uploadDevicePwdSuccessed() {
    System.out.println("设备密码 猫眼 重置成功 上传服务器成功");
  }
}
