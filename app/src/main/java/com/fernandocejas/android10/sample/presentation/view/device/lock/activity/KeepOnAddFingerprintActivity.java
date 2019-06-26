package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityKeepOnAddLockFingerPrintBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class KeepOnAddFingerprintActivity extends BaseActivity {
  private ActivityKeepOnAddLockFingerPrintBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_keep_on_add_lock_finger_print);

    initView();
  }

  private void initView() {
    initTitleBar("门锁设置");
    if (AppConstant.isgFirstAddFingerPrint()) {
      mBinding.llFirstAddFingerprint.setVisibility(View.VISIBLE);
      mBinding.llFirstAddPwd.setVisibility(View.GONE);
    } else {
      mBinding.llFirstAddFingerprint.setVisibility(View.GONE);
      mBinding.llFirstAddPwd.setVisibility(View.VISIBLE);
    }
  }

  public void addFingerprint(View view) {
    mNavigator.navigateTo(getContext(), AddLockFpActivity.class, createIntent());
  }


  public void skipAddFingerprint(View view) {
    mNavigator.navigateTo(getContext(), BindRouterToLockActivity.class, createIntent());
  }

  public void addPwd(View view) {
    mNavigator.navigateTo(getContext(), AddLockPwdActivity.class, createIntent());
  }

  public void skipAddPwd(View view) {
    mNavigator.navigateTo(getContext(), BindRouterToLockActivity.class, createIntent());
  }

  @NonNull
  private Intent createIntent() {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
    return intent;
  }

}
