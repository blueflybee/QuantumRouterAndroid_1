package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLiteGatewayInfoBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LiteGatewayInfoActivity extends BaseActivity {

  private static final String TAG = LiteGatewayInfoActivity.class.getSimpleName();
  public static final int REQUEST_CODE = 0;

  private ActivityLiteGatewayInfoBinding mBinding;
  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> mLiteGw;

//  @Inject
//  AddLockFpPresenter mAddLockFpPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lite_gateway_info);

//    initializeInjector();
//
//    initPresenter();

    initData();

    initView();

  }

//  private void initPresenter() {
//    mAddLockFpPresenter.setView(this);
//  }
//
//  private void initializeInjector() {
//    DaggerLockComponent.builder()
//        .applicationComponent(getApplicationComponent())
//        .activityModule(getActivityModule())
//        .lockModule(new LockModule())
//        .build()
//        .inject(this);
//  }

  private void initData() {
    mLiteGw = getLiteGw();
  }

  private void initView() {
    initTitleBar("网关信息");
    mBinding.tvGateName.setText(mLiteGw.getDeviceName());
    mBinding.tvGateId.setText(mLiteGw.getDeviceSerialNo());
  }

  public void modifyGateName(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_MODIFY_PROPERTY, getText(mBinding.tvGateName));
    intent.putExtra(Navigator.EXTRA_MODIFY_HINT, "请输入LITE网关名称");
    intent.putExtra(Navigator.EXTRA_ROUTER_ID, mLiteGw.getDeviceSerialNo());
    intent.setClass(getContext(), ModifyLiteGwNameActivity.class);
    startActivityForResult(intent, REQUEST_CODE);
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLiteGw() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> data =
        (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> ) getIntent().getSerializableExtra(Navigator.EXTRA_LITE_GATEWAY);
//    data.setDeviceList(null);
    return data == null ? new GetDevTreeResponse<>() : data;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      mBinding.tvGateName.setText(modifiedName(data));
    }
  }

  private String modifiedName(Intent intent) {
    return intent.getStringExtra(Navigator.EXTRA_FOR_RESULT_DATA);
  }

}
