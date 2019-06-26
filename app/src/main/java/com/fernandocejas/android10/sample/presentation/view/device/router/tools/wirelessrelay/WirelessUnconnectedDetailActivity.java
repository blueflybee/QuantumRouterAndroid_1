package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWirelessUnconnectDetailBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.GetWirelessListResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WirelessUnconnectedDetailActivity extends BaseActivity{
  private ActivityWirelessUnconnectDetailBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wireless_unconnect_detail);

    initData();

  }


  private void initData() {

    if(getDetails() != null){
      initTitleBar(getDetails().getSsid());

      mBinding.tvDetailMac.setText(getDetails().getMac());

      mBinding.tvDetailChangel.setText(getDetails().getChannel()+"");

      mBinding.tvDetailSafe.setText(getDetails().getEncrypt());

      if(getDetails().getPower()<50){
        mBinding.tvDetailPower.setText("弱");
      }else if((getDetails().getPower()>=50)&&(getDetails().getPower()<=75)){
        mBinding.tvDetailPower.setText("一般");
      }else{
        mBinding.tvDetailPower.setText("强");
      }

      if(getDetails().getMode() == 0){
        mBinding.tvDetailType.setText("2.4G");
      }else {
        mBinding.tvDetailType.setText("5G");
      }
    }

    mBinding.tvJoinNet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(WirelessUnconnectedDetailActivity.this,WirelessRelayPwdActivity.class);
        intent.putExtra("Detail",getDetails());
        startActivity(intent);
      }
    });

  }

  private GetWirelessListResponse.WirelessBean getDetails(){
    return (GetWirelessListResponse.WirelessBean)getIntent().getSerializableExtra("Detail");
  }

}
