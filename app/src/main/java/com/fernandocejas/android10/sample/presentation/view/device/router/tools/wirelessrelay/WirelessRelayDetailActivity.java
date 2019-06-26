package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWirelessRelayWifiDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.GetWifiDetailResponse;
import com.qtec.router.model.rsp.GetWirelessListResponse;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WirelessRelayDetailActivity extends BaseActivity implements GetWifiDetailView{
  private ActivityWirelessRelayWifiDetailBinding mBinding;

  @Inject
  GetWifiDetailPresenter mGetWifiDetailPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wireless_relay_wifi_detail);

    initializeInjector();

    initPresenter();

    initView();

    queryWifiStateRequest();

    mBinding.tvJoinNet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(WirelessRelayDetailActivity.this,WirelessRelayPwdActivity.class);
        GetWirelessListResponse.WirelessBean bean = new GetWirelessListResponse.WirelessBean();
        bean.setSsid(getDetails().getSsid());
        bean.setMode(getDetails().getMode());
        bean.setEncrypt(getDetails().getEncrypt());
        bean.setMac(getDetails().getMac());
        intent.putExtra("Detail",bean);

        startActivity(intent);
      }
    });

  }

  private void initView() {
    initTitleBar(getWifiSSid());
    refreshUi();
  }

  private String getWifiSSid() {
    return getIntent().getStringExtra(Navigator.EXTRA_WIRELESS_RELAY_WIFI_NAME);
  }

  private void initPresenter() {
    mGetWifiDetailPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void queryWifiStateRequest() {
    mGetWifiDetailPresenter.getWifiDetail(GlobleConstant.getgDeviceId());
  }


  @Override
  public void getWifiDetail(GetWifiDetailResponse responses) {

    getDetails().setNetmask(responses.getNetmask());
    getDetails().setIpaddr(responses.getIpaddr());
    getDetails().setGateway(responses.getGateway());
    getDetails().setDns(responses.getDns());

    refreshUi();

  }

  private ConnectedWifiBean getDetails(){
    return (ConnectedWifiBean)getIntent().getSerializableExtra("Detail");
  }

  private void refreshUi(){
    if(getDetails() != null){
      if(getDetails().getPower()<50){
        mBinding.tvDetailPower.setText("弱");
      }else if((getDetails().getPower()>=50)&&(getDetails().getPower()<=75)){
        mBinding.tvDetailPower.setText("一般");
      }else{
        mBinding.tvDetailPower.setText("强");
      }

      mBinding.tvDetailMac.setText(getDetails().getMac());
      mBinding.tvDetailChangel.setText(getDetails().getChannel()+"");
      mBinding.tvDetailSafe.setText(getDetails().getEncrypt());
      mBinding.tvDetailIp.setText(getDetails().getIpaddr());
      mBinding.tvDetailMask.setText(getDetails().getNetmask());
      mBinding.tvDetailGateway.setText(getDetails().getGateway());
      mBinding.tvDetailDns.setText(getDetails().getDns());
    }

  }

}
