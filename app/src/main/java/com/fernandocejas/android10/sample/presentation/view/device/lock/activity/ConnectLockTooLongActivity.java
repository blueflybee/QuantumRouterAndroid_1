package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.DialogConnectLockTooMuchBinding;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.IBleOperable;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class ConnectLockTooLongActivity extends Activity {

  private DialogConnectLockTooMuchBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_connect_lock_too_much);
    setFinishOnTouchOutside(false);

    initView();


  }

  private void initView() {
    mBinding.rlClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    mBinding.btnIKnow.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    mBinding.btnRestart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        restartConnect();
        finish();
      }
    });
  }

  @Override
  public void onBackPressed() {
  }

  private void restartConnect() {
    IBleOperable bleLockService = ((AndroidApplication) getApplication()).getBleLockService();
    if (bleLockService != null) {
      bleLockService.setCanConnect(true);
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
    finish();
  }
}