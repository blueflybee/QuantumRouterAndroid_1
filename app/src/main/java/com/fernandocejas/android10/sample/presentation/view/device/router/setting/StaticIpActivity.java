package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityStaticIpBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.rsp.GetNetModeResponse;
import com.qtec.router.model.rsp.SetStaticIPResponse;

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
public class StaticIpActivity extends BaseActivity implements StaticIPView {
  private ActivityStaticIpBinding mBinding;

  @Inject
  StaticIPPresenter mStaticIPPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_static_ip);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("静态IP");
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition ipCondition = new InputWatcher.WatchCondition();
    ipCondition.setInputRegular(new InputWatcher.InputRegular(InputUtil.REGULAR_IP));

    InputWatcher.WatchCondition maskCondition = new InputWatcher.WatchCondition();
    maskCondition.setInputRegular(new InputWatcher.InputRegular(InputUtil.REGULAR_MASK));

    watcher.addEt(mBinding.etIp, ipCondition);
    watcher.addEt(mBinding.etSubnetMask, maskCondition);
    watcher.addEt(mBinding.etGateway, ipCondition);
    watcher.addEt(mBinding.etFirstDns, ipCondition);
    watcher.addEt(mBinding.etSecondDns, ipCondition);

    watcher.setInputListener(isEmpty -> {
      mBinding.btnConfirm.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnConfirm.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnConfirm.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    mBinding.setNetModeResponse(getNetModeData());

  }

  public void confirm(View view) {

    /**
     *  校验字段：
     *  静态ip上网设置的ip掩码网关，三个变量校验原理：
     *  ip和掩码每段转二进制后的与要等于掩码和网关每段转二进制后的与相等，
     *  如果不相等提示 默认网关不在由ip地址和子网掩码定义的同一网络段上
     */
    String[] ipStr = getText(mBinding.etIp).split("[.]");
    String[] maskStr = getText(mBinding.etSubnetMask).split("[.]");
    String[] gateWayStr = getText(mBinding.etGateway).split("[.]");

    int count = 0;//统计网段相同的数目

    if((Integer.parseInt(ipStr[0]) & Integer.parseInt(maskStr[0]))
         == (Integer.parseInt(gateWayStr[0]) & Integer.parseInt(maskStr[0]))){
      count++;
    }
    if((Integer.parseInt(ipStr[1]) & Integer.parseInt(maskStr[1]))
         == (Integer.parseInt(gateWayStr[1]) & Integer.parseInt(maskStr[1]))){
      count++;
    }
    if((Integer.parseInt(ipStr[2]) & Integer.parseInt(maskStr[2]))
         == (Integer.parseInt(gateWayStr[2]) & Integer.parseInt(maskStr[2]))){
      count++;
    }
    if((Integer.parseInt(ipStr[3]) & Integer.parseInt(maskStr[3]))
         == (Integer.parseInt(gateWayStr[3]) & Integer.parseInt(maskStr[3]))){
      count++;
    }

    if(count != 4){
      //网段不同
      count = 0;
      Toast.makeText(this, "默认网关不在由ip地址和子网掩码定义的同一网络段上", Toast.LENGTH_SHORT).show();
      return;
    }

    mStaticIPPresenter.setStaticIp(
        getText(mBinding.etIp),
        getText(mBinding.etSubnetMask),
        getText(mBinding.etGateway),
        getText(mBinding.etFirstDns),
        getText(mBinding.etSecondDns), 1);
  }

  private GetNetModeResponse getNetModeData() {
    Intent intent = getIntent();
    if (intent == null) return new GetNetModeResponse();
    GetNetModeResponse extra = (GetNetModeResponse) intent.getSerializableExtra(Navigator.EXTRA_NET_MODE_DATA);
    if (extra == null) return new GetNetModeResponse();
    return extra;
  }

  @Override
  public void showSetStaticIpSuccess(SetStaticIPResponse response) {
    ToastUtils.showShort("设置上网方式为静态IP");
    finish();
  }

  private void initPresenter() {
    mStaticIPPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

}
