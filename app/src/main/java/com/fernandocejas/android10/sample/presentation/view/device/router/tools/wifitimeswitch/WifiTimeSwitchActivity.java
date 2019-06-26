package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWifiTimeSwitchBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.WifiTimeIntervalListAdapter;
import com.qtec.router.model.req.SetWifiAllSwitchRequest;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;
import com.qtec.router.model.rsp.SetWifiAllSwitchResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: wifi定时开关
 *      version: 1.0
 * </pre>
 */

public class WifiTimeSwitchActivity extends BaseActivity implements View.OnClickListener, IGetWifiTimeConfigView, ISetWifiAllSwitchView {
  private ActivityWifiTimeSwitchBinding mBinding;
  private Boolean isWifiSwitch = false;//判断总开关切换之前的状态
  private WifiTimeIntervalListAdapter adapter;
  private List<GetWifiTimeConfigResponse.WifiTimeConfig> timeIntervalList;
  private Boolean isSumSwitch = false;//判断是否是总开关关闭
  private Boolean isGetInfoRequest = false; //get请求不成功才显示断网视图

  @Inject
  GetWifiTimeConfigPresenter mWifiTimeConfigPresenter;
  @Inject
  SetWifiAllSwitchPresenter mSetWifiAllSwitchPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wifi_time_switch);
    initSpecialAttentionTitleBar("WIFI定时开关");

    initializeInjector();
    initPresenter();
    initData();

    mBinding.scrollView.setVisibility(View.VISIBLE);
    mBinding.llWifiOffline.setVisibility(View.GONE);
    getWifiTimeConfig();

  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mWifiTimeConfigPresenter.setView(this);
    mSetWifiAllSwitchPresenter.setView(this);
  }

  private void getWifiTimeConfig() {
    isGetInfoRequest = true;
    mWifiTimeConfigPresenter.getWifiTimeConfig(GlobleConstant.getgDeviceId());
  }

  private void setWifiAllSwitchRequest(int flag) {
    SetWifiAllSwitchRequest<List<SetWifiAllSwitchRequest.WifiSwitchConfig>> data = new SetWifiAllSwitchRequest<List<SetWifiAllSwitchRequest.WifiSwitchConfig>>();
    data.setEnable(flag);

    List<SetWifiAllSwitchRequest.WifiSwitchConfig> beans = new ArrayList<SetWifiAllSwitchRequest.WifiSwitchConfig>();
    for (int i = 0; i < timeIntervalList.size(); i++) {
      SetWifiAllSwitchRequest.WifiSwitchConfig bean = new SetWifiAllSwitchRequest.WifiSwitchConfig();
      bean.setId(timeIntervalList.get(i).getId());
      bean.setRule_enable(timeIntervalList.get(i).getRule_enable());
      beans.add(bean);
    }
    data.setRules(beans);

    mSetWifiAllSwitchPresenter.setWifiAllSwitch(GlobleConstant.getgDeviceId(),data);
  }

  private void initData() {
    timeIntervalList = new ArrayList<>();
    mBinding.rlAddTimeInterval.setOnClickListener(this);

    mBinding.switchWifiBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        isSumSwitch = true;
        if (isWifiSwitch) {
          setWifiAllSwitchRequest(0);
        } else {
          setWifiAllSwitchRequest(1);
        }
      }
    });
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      case R.id.rl_add_time_interval:
        Intent intent = new Intent(this, AddTimeIntervalActivity.class);
        intent.putExtra("FLAG","0");//0 新增
        startActivityForResult(intent,3);
        break;

      default:
        break;
    }
  }

  @Override
  public void getWifiTimeConfig(GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>> response) {

    if (response.getEnable() == 1) {
      isWifiSwitch = true;
      mBinding.switchWifiBtn.setChecked(true);

      if(response.getRules().size() == 0){
        mBinding.rlAddTimeInterval.setVisibility(View.VISIBLE);
        /*mBinding.btnFinishWifi.setVisibility(View.GONE);*/
        mBinding.llWifiTimeInterval.setVisibility(View.GONE);
        mTitleBar.getRightBtn().setVisibility(View.GONE);
      }else{
        mBinding.rlAddTimeInterval.setVisibility(View.GONE);
        mBinding.llWifiTimeInterval.setVisibility(View.VISIBLE);
        /*mBinding.btnFinishWifi.setVisibility(View.VISIBLE);*/
        mTitleBar.getRightBtn().setVisibility(View.VISIBLE);


        for (int i = 0; i < response.getRules().size(); i++) {
          GetWifiTimeConfigResponse.WifiTimeConfig bean = new GetWifiTimeConfigResponse.WifiTimeConfig();
          bean.setName(response.getRules().get(i).getName());
          bean.setId(response.getRules().get(i).getId());
          bean.setRule_enable(response.getRules().get(i).getRule_enable());
          bean.setStart_hour(response.getRules().get(i).getStart_hour());
          bean.setStart_min(response.getRules().get(i).getStart_min());
          bean.setStop_hour(response.getRules().get(i).getStop_hour());
          bean.setStop_min(response.getRules().get(i).getStop_min());
          bean.setWeek_day(response.getRules().get(i).getWeek_day());
          timeIntervalList.add(bean);
        }

        adapter = new WifiTimeIntervalListAdapter(this, timeIntervalList, R.layout.item_add_time_interval);
        mBinding.lvTimeInterval.setAdapter(adapter);

        adapter.setOnDetailClickListener(new WifiTimeIntervalListAdapter.OnDetailClickListener() {
          @Override
          public void onDetailClick(int position) {
            Intent intent = new Intent(WifiTimeSwitchActivity.this, AddTimeIntervalActivity.class);
            intent.putExtra("DETAIL",response.getRules().get(position));
            intent.putExtra("FLAG","1");
            startActivityForResult(intent,3);
          }
        });

        adapter.setSwitchClickListener(new WifiTimeIntervalListAdapter.OnSetSwitchClickListener() {
          @Override
          public void onSetSwitchClick(int position, Boolean flag) {
            if(flag){
              timeIntervalList.get(position).setRule_enable(1);
            }else{
              timeIntervalList.get(position).setRule_enable(0);
            }

            isSumSwitch = false;
            setWifiAllSwitchRequest(1);

          }
        });

     /*   mBinding.scrollView.addOnScrollChangedListener(new ScrollViewForMove.onScrollChangedListener() {
          @Override
          public void onScrollChanged(int x, int y, int oldx, int oldy) {
        *//*    if(y<0){
              //未滑动
              mBinding.btnFinishWifi.setVisibility(View.VISIBLE);
            }else {
              mBinding.btnFinishWifi.setVisibility(View.GONE);
            }*//*

          }
        });*/

        /*mBinding.scrollView.setOnTouchListener();*/




        mTitleBar.setRightAs(R.drawable.ic_add_white_20dp, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(WifiTimeSwitchActivity.this, AddTimeIntervalActivity.class);
            intent.putExtra("FLAG","0");//true表示新增
            startActivityForResult(intent,3);
          }
        });

    /*    mBinding.btnFinishWifi.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            isSumSwitch = false;
            setWifiAllSwitchRequest(1);
          }
        });*/
      }
     // mBinding.rlAddTimeInterval.setVisibility(View.GONE);

    } else {
      isWifiSwitch = false;
      mBinding.switchWifiBtn.setChecked(false);
      mBinding.llWifiTimeInterval.setVisibility(View.GONE);
      mBinding.rlAddTimeInterval.setVisibility(View.GONE);
    }

  }

  @Override
  public void onError(String message) {
    if(!TextUtils.isEmpty(message)){
      super.onError(message);
    }

    if(isGetInfoRequest){
      isGetInfoRequest = false;

      //网络异常
      mBinding.scrollView.setVisibility(View.GONE);
      mBinding.llWifiOffline.setVisibility(View.VISIBLE);
      findViewById(R.id.ll_offline).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mBinding.scrollView.setVisibility(View.VISIBLE);
          mBinding.llWifiOffline.setVisibility(View.GONE);
          getWifiTimeConfig();
        }
      });
    }

  }

  @Override
  public void setWifiAllSwitch(SetWifiAllSwitchResponse response) {

    if(isSumSwitch){
      Toast.makeText(this, "操作成功", Toast.LENGTH_SHORT).show();
      if (isWifiSwitch) {
        isWifiSwitch = false;
        mBinding.switchWifiBtn.setChecked(false);
        mBinding.llWifiTimeInterval.setVisibility(View.GONE);
        /*mBinding.btnFinishWifi.setVisibility(View.GONE);*/
        mBinding.rlAddTimeInterval.setVisibility(View.GONE);
        mTitleBar.getRightBtn().setVisibility(View.GONE);
      } else {
        isWifiSwitch = true;
        mBinding.switchWifiBtn.setChecked(true);
       // mBinding.llWifiTimeInterval.setVisibility(View.VISIBLE);
        timeIntervalList.clear();
        getWifiTimeConfig();
      }
    }else{
      Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
      timeIntervalList.clear();
      getWifiTimeConfig();
    }

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == 3){
      timeIntervalList.clear();
      getWifiTimeConfig();
    }

  }
}
