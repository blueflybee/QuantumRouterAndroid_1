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

package com.fernandocejas.android10.sample.presentation.view.device.router;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.SearchRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterSettingFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.status.RouterStatusFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.RouterToolsFragment;

import org.bouncycastle.voms.VOMSAttribute;

public class RouterMainActivity extends BaseActivity implements HasComponent<RouterComponent> {
  private ActivityRouterBinding mBinding;
  private RouterComponent mRouterComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_router);

    initializeInjector();

    initView();

  }

  private void initView() {
    initTitleBar(getRouterName());
    mBinding.vpRouter.setAdapter(new RouterMainActivity.MainRouterPagerAdapter(getSupportFragmentManager()));
    mBinding.vpRouter.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tlRouterTab));
    mBinding.tlRouterTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.vpRouter));
    mBinding.vpRouter.setCurrentItem(getStartFragmentIndex());

  }

  private void initializeInjector() {
    this.mRouterComponent = DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build();
  }

  public void addDevice(View view) {
    mNavigator.navigateTo(getContext(), SearchRouterActivity.class, null);
  }

  private int getStartFragmentIndex() {
    return getIntent() == null ? 0 : getIntent().getIntExtra(Navigator.EXTRA_TO_FRAGMENT, 0);
  }

  public void scrollToPage(int page) {
    mBinding.vpRouter.setCurrentItem(page);
  }

  @Override
  public RouterComponent getComponent() {
    return mRouterComponent;
  }

  private class MainRouterPagerAdapter extends FragmentPagerAdapter {

    MainRouterPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return RouterStatusFragment.newInstance();
        case 1:
          return RouterToolsFragment.newInstance();
        case 2:
          return RouterSettingFragment.newInstance();
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tlRouterTab.getTabCount();
    }
  }

  private String getRouterName() {
    return getIntent().getStringExtra(Navigator.EXTRA_ROUTER_NAME);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    String newRouterName = intent.getStringExtra(Navigator.EXTRA_NEW_ROUTER_NAME);
    if (TextUtils.isEmpty(newRouterName)) return;
    mTitleBar.setTitleCenter(newRouterName);
    getIntent().putExtra(Navigator.EXTRA_ROUTER_NAME, newRouterName);
  }
}
