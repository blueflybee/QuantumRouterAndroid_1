package com.fernandocejas.android10.sample.presentation.view.device.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySelectDevTypeBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CaptureActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.SearchLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SelectDevTypeActivity extends BaseActivity {
  private ActivitySelectDevTypeBinding mBinding;
  private final static int SCANNIN_GREQUEST_CODE = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_dev_type);

    initView();
  }

  private void initView() {
    initTitleBar("添加设备");
  }

  public void goAddLock(View view) {
    mNavigator.navigateTo(getContext(), SearchLockActivity.class);
  }

  public void goAddRouter(View view) {
    mNavigator.navigateTo(getContext(), AddRouterActivity.class);
  }

  public void goAddCatEye(View view) {
    GlobleConstant.setgDeviceType("2");//猫眼

    Intent intent = new Intent(this, CaptureActivity.class);
    startActivity(intent);
  }

  public void goAddCamera(View view) {
    GlobleConstant.setgDeviceType("3");//摄像机

    Intent intent = new Intent(this, CaptureActivity.class);
    startActivity(intent);
  }

  public void goAddLiteGateway(View view) {
    GlobleConstant.setgDeviceType(LiteGateway.DEVICE_TYPE_LITE_GATEWAY);
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_BIND_LITE_GATEWAY, new LiteGateway());
    mNavigator.navigateTo(getContext(), CaptureActivity.class, intent);
//    mNavigator.navigateTo(getContext(), LiteGatewayMainActivity.class);
//    mNavigator.navigateTo(getContext(), EsptouchDemoActivity.class);
  }
}
