package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLiteGatewaySettingBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterSettingPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterSettingView;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;

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
public class LiteGatewaySettingActivity extends BaseActivity implements RouterSettingView {

  private static final String TAG = LiteGatewaySettingActivity.class.getSimpleName();

  private ActivityLiteGatewaySettingBinding mBinding;
  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> mLiteGw;

  @Inject
  RouterSettingPresenter mRouterSettingPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lite_gateway_setting);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initPresenter() {
    mRouterSettingPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initData() {
    mLiteGw = getLiteGw();
  }

  private void initView() {
    initTitleBar("设置");
  }

  public void unbind(View view) {
    DialogUtil.showOkCancelAlertDialogWithMessage(
        getContext(), "解绑LITE网关", "确认要解绑LITE网关吗？",
        v1 -> mRouterSettingPresenter.unbindRouter(mLiteGw.getDeviceSerialNo()), null);
  }

  public void configNet(View view) {
    Intent intent = new Intent();
    LiteGateway gw = new LiteGateway();
    gw.setId(mLiteGw.getDeviceSerialNo());
    gw.setBind(true);
    intent.putExtra(Navigator.EXTRA_BIND_LITE_GATEWAY, gw);
    mNavigator.navigateTo(getContext(), PairWifiActivity.class, intent);
  }

  public void showGateInfo(View view) {
    mNavigator.navigateTo(getContext(), LiteGatewayInfoActivity.class, getIntent());
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLiteGw() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> data =
        (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> ) getIntent().getSerializableExtra(Navigator.EXTRA_LITE_GATEWAY);
    return data == null ? new GetDevTreeResponse<>() : data;
  }

  @Override
  public void restartRouterSuccess(RestartRouterResponse response) {}

  @Override
  public void factoryResetSuccess(FactoryResetResponse response) {}

  @Override
  public void unbindRouterSuccess(UnbindRouterResponse response, String deviceId) {
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    ToastUtils.showLong("LITE网关解除绑定成功");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mRouterSettingPresenter.destroy();
  }

  public void update(View view) {
    mNavigator.navigateTo(getContext(), CheckLiteVersionActivity.class, getIntent());
  }
}
