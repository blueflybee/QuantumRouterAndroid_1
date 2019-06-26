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

package com.fernandocejas.android10.sample.presentation.view.device.intelDev.addIntelDev;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddIntelDevVerifyBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddIntelDevVerifyRequest;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse.IntelDev;

import javax.inject.Inject;

public class AddIntelDevVerifyActivity extends BaseActivity implements AddIntelDevVerifyView {

  @Inject
  AddIntelDevVerifyPresenter addIntelDevVerifyPresenter;

  private ActivityAddIntelDevVerifyBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_intel_dev_verify);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("添加门锁");
    InputWatcher watcher = new InputWatcher();
    watcher.addEt(mBinding.etVerifyEnterPwd);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnVerifyDone.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnVerifyDone.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnVerifyDone.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etVerifyEnterPwd);
  }


  private void initPresenter() {
    addIntelDevVerifyPresenter.setView(this);
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  public void bind(View view) {
    bind();
  }

  private void bind() {

    //build router request
    QtecEncryptInfo<AddIntelDevVerifyRequest> routerRequest = new QtecEncryptInfo<>();
    AddIntelDevVerifyRequest data = new AddIntelDevVerifyRequest();
    data.setPassword(pwd());
    data.setDevid(getDevInfo().getDevid());
    data.setUsername(PrefConstant.getUserPhone());
    data.setMac(DeviceUtils.getMacAddress());
    routerRequest.setRequestUrl(RouterUrlPath.PATH_ADD_INTEL_DEV_VERIFY);
    routerRequest.setMethod(RouterUrlPath.METHOD_POST);
    routerRequest.setData(data);

    //build transmit
    TransmitRequest<QtecEncryptInfo<AddIntelDevVerifyRequest>> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(GlobleConstant.getgDeviceId());
    transmit.setEncryptInfo(routerRequest);

    //build cloud request
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerRequest);

    addIntelDevVerifyPresenter.addIntelDevVerify(multiEncryptInfo, pwd());
  }


  private String pwd() {
    return getText(mBinding.etVerifyEnterPwd);
  }


  @Override
  public void showAdminPwdEmp() {
    ToastUtils.showShort(R.string.toast_add_router_pwd_input);
  }

  @Override
  public void onAddSuccess(AddIntelDevVerifyResponse response) {
    DialogUtil.showSuccessDialog(getContext(), "添加成功", () ->
        mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class));
  }

  private IntelDev getDevInfo() {
    IntelDev dev = (IntelDev) getIntent().getSerializableExtra(Navigator.EXTRA_ADD_INTEL_DEV_INFO);
    return dev == null ? new IntelDev() : dev;
  }

}
