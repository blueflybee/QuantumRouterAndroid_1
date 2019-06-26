package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWirelessRelayBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.WirelessRelayWifisAdapter;
import com.qtec.router.model.rsp.GetConnectedWifiResponse;
import com.qtec.router.model.rsp.GetWirelessListResponse;
import com.qtec.router.model.rsp.SetWifiSwitchResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class WirelessRelayActivity extends BaseActivity implements GetWirelessListView, GetConnectedWifiView, SetWifiSwitchView {
  private ActivityWirelessRelayBinding mBinding;
  private Boolean isWirelessOn = false;
  private List<GetWirelessListResponse.WirelessBean> mWifiList;
  private ConnectedWifiBean mConnectedWifi;
  private WifiReloadPopupWindow mWifiReloadPopWin;
  private Boolean isConnecting = false;

  private int mRefreshCount = 0;

  private Handler mHandler = new Handler();

  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      queryConnectedWifi(); //5s刷一次,共刷180s,60次
      mRefreshCount++;
      System.out.println("wireless 请求:"+mRefreshCount+"次数");
      if(mHandler != null){
        mHandler.postDelayed(this, 4900);//每隔5s轮询一次
      }
    }
  };

  @Inject
  GetWirelessListPresenter mGetWirelessPresenter;
  @Inject
  GetConnectedWifiPresenter mGetConnectedWifiPresenter;
  @Inject
  SetWifiSwitchPresenter mSetWifiSwitchPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wireless_relay);

    initializeInjector();

    initPresenter();

    initView();

    mGetConnectedWifiPresenter.getConnectedWifi(GlobleConstant.getgDeviceId());

  }


  private void queryWirelessList() {
    mGetWirelessPresenter.getWirelessList(GlobleConstant.getgDeviceId());
  }

  private void queryConnectedWifi() {

    if(GlobleConstant.isTimeZero){
      if(mWifiReloadPopWin != null){

        removeHandler();

        isConnecting = false;
        mRefreshCount = 0;
        mWifiReloadPopWin.dismiss();
        mWifiReloadPopWin = null;

        GlobleConstant.isTimeZero = false;
      }
    }

    //判断网络是否通路
    if(NetworkUtils.isAvailableByPing()){
      System.out.println("无线中继 查询已连接的网络");
      mGetConnectedWifiPresenter.getConnectedWifi(GlobleConstant.getgDeviceId());
    }
  }

  private void removeHandler(){
    if(mHandler != null){
      if(mRunnable != null){
        mHandler.removeCallbacks(mRunnable);
        System.out.println("无线中继 移除handler");
        mHandler = null;
        mRunnable = null;
      }
    }
  }

  private void setWifiSwitchRequest(int enable) {
    mSetWifiSwitchPresenter.setWifiSwitch(GlobleConstant.getgDeviceId(), enable);
  }

  private void initView() {
    initTitleBar("无线中继");
    mWifiList = new ArrayList<>();
    mConnectedWifi = new ConnectedWifiBean();

    mBinding.imgWirelessRelay.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isWirelessOn) {
          setWifiSwitchRequest(0);
        } else {
          setWifiSwitchRequest(1);
        }
      }
    });
  }

  public void goDetail(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_WIRELESS_RELAY_WIFI_NAME, getText(mBinding.tvConnectName));
    intent.putExtra(Navigator.EXTRA_ROUTER_ID, GlobleConstant.getgDeviceId());
    intent.putExtra("Detail",mConnectedWifi);
    mNavigator.navigateTo(getContext(), WirelessRelayDetailActivity.class, intent);
  }

  public void goOther(View view) {
    Intent intent = new Intent(getContext(), WirelessRelayOtherActivity.class);
    intent.putExtra("WIFILIST",(Serializable) mWifiList);
    startActivityForResult(intent,55);
  }

  private void initPresenter() {
    mGetWirelessPresenter.setView(this);
    mGetConnectedWifiPresenter.setView(this);
    mSetWifiSwitchPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  /**
   * 获取发现的WiFi列表
   *
   * @param
   * @return
   */
  @Override
  public void getWirelessList(GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>> responses) {
    //移除已经连接的wifi
    mWifiList.clear();

    mWifiList.addAll(responses.getWifi());

    for (int i = 0; i < mWifiList.size(); i++) {
      if(TextUtils.isEmpty(mConnectedWifi.getMac())){
        break;
      }

      if (mConnectedWifi.getMac().equals(mWifiList.get(i).getMac())) {
        mConnectedWifi.setPower(mWifiList.get(i).getPower());
        mConnectedWifi.setChannel(mWifiList.get(i).getChannel());
        mConnectedWifi.setEncrypt(mWifiList.get(i).getEncrypt());
        mConnectedWifi.setMode(mWifiList.get(i).getMode());

        mWifiList.remove(i);
        break;
      }
    }

    if(mWifiList.size() > 0){
      WirelessRelayWifisAdapter adapter = new WirelessRelayWifisAdapter(getContext(), mWifiList);
      mBinding.lvWifi.setAdapter(adapter);

      adapter.setOnConnectClickListener(new WirelessRelayWifisAdapter.OnConnectClickListener() {
        @Override
        public void onConnectClick(int position) {
          /*GetWirelessListResponse.WirelessBean item = adapter.getItem(position);*/
          Intent intent = new Intent(getContext(), WirelessRelayPwdActivity.class);
          intent.putExtra("Detail", mWifiList.get(position));
          startActivityForResult(intent,55);
        }
      });

      adapter.setOnDetailClickListener(new WirelessRelayWifisAdapter.OnDetailClickListener() {
        @Override
        public void onDetailClick(int position) {
          Intent intent = new Intent();
          intent.putExtra("Detail",mWifiList.get(position));
          mNavigator.navigateTo(getContext(), WirelessUnconnectedDetailActivity.class, intent);
        }
      });
    }

  }

  /**
   * 获取已连接的wifi
   *
   * @param
   * @return
   */
  @Override
  public void getConnectedWifi(GetConnectedWifiResponse response) {
    System.out.println("wireless getConnectedWifi请求返回!");

    if (response.getEnable() == 1) {
      isWirelessOn = true;

      mBinding.imgWirelessRelay.setBackgroundResource(R.drawable.ic_open);
      mBinding.line1.setVisibility(View.VISIBLE);
      mBinding.tvSelectWifi.setVisibility(View.VISIBLE);
      mBinding.lvWifi.setVisibility(View.VISIBLE);
      mBinding.rlOther.setVisibility(View.VISIBLE);
      /*mBinding.rlAutoSwitch.setVisibility(View.VISIBLE);*/
      mBinding.tvAutoSwitchTips.setVisibility(View.VISIBLE);
      mBinding.tvWirelessRelayTips.setVisibility(View.GONE);

      if (TextUtils.isEmpty(response.getSsid())) {
        mBinding.rlConnected.setVisibility(View.GONE);
      } else {
        mBinding.rlConnected.setVisibility(View.VISIBLE);
        mBinding.tvConnectName.setText(response.getSsid());

        if (response.getStatus() == 1) {
          mBinding.tvConnectState.setText("已连接");

          if(mWifiReloadPopWin != null){
            isConnecting = false;
            mRefreshCount = 0;
            removeHandler();
            mWifiReloadPopWin.dismiss();
            mWifiReloadPopWin = null;
            //Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show();
          }
        } else {
          mBinding.tvConnectState.setText("未连接");

          if(mWifiReloadPopWin != null){
            if(mRefreshCount > 36 || GlobleConstant.isTimeZero){//300s
              removeHandler();
              isConnecting = false;
              mRefreshCount = 0;
              mWifiReloadPopWin.dismiss();
              mWifiReloadPopWin = null;
              Toast.makeText(this, "连接失败", Toast.LENGTH_SHORT).show();

              GlobleConstant.isTimeZero = false;
            }
          }
          
        }
      }

      mConnectedWifi.setMac(response.getMac());
      mConnectedWifi.setStatus(response.getStatus());
      mConnectedWifi.setSsid(response.getSsid());
      mConnectedWifi.setAuto_switch(response.getAuto_switch());
      mConnectedWifi.setEnable(response.getEnable());

      if (response.getAuto_switch() == 1) {
        mBinding.switchAuto.setChecked(true);
      } else {
        mBinding.switchAuto.setChecked(false);
      }

      if(isConnecting){
          //不查询
      }else {
        queryWirelessList();
      }

    } else {
      isWirelessOn = false;
      mBinding.imgWirelessRelay.setBackgroundResource(R.drawable.ic_off);
    }
  }

  /**
   * 设置wifi开关
   *
   * @param
   * @return
   */
  @Override
  public void setWifiSwitch(SetWifiSwitchResponse response) {
    if (isWirelessOn) {
      isWirelessOn = false;
      mBinding.imgWirelessRelay.setBackgroundResource(R.drawable.ic_off);
      mBinding.rlConnected.setVisibility(View.GONE);
    } else {
      isWirelessOn = true;
      mBinding.imgWirelessRelay.setBackgroundResource(R.drawable.ic_open);
    }

    mBinding.line1.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);
    /*mBinding.rlConnected.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);*/
    mBinding.tvSelectWifi.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);
    mBinding.lvWifi.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);
    mBinding.rlOther.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);
    /*mBinding.rlAutoSwitch.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);*/
    mBinding.tvAutoSwitchTips.setVisibility(isWirelessOn ? View.VISIBLE : View.GONE);
    mBinding.tvWirelessRelayTips.setVisibility(isWirelessOn ? View.GONE : View.VISIBLE);

    queryConnectedWifi();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(data != null){
      if(requestCode == 55){
        mWifiReloadPopWin = new WifiReloadPopupWindow(WirelessRelayActivity.this);

        mWifiReloadPopWin.showAtLocation(mWifiReloadPopWin.getOuterLayout(), Gravity.NO_GRAVITY,0,0);

        //开始每5s轮询
        if(mHandler != null && mRunnable != null){
          mHandler.postDelayed(mRunnable, 100);
        }

        isConnecting = true;
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if(isConnecting){
      if(keyCode == KeyEvent.KEYCODE_BACK){
        return true;
      }else{
        return super.onKeyDown(keyCode, event);
      }
    }else {
      return super.onKeyDown(keyCode,event);
    }

    /*return super.onKeyDown(keyCode, event);*/
  }

  @Override
  protected void onDestroy() {
   /* if(mRunnable != null){
      mHandler.removeCallbacks(mRunnable);
    }*/
    super.onDestroy();
    mGetWirelessPresenter.destroy();
    mGetConnectedWifiPresenter.destroy();
    mSetWifiSwitchPresenter.destroy();
  }

  @Override
  protected void onPause() {
   /* if(mRunnable != null){
      mHandler.removeCallbacks(mRunnable);
    }*/
    super.onPause();
  }

  @Override
  public void onError(String message) {
    System.out.println("无线中继 WirelessRelayActivity.onError");
  }
}
