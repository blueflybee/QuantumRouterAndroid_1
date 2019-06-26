package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTouchLockBinding;
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
public class TouchLockActivity extends BaseActivity {
  private ActivityTouchLockBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_touch_lock);

    initView();
  }

  private void initView() {
    initTitleBar("添加门锁");
  }

  public void startSearch(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    mNavigator.navigateTo(getContext(), SearchLockActivity.class);
  }

}
