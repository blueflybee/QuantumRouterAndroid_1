package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetLockBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.FingerprintListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.LockPwdListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.SecurityManagePresenter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.SecurityManageView;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;

import java.util.List;

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
public class SetLockActivity extends BaseActivity implements SecurityManageView {

  private ActivitySetLockBinding mBinding;

  @Inject
  SecurityManagePresenter mSecurityManagePresenter;

  private FingerprintListAdapter mFingerprintListAdapter;
  private LockPwdListAdapter mPwdListAdapter;
  private BleLock mBleLock;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_lock);

    initializeInjector();

    initPresenter();

    initData();

    initView();

    mSecurityManagePresenter.getFingerprints(mBleLock.getId());
    mSecurityManagePresenter.getPasswords(mBleLock.getId());
  }

  private void initData() {
    mBleLock = LockManager.getLock(getContext(), lockMacAddress());
  }

  private void initView() {
    initTitleBar("门锁设置");
    mTitleBar.setRightTextAs("跳过", getResources().getColor(R.color.blue_2196f3), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
        mNavigator.navigateTo(getContext(), BindRouterToLockActivity.class, intent);
      }
    });

    mFingerprintListAdapter = new FingerprintListAdapter(getContext(), false);
    mBinding.lvFingerprint.setAdapter(mFingerprintListAdapter);

    mPwdListAdapter = new LockPwdListAdapter(getContext(), false);
    mBinding.lvPwd.setAdapter(mPwdListAdapter);
  }

  public void addFingerprint(View view) {
    AppConstant.setgFirstAddFingerPrint(true);
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
    mNavigator.navigateTo(getContext(), AddLockFpActivity.class, intent);
  }

  public void addPassword(View view) {
    AppConstant.setgFirstAddFingerPrint(false);
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
    mNavigator.navigateTo(getContext(), AddLockPwdTouchActivity.class, intent);
  }

  private void initPresenter() {
    mSecurityManagePresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  @Override
  public void showFingerprints(List<GetFingerprintsResponse> fingerprints) {
    mBinding.rlAddFingerprint.setVisibility(View.VISIBLE);
    mBinding.lvFingerprint.setVisibility(View.VISIBLE);
    mFingerprintListAdapter.update(fingerprints);
  }

  @Override
  public void showPasswords(List<GetPasswordsResponse> passwords) {
    mBinding.rlAddPwd.setVisibility(View.VISIBLE);
    mBinding.lvPwd.setVisibility(View.VISIBLE);
    mPwdListAdapter.update(passwords);
  }

  @Override
  public void showNoFingerprints() {
    mBinding.rlAddFingerprint.setVisibility(View.GONE);
    mBinding.lvFingerprint.setVisibility(View.GONE);
  }

  @Override
  public void showNoPasswords() {
    mBinding.rlAddPwd.setVisibility(View.GONE);
    mBinding.lvPwd.setVisibility(View.GONE);
  }

  @Override
  public void showNoDoorCards() {

  }

  @Override
  public void showDoorCards(List<GetDoorCardsResponse> responses) {

  }

}
