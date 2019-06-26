package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityPppoeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.rsp.GetNetModeResponse;
import com.qtec.router.model.rsp.SetPPPOEResponse;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PPPOEActivity extends BaseActivity implements PPPOEView {
  private ActivityPppoeBinding mBinding;

  @Inject
  PPPOEPresenter mPPPOEPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pppoe);

    initializeInjector();
    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("宽带拨号（PPPOE）");

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(4, 20));
    watcher.addEt(mBinding.etAccount, condition);
    watcher.addEt(mBinding.etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnConfirm.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnConfirm.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnConfirm.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
    mBinding.setNetModeResponse(getNetModeData());

    InputUtil.inhibit(mBinding.etAccount, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);

    //限制表情输入
    watchEditTextNoClear(mBinding.etAccount);
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  public void confirm(View view) {
    mPPPOEPresenter.setPPPOE(account(), password(), 1);
  }

  @NonNull
  private String password() {
    return getText(mBinding.etPwd);
  }

  @NonNull
  private String account() {
    return getText(mBinding.etAccount);
  }

  public void showSp(View view) {
    mNavigator.navigateTo(getContext(), ServiceProviderActivity.class);
  }

  private GetNetModeResponse getNetModeData() {
    Intent intent = getIntent();
    if (intent == null) return new GetNetModeResponse();
    GetNetModeResponse extra = (GetNetModeResponse) intent.getSerializableExtra(Navigator.EXTRA_NET_MODE_DATA);
    if (extra == null) return new GetNetModeResponse();
    return extra;
  }

  @Override
  public void showSetPPPOESuccess(SetPPPOEResponse response) {
    ToastUtils.showShort("设置上网模式为PPPOE");
    finish();
  }

  private void initPresenter() {
    mPPPOEPresenter.setView(this);
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
