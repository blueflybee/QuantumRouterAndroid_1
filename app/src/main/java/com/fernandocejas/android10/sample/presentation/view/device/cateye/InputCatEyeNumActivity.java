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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityInputCatEyeNumBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.IntentUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.PairWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.qtec.mapp.model.rsp.QueryLockedCatResponse;

import javax.inject.Inject;

public class InputCatEyeNumActivity extends BaseActivity implements QueryLockedCatView {

  private ActivityInputCatEyeNumBinding mBinding;
  private AboutCatEyePopupWindow mPopWindow;

  @Inject
  QueryLockedCatPresenter mQueryLockedCatPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_input_cat_eye_num);

    initializeInjector();

    initPresenter();

    initView();

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
    mQueryLockedCatPresenter.setView(this);
  }

  private void initView() {
    initTitleBar("输入设备序列号");

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
          GlobleConstant.setgCatEyeId(mBinding.etInputCatNo.getText().toString().trim());

          //查询是否绑定
          mQueryLockedCatPresenter.queryLockedCat(GlobleConstant.getgCatEyeId());
        } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
          GlobleConstant.setgCameraId(mBinding.etInputCatNo.getText().toString().trim());

          //查询是否绑定
          mQueryLockedCatPresenter.queryLockedCat(GlobleConstant.getgCameraId());
        } else if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(GlobleConstant.getgDeviceType())) {
          LiteGateway liteGateway = (LiteGateway) IntentUtil.getSerialExtra(getActivity(), Navigator.EXTRA_BIND_LITE_GATEWAY);
          liteGateway.setId(getText(mBinding.etInputCatNo));
          mNavigator.navigateTo(getContext(), PairWifiActivity.class, getIntent());
        }
      }
    });

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setByteRange(new InputWatcher.InputByteRange(1, 32));

    watcher.addEt(mBinding.etInputCatNo, condition1);

    watcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean empty) {
        changeTitleRightBtnStyle(empty);
      }
    });
  }

  @NonNull
  private InputCatEyeNumActivity getActivity() {
    return InputCatEyeNumActivity.this;
  }

  @Override
  public void queryLockedCat(QueryLockedCatResponse response) {
    //:{"bindStatus":"0,未绑定；1,已绑定"}
    if ("0".equals(response.getBindStatus())) {
      GlobleConstant.isCatBinded = false;
      startActivity(new Intent(this, WaitConfigActivity.class));
    } else {
      GlobleConstant.isCatBinded = true;
      mPopWindow = new AboutCatEyePopupWindow(this, 2, false);
      mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
      mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
        @Override
        public void onPositiveClick(int index) {
          startActivity(new Intent(InputCatEyeNumActivity.this, WaitConfigActivity.class));
        }
      });
    }
  }
}
