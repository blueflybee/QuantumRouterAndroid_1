package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterMemDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.req.RemoveBlackMemRequest;
import com.qtec.router.model.rsp.RemoveBlackMemResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 网关成员详情  加入黑名单复用移除黑名单的接口
 *      version: 1.0
 * </pre>
 */

public class RouterMemDetailActivity extends BaseActivity implements View.OnClickListener, AddBlackMemView {
  private ActivityRouterMemDetailBinding mBinding;
  private Boolean isOnlinePrompt = false, isOfflinePrompt = false;
  @Inject
  AddBlackMemPresenter mAddBlackMemPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_router_mem_detail);
    initTitleBar("成员详情");

    initializeInjector();
    initPresenter();
    initView();

    initData();
  }

  private void addMemRequest(RemoveBlackMemRequest bean) {
    mAddBlackMemPresenter.addMem(getRouterId(), bean);
  }

  private String getRouterId() {
    return getIntent().getStringExtra(Navigator.EXTRA_ROUTER_ID);
  }

  private void initPresenter() {
    mAddBlackMemPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  private void initView() {
    mBinding.rlDeviceName.setOnClickListener(this);
    mBinding.rlNetLimitSpeed.setOnClickListener(this);
    mBinding.rlOnlineTimeLimit.setOnClickListener(this);
    mBinding.imgOnline.setOnClickListener(this);
    mBinding.imgOffline.setOnClickListener(this);
    mBinding.btnAddBlackList.setOnClickListener(this);
  }

  private void initData() {


    switch (getDetails().getDevicetype()) {
      case 0:
        //computer
        mBinding.imgPhone.setBackground(getResources().getDrawable(R.drawable.ic_mac_blue));
        mBinding.tvDeviceType.setText("computer");
        break;

      case 1:
        //android
        mBinding.imgPhone.setBackground(getResources().getDrawable(R.drawable.ic_phone));
        mBinding.tvDeviceType.setText("android");
        break;

      case 2:
        // iphone
        mBinding.imgPhone.setBackground(getResources().getDrawable(R.drawable.ic_phone));
        mBinding.tvDeviceType.setText("iphone");
        break;

      default:
        mBinding.imgPhone.setBackground(getResources().getDrawable(R.drawable.ic_phone));
        mBinding.tvDeviceType.setText("未知");
        break;
    }

    String tx = String.format("%.2f", Float.parseFloat((getDetails().getTx() * 1f / 1024) + "")) + " KB/s";
    String rx = String.format("%.2f", Float.parseFloat((getDetails().getRx() * 1f / 1024) + "")) + " KB/s";

    mBinding.tvSpeed.setText("上行:" + tx + " " + "下行:" + rx);

    //在线时长:显示时分
    mBinding.tvOnlineTime.setText(convertTimeToHourAndMinute(getDetails().getStastatus()));

    if (!TextUtils.isEmpty(getDetails().getStaname())) {
      mBinding.tvDeviceRemark.setText(getDetails().getStaname());
    } else {
      mBinding.tvDeviceRemark.setText("unknown");
    }

    try{
      if ((!TextUtils.isEmpty(AppConstant.getMacAddress(this)))&&(!TextUtils.isEmpty(getDetails().getMacaddr()))) {
        if (AppConstant.getMacAddress(this).equalsIgnoreCase(getDetails().getMacaddr())) {
          mBinding.btnAddBlackList.setVisibility(View.GONE);
        } else {
          mBinding.btnAddBlackList.setVisibility(View.VISIBLE);
        }
      }else {
        mBinding.btnAddBlackList.setVisibility(View.VISIBLE);
      }
    }catch (Exception e){
      e.printStackTrace();
    }

  }

  /**
   * 显示时间为时分
   *
   * @param
   * @return
   */
  private String convertTimeToHourAndMinute(int status) {
    int mHour = 0;
    int mMinute = 0;

    if (getDetails().getStastatus() < 60) {
      mHour = 0;
      mMinute = 0;
    } else {
      mMinute = getDetails().getStastatus() / 60;
    }

    if (mMinute > 60) {
      mHour = mMinute / 60;
      mMinute = mMinute % 60;
    }
    return mHour + "时" + mMinute + "分钟";
  }

  private String convertSizeToMGB(int size) {
    String result = "";
    if (getDetails().getTx() < 1024) {
      result = size + "kb/s";
    } else if (getDetails().getTx() < 1024 * 1024) {
      result = (size / 1024) + "Mb/s";
    } else {
      result = (size / 1024 / 1024) + "G/s";
    }
    return result;
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_deviceName:
        // TODO: 2017/9/14 暂时屏蔽
                /*mNavigator.navigateTo(this,DeviceNameActivity.class,null);*/
        break;

      case R.id.rl_netLimitSpeed:
        mNavigator.navigateTo(this, NetLimitActivity.class, null);
        break;

      case R.id.rl_onlineTimeLimit:
        mNavigator.navigateTo(this, TimeLimitActivity.class, null);
        break;

      case R.id.img_online:
        if (isOnlinePrompt) {
          isOnlinePrompt = false;
          mBinding.imgOnline.setBackgroundResource(R.drawable.ic_off);
        } else {
          isOnlinePrompt = true;
          mBinding.imgOnline.setBackgroundResource(R.drawable.ic_open);
        }

        break;

      case R.id.img_offline:
        if (isOfflinePrompt) {
          isOfflinePrompt = false;
          mBinding.imgOffline.setBackgroundResource(R.drawable.ic_off);
        } else {
          isOfflinePrompt = true;
          mBinding.imgOffline.setBackgroundResource(R.drawable.ic_open);
        }

        break;

      case R.id.btn_add_blackList:
        //加入黑名单：复用移除黑名单的response
        RemoveBlackMemRequest bean = new RemoveBlackMemRequest();
        bean.setEnabled(1);
        bean.setMacaddr(getDetails().getMacaddr());
        bean.setName(getDetails().getStaname());
        addMemRequest(bean);
        break;

      default:
        break;
    }
  }

  private RouterStatusResponse.Status getDetails() {
    return (RouterStatusResponse.Status) getIntent().getSerializableExtra("Detail");
  }

  @Override
  public void addBlackMem(RemoveBlackMemResponse response) {
    Toast.makeText(this, "拉黑成功", Toast.LENGTH_SHORT).show();
    finish();
  }
}
