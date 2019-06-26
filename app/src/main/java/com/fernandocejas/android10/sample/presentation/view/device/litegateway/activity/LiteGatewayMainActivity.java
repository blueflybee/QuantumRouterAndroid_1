package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLiteGatewayMainBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockBindRouterInfoPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockBindRouterInfoView;
import com.qtec.mapp.model.rsp.CloudUnbindRouterLockResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;
import com.qtec.router.model.rsp.UnbindRouterToLockResponse;

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
public class LiteGatewayMainActivity extends BaseActivity implements LockBindRouterInfoView {

  private static final String TAG = LiteGatewayMainActivity.class.getSimpleName();

  private ActivityLiteGatewayMainBinding mBinding;
  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> mLiteGw;

  @Inject
  LockBindRouterInfoPresenter mLockBindRouterInfoPresenter;
  private GetDevTreeResponse.DeviceBean mLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lite_gateway_main);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initPresenter() {
    mLockBindRouterInfoPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
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
    initTitleBar("LITE网关");
    mTitleBar.setRightAs("设置", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mNavigator.navigateTo(getContext(), LiteGatewaySettingActivity.class, getIntent());
      }
    });
    showPaired();
  }

  private void showNotPaired() {
    mBinding.llNotPair.setVisibility(View.VISIBLE);
    mBinding.llPaired.setVisibility(View.GONE);
  }

  private void showPaired() {
    mBinding.llNotPair.setVisibility(View.GONE);
    mBinding.llPaired.setVisibility(View.VISIBLE);
    mLock = mLiteGw.getDeviceList().get(1);
    mBinding.tvLockName.setText(mLock.getDeviceName());
    mBinding.tvLockModel.setText(mLock.getDeviceModel());
  }

  public void unbind(View view) {
    DialogUtil.showConfirmCancelDialog(getContext(), "提示", "确定要解绑门锁吗？", "确定", "取消", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mLockBindRouterInfoPresenter.unbindRouter(mLiteGw.getDeviceSerialNo(), mLock.getDeviceSerialNo());
      }
    }, null);
  }

  public void pair(View view) {
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLiteGw() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> data =
        (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_LITE_GATEWAY);
    return data == null ? new GetDevTreeResponse<>() : data;
  }

  @Override
  public void showNotifyUnbindSuccess(UnbindRouterToLockResponse response) {
    DialogUtil.showQueryBindRouterDialog(getContext(), "正在解绑网关", "解绑网关可能需要10秒，请耐心等待", 10000, () -> {
      mLockBindRouterInfoPresenter.queryUnbindRouter(mLiteGw.getDeviceSerialNo(), mLock.getDeviceSerialNo());
    });
  }

  @Override
  public void showUnbindFail(QueryBindRouterToLockResponse response) {
    ToastUtils.showShort("解绑门锁失败");
  }

  @Override
  public void showUnbindSuccess(QueryBindRouterToLockResponse response) {
    ToastUtils.showShort("解绑门锁成功");
    finish();
  }

  @Override
  public void showCloudUnbindSuccess(CloudUnbindRouterLockResponse response) {

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mLockBindRouterInfoPresenter.destroy();
  }
}
