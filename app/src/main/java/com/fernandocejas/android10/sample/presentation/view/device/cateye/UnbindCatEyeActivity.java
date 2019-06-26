package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUnbindCatEyeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.UnbindCatLockResponse;

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
public class UnbindCatEyeActivity extends BaseActivity implements UnbindCatLockView{
  private ActivityUnbindCatEyeBinding mBinding;
  private int REQUEST_UNBIND_CAT_LOCK_CODE = 0x12;

  @Inject
  UbindCatLockPresenter mUnbindCatPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_unbind_cat_eye);

    initializeInjector();

    initPresenter();

    initView();

    initData();

  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mUnbindCatPresenter.setView(this);
  }

  private void initData() {
    mBinding.tvTips.setText("您已经连接门锁\n"+getLockInfo().getDeviceNickName());
  }

  private void initView() {
    initTitleBar("配对门锁");

  }

  /**
  * 解除配对
  *
  * @param
  * @return
  */
  public void onUnbindCatEyeClick(View view) {
    mUnbindCatPresenter.unbindCatLock(getLockInfo().getDeviceSerialNo(),GlobleConstant.getgCatEyeId());
  }

  @Override
  public void unbindCayLock(UnbindCatLockResponse response) {
    Toast.makeText(this, "解除成功", Toast.LENGTH_SHORT).show();
    this.setResult(REQUEST_UNBIND_CAT_LOCK_CODE);
    finish();
  }

  private QueryCatLockResponse getLockInfo(){
    return (QueryCatLockResponse)getIntent().getSerializableExtra("LOCK_BEAN");
  }
}
