package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeLimitBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWaitAuthDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.GetSambaAccountPresenter;
import com.qtec.router.model.rsp.AllowAuthDeviceResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

import java.io.Serializable;
import java.util.ArrayList;
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
public class WaitAuthDetailActivity extends BaseActivity implements View.OnClickListener, IAllowAuthDeviceView {
  private ActivityWaitAuthDetailBinding mBinding;

  @Inject
  AllowAuthDevicePresenter mAllowAuthDevicePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wait_auth_detail);
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
    mBinding.btnSuccessAuthed.setOnClickListener(this);
  }

  private void initData() {
    if(TextUtils.isEmpty(getDeviceDetail().getDev_name())){
      mBinding.tvDeviceName.setText("未知设备");
    }else {
      mBinding.tvDeviceName.setText(getDeviceDetail().getDev_name());
    }

    mBinding.tvMac.setText(getDeviceDetail().getDev_mac());
  }


  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_success_authed:
        allowAuthDeviceRequest();
        break;

      case R.id.btn_black_list:
        blockListRequest();
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
    intent.putExtra("FragmentPosition",0);
    mNavigator.navigateExistAndClearTop(getContext(), AntiFritNetMainActivity.class,intent);
  }

  private void allowAuthDeviceRequest() {
    if (getDeviceDetail().getDev_mac() == null || ("".equals(getDeviceDetail().getDev_mac()))) {
      return;
    }
    mAllowAuthDevicePresenter.allowAuthDevive(GlobleConstant.getgDeviceId(), getDeviceDetail().getDev_mac(),getDeviceDetail().getDev_name(), 1, 0);
  }

  private void blockListRequest() {
    if (getDeviceDetail().getDev_mac() == null || ("".equals(getDeviceDetail().getDev_mac()))) {
      return;
    }
    mAllowAuthDevicePresenter.allowAuthDevive(GlobleConstant.getgDeviceId(), getDeviceDetail().getDev_mac(),getDeviceDetail().getDev_name(), 0, 1);
  }
}
