package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterInfoBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.RouterMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.RouterInfoAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router.BaseInfo;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;

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
public class RouterInfoActivity extends BaseActivity implements RouterInfoView {
  private ActivityRouterInfoBinding mBinding;

  @Inject
  RouterInfoPresenter mRouterInfoPresenter;
  private RouterInfoAdapter mAdapter;
  private String mDescription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_router_info);

    initializeInjector();

    initPresenter();

    initView();

    mRouterInfoPresenter.getRouterInfo();

    mRouterInfoPresenter.getRouterInfoCloud(GlobleConstant.getgDeviceId());

  }

  private void initView() {
    initTitleBar("网关信息");
    mTitleBar.setLeftAsBackButton(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendNewRouterName();
        finish();
      }
    });

    mAdapter = new RouterInfoAdapter(getContext());
    mBinding.lvRouterInfo.setAdapter(mAdapter);
  }

  public void modifyRouterName(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_MODIFY_PROPERTY, getText(mBinding.tvRouterName));
    intent.putExtra(Navigator.EXTRA_MODIFY_HINT, "请输入网关名称");
    mNavigator.navigateTo(getContext(), ModifyRouterNameActivity.class, intent);
  }

  public void routerGroup(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_ROUTER_GROUP, getText(mBinding.tvRouterGroup));
    mNavigator.navigateTo(getContext(), RouterGroupActivity.class, intent);
  }

  public void routerDesc(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_ROUTER_DESCRIPTION, mDescription);
    mNavigator.navigateTo(getContext(), RouterDesActivity.class, intent);
  }

  private void initPresenter() {
    mRouterInfoPresenter.setView(this);
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
  public void showRouterInfo(BaseInfo baseInfo) {
    mAdapter.notifyDataSetChanged(baseInfo.getInfoItems());
  }

  @Override
  public void showRouterInfoCloud(GetRouterInfoCloudResponse response) {
    mBinding.tvRouterName.setText(response.getRouterNickName());
    mBinding.tvRouterGroup.setText(response.getGroupName());
    mDescription = response.getDescription();
    mBinding.tvDesc.setText(mDescription);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    mRouterInfoPresenter.getRouterInfo();
    mRouterInfoPresenter.getRouterInfoCloud(GlobleConstant.getgDeviceId());
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    sendNewRouterName();
  }

  private void sendNewRouterName() {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_NEW_ROUTER_NAME, getText(mBinding.tvRouterName));
    mNavigator.navigateTo(getContext(), RouterMainActivity.class, intent);
  }
}
