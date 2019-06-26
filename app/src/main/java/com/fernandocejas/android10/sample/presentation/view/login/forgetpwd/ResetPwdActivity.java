package com.fernandocejas.android10.sample.presentation.view.login.forgetpwd;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SpannableStringUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityResetPwdBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLoginComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LoginModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.qtec.mapp.model.req.ResetPwdGetIdCodeRequest;
import com.qtec.mapp.model.req.ResetPwdRequest;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/09
 *      desc: 重置密码页(忘记密码)
 *      version: 1.0
 * </pre>
 */

public class ResetPwdActivity extends BaseActivity implements ResetPwdView {

  private ActivityResetPwdBinding mBinding;

  @Inject
  ResetPwdPresenter mResetPwdPresenter;
  private TimeCounter mTimer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_pwd);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initData() {
    mTimer = new TimeCounter(60000, 1000);
    mTimer.start();
  }

  public void sendVerificationCode(View view) {
    mResetPwdPresenter.getIdCode(userPhone());
  }

  private void initView() {
    initTitleBar("忘记密码");

    mBinding.tvTipTitle.setText(new SpannableStringUtils.Builder()
        .append("验证码已发送至您的手机").setForegroundColor(getResources().getColor(R.color.gray_888888))
        .append(" ")
        .append(userPhone()).setForegroundColor(getResources().getColor(R.color.blue_2196f3))
        .create());

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setLength(6);
    InputWatcher.WatchCondition condition2 = new InputWatcher.WatchCondition();
    condition2.setRange(new InputWatcher.InputRange(6, 20));
    watcher.addEt(mBinding.etEnterVertifiCode, condition1);
    watcher.addEt(mBinding.etPwd, condition2);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnResetPwd.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnResetPwd.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnResetPwd.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
    watchEditTextNoClear(mBinding.etPwd);

  }

  private String pwd() {
    return mBinding.etPwd.getText().toString();
  }

  private String smsCode() {
    return mBinding.etEnterVertifiCode.getText().toString();
  }

  private String userPhone() {
    return getIntent().getStringExtra(Navigator.EXTR_RESET_PWD_PHONE);
  }

  private void initPresenter() {
    mResetPwdPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLoginComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .loginModule(new LoginModule())
        .build()
        .inject(this);
  }

  public void resetPwd(View view) {
    mResetPwdPresenter.resetPwd(userPhone(), smsCode(), pwd());
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  @Override
  public void showUserPhoneEmp() {
    Toast.makeText(this, R.string.register_toast_number_empty, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onGetIdCodeSuccess() {
    Toast.makeText(this, R.string.register_toast_id_code_success, Toast.LENGTH_SHORT).show();
    mTimer.start();
  }

  @Override
  public void showIdCodeEmp() {
    Toast.makeText(this, R.string.register_toast_id_code_emp, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showPasswordEmp() {
    Toast.makeText(this, R.string.register_toast_pwd_emp, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showResetPwdSuccess() {
    Toast.makeText(this, R.string.reset_pwd_toast_success, Toast.LENGTH_SHORT).show();
    mNavigator.navigateExistAndClearTop(getContext(), LoginActivity.class);
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  private class TimeCounter extends CountDownTimer {

    TimeCounter(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
      mBinding.tvReSendVertifiCode.setTextColor(getResources().getColor(R.color.gray_cdcdcd));
      mBinding.tvReSendVertifiCode.setClickable(false);
      mBinding.tvReSendVertifiCode.setText("重新发送（" + millisUntilFinished / 1000 + "）");
    }

    @Override
    public void onFinish() {
      mBinding.tvReSendVertifiCode.setText("重新发送");
      mBinding.tvReSendVertifiCode.setClickable(true);
      mBinding.tvReSendVertifiCode.setTextColor(getResources().getColor(R.color.blue_2196f3));
    }
  }

}
