package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUpdatingFirmwareBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUpdateFirmwareActivity;
import com.qtec.mapp.model.rsp.CheckLiteVersionResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLiteUpdateResponse;
import com.qtec.mapp.model.rsp.UpdateLiteResponse;

import java.util.List;

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
public class LiteUpdateActivity extends BaseActivity implements UpdateLiteView {
  private ActivityUpdatingFirmwareBinding mBinding;

  @Inject
  UpdateLitePresenter mUpdateLitePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_updating_firmware);

    initializeInjector();

    initPresenter();

    initView();

    mUpdateLitePresenter.getLiteUpdate(liteGw().getDeviceSerialNo());
  }

  private void initView() {
    initTitleBar("固件升级");
    mBinding.tvTips.setText("当前版本正在升级，请等待");
  }

  private void initPresenter() {
    mUpdateLitePresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

//  @Override
//  public void showFirmwareUpdateSuccess() {
//    mBinding.tvUpdateStatus.setText("固件升级成功，网关正在重启...");
//    new Handler().postDelayed(new Runnable() {
//      @Override
//      public void run() {
//        mNavigator.navigateExistAndClearTop(getContext(), UpdateFirmwareActivity.class);
//      }
//    }, 2 * 60 * 1000);
//  }
//
//  @Override
//  public void showFirmwareUpdateFailed(GetFirmwareUpdateStatusResponse response) {
//    mBinding.tvUpdateStatus.setText(AppConstant.getFirmUpdateStatus(response.getStatus()));
//    DialogUtil.showSuccessDialog(getContext(), "升级失败，请重新升级", () -> {
//      mNavigator.navigateExistAndClearTop(getContext(), UpdateFirmwareActivity.class);
//    });
//  }

  @Override
  public void showVersion(CheckLiteVersionResponse response) {
  }

  @Override
  public void onUpdate(UpdateLiteResponse response) {
  }

  @Override
  public void showUpdateStatus(GetLiteUpdateResponse response) {
    //upgradeStatus : 升级结果,0表示升级成功,-1表示升级失败,1表示升级中
//    mBinding.tvUpdateStatus.setText(AppConstant.getFirmUpdateStatus(response.getStatus()));
    String status = response.getUpgradeStatus();
    if ("0".equals(status)) {
      onUpdateSuccess();
    } else if ("1".equals(status)) {
      onUpdating();
    } else if ("-1".equals(status)) {
      onUpdateFail();
    } else {
      onUpdating();
    }
  }

  private void onUpdateFail() {
    mBinding.tvTips.setText("固件升级失败，请重新升级");
  }

  private void onUpdating() {
    mBinding.tvTips.setText("当前版本正在升级，请等待");
  }

  private void onUpdateSuccess() {
    mBinding.tvTips.setText("固件升级成功，网关正在重启...");
    mBinding.progressUpdate.setVisibility(View.VISIBLE);
    new TimeCounter(30000, 100).start();
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> liteGw() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> data =
        (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_LITE_GATEWAY);
    return data == null ? new GetDevTreeResponse<>() : data;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mUpdateLitePresenter.destroy();
  }

  private class TimeCounter extends CountDownTimer {

    TimeCounter(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
      int progress = (int) ((1 - millisUntilFinished / 30000f) * 100);
      mBinding.progressUpdate.setProgress(progress);
    }

    @Override
    public void onFinish() {
      finish();
    }
  }
}
