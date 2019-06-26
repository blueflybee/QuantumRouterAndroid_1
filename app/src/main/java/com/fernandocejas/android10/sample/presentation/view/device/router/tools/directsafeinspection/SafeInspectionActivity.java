package com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySafeInspectionBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 一键体检
 *      version: 1.0
 * </pre>
 */

public class SafeInspectionActivity extends BaseActivity{
  private ActivitySafeInspectionBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_safe_inspection);
    initData();
  }

  private void initData() {
    mBinding.btnInspection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
        mNavigator.navigateTo(SafeInspectionActivity.this,SafeInspectionDetailActivity.class,intent);

      }
    });

    mBinding.rlBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
  }

}
