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
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLicenseAgreeBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterVerifyActivity;

public class AgreeLicenseActivity extends BaseActivity  {

//  @Inject
//  AddRouterPresenter mAddRouterPresenter;

  private ActivityLicenseAgreeBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_license_agree);

//    initializeInjector();
//
//    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("添加新网关");
  }


//  private void initPresenter() {
//    mAddRouterPresenter.setView(this);
//  }
//
//  private void initializeInjector() {
//    DaggerRouterComponent.builder()
//        .applicationComponent(getApplicationComponent())
//        .activityModule(getActivityModule())
//        .routerModule(new RouterModule())
//        .build()
//        .inject(this);
//  }


  public void agree(View view) {
//    Intent intent = new Intent();
//    intent.putExtra(Navigator.EXTRA_ADD_ROUTER_INFO, mBinding.getRsp());
    mNavigator.navigateTo(getContext(), FirstConfigNetModeActivity.class);
  }

}
