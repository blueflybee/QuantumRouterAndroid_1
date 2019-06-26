

package com.fernandocejas.android10.sample.presentation.view.login.welcome;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWelcomeBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.fragment.DeviceFragment;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.MineFragment;
import com.fernandocejas.android10.sample.presentation.view.safe.SafeFragment;

/**
 * 首次启动引导页面
 */
public class WelcomeActivity extends BaseActivity {

  private static final String TAG = WelcomeActivity.class.getName();


  private ActivityWelcomeBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    checkFirstLaunch();
  }

  private void checkFirstLaunch() {
    int nowVersionCode = AppUtils.getAppVersionCode();
    int savedVersionCode = PrefConstant.getAppVersionCode();

    if (nowVersionCode > savedVersionCode) {
      mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
      PrefConstant.saveAppVersionCode(nowVersionCode);

      initView();
//      ToastUtils.showShort("首次启动");
    } else {
      mNavigator.navigateTo(getContext(), LoginActivity.class);
      finish();
    }
  }

  private void initView() {
    mBinding.viewpager.setAdapter(new WelcomeViewPagerAdapter(getSupportFragmentManager()));
    mBinding.viewpager.addOnPageChangeListener(new WelcomeOnPageChangeListener(mBinding.tablayout));
    mBinding.tablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewpager));

    mBinding.tablayout.setSmoothScrollingEnabled(true);
    mBinding.viewpager.setCurrentItem(0);
  }

  private class WelcomeViewPagerAdapter extends FragmentPagerAdapter {

    WelcomeViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return FirstViewFragment.newInstance();
        case 1:
          return SecondViewFragment.newInstance();
        case 2:
          return ThirdViewFragment.newInstance();
        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return mBinding.tablayout.getTabCount();
    }
  }

  private class WelcomeOnPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener{

    public WelcomeOnPageChangeListener(TabLayout tabLayout) {
      super(tabLayout);
    }

    @Override
    public void onPageSelected(int position) {
      super.onPageSelected(position);
      if (position == 2) {
        mBinding.tablayout.setVisibility(View.INVISIBLE);
      } else {
        mBinding.tablayout.setVisibility(View.VISIBLE);
      }
    }
  }



}
