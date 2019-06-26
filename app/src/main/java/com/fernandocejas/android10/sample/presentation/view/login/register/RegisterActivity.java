package com.fernandocejas.android10.sample.presentation.view.login.register;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SpannableStringUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRegisterBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLoginComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LoginModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.mapp.model.rsp.RegisterResponse;

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
public class RegisterActivity extends BaseActivity implements RegisterView {
  private ActivityRegisterBinding mBinding;

  private TimeCounter mTimer;

  @Inject
  RegisterPresenter mRegisterPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initData() {
    mTimer = new TimeCounter(60000, 1000);
    mTimer.start();
  }

  private void initView() {
    initTitleBar("注册");

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
      mBinding.btnRegister.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnRegister.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnRegister.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);

    watchEditTextNoClear(mBinding.etPwd);
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  public void register(View view) {
    mRegisterPresenter.register(userPhone(), smsCode(), pwd());
  }

  public void sendVerificationCode(View view) {
    mRegisterPresenter.getIdCode(userPhone());
  }

  private String pwd() {
    return mBinding.etPwd.getText().toString();
  }

  private String smsCode() {
    return mBinding.etEnterVertifiCode.getText().toString();
  }

  private String userPhone() {
    return getIntent().getStringExtra(Navigator.EXTR_REGISTER_PHONE);
  }

  private void initPresenter() {
    mRegisterPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLoginComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .loginModule(new LoginModule())
        .build()
        .inject(this);

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
  public void showRegisterSuccess(RegisterResponse response) {
    DialogUtil.showSuccessDialog(getContext(), "注册成功", () ->
        mNavigator.navigateTo(getContext(), PersonalProfileActivity.class));
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
