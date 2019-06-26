package com.fernandocejas.android10.sample.presentation.view.device.router.tools.firewall;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityFireWallBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityGuestWifiBinding;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 防火墙
 *      version: 1.0
 * </pre>
 */

public class FireWallMainActivity extends BaseActivity implements View.OnClickListener {
  private ActivityFireWallBinding mBinding;
  Boolean isPwdWallOn = false,isWifiWallOn = false,isPhoneWallOn = false,isShoppingWallOn = false,isInternetWallOn = false, isAttentionWallOn = false;

  //IGetAgreementView
   /* @Inject
    GetAgreementPresenter mGetAgreementPresenter;*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_fire_wall);
    initSpecialAttentionTitleBar("网关防火墙");
       /* initializeInjector();
        initPresenter();*/
    initData();
  }

  private void initializeInjector() {
      /*  DaggerRouterComponent.builder()
            .applicationComponent(getApplicationComponent())
            .activityModule(getActivityModule())
            .routerModule(new RouterModule())
            .build()
            .inject(this);*/
  }
/*
    private void initPresenter() {
        mGetAgreementPresenter.setView(this);
    }*/

  private void initData() {
    mBinding.imgPwdWall.setOnClickListener(this);
    mBinding.imgWifiWall.setOnClickListener(this);
    mBinding.imgPhoneWall.setOnClickListener(this);
    mBinding.imgShoppingWall.setOnClickListener(this);
    mBinding.imgInternetWall.setOnClickListener(this);
    mBinding.imgOnlineWall.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
        case R.id.img_pwd_wall:
          if(isPwdWallOn){
            isPwdWallOn = false;
            mBinding.imgPwdWall.setBackgroundResource(R.drawable.ic_off);
          }else {
            isPwdWallOn = true;
            mBinding.imgPwdWall.setBackgroundResource(R.drawable.ic_open);
          }
          break;
        case R.id.img_wifi_wall:
          if(isWifiWallOn){
            isWifiWallOn = false;
            mBinding.imgWifiWall.setBackgroundResource(R.drawable.ic_off);
          }else {
            isWifiWallOn = true;
            mBinding.imgWifiWall.setBackgroundResource(R.drawable.ic_open);
          }
          break;
        case R.id.img_phone_wall:
          if(isPhoneWallOn){
            isPhoneWallOn = false;
            mBinding.imgPhoneWall.setBackgroundResource(R.drawable.ic_off);
          }else {
            isPhoneWallOn = true;
            mBinding.imgPhoneWall.setBackgroundResource(R.drawable.ic_open);
          }
          break;
        case R.id.img_shopping_wall:
          if(isShoppingWallOn){
            isShoppingWallOn = false;
            mBinding.imgShoppingWall.setBackgroundResource(R.drawable.ic_off);
          }else {
            isShoppingWallOn = true;
            mBinding.imgShoppingWall.setBackgroundResource(R.drawable.ic_open);
          }
          break;
        case R.id.img_internet_wall:
          if(isInternetWallOn){
            isInternetWallOn = false;
            mBinding.imgInternetWall.setBackgroundResource(R.drawable.ic_off);
          }else {
            isInternetWallOn = true;
            mBinding.imgInternetWall.setBackgroundResource(R.drawable.ic_open);
          }
          break;
        case R.id.img_online_wall:
          if(isAttentionWallOn){
            isAttentionWallOn = false;
            mBinding.imgOnlineWall.setBackgroundResource(R.drawable.ic_off);
          }else {
            isAttentionWallOn = true;
            mBinding.imgOnlineWall.setBackgroundResource(R.drawable.ic_open);
          }
          break;


      default:
        break;
    }
  }

}
