package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SpannableStringUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModifyPwdBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.MyStringUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.mapp.model.req.ModifyPwdGetIdCodeRequest;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/09
 *      desc: 修改密码
 *      version: 1.1
 * </pre>
 */
public class ModifyPwdActivity extends BaseActivity implements ModifyPwdView {

  private ActivityModifyPwdBinding mBinding;

  @Inject
  ModifyPwdPresenter mModifyPwdPresenter;

  private TimeCounter mTimer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_pwd);

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
    ModifyPwdGetIdCodeRequest request = new ModifyPwdGetIdCodeRequest();
    request.setUserPhone(userPhone());
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    mModifyPwdPresenter.getIdCode(encryptInfo);
  }

  private void initView() {
    initTitleBar("修改密码");
    mBinding.tvTipTitle.setText(new SpannableStringUtils.Builder()
        .append("验证码已发送至您的手机").setForegroundColor(getResources().getColor(R.color.gray_888888))
        .append(" ")
        .append(MyStringUtil.addStar(userPhone())).setForegroundColor(getResources().getColor(R.color.blue_2196f3))
        .create());

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setLength(6);
    InputWatcher.WatchCondition condition2 = new InputWatcher.WatchCondition();
    condition2.setRange(new InputWatcher.InputRange(6, 20));
    watcher.addEt(mBinding.etEnterVertifiCode, condition1);
    watcher.addEt(mBinding.etPwd, condition2);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnModifyPwd.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnModifyPwd.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnModifyPwd.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
    watchEditTextNoClear(mBinding.etPwd);
  }

  public void modifyPwd(View view) {
    mModifyPwdPresenter.modifyPwd(userPhone(), smsCode(), pwd());
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  private String pwd() {
    return mBinding.etPwd.getText().toString();
  }

  private String smsCode() {
    return mBinding.etEnterVertifiCode.getText().toString();
  }


  private String userPhone() {
    return new SPUtils(PrefConstant.SP_NAME).getString(PrefConstant.SP_USER_PHONE);
  }

  private void initPresenter() {
    mModifyPwdPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
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
  public void showModifyPwdSuccess() {
    Toast.makeText(this, R.string.modify_pwd_toast_success, Toast.LENGTH_SHORT).show();
    mNavigator.navigateExistAndClearTop(getContext(), MyInfoActivity.class);
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
