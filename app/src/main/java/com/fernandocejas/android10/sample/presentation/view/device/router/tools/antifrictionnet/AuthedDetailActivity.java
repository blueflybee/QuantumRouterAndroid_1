package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAuthedDetailBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWaitAuthDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.AllowAuthDeviceResponse;
import com.qtec.router.model.rsp.BlockAuthedResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

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
public class AuthedDetailActivity extends BaseActivity implements View.OnClickListener, IAllowAuthDeviceView{
  private ActivityAuthedDetailBinding mBinding;

  @Inject
  AllowAuthDevicePresenter mAllowAuthDevicePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_authed_detail);
    initializeInjector();

    initPresenter();

    initView();
    initData();

  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mAllowAuthDevicePresenter.setView(this);
  }


  private void initView() {
    initTitleBar("设备详情");
    mBinding.btnBlackList.setOnClickListener(this);
    mBinding.btnCancleAuth.setOnClickListener(this);
  }

  private void initData() {
    if (getDeviceDetail().getDev_name() == null | ("".equals(getDeviceDetail().getDev_name()))) {
      return;
    } else {
      mBinding.tvDeviceName.setText(getDeviceDetail().getDev_name());
    }

    if (getDeviceDetail().getDev_ip() == null | ("".equals(getDeviceDetail().getDev_ip()))) {
      return;
    } else {
      mBinding.tvMac.setText(getDeviceDetail().getDev_mac());
    }

  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_cancle_auth:
        allowAuthDeviceRequest();
        break;

      case R.id.btn_black_list:
        blockAuthedRequest();
        break;

      default:
        break;
    }
  }

  private GetWaitingAuthDeviceListResponse getDeviceDetail() {
    return (GetWaitingAuthDeviceListResponse) getIntent().getSerializableExtra("DEVICEDETAIL");
  }

  @Override
  public void allowAuthDevice(AllowAuthDeviceResponse response) {
    Intent intent = new Intent();
    intent.putExtra("FragmentPosition",1);

    //判断一下本机是否已经认证
    try {
      if((!TextUtils.isEmpty(AppConstant.getMacAddress(getContext()))) && (!(TextUtils.isEmpty(getDeviceDetail().getDev_mac())))){
        if(AppConstant.getMacAddress(getContext()).equalsIgnoreCase(getDeviceDetail().getDev_mac())){
          PrefConstant.IS_AUTI_DEVICE_AUTHED = false;
          System.out.println("防蹭网：allowAuthDevice IS_AUTI_DEVICE_AUTHED "+PrefConstant.IS_AUTI_DEVICE_AUTHED);
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }

    mNavigator.navigateExistAndClearTop(getContext(), AntiFritNetMainActivity.class,intent);
  }

  private void allowAuthDeviceRequest() {
    if (getDeviceDetail().getDev_mac() == null || ("".equals(getDeviceDetail().getDev_mac()))) {
      return;
    }

    mAllowAuthDevicePresenter.allowAuthDevive(GlobleConstant.getgDeviceId(),getDeviceDetail().getDev_mac(),getDeviceDetail().getDev_name(),2,0);
  }

  private void blockAuthedRequest() {
    if (getDeviceDetail().getDev_mac() == null || ("".equals(getDeviceDetail().getDev_mac()))) {
      return;
    }

    mAllowAuthDevicePresenter.allowAuthDevive(GlobleConstant.getgDeviceId(),getDeviceDetail().getDev_mac(),getDeviceDetail().getDev_name(),0,1);

  }

}
