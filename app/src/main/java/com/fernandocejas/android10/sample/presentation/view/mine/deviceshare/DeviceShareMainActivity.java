package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityDeviceShareListBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/09/22
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class DeviceShareMainActivity extends BaseActivity {
  private ActivityDeviceShareListBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_share_list);
    initView();
  }

  private void initView() {
    initTitleBar("设备共享");
    mBinding.vpDeviceShare.setAdapter(new DeviceShareListPagerAdapter(getSupportFragmentManager()));
    mBinding.vpDeviceShare.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tlDeviceShareTab));
    mBinding.tlDeviceShareTab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.vpDeviceShare));
    mBinding.vpDeviceShare.setCurrentItem(0);
  }

  private class DeviceShareListPagerAdapter extends FragmentPagerAdapter {

    public DeviceShareListPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return RouterShareListFragment.newInstance();
        case 1:
          return LockShareListFragment.newInstance();

        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tlDeviceShareTab.getTabCount();
    }
  }

}
