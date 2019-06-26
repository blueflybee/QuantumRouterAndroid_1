package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAdminNewPwdBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.router.RouterMainActivity;
import com.qtec.router.model.rsp.SetAdminPwdResponse;

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
public class AdminNewPwdActivity extends BaseActivity implements AdminNewPwdView {
  private ActivityAdminNewPwdBinding mBinding;

  @Inject
  AdminNewPwdPresenter mAdminNewPwdPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_admin_new_pwd);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("管理密码");

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(4, 63));
    watcher.addEt(mBinding.etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 63);
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  public void confirm(View view) {
    mAdminNewPwdPresenter.setAdminPwd(getText(mBinding.etPwd), oldPwd());
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  private void initPresenter() {
    mAdminNewPwdPresenter.setView(this);
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
  public void setAdminPwdSuccess(SetAdminPwdResponse response) {
    ToastUtils.showShort("管理员密码设置成功");
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_TO_FRAGMENT, 2);
    mNavigator.navigateExistAndClearTop(getContext(), RouterMainActivity.class, intent);
  }

  private String oldPwd() {
    return getIntent().getStringExtra(Navigator.EXTRA_OLD_ADMIN_PWD);
  }

}
