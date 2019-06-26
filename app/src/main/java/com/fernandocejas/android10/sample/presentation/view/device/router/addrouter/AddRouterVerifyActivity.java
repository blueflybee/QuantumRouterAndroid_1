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

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddRouterVerifyBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.router.loadkey.MatchPinActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.mapp.model.req.CommitAddRouterInfoRequest;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

import javax.inject.Inject;

public class AddRouterVerifyActivity extends BaseActivity implements AddRouterVerifyView {

  public static final String DEFAULT_ROUTER_NAME = "三点安全网关";
  @Inject
  AddRouterVerifyPresenter mAddRouterVerifyPresenter;

  private ActivityAddRouterVerifyBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_router_verify);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("添加新网关");
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(4, 63));
    watcher.addEt(mBinding.etVerifyEnterPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnVerifyDone.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnVerifyDone.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnVerifyDone.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etVerifyEnterPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 63);

    int configured = getRouterInfo().getConfigured();
    mBinding.rlAdminPwd.setVisibility(configured == 1 ? View.VISIBLE : View.GONE);
    mBinding.rlNewRouter.setVisibility(configured == 1 ? View.GONE : View.VISIBLE);
    mBinding.btnVerifyDone.setClickable(configured == 0);
    if (configured == 0) {
      mBinding.btnVerifyDone.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
    }

  }

  private void initPresenter() {
    mAddRouterVerifyPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etVerifyEnterPwd);
  }

  public void bind(View view) {
    mAddRouterVerifyPresenter.addRouterVerify(getRouterInfo().getSerialnum(), pwd(), getRouterInfo().getConfigured());
  }

  private String pwd() {
    return getText(mBinding.etVerifyEnterPwd);
  }

  @Override
  public void showAdminPwdEmp() {
    ToastUtils.showShort(R.string.toast_add_router_pwd_input);
  }

  @Override
  public void onAddSuccess(AddRouterVerifyResponse response) {
    QtecEncryptInfo<CommitAddRouterInfoRequest> request = new QtecEncryptInfo<>();
    CommitAddRouterInfoRequest data = new CommitAddRouterInfoRequest();
    data.setDeviceName(DEFAULT_ROUTER_NAME);
    data.setDeviceSerialNo(getRouterInfo().getSerialnum());
    data.setDeviceModel(getRouterInfo().getDevmodel());
    data.setDeviceVersion(getRouterInfo().getVersion());
    data.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    data.setDeviceType("0");
    request.setData(data);
    mAddRouterVerifyPresenter.commitRouterInfo(request);
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  @Override
  public void onCommitSuccess(CommitAddRouterInfoResponse response) {
    if (KeystoreRepertory.getInstance().has(GlobleConstant.getgKeyRepoId())) {
      DialogUtil.showSuccessDialog(getContext(), "绑定成功", () ->
          mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class));
    } else {
      DialogUtil.showSuccessDialog(getContext(), "绑定成功", () ->
          mNavigator.navigateTo(getContext(), MatchPinActivity.class, getIntent()));
    }

  }

  private SearchRouterResponse getRouterInfo() {
    SearchRouterResponse routerResponse = (SearchRouterResponse)
        getIntent().getSerializableExtra(Navigator.EXTRA_ADD_ROUTER_INFO);
    return routerResponse == null ? new SearchRouterResponse() : routerResponse;
  }

}
