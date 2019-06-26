package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockFactoryResetBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;

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
public class LockFactoryResetActivity extends Activity implements AddLockView {

  private ActivityLockFactoryResetBinding mBinding;
  private BleLock mBleLock;

  @Inject
  AddLockPresenter mAddLockPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_factory_reset);
    setFinishOnTouchOutside(false);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initPresenter() {
    mAddLockPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .lockModule(new LockModule())
        .build()
        .inject(this);
  }

  private void initData() {
    mBleLock = LockManager.getLock(this, lockMac());
  }

  private void initView() {
    if (mBleLock == null) finish();
    mBinding.tvContent.setText("您的门锁“" + mBleLock.getDeviceName() + "”已经恢复出厂设置，是否通知该门锁的其他用户？");
  }

  private void clearLockData() {
    KeystoreRepertory.getInstance().clear(mBleLock.getKeyRepoId());
    LockManager.delete(this, lockMac());
    DeviceCacheManager.delete(this, mBleLock.getId());
  }

  @Override
  public void onBackPressed() {
  }

  public void confirm(View view) {

    mAddLockPresenter.lockFactoryReset(mBleLock.getId());
  }

  public void cancel(View view) {
    finish();
  }

  public void closeDialog(View view) {
    finish();
  }

  private String lockMac() {
    return getIntent().getStringExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS);
  }

  private ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  @Override
  public void showLoading() {
    DialogUtil.showProgress(this);
  }

  @Override
  public void hideLoading() {
    DialogUtil.hideProgress();
  }

  @Override
  public void onError(String message) {
    if (!TextUtils.isEmpty(message)) {
      ToastUtils.showShort(message);
    }
  }

  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void showLoginInvalid() {
    DialogUtil.showOkAlertDialogWithMessage(
        getContext(),
        "登录失效", "您的登录已失效，请重新登录",
        v -> new Navigator().navigateNewAndClearTask(getContext(), LoginActivity.class));
  }

  @Override
  public void onAddLockSuccess(CommitAddRouterInfoResponse response) {}

  @Override
  public void onGetUsersOfLockSuccess(String userIds) {}

  @Override
  public void onFactoryResetSuccess(LockFactoryResetResponse response) {
    ToastUtils.showShort("恢复出厂设置信息同步成功");
    clearLockData();
    finish();
    new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
  }
}