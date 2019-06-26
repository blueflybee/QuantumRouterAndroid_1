package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUpdateFirmwareBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.GetFirmwareUpdateStatusResponse;
import com.qtec.router.model.rsp.UpdateFirmwareResponse;

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
public class UpdateFirmwareActivity extends BaseActivity implements UpdateFirmwareView {
  private ActivityUpdateFirmwareBinding mBinding;

  @Inject
  UpdateFirmwarePresenter mUpdateFirmwarePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_firmware);

    initializeInjector();

    initPresenter();

    initView();

    mUpdateFirmwarePresenter.checkUpdate();

  }

  private void initView() {
    initTitleBar("固件升级");
  }

  public void checkUpdate(View view) {
    mUpdateFirmwarePresenter.checkUpdate();
  }

  public void update(View view) {
    DialogUtil.showUpdateFirmwareDialog(
        getContext(), v -> mUpdateFirmwarePresenter.update());
  }

  private void initPresenter() {
    mUpdateFirmwarePresenter.setView(this);
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
  public void updateFirmwareSuccess(UpdateFirmwareResponse response) {
    mNavigator.navigateTo(getContext(), UpdatingFirmwareActivity.class);
  }

  @Override
  public void showFirmwareVersion(CheckFirmwareResponse response) {
    int effectivity = response.getEffectivity();
    mBinding.tvCurrentVersion.setText("当前固件版本：" + response.getLocalversionNo());
    mBinding.tvLastVersion.setText(effectivity == 0 ? "当前为最新版本" : "发现新版本：" + response.getUpdateversionNo());
    mBinding.btnCheckVersion.setVisibility(effectivity == 0 ? View.VISIBLE : View.GONE);
    mBinding.btnUpdate.setVisibility(effectivity == 0 ? View.GONE : View.VISIBLE);
  }

  @Override
  public void showFirmwareUpdateStatus(GetFirmwareUpdateStatusResponse response) {
  }

  @Override
  public void showFirmwareUpdateSuccess() {

  }

  @Override
  public void showFirmwareUpdateFailed(GetFirmwareUpdateStatusResponse response) {

  }

}
