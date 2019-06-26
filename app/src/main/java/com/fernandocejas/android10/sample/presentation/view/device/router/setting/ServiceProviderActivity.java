package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityServiceProviderBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.adapter.ServiceProviderAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.ServiceProvider;

import java.util.ArrayList;
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
public class ServiceProviderActivity extends BaseActivity {


  private static final String TAG = ServiceProviderActivity.class.getName();
  public static final int PERMISSION_REQUEST_CODE = 2;
  private ActivityServiceProviderBinding mBinding;

//    @Inject
//    IntelDeviceListPresenter mStaticIPPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_service_provider);

//    initializeInjector();
//
//        initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("联系运营商");

    ServiceProviderAdapter adapter = new ServiceProviderAdapter(getContext(), serviceProviders());
    mBinding.lvSp.setAdapter(adapter);
    mBinding.lvSp.setOnItemClickListener((parent, view, position, id) -> {
      //    //所要申请的权限
      String[] perms = {Manifest.permission.CALL_PHONE};
      if (hasPermission(perms)) {
        ServiceProvider sp = adapter.getItem(position);
        DialogUtil.showDiaDialogWithMessage(getContext(), sp.getTel(), v -> {

          Intent callIntent = IntentUtils.getCallIntent(sp.getTel());
          startActivity(callIntent);
        }, null);
      } else {
        requestPermissions();
      }

    });
  }

  private void requestPermissions() {
    String[] perms = {Manifest.permission.CALL_PHONE};
    PermissionUtils.requestPermissions(getContext(), PERMISSION_REQUEST_CODE, perms, new PermissionUtils.OnPermissionListener() {
      @Override
      public void onPermissionGranted() {
        Log.i(TAG, "允许权限");
      }

      @Override
      public void onPermissionDenied(String[] deniedPermissions) {
        Log.i(TAG, "拒绝权限");
      }
    });
  }

  private List<ServiceProvider> serviceProviders() {
    List<ServiceProvider> sps = new ArrayList<>();
    sps.add(new ServiceProvider("中国电信宽带客服", "10000"));
    sps.add(new ServiceProvider("中国联通宽带客服", "10010"));
    sps.add(new ServiceProvider("中国移动宽带客服", "10086"));
    sps.add(new ServiceProvider("歌华有限客服", "96196"));
    sps.add(new ServiceProvider("艾普宽带客服", "952155"));
    sps.add(new ServiceProvider("有线通客服", "96877"));
    sps.add(new ServiceProvider("宽带通客服", "96007"));
    sps.add(new ServiceProvider("长城宽带客服", "95097"));
    sps.add(new ServiceProvider("铁通宽带客服", "10050"));
    return sps;
  }


//  private void initPresenter() {
//    mStaticIPPresenter.setView(this);
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
