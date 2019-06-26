package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockTipsBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ExceptionWarnMoreActivity;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class LockTipsActivity extends Activity {

  private ActivityLockTipsBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_tips);
    setFinishOnTouchOutside(false);

    initView();

  }

  private void initView() {
    mBinding.tvTitle.setText(title());
    mBinding.tvContent.setText(content());
  }

  @Override
  public void onBackPressed() {}

  public void closeDialog(View view) {
    finish();
  }

  public void leftBtnClick(View view) {
    finish();
  }

  public void rightBtnClick(View view) {
    Intent intent = new Intent(this, ExceptionWarnMoreActivity.class);
    intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, getLockId());
    startActivity(intent);
  }

  private String getLockId() {
    return getIntent().getStringExtra(Navigator.EXTRA_DEVICE_SERIAL_NO);
  }

  private String title() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_TIPS_TITLE);
  }

  private String content() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_TIPS_CONTENT);
  }

}