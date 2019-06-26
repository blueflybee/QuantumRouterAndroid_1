package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUpdatingFirmwareBinding;
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
public class UpdatingFirmwareActivity extends BaseActivity implements UpdateFirmwareView {
  private ActivityUpdatingFirmwareBinding mBinding;

  @Inject
  UpdateFirmwarePresenter mUpdateFirmwarePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_updating_firmware);

    initializeInjector();

    initPresenter();

    initView();

    mUpdateFirmwarePresenter.getUpdateStatus();

  }

  private void initView() {
    initTitleBar("固件升级");
    mTitleBar.hideNavigation();
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

  }

  @Override
  public void showFirmwareVersion(CheckFirmwareResponse response) {

  }

  @Override
  public void showFirmwareUpdateStatus(GetFirmwareUpdateStatusResponse response) {
    mBinding.tvUpdateStatus.setText(AppConstant.getFirmUpdateStatus(response.getStatus()));
  }

  @Override
  public void showFirmwareUpdateSuccess() {
    mBinding.tvUpdateStatus.setText("固件升级成功，网关正在重启...");
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        mNavigator.navigateExistAndClearTop(getContext(), UpdateFirmwareActivity.class);
      }
    }, 2 * 60 * 1000);
  }

  @Override
  public void showFirmwareUpdateFailed(GetFirmwareUpdateStatusResponse response) {
    mBinding.tvUpdateStatus.setText(AppConstant.getFirmUpdateStatus(response.getStatus()));
    DialogUtil.showSuccessDialog(getContext(), "升级失败，请重新升级", () -> {
      mNavigator.navigateExistAndClearTop(getContext(), UpdateFirmwareActivity.class);
    });
  }

}
