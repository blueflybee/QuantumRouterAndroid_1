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

package com.fernandocejas.android10.sample.presentation.view.device.router.loadkey;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityInjectKeyBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerKeyComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.KeyModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;

import javax.inject.Inject;

public class InjectKeyActivity extends BaseActivity implements InjectKeyView {

  @Inject
  InjectKeyPresenter mInjectKeyPresenter;

  private ActivityInjectKeyBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inject_key);

    initializeInjector();

    initPresenter();

    initView();

    injectKey();

  }

  private void initView() {
    initTitleBar("注入密钥");
  }

  public void reloadKey(View view) {
    mBinding.btnReloadKey.setVisibility(View.GONE);
    mBinding.btnLoadFinish.setVisibility(View.GONE);
    injectKey();
  }

  private void injectKey() {
    mInjectKeyPresenter.injectKey(getIntent());
  }

  public void injectFinish(View view) {
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
  }

  private void initPresenter() {
    mInjectKeyPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerKeyComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .keyModule(new KeyModule())
        .build()
        .inject(this);
  }

  @Override
  public void onKeyInjectStart() {
    mBinding.tvProgress.setText("0");
    mBinding.tvProgress.setVisibility(View.VISIBLE);
    mBinding.tvTips.setVisibility(View.INVISIBLE);

    mBinding.progressLoadKey.setBackgroundColor(getResources().getColor(R.color.gray_fafafa));
    mBinding.rlKeyProgress.setBackgroundResource(R.drawable.key_contour);
    mBinding.ivKey.setBackgroundResource(R.drawable.key_key);
  }

  @Override
  public void onKeyInjectSuccess(String routerId) {
    mBinding.btnReloadKey.setVisibility(View.GONE);
    mBinding.btnLoadFinish.setVisibility(View.VISIBLE);
    mBinding.tvTips.setVisibility(View.VISIBLE);
    mBinding.tvTips.setText("密钥注入完成！");
    mBinding.tvTips.setTextColor(getResources().getColor(R.color.blue_2196f3));
  }

  @Override
  public void onKeyInjectFail() {
    mBinding.tvTips.setVisibility(View.VISIBLE);
    mBinding.tvTips.setText("密钥获取失败");
    mBinding.tvTips.setTextColor(getResources().getColor(R.color.red_fc7b8a));
    mBinding.tvProgress.setVisibility(View.INVISIBLE);
    mBinding.btnReloadKey.setVisibility(View.VISIBLE);
    mBinding.btnLoadFinish.setVisibility(View.GONE);
    mBinding.rlKeyProgress.setBackgroundResource(R.drawable.key_failcontour);
    mBinding.ivKey.setBackgroundResource(R.drawable.key_failkey);
    mBinding.progressLoadKey.setProgress(0);
    mBinding.progressLoadKey.setBackgroundColor(getResources().getColor(R.color.red_ff68a4));
  }

  @Override
  public void onKeyInjectProgress(int progress) {
    mBinding.progressLoadKey.setProgress(progress);
    mBinding.tvProgress.setText(progress + "%");
  }

  @Override
  public void onKeyInjectCancel(String routerId) {

  }
}
