package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentSecurityManageBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DeviceComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockDoorCardActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockFpActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockPwdTouchActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ModifyLockDoorCardActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ModifyLockFpActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ModifyLockPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.FingerprintListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.LockDoorCardListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.LockPwdListAdapter;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;

import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc: 指纹管理
 *      version: 1.0
 * </pre>
 */
public class SecurityManageFragment extends BaseFragment implements SecurityManageView {

  private FragmentSecurityManageBinding mBinding;

  @Inject
  SecurityManagePresenter mSecurityManagePresenter;

  private FingerprintListAdapter mFingerprintListAdapter;
  private LockPwdListAdapter mPwdListAdapter;
  private LockDoorCardListAdapter mLockDoorCardListAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(DeviceComponent.class).inject(this);
  }

  public static SecurityManageFragment newInstance() {
    SecurityManageFragment fragment = new SecurityManageFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_security_manage, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    initView();
    initPresenter();

  }

  private void initPresenter() {
    mSecurityManagePresenter.setView(this);
  }

  private void initView() {
    initAddFingerprintView();

    initAddPwdView();

    initAddDoorCardView();
  }

  private void initAddDoorCardView() {
    mBinding.rlAddDoorCard.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getContext(), AddLockDoorCardActivity.class, intent);
    });
    mLockDoorCardListAdapter = new LockDoorCardListAdapter(getContext());
    mBinding.lvDoorCard.setAdapter(mLockDoorCardListAdapter);
    mBinding.lvDoorCard.setOnItemClickListener((parent, view, position, id) -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_LOCK_DOOR_CARD, mLockDoorCardListAdapter.getItem(position));
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getContext(), ModifyLockDoorCardActivity.class, intent);
    });
  }

  private void initAddPwdView() {
    mBinding.rlAddPwd.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_ADD_PWD_ONLY, true);
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getContext(), AddLockPwdTouchActivity.class, intent);
    });
    mPwdListAdapter = new LockPwdListAdapter(getContext());
    mBinding.lvPwd.setAdapter(mPwdListAdapter);
    mBinding.lvPwd.setOnItemClickListener((parent, view, position, id) -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_LOCK_PWD, mPwdListAdapter.getItem(position));
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getContext(), ModifyLockPwdActivity.class, intent);
    });
  }

  private void initAddFingerprintView() {
    mBinding.rlAddFingerprint.setOnClickListener(v -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_ADD_FP_ONLY, true);
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getContext(), AddLockFpActivity.class, intent);
    });
    mFingerprintListAdapter = new FingerprintListAdapter(getContext());
    mBinding.lvFingerprint.setAdapter(mFingerprintListAdapter);
    mBinding.lvFingerprint.setOnItemClickListener((parent, view, position, id) -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_LOCK_FP, mFingerprintListAdapter.getItem(position));
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, getLock().getMac());
      mNavigator.navigateTo(getContext(), ModifyLockFpActivity.class, intent);
    });
  }

  @Override
  public void showFingerprints(List<GetFingerprintsResponse> fingerprints) {
    mFingerprintListAdapter.update(fingerprints);
  }

  @Override
  public void showPasswords(List<GetPasswordsResponse> passwords) {
    mPwdListAdapter.update(passwords);
  }

  @Override
  public void showNoFingerprints() {
    mFingerprintListAdapter.clear();
  }

  @Override
  public void showNoPasswords() {
    mPwdListAdapter.clear();
  }

  @Override
  public void showNoDoorCards() {
    mLockDoorCardListAdapter.clear();
  }

  @Override
  public void showDoorCards(List<GetDoorCardsResponse> responses) {
    mLockDoorCardListAdapter.update(responses);
  }

  @Override
  public void onResume() {
    super.onResume();
    mSecurityManagePresenter.getFingerprints(getLock().getDeviceSerialNo());
    mSecurityManagePresenter.getPasswords(getLock().getDeviceSerialNo());
    mSecurityManagePresenter.getDoorCards(getLock().getDeviceSerialNo());
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return ((LockMainActivity) getActivity()).getLock();
  }


}