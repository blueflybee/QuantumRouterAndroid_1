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

package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAntiFritNetBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;


/**
* 防蹭网 主界面
*
* @param
* @return
*/
public class AntiFritNetMainActivity extends BaseActivity implements HasComponent<RouterComponent> {
  private ActivityAntiFritNetBinding mBinding;
  private RouterComponent mRouterComponent;
  private AuthedFragment authedFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_anti_frit_net);
    initializeInjector();
    initView();
  }

  private void initializeInjector() {
    this.mRouterComponent = DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build();
  }

  private void initView() {
    initTitleBar("防蹭网");
    mBinding.vpFirtNet.setAdapter(new AntiFritNetMainActivity.MainFritNetPagerAdapter(getSupportFragmentManager()));
    mBinding.vpFirtNet.addOnPageChangeListener(new MyTabListener(mBinding.tlFritTab));
    mBinding.tlFritTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.vpFirtNet));
    /*mBinding.vpFirtNet.setCurrentItem(0);*/
    setCurrentFragment(getFragmentPosition());
    System.out.println("FragmentPosition:"+getFragmentPosition());
  }

  private class MainFritNetPagerAdapter extends FragmentPagerAdapter {

    public MainFritNetPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return WaitingAuthFragment.newInstance();
        case 1:
          authedFragment = AuthedFragment.newInstance();
          return authedFragment;
        case 2:
          return SetNetModeFragment.newInstance();
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tlFritTab.getTabCount();
    }
  }

  @Override
  public RouterComponent getComponent() {
    return mRouterComponent;
  }

  /**
  * 跳转到哪Fragment
  *
  * @param
  * @return
  */
  private void setCurrentFragment(int index){
    mBinding.vpFirtNet.setCurrentItem(index);
  }

  private int getFragmentPosition(){
    return (int)getIntent().getIntExtra("FragmentPosition",0);
  }

  private class MyTabListener extends TabLayout.TabLayoutOnPageChangeListener{


    public MyTabListener(TabLayout tabLayout) {
      super(tabLayout);
    }

    @Override
    public void onPageSelected(int position) {
      super.onPageSelected(position);
      System.out.println("position = " + position);
      if (position == 1 && authedFragment != null) {
        authedFragment.refreshAuthedListRequest();
      }
    }
  }




}
