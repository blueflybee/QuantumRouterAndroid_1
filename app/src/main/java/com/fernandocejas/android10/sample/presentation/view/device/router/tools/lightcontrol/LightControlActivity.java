package com.fernandocejas.android10.sample.presentation.view.device.router.tools.lightcontrol;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLightControlBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 灯光控制
 *      version: 1.0
 * </pre>
 */

public class LightControlActivity extends BaseActivity implements View.OnClickListener {
  private ActivityLightControlBinding mBinding;
  private Boolean isLightOpen = false;
  //IGetAgreementView
   /* @Inject
    GetAgreementPresenter mGetAgreementPresenter;*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_light_control);
    initSpecialAttentionTitleBar("灯光控制");
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
    mBinding.imgLightSwitch.setOnClickListener(this);
    mBinding.rlStartTimeLight.setOnClickListener(this);
    mBinding.rlEndTimeLight.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_startTime_light:
        DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
          @Override
          public void onTimeSelected(String[] times) {
            mBinding.tvStartTimeLight.setText(times[0] + ":" + times[1]);
          }

          @Override
          public void onTimeSelectedInInt(int[] times) {

          }
        },"00","00");
        break;

      case R.id.rl_endTime_light :
        DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
          @Override
          public void onTimeSelected(String[] times) {
            mBinding.tvEndTimeLight.setText(times[0] + ":" + times[1]);
          }

          @Override
          public void onTimeSelectedInInt(int[] times) {

          }
        },"00","00");
        break;

      case R.id.img_lightSwitch:
        if (isLightOpen) {
          isLightOpen = false;
          mBinding.imgLightSwitch.setBackgroundResource(R.drawable.ic_off);
          mBinding.llLightControl.setVisibility(View.GONE);
        } else {
          isLightOpen = true;
          mBinding.imgLightSwitch.setBackgroundResource(R.drawable.ic_open);
          mBinding.llLightControl.setVisibility(View.VISIBLE);
        }
          break;

      default:
        break;
    }
  }

}
