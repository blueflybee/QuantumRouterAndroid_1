package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityDhcpBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.SetDHCPResponse;

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
public class DHCPActivity extends BaseActivity implements DHCPView {
  private ActivityDhcpBinding mBinding;

  @Inject
  DHCPPresenter mDHCPPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dhcp);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("动态IP（DHCP）");
  }

  public void confirm(View view) {
    mDHCPPresenter.setDHCP(1);
  }

  private void initPresenter() {
    mDHCPPresenter.setView(this);
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
  public void showSetDHCPSuccess(SetDHCPResponse response) {
    ToastUtils.showShort("设置上网方式为DHCP");
    finish();
  }


//  @Override
//  protected void onRestart() {
//    super.onRestart();
//    queryIntelDeviceList();
//  }
}
