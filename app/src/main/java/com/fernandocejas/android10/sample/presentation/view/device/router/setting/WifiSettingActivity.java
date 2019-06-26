package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.domain.constant.ModelConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWifiSettingBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.req.ConfigWifiRequest;
import com.qtec.router.model.rsp.ConfigWifiResponse;
import com.qtec.router.model.rsp.GetWifiConfigResponse;

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
public class WifiSettingActivity extends BaseActivity implements WifiSettingView {
  private static final String FILL_PWD = "**********";
  private ActivityWifiSettingBinding mBinding;

  @Inject
  WifiSettingPresenter mWifiSettingPresenter;

  private ConfigWifiRequest mConfigWifiRequest = new ConfigWifiRequest();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wifi_setting);

    initializeInjector();

    initPresenter();

    initView();

    mWifiSettingPresenter.getWifiConfig();

  }

  private void initView() {
    initTitleBar("WiFi设置");


    mTitleBar.setRightAs("完成", v -> {
      mConfigWifiRequest.setLfssid(getText(mBinding.et2p4gWifiName));
      mConfigWifiRequest.setHfssid(getText(mBinding.et5gWifiName));
      String lfkey = getText(mBinding.et2p4gWifiPwd);
      mConfigWifiRequest.setLfkey(FILL_PWD.equals(lfkey) ? "" : lfkey);
      String hfkey = getText(mBinding.et5gWifiPwd);
      mConfigWifiRequest.setHfkey(FILL_PWD.equals(hfkey) ? "" : hfkey);
      mWifiSettingPresenter.configWifi(mConfigWifiRequest);
    });

    mBinding.switch2p4g.setOnCheckedChangeListener((buttonView, isChecked) -> {
      mConfigWifiRequest.setLfdisabled(isChecked ? ModelConstant.DISABLE : ModelConstant.ENABLE);
    });

    mBinding.switch2p4gHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
      mConfigWifiRequest.setLfhiden(isChecked ? ModelConstant.ENABLE : ModelConstant.DISABLE);
    });

    mBinding.switch5g.setOnCheckedChangeListener((buttonView, isChecked) -> {
      mConfigWifiRequest.setHfdisabled(isChecked ? ModelConstant.DISABLE : ModelConstant.ENABLE);
    });

    mBinding.switch5gHide.setOnCheckedChangeListener((buttonView, isChecked) -> {
      mConfigWifiRequest.setHfhiden(isChecked ? ModelConstant.ENABLE : ModelConstant.DISABLE);
    });

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setByteRange(new InputWatcher.InputByteRange(1, 32));
    InputWatcher.WatchCondition condition2 = new InputWatcher.WatchCondition();
    condition2.setRange(new InputWatcher.InputRange(8, 63));

    watcher.addEt(mBinding.et2p4gWifiName, condition1);
    watcher.addEt(mBinding.et5gWifiName, condition1);
    watcher.addEt(mBinding.et2p4gWifiPwd, condition2);
    watcher.addEt(mBinding.et5gWifiPwd, condition2);
    watcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean empty) {
        changeTitleRightBtnStyle(empty);
      }
    });

    InputUtil.inhibit(mBinding.et2p4gWifiPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 63);
    InputUtil.inhibit(mBinding.et5gWifiPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 63);
  }

 /* public void showHide2p4gPwd(View view) {
    InputUtil.showHidePwd(mBinding.et2p4gWifiPwd);
  }

  public void showHide5gPwd(View view) {
    InputUtil.showHidePwd(mBinding.et5gWifiPwd);
  }*/

  private void initPresenter() {
    mWifiSettingPresenter.setView(this);
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
  public void getWifiConfigSuccess(GetWifiConfigResponse response) {
    mBinding.setWifiResponse(response);
    mBinding.et2p4gWifiName.setText(response.getLfssid());
    moveEditCursorToEnd(mBinding.et2p4gWifiName);

    mBinding.et2p4gWifiPwd.setText(FILL_PWD);
    mBinding.et5gWifiPwd.setText(FILL_PWD);

    mConfigWifiRequest.setLfdisabled(mBinding.switch2p4g.isChecked() ? ModelConstant.DISABLE : ModelConstant.ENABLE);
    mConfigWifiRequest.setLfhiden(mBinding.switch2p4gHide.isChecked() ? ModelConstant.ENABLE : ModelConstant.DISABLE);
    mConfigWifiRequest.setHfdisabled(mBinding.switch5g.isChecked() ? ModelConstant.DISABLE : ModelConstant.ENABLE);
    mConfigWifiRequest.setHfhiden(mBinding.switch5gHide.isChecked() ? ModelConstant.ENABLE : ModelConstant.DISABLE);
  }

  @Override
  public void configWifiSuccess(ConfigWifiResponse response) {
    finish();
  }
}
