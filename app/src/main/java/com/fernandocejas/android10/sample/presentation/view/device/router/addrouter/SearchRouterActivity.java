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

package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySearchRouterBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.AgreeLicenseActivity;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.router.model.req.SearchRouterRequest;
import com.qtec.router.model.rsp.SearchRouterResponse;

import javax.inject.Inject;

public class SearchRouterActivity extends BaseActivity implements AddRouterView {

  @Inject
  AddRouterPresenter mAddRouterPresenter;

  private ActivitySearchRouterBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_router);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("添加新网关");

    new Handler().postDelayed(this::searchRouter, 2000);
  }


  private void initPresenter() {
    mAddRouterPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void searchRouter() {
    mAddRouterPresenter.searchRouter();
  }

  public void addDevice(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_ADD_ROUTER_INFO, mBinding.getRsp());
    mNavigator.navigateTo(getContext(), AddRouterVerifyActivity.class, intent);
  }
  
  public void firstConfig(View view) {
    mNavigator.navigateTo(getContext(), AgreeLicenseActivity.class);
  }

  @Override
  public void showSearchSuccess(SearchRouterResponse response) {
    String deviceId = response.getSerialnum();
    String keyRepoId = deviceId + "_" + PrefConstant.getUserUniqueKey("0");
    GlobleConstant.setgDeviceId(deviceId);
    GlobleConstant.setgKeyRepoId(keyRepoId);

    mBinding.setRsp(response);
    mBinding.llSearchDeviceSearching.setVisibility(View.GONE);
    mBinding.llSearchDeviceSuccess.setVisibility(View.VISIBLE);
    int configured = response.getConfigured();
    mBinding.btnSearchDeviceAdd.setVisibility(configured == 1 ? View.VISIBLE : View.GONE);
    mBinding.btnFirstConfig.setVisibility(configured == 1 ? View.GONE : View.VISIBLE);
    mBinding.tvTips.setText(configured == 1 ? "您已经连接网关" : "您已经连接一台新网关");
//    mBinding.tvTips.setText("您已经连接网关");

  }

  @Override
  public void showSearchFailed(Throwable e) {
    mBinding.llSearchDeviceSearching.setVisibility(View.GONE);
    mBinding.llSearchDeviceSuccess.setVisibility(View.GONE);
    mBinding.llSearchDeviceFailed.setVisibility(View.VISIBLE);
  }
}
