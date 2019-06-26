package com.fernandocejas.android10.sample.presentation.view.device.keystore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.net.RouterRestApiImpl;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityKeyInvalidBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMatchPinActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterView;
import com.fernandocejas.android10.sample.presentation.view.device.router.loadkey.MatchPinActivity;
import com.qtec.router.model.rsp.SearchRouterResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class KeyInvalidActivity extends Activity implements AddRouterView {

  @Inject
  AddRouterPresenter mAddRouterPresenter;

  private ActivityKeyInvalidBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_key_invalid);
    setFinishOnTouchOutside(false);

    initializeInjector();

    initPresenter();
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

  @Override
  public void onBackPressed() {}

  public void confirm(View view) {
    if (AppConstant.DEVICE_TYPE_ROUTER.equals(getKeyInvalidDeviceType())) {
      KeystoreRepertory.getInstance().clear(GlobleConstant.getgKeyRepoId());
      mAddRouterPresenter.searchRouter();
    } else if (AppConstant.DEVICE_TYPE_LOCK.equals(getKeyInvalidDeviceType())) {
      String lockMac = getIntent().getStringExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS);
      BleLock bleLock = LockManager.getLock(this, lockMac);
      if (bleLock != null) {
        KeystoreRepertory.getInstance().clear(bleLock.getKeyRepoId());
      }
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_ONLY_INJECT_KEY, true);
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMac);
      new Navigator().navigateTo(this, LockMatchPinActivity.class, intent);
      finish();
    }
  }

  private void routerKeyInvalid(SearchRouterResponse response) {
    String keyInvalidRouterId = getIntent().getStringExtra(RouterRestApiImpl.KEY_INVALID_ROUTER_ID);
    if (!TextUtils.isEmpty(keyInvalidRouterId) && keyInvalidRouterId.equals(response.getSerialnum())) {
      new Navigator().navigateTo(this, MatchPinActivity.class);
      finish();
    } else {
      showWifiNotConnectDialog();
    }
  }

  private void showWifiNotConnectDialog() {
    View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_ble_ok, null);
    ((TextView) dialogView.findViewById(R.id.tv_title)).setText("未连接网关网络");
    ((TextView) dialogView.findViewById(R.id.tv_content)).setText("请连接该网关的WiFi后尝试注入密钥");
    Button button = (Button) dialogView.findViewById(R.id.btn_confirm);
    button.setText("确定");
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    dialogView.findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
    setContentView(dialogView);
  }

  private String getKeyInvalidDeviceType() {
    return getIntent().getStringExtra(Navigator.EXTRA_KEY_INVALID_TYPE);
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link ApplicationComponent}
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  /**
   * Get an Activity module for dependency injection.
   *
   * @return {@link ActivityModule}
   */
  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  @Override
  public void showSearchSuccess(SearchRouterResponse response) {
    routerKeyInvalid(response);
  }

  @Override
  public void showSearchFailed(Throwable e) {
    showWifiNotConnectDialog();
  }

  @Override
  public void onError(String message) {
    showWifiNotConnectDialog();
  }

  @Override
  public void showLoading() {
    DialogUtil.showProgress(getContext());
  }


  @Override
  public void hideLoading() {
    DialogUtil.hideProgress();

  }

  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void showLoginInvalid() {}
}