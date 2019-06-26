package com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddFingerBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.AddFingerPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger.IAddFingerView;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.SetFingerNameActivity;
import com.qtec.router.model.rsp.AddFingerResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/08/01
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class AddFingerActivity extends BaseActivity implements View.OnClickListener, IAddFingerView{
  private ActivityAddFingerBinding mBinding;
  @Inject
  AddFingerPresenter mAddFingerPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_finger);
    initTitleBar("添加指纹");

    initializeInjector();
    initPresenter();
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
    mAddFingerPresenter.setView(this);
  }

  private void initData() {
   mBinding.btnAddFinger.setOnClickListener(this);
  }

  /**
   * 添加指纹请求
   *
   * @param
   * @return
   */
  private void addFingerRequest() {
    mAddFingerPresenter.postFingerInfo(getRouterId(), getLockId());
  }

  private String getLockId() {
    return getIntent().getStringExtra(mNavigator.EXTRA_DEVICE_SERIAL_NO);
  }

  private String getRouterId() {
    return getIntent().getStringExtra(mNavigator.EXTR_ROUTER_SERIAL_NUM);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_addFinger:
        addFingerRequest();

        mBinding.tvAddFingerTitle.setVisibility(View.GONE);
        mBinding.btnAddFinger.setVisibility(View.GONE);
        mBinding.imgLogo.setVisibility(View.GONE);

        mBinding.imgPrompt.setVisibility(View.VISIBLE);
        mBinding.tvPrompt.setVisibility(View.VISIBLE);
        break;

      default:
        break;
    }
  }

/*  @Override
  public void onError(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public Context getContext() {
    return this;
  }*/

  /**
  * 添加指纹 返回
  *
  * @param
  * @return
  */
  @Override
  public void getFingerInfo(AddFingerResponse response) {
    if(TextUtils.isEmpty(response.getFpid())){
      return;
    }

    Intent intent = new Intent(this, SetFingerNameActivity.class);
    intent.putExtra(Navigator.EXTRA_FINGER_EDIT_FLAG, "0"); //1代表 指纹处于编辑状态，其他非编辑

    intent.putExtra(Navigator.EXTRA_FINGER_ID, response.getFpid());

    intent.putExtra(Navigator.EXTRA_DEVICE_SERIAL_NO, getLockId());
    intent.putExtra(Navigator.EXTR_ROUTER_SERIAL_NUM, getRouterId());

    DialogUtil.showSuccessDialog(getContext(), "添加成功", new DialogUtil.DialogCallback() {
      @Override
      public void onDismiss() {
        startActivityForResult(intent, mNavigator.ADD_FINGER_NAME);
        AddFingerActivity.this.finish();
      }
    });

  }
}
