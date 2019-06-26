package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.domain.constant.ModelConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityNetModeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.GetNetModeResponse;

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
public class NetModeActivity extends BaseActivity implements NetModeView {
  private ActivityNetModeBinding mBinding;

  @Inject
  NetModePresenter mNetModePresenter;

  private GetNetModeResponse mGetNetModeResponse = new GetNetModeResponse();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_net_mode);

    initializeInjector();

    initPresenter();

    initView();

    getNetMode();

  }

  private void getNetMode() {
    mNetModePresenter.getNetMode();
  }

  private void initView() {
    initTitleBar("上网方式");
  }

  public void dhcpMode(View view) {
    mNavigator.navigateTo(getContext(), DHCPActivity.class);
  }

  public void staticMode(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_NET_MODE_DATA, mGetNetModeResponse);
    mNavigator.navigateTo(getContext(), StaticIpActivity.class, intent);
  }

  public void pppoeMode(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_NET_MODE_DATA, mGetNetModeResponse);
    mNavigator.navigateTo(getContext(), PPPOEActivity.class, intent);
  }


  private void initPresenter() {
    mNetModePresenter.setView(this);
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
  protected void onRestart() {
    super.onRestart();
    getNetMode();
  }

  @Override
  public void showNetMode(GetNetModeResponse response) {
    mGetNetModeResponse = response;
    String connectiontype = response.getConnectiontype();
    switch (connectiontype) {
      case ModelConstant.NET_MODE_DHCP:
        mBinding.tvDhcpOn.setText("当前上网方式");
        mBinding.tvPppoeOn.setText("");
        mBinding.tvStaticOn.setText("");
        break;
      case ModelConstant.NET_MODE_STATIC:
        mBinding.tvStaticOn.setText("当前上网方式");
        mBinding.tvPppoeOn.setText("");
        mBinding.tvDhcpOn.setText("");
        break;
      case ModelConstant.NET_MODE_PPPOE:
        mBinding.tvPppoeOn.setText("当前上网方式");
        mBinding.tvDhcpOn.setText("");
        mBinding.tvStaticOn.setText("");
        break;
      default:

        break;
    }

  }
}
