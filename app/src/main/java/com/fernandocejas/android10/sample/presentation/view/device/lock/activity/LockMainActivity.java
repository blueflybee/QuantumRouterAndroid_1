/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.LockSettingFragment;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.LockStatusFragment;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.SecurityManageFragment;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import java.util.List;

public class LockMainActivity extends BaseActivity implements HasComponent<DeviceComponent> {
  private ActivityLockBinding mBinding;
  private DeviceComponent mLockComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock);
    initializeInjector();
    initPresenter();
    initView();

  }

  private void initPresenter() {
  }

  private void initView() {
    initTitleBar(getLock().getDeviceName());
    mBinding.vpLock.setAdapter(new LockMainActivity.MainLockPagerAdapter(getSupportFragmentManager()));
    mBinding.vpLock.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tlLockTab));
    mBinding.tlLockTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.vpLock));
    int lockPage = getLockPage();
    mBinding.vpLock.setCurrentItem(lockPage == -1 ? 0 : lockPage);
  }

  @Override
  public DeviceComponent getComponent() {
    return mLockComponent;
  }

  private void initializeInjector() {
    this.mLockComponent = DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build();
    this.mLockComponent.inject(this);
  }


  private int getLockPage() {
    return getIntent().getIntExtra(Navigator.EXTRA_LOCK_PAGE, -1);
  }

  public GetDevTreeResponse<List<DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
  }

  public void setNewLockName(String lockName) {
    initTitleBar(lockName);
  }

  private class MainLockPagerAdapter extends FragmentPagerAdapter {

    public MainLockPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return LockStatusFragment.newInstance();
        case 1:
          return SecurityManageFragment.newInstance();
        case 2:
          return LockSettingFragment.newInstance();
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tlLockTab.getTabCount();
    }
  }
}
