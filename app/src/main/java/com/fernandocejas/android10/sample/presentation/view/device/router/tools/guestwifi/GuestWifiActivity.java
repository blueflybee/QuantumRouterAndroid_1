package com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityGuestWifiBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.req.SetGuestWifiSwitchRequest;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.SetGuestWifiSwitchResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 访客wifi
 *      version: 1.0
 * </pre>
 */

public class GuestWifiActivity extends BaseActivity implements IGetGuestSwitchView, ISetGuestWifiSwitchView {
  private ActivityGuestWifiBinding mBinding;
  private Boolean isHideWifi = false;
  private Boolean isGuestWifiVisiable = false;
  private Boolean isGetInfoRequest = false; //get请求不成功才显示断网视图

  @Inject
  GuestWifiSwitchPresenter mGetWifiSwitchPresenter;
  @Inject
  SetGuestWifiSwitchPresenter mSetGuestPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_guest_wifi);
    initSpecialAttentionTitleBar("访客wifi");
    initializeInjector();
    initPresenter();

    initData();

    mBinding.llGuestwifiOffline.setVisibility(View.GONE);
    mBinding.rlGuestwifiOnline.setVisibility(View.VISIBLE);
    getGuestSwitch();

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
    mGetWifiSwitchPresenter.setView(this);
    mSetGuestPresenter.setView(this);
  }

  private void initData() {
    mBinding.switchBtnWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
          isGuestWifiVisiable = true;
          mBinding.llGuestWifiSet.setVisibility(View.VISIBLE);
          mBinding.tvGuestWifiTitle.setText("0位客人正在使用网络");
        }else {
          isGuestWifiVisiable = false;
          mBinding.llGuestWifiSet.setVisibility(View.GONE);
          mBinding.tvGuestWifiTitle.setText("家里来客人了，能让访客短暂使用的访客WiFi");
        }
      }
    });

    mBinding.switchBtnHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
          isHideWifi = true;
        }else {
          isHideWifi = false;
        }
      }
    });

    mTitleBar.setRightAs("保存", new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        SetGuestWifiSwitchRequest bean = new SetGuestWifiSwitchRequest();
        bean.setName(getText(mBinding.etWifiName));

        if (isGuestWifiVisiable) {
          bean.setEnable(1);

          //开启的时候才校验是不是输入名称
          if(TextUtils.isEmpty(getText(mBinding.etWifiName))){
            Toast.makeText(GuestWifiActivity.this, "请输入wifi名称", Toast.LENGTH_SHORT).show();
            return;
          }

        } else {
          bean.setEnable(0);
        }

        if (isHideWifi) {
          bean.setIsHide(1);
        } else {
          bean.setIsHide(0);
        }

        setGuestSwitch(bean);
      }
    });

    mTitleBar.getRightBtn().setTextColor(getResources().getColor(R.color.white));

    watchEditTextNoClear(mBinding.etWifiName);//禁止表情输入
    //监听wifi名称设置
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setByteRange(new InputWatcher.InputByteRange(1, 32));

    watcher.addEt(mBinding.etWifiName, condition1);

  }

  private void getGuestSwitch() {
    isGetInfoRequest = true;
    mGetWifiSwitchPresenter.getGuestSwitch(GlobleConstant.getgDeviceId());
  }

  private void setGuestSwitch(SetGuestWifiSwitchRequest bean) {
    mSetGuestPresenter.setGuestSwitch(GlobleConstant.getgDeviceId(), bean);
  }

  /**
   * 获取访问wifi状态
   *
   * @param
   * @return
   */
  @Override
  public void getGuestWifiSwitch(GetGuestWifiSwitchResponse response) {
    if (response.getEnable() == 1) {
      mBinding.switchBtnWifi.setChecked(true);
      mBinding.llGuestWifiSet.setVisibility(View.VISIBLE);

      if (!"".equals(response.getName())) {
        mBinding.etWifiName.setText(response.getName());
        //当号码返回时光标放在最后
        Selection.setSelection(mBinding.etWifiName.getText(), mBinding.etWifiName.getText().length());
        //mBinding.etWifiName.setCursorVisible(false);//隐藏光标

      }
      mBinding.tvGuestWifiTitle.setText(response.getUserNum() + "位客人正在使用网络");
      if (response.getIsHide() == 1) {
        mBinding.switchBtnHide.setChecked(true);
      }

    } else {
      mBinding.switchBtnWifi.setChecked(false);
      mBinding.llGuestWifiSet.setVisibility(View.GONE);

      mBinding.tvGuestWifiTitle.setText("家里来客人了，能让访客短暂使用的访客WiFi");
    }

  }

  @Override
  public void setGuestWifiSwitch(SetGuestWifiSwitchResponse response) {
    Toast.makeText(this, "设置成功，但您的网络会暂时断开几分钟，请稍候...", Toast.LENGTH_SHORT).show();
    finish();
  }

  @Override
  public void onError(String message) {
    if(!TextUtils.isEmpty(message)){
      super.onError(message);
    }

    if(isGetInfoRequest){
      isGetInfoRequest = false;

      //网络异常
      mBinding.rlGuestwifiOnline.setVisibility(View.GONE);
      mBinding.llGuestwifiOffline.setVisibility(View.VISIBLE);
      mBinding.llGuestwifiOffline.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mBinding.llGuestwifiOffline.setVisibility(View.GONE);
          mBinding.rlGuestwifiOnline.setVisibility(View.VISIBLE);
          getGuestSwitch();
        }
      });
    }
  }
}
