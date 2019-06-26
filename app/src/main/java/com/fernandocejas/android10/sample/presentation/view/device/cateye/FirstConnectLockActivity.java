package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFirstConnectLockBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTouchLockBinding;
import com.fernandocejas.android10.sample.presentation.utils.ToastUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.SearchLockActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   : 首次配对门锁
 *     version: 1.0
 * </pre>
 */
public class FirstConnectLockActivity extends BaseActivity {
  private ActivityFirstConnectLockBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_connect_lock);

    initView();
  }

  private void initView() {
    initTitleBar("配对门锁");
  }

  /**
  * 点击事件
  *
  * @param
  * @return
  */
  public void onFirstConnectLock(View view) {
    Intent intent = new Intent(this, ConnectLockActivity.class);
    intent.putExtra("IS_FIRST_CONFIG",0);// 0 首次配置
    startActivity(intent);
  }

  public void onAfterConnectLockClick(View view) {
    Intent intent = new Intent(this,MainActivity.class);
    startActivity(intent);
  }

}
