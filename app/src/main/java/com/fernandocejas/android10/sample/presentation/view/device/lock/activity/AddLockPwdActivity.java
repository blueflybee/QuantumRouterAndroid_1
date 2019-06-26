package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddLockPwdBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddLockPwdActivity extends BaseActivity {
  private ActivityAddLockPwdBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_lock_pwd);

    initView();
  }

  private void initView() {
    initTitleBar("添加密码");
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setLength(6);
    watcher.addEt(mBinding.etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  public void nextStep(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_ADD_LOCK_PWD, getText(mBinding.etPwd));
    intent.putExtra(Navigator.EXTRA_ADD_PWD_ONLY, isAddPwdOnly());
    intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
    mNavigator.navigateTo(getContext(), AddLockPwdAgainActivity.class, intent);
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  private boolean isAddPwdOnly() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_ADD_PWD_ONLY, false);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    System.out.println("AddLockPwdActivity.onDestroy");
  }


}
