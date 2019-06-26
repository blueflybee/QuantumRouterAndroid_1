package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityNetLimitBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeLimitBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TimeLimitActivity extends BaseActivity implements View.OnClickListener{
  private ActivityTimeLimitBinding mBinding;
  private Boolean isTimeLimit = false;
  private List<Button> mBtnList;
  private List<Boolean> mPressFlag;

//    @Inject
//    IntelDeviceListPresenter mIntelDeviceListPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_limit);
//    initializeInjector();

//        initPresenter();

    initView();
    initData();

  }

  private void initView() {
    initTitleBar("时段限制");

    mBtnList = new ArrayList<>();
    mPressFlag = new ArrayList<>();

    mBtnList.add(mBinding.btn1);
    mBtnList.add(mBinding.btn2);
    mBtnList.add(mBinding.btn3);
    mBtnList.add(mBinding.btn4);
    mBtnList.add(mBinding.btn5);
    mBtnList.add(mBinding.btn6);
    mBtnList.add(mBinding.btn7);
  }

  private void initData() {
    mBinding.imgTimeSwitch.setOnClickListener(this);
    mBinding.rlStartTime.setOnClickListener(this);
    mBinding.rlEndTime.setOnClickListener(this);

    for (int i = 0; i < 7; i++) {
      mPressFlag.add(false);
    }

    for (int i = 0; i < 7; i++) {
      mBtnList.get(i).setOnClickListener(this);
    }
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.img_timeSwitch:
        if(isTimeLimit){
          isTimeLimit = false;
          mBinding.imgTimeSwitch.setBackgroundResource(R.drawable.ic_off);
          mBinding.llLimitTime.setVisibility(View.GONE);
        }else {
          isTimeLimit = true;
          mBinding.imgTimeSwitch.setBackgroundResource(R.drawable.ic_open);
          mBinding.llLimitTime.setVisibility(View.VISIBLE);
        }
        break;

      case R.id.rl_startTime:
        DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
          @Override
          public void onTimeSelected(String[] times) {
            mBinding.tvStartTime.setText(times[0] + ":" + times[1]);
          }

          @Override
          public void onTimeSelectedInInt(int[] times) {

          }
        },"00","00");
        break;

      case R.id.rl_endTime:
        DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
          @Override
          public void onTimeSelected(String[] times) {
            mBinding.tvEndTime.setText(times[0] + ":" + times[1]);
          }

          @Override
          public void onTimeSelectedInInt(int[] times) {

          }
        },"00","00");
        break;

      case R.id.btn1:
        if(mPressFlag.get(0)){
          mPressFlag.set(0,false);
          mBtnList.get(0).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(0).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(0,true);
          mBtnList.get(0).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(0).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn2:
        if(mPressFlag.get(1)){
          mPressFlag.set(1,false);
          mBtnList.get(1).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(1).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(1,true);
          mBtnList.get(1).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(1).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn3:
        if(mPressFlag.get(2)){
          mPressFlag.set(2,false);
          mBtnList.get(2).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(2).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(2,true);
          mBtnList.get(2).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(2).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn4:
        if(mPressFlag.get(3)){
          mPressFlag.set(3,false);
          mBtnList.get(3).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(3).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(3,true);
          mBtnList.get(3).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(3).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn5:
        if(mPressFlag.get(4)){
          mPressFlag.set(4,false);
          mBtnList.get(4).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(4).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(4,true);
          mBtnList.get(4).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(4).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn6:
        if(mPressFlag.get(5)){
          mPressFlag.set(5,false);
          mBtnList.get(5).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(5).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(5,true);
          mBtnList.get(5).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(5).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn7:
        if(mPressFlag.get(6)){
          mPressFlag.set(6,false);
          mBtnList.get(6).setTextColor(getResources().getColor(R.color.gray_999999));
          mBtnList.get(6).setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          mPressFlag.set(6,true);
          mBtnList.get(6).setTextColor(getResources().getColor(R.color.white));
          mBtnList.get(6).setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;

      default:
        break;
    }
  }

//  private void initPresenter() {
//    mIntelDeviceListPresenter.setView(this);
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
