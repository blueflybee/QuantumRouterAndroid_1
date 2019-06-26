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
import android.widget.AdapterView;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddRouterBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.AddRouterAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.AgreeLicenseActivity;
import com.qtec.router.model.rsp.SearchRouterResponse;

import javax.inject.Inject;

public class AddRouterActivity extends BaseActivity implements AddRouterView {

  @Inject
  AddRouterPresenter mAddRouterPresenter;

  private ActivityAddRouterBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_router);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("添加新网关");

    new Handler().postDelayed(() -> mAddRouterPresenter.searchRouter(), 2000);

    AddRouterAdapter adapter = new AddRouterAdapter(getContext());
    mBinding.lvRouters.setAdapter(adapter);
    mBinding.lvRouters.setOnItemClickListener(
        (parent, view, position, id) -> mNavigator.navigateTo(getContext(), SearchRouterActivity.class));
  }

  @Override
  public void showSearchSuccess(SearchRouterResponse response) {
    mBinding.setRsp(response);
    mBinding.llAutoSearching.setVisibility(View.GONE);
    mBinding.rlAutoSearchAdd.setVisibility(View.VISIBLE);
  }

  @Override
  public void showSearchFailed(Throwable e) {
    mBinding.gifAddSearch.setVisibility(View.GONE);
    mBinding.tvSearchFail.setVisibility(View.VISIBLE);
  }

  public void addDevAuto(View view) {
    SearchRouterResponse rsp = mBinding.getRsp();

    String deviceId = rsp.getSerialnum();
    String keyRepoId = deviceId + "_" + PrefConstant.getUserUniqueKey("0");
    GlobleConstant.setgDeviceId(deviceId);
    GlobleConstant.setgKeyRepoId(keyRepoId);
    GlobleConstant.setgDeviceType(AppConstant.DEVICE_TYPE_ROUTER);
//    int configured = rsp.getConfigured();
//    if (configured == 1) {
//      Intent intent = new Intent();
//      intent.putExtra(Navigator.EXTRA_ADD_ROUTER_INFO, rsp);
//      mNavigator.navigateTo(getContext(), AddRouterVerifyActivity.class, intent);
//    } else {
//      mNavigator.navigateTo(getContext(), AgreeLicenseActivity.class);
//    }
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_ADD_ROUTER_INFO, rsp);
    mNavigator.navigateTo(getContext(), AddRouterVerifyActivity.class, intent);
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
}
