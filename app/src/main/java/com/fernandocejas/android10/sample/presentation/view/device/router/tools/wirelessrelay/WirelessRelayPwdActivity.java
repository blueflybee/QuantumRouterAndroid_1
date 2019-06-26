package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWirelessRelayPwdBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.req.PostConnectWirelessRequest;
import com.qtec.router.model.rsp.GetWirelessListResponse;
import com.qtec.router.model.rsp.PostConnectWirelessResponse;

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
public class WirelessRelayPwdActivity extends BaseActivity implements PostConnectWirelessView {
  private ActivityWirelessRelayPwdBinding mBinding;

  @Inject
  PostConnectWirelessPresenter mConnectWirelessPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wireless_relay_pwd);

    initializeInjector();

    initPresenter();

    initView();

    watchEditTextNoClear(mBinding.etPwd);

  }

  private void initView() {
    if(getDetails() != null){
      initTitleBar(getDetails().getSsid());
    }

    InputWatcher watcher = new InputWatcher();

    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputRange(8, 63));

    watcher.addEt(mBinding.etPwd, pwdCondition);

    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  public void confirm(View view) {
    PostConnectWirelessRequest bean = new PostConnectWirelessRequest();
    bean.setPassword(getText(mBinding.etPwd));

    if(getDetails() != null){
      bean.setSsid(getDetails().getSsid());
      bean.setEncrypt(getDetails().getEncrypt());
      bean.setMac(getDetails().getMac());
      bean.setMode(getDetails().getMode());
    }

    connectWirelessRequest(bean);
  }

  private void connectWirelessRequest(PostConnectWirelessRequest bean) {
    mConnectWirelessPresenter.connectWireless(GlobleConstant.getgDeviceId(), bean);
  }


  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  private void initPresenter() {
    mConnectWirelessPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void postConnectWireless(PostConnectWirelessResponse response) {
    Intent intent = new Intent();
    intent.putExtra("EXTRA",2);
    this.setResult(55,intent);
    finish();
  }


//  @Override
//  public void checkAdminPwdSuccess(CheckAdminPwdResponse response) {
//    mNavigator.navigateTo(getContext(), AdminNewPwdActivity.class);
//  }

  private GetWirelessListResponse.WirelessBean getDetails(){
    return (GetWirelessListResponse.WirelessBean)getIntent().getSerializableExtra("Detail");
  }
}
