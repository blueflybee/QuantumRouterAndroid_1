package com.fernandocejas.android10.sample.presentation.view.login.forgetpwd;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityResetPwdGetIdCodeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLoginComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LoginModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ResetPwdGetIdCodeRequest;
import com.qtec.mapp.model.rsp.ResetPwdGetIdCodeResponse;

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
public class ResetPwdGetIdCodeActivity extends BaseActivity implements ResetPwdGetIdCodeView {
  private ActivityResetPwdGetIdCodeBinding mBinding;

  @Inject
  ResetPwdGetIdCodePresenter mResetPwdGetIdCodePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_pwd_get_id_code);

    initializeInjector();

    initPresenter();

    initView();
  }

  private void initView() {
    initTitleBar("忘记密码");

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setLength(11);
    watcher.addEt(mBinding.etEnterNum, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  private String userPhone() {
    return mBinding.etEnterNum.getText().toString();
  }

  private void initPresenter() {
    mResetPwdGetIdCodePresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLoginComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .loginModule(new LoginModule())
        .build()
        .inject(this);
  }

  public void clearUsername(View view) {
    mBinding.etEnterNum.setText("");
  }

  public void nextStep(View view) {
    mResetPwdGetIdCodePresenter.getIdCode(userPhone());
  }

  @Override
  public void showUserPhoneEmp() {
    Toast.makeText(this, R.string.reset_pwd_toast_phone_emp, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showGetIdCodeSuccess() {
    Toast.makeText(getContext(), R.string.register_toast_get_id_code_success, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void openResetPwd(ResetPwdGetIdCodeResponse response) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTR_RESET_PWD_PHONE, userPhone());
    mNavigator.navigateTo(getContext(), ResetPwdActivity.class, intent);
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }
}
