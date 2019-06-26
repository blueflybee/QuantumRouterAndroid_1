package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityDeviceNameBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityNetModeBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.DHCPActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.PPPOEActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.StaticIpActivity;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DeviceNameActivity extends BaseActivity implements View.OnClickListener{
  //private ActivityNetModeBinding mBinding;
  private ActivityDeviceNameBinding mBinding;

//    @Inject
//    IntelDeviceListPresenter mIntelDeviceListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_name);
//    initializeInjector();

//        initPresenter();

    initView();
    initData();

  }

  private void initView() {
    initTitleBar("设备名称");
  }

  private void initData() {
    mBinding.rlDeviceName.setOnClickListener(this);
    mBinding.rlDeviceRemark.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.rl_deviceRemark:
        mNavigator.navigateTo(this,SetDeviceRemarkActivity.class,null);
        break;

      case R.id.rl_deviceName:
        mNavigator.navigateTo(this,SetDeviceNameActivity.class,null);
        break;

    }
  }

//  private void initPresenter() {
//    mIntelDeviceListPresenter.setView(this);
//  }

//  private void initializeInjector() {
//    DaggerRouterComponent.builder()
//        .applicationComponent(getApplicationComponent())
//        .activityModule(getActivityModule())
//        .routerModule(new RouterModule())
//        .build()
//        .inject(this);
//  }
//
//  @Override
//  public void showIntelDeviceList(List<IntelDeviceListResponse> response) {
//    mAdapter.notifyDataSetChanged(response);
//  }

//  @Override
//  protected void onRestart() {
//    super.onRestart();
//    queryIntelDeviceList();
//  }
}
