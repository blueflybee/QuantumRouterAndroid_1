package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAboutHardwareBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 关于硬件
 *      version: 1.0
 * </pre>
 */

public class AboutHardwareActivity extends BaseActivity {
  private ActivityAboutHardwareBinding mBinding;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_hardware);

    initView();
  }

  private void initView() {
    initTitleBar("关于硬件");

    mBinding.tvDevNo.setText(getLock().getDeviceSerialNo());
    mBinding.tvDevVersion.setText(getLock().getDeviceVersion());
    mBinding.tvDevModel.setText(getLock().getDeviceModel());
    mBinding.tvDevType.setText(getLock().getDeviceType());
    mBinding.tvDevName.setText(getLock().getDeviceName());
  }

  private GetDevTreeResponse<List<DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
  }

}
