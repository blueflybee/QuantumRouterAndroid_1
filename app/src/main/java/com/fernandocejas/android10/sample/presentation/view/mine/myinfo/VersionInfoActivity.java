package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModifyPwdGetIdCodeBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityVersionInfoBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.utils.MyStringUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.CheckAppVersionResponse;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/14
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class VersionInfoActivity extends BaseActivity implements VersionInfoView {

  private ActivityVersionInfoBinding mBinding;

  @Inject
  VersionInfoPresenter mVersionInfoPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_version_info);

    initializeInjector();

    initPresenter();

    initView();

    mVersionInfoPresenter.checkVersion();
  }

  private void initView() {
    initTitleBar("版本信息");
    mBinding.tvVersionName.setText("版本号：" + AppUtils.getAppVersionName());

  }

  public void update(View view) {
    mVersionInfoPresenter.update();
  }

  @Override
  public void showVersionInfo(CheckAppVersionResponse response) {
    int newVersion = response.getVersionNum();
    int currentVersion = AppUtils.getAppVersionCode();
    System.out.println("版本 newVersion = " + newVersion);
    System.out.println("版本 currentVersion = " + currentVersion);
    boolean canUpdate = newVersion > currentVersion;
    System.out.println("版本 canUpdate = " + canUpdate);
    mBinding.tvVersionInfo.setText(canUpdate ? "发现新版本" + response.getVersionNo() : "当前已是最新版本！");
    mBinding.btnUpdate.setVisibility(canUpdate ? View.VISIBLE : View.GONE);

  }

  private void initPresenter() {
    mVersionInfoPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
        .build()
        .inject(this);
  }

}
