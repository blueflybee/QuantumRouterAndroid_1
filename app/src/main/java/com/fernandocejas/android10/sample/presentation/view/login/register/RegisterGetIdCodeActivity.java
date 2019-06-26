package com.fernandocejas.android10.sample.presentation.view.login.register;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRegisterGetIdCodeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLoginComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LoginModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.GetIdCodeRequest;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/09
 *      desc: 输入手机号(注册页)
 *      version: 1.0
 * </pre>
 */
public class RegisterGetIdCodeActivity extends BaseActivity implements GetIdCodeView {

  @Inject
  GetIdCodePresenter mGetIdCodePresenter;

  private ActivityRegisterGetIdCodeBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_get_id_code);

    initializeInjector();

    initPresenter();

    initView();
  }

  private void initView() {
    initTitleBar("注册");

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

  private void initPresenter() {
    mGetIdCodePresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLoginComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .loginModule(new LoginModule())
        .build()
        .inject(this);
  }

  public void nextStep(View view) {
    mGetIdCodePresenter.getIdCode(userPhone());
  }

  public void clearUsername(View view) {
    mBinding.etEnterNum.setText("");
  }

  @NonNull
  private String userPhone() {
    return mBinding.etEnterNum.getText().toString();
  }

  @Override
  public void showUserPhoneEmp() {
    Toast.makeText(getContext(), R.string.register_toast_number_empty, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void openRegister(GetIdCodeResponse response) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTR_REGISTER_PHONE, userPhone());
    mNavigator.navigateTo(getContext(), RegisterActivity.class, intent);
  }

  @Override
  public void showGetIdCodeSuccess() {
    Toast.makeText(getContext(), R.string.register_toast_get_id_code_success, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }
}
