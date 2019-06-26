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

package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFirstConfigBinding;
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

public class FirstConfigNetModeActivity extends BaseActivity implements HasComponent<RouterComponent> {
  private ActivityFirstConfigBinding mBinding;
  private RouterComponent mRouterComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_first_config);

    initializeInjector();

    initView();

  }

  private void initView() {
    initTitleBar("选择上网方式");
    mBinding.vpRouter.setAdapter(new FirstConfigNetModeActivity.MainRouterPagerAdapter(getSupportFragmentManager()));
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
    mNavigator.navigateTo(getContext(), SearchRouterActivity.class);
  }

  private int getStartFragmentIndex() {
    return getIntent() == null ? 0 : getIntent().getIntExtra(Navigator.EXTRA_TO_FRAGMENT, 0);
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
          return FirstConfigPPPOEFragment.newInstance();
        case 1:
          return FirstConfigDHCPFragment.newInstance();
        case 2:
          return FirstConfigStaticFragment.newInstance();
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tlRouterTab.getTabCount();
    }
  }

}
