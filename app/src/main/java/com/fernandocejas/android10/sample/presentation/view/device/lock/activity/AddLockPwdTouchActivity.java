package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddLockPwdTouchBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddLockPwdTouchActivity extends BaseActivity {

  private static final String TAG = AddLockPwdTouchActivity.class.getName();

  private ActivityAddLockPwdTouchBinding mBinding;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_lock_pwd_touch);

    initView();
  }

  private void initView() {
    initTitleBar("添加密码");
  }

  public void addPwd(View view) {

    if(!BleLockUtils.isBleEnable()) return;

    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_ADD_PWD_ONLY, isAddPwdOnly());
    intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
    mNavigator.navigateTo(getContext(), AddLockPwdActivity.class, intent);
  }

  private boolean isAddPwdOnly() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_ADD_PWD_ONLY, false);
  }

}
