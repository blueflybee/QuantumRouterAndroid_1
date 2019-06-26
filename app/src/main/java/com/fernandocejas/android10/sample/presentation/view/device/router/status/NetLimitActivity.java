package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityNetLimitBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class NetLimitActivity extends BaseActivity implements View.OnClickListener{
  private ActivityNetLimitBinding mBinding;
  private Boolean isNetLimit = false;

//    @Inject
//    IntelDeviceListPresenter mIntelDeviceListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_net_limit);
//    initializeInjector();

//        initPresenter();

    initView();
    initData();

  }

  private void initData() {
    mBinding.imgNetSwitch.setOnClickListener(this);
  }

  private void initView() {
    initTitleBar("网速限制");
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.img_netSwitch:
        if(isNetLimit){
          isNetLimit = false;
          mBinding.imgNetSwitch.setBackgroundResource(R.drawable.ic_off);
          mBinding.llLimitSpeed.setVisibility(View.GONE);
        }else {
          isNetLimit = true;
          mBinding.imgNetSwitch.setBackgroundResource(R.drawable.ic_open);
          mBinding.llLimitSpeed.setVisibility(View.VISIBLE);
        }
        break;

      default:
        break;
    }
  }

//  private void initPresenter() {
//    mIntelDeviceListPresenter.setView(this);
//  }

//  private void initializeInjector() {
//    DaggerRouterComponent.builder()
//        .applicationComponent(getApplicationComponent())
//        .activityModule(getActivityModule())
//        .routerModule(new RouterModule())
//        .build()
//        .inject(this);
//  }
//
//  @Override
//  public void showIntelDeviceList(List<IntelDeviceListResponse> response) {
//    mAdapter.notifyDataSetChanged(response);
//  }

//  @Override
//  protected void onRestart() {
//    super.onRestart();
//    queryIntelDeviceList();
//  }
}
