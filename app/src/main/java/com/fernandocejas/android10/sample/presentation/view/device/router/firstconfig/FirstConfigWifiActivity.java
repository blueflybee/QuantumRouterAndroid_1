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

package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFirstConfigWifiBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.router.model.rsp.FirstConfigResponse;

import javax.inject.Inject;

public class FirstConfigWifiActivity extends BaseActivity implements FirstConfigView {

  @Inject
  FirstConfigPresenter mFirstConfigPresenter;

  private ActivityFirstConfigWifiBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_config_wifi);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("WiFi设置");
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setRange(new InputWatcher.InputRange(1, 32));
    InputWatcher.WatchCondition condition2 = new InputWatcher.WatchCondition();
    condition2.setRange(new InputWatcher.InputRange(8, 63));
    InputWatcher.WatchCondition condition3 = new InputWatcher.WatchCondition();
    condition3.setRange(new InputWatcher.InputRange(4, 63));


    watcher.addEt(mBinding.etWifiName, condition1);
    watcher.addEt(mBinding.etWifiPwd, condition2);
    watcher.addEt(mBinding.etAdminPwd, condition3);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnFinish.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnFinish.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnFinish.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etWifiPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 63);
    InputUtil.inhibit(mBinding.etAdminPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 63);
  }

  public void showHideWifiPwd(View view) {
    InputUtil.showHidePwd(mBinding.etWifiPwd);
  }

  public void showHideAdminPwd(View view) {
    InputUtil.showHidePwd(mBinding.etAdminPwd);
  }

  private void initPresenter() {
    mFirstConfigPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }


  public void confirm(View view) {
    mFirstConfigPresenter.firstConfig(getText(mBinding.etWifiName), getText(mBinding.etWifiPwd), getText(mBinding.etAdminPwd));
  }

  @Override
  public void showFirstConfigSuccess(FirstConfigResponse response) {
    DialogUtil.showRestartWifiDialog(getContext(),
        getText(mBinding.etWifiName),
        getText(mBinding.etWifiPwd),
        v -> mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class));
  }
}
