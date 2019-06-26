package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUpdateFirmwareBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.UpdatingFirmwareActivity;
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
public class CheckLiteVersionActivity extends BaseActivity implements UpdateLiteView {
  private ActivityUpdateFirmwareBinding mBinding;

  @Inject
  UpdateLitePresenter mUpdateLitePresenter;
  private String mUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_firmware);

    initializeInjector();

    initPresenter();

    initView();

    mUpdateLitePresenter.checkVersion(liteGw().getDeviceSerialNo());

  }

  private void initView() {
    initTitleBar("固件升级");
    mBinding.tvStatus.setText("正在检查更新，请稍候...");
  }

  public void checkUpdate(View view) {
    mBinding.tvStatus.setText("正在检查更新，请稍候...");
    mBinding.tvCurrentVersion.setText("");
    mBinding.tvLastVersion.setText("");
    mUpdateLitePresenter.checkVersion(liteGw().getDeviceSerialNo());
  }

  public void update(View view) {
    DialogUtil.showUpdateFirmwareDialog(
        getContext(), v -> mUpdateLitePresenter.updateLite(liteGw().getDeviceSerialNo(), mUrl));
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

  @Override
  public void showVersion(CheckLiteVersionResponse response) {
    mUrl = response.getDownloadUrl();
    int isLatest = response.getIsLatest();
    mBinding.tvCurrentVersion.setText("当前固件版本：" + response.getCurrentVersionNo());
    mBinding.tvLastVersion.setText(isLatest == 1 ? "当前为最新版本" : "发现新版本：" + response.getLatestVersionNo());
    mBinding.tvStatus.setText("");
    mBinding.btnCheckVersion.setVisibility(isLatest == 1 ? View.VISIBLE : View.GONE);
    mBinding.btnUpdate.setVisibility(isLatest == 1 ? View.GONE : View.VISIBLE);
  }

  @Override
  public void onUpdate(UpdateLiteResponse response) {
    mNavigator.navigateTo(getContext(), LiteUpdateActivity.class, getIntent());
    finish();
  }

  @Override
  public void showUpdateStatus(GetLiteUpdateResponse response) {

  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> liteGw() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> data =
        (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> ) getIntent().getSerializableExtra(Navigator.EXTRA_LITE_GATEWAY);
//    data.setDeviceList(null);
    return data == null ? new GetDevTreeResponse<>() : data;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mUpdateLitePresenter.destroy();
  }
}
