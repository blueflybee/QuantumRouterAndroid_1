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

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.passowrdinputlib.PasswordInputView;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMatchPinBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerKeyComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.KeyModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.MD5Util;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.adapter.NetAvaivablePopupWindow;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;

import javax.inject.Inject;

public class MatchPinActivity extends BaseActivity implements MatchPinView {

  @Inject
  MatchPinPresenter mMatchPinPresenter;

  private ActivityMatchPinBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_pin);

    initializeInjector();

    initPresenter();

    initView();

    firstGetKey();
  }

  private void firstGetKey() {
    mMatchPinPresenter.firstGetKey();
  }

  private void initView() {
    initTitleBar("密钥同步");
    mBinding.btnLoadKey.setOnClickListener(v -> mMatchPinPresenter.decryptKey(pin()));
    mBinding.viewPassword.setOnFinishListener(new PasswordInputView.OnFinishListener() {
      @Override
      public void setOnPasswordFinished() {

        String originText = mBinding.viewPassword.getOriginText();
        int passwordLength = mBinding.viewPassword.getMaxPasswordLength();
        if (originText.length() == passwordLength) {
          mMatchPinPresenter.decryptKey(MD5Util.encryption(originText));
          //ToastUtils.showShort(originText);
        }
      }
    });


  }

  private String pin() {
    return getText(mBinding.etTestPin);
  }

  private void initPresenter() {
    mMatchPinPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerKeyComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .keyModule(new KeyModule())
        .build()
        .inject(this);
  }

  public void skip(View view) {
    DialogUtil.showSkipKeyInjectDialog(getContext(),
        v -> mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class));
  }

  @Override
  public void showPinEmp() {
    Toast.makeText(this, "请输入网关上显示的匹配码", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showFirstGetKeySuccess() {
    mBinding.tvState.setVisibility(View.VISIBLE);
    mBinding.tvState.setTextColor(Color.BLACK);
    mBinding.tvState.setText("请输入网关上显示的匹配码");
  }

  @Override
  public void showPinVerifyFail() {

    DialogUtil.showInputPinErrDialog(getContext(), new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });
  }

  @Override
  public void goLoadKey(LZKeyInfo<LZKeyInfo.KeyBean> data) {
    Intent intent = getIntent();
    intent.putExtra(Navigator.EXTRA_LZKEYINFO, data);
    mNavigator.navigateTo(getContext(), InjectKeyActivity.class, intent);
  }


  @Override
  public void showPinVerifySuccess() {
    mBinding.tvState.setVisibility(View.VISIBLE);
    mBinding.tvState.setTextColor(Color.GREEN);
    mBinding.tvState.setText("匹配成功！");
  }


}
