package com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityChildCareDetailBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeLimitBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.ChildCareBean;
import com.qtec.router.model.req.PostChildCareDetailRequest;
import com.qtec.router.model.req.UpdateChildCareDetailRequest;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 儿童关怀 详情设置
 *      version: 1.0
 * </pre>
 */

public class ChildCareDetailActivity extends BaseActivity implements View.OnClickListener,PostChildCareDetailView,UpdateChildCareDetailView{
  private ActivityChildCareDetailBinding mBinding;
  private Boolean isTimeLimit = false;
  private List<WeekDayStatusBean> weekBeanList;
  private String[] mStartTime,mStopTime;

  @Inject
  PostCareDetailPresenter mPostCareDetailPresenter;
  @Inject
  UpdateCareDetailPresenter mUpdateCareDetailPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_child_care_detail);
    initializeInjector();
    initPresenter();

    initView();
    initData();

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
    mPostCareDetailPresenter.setView(this);
    mUpdateCareDetailPresenter.setView(this);
  }

  private void postChildCareDetail(PostChildCareDetailRequest bean) {
    mPostCareDetailPresenter.postCareDetail(GlobleConstant.getgDeviceId(),bean);
  }

  private void updateChildCareDetail(UpdateChildCareDetailRequest updateBean) {
    mUpdateCareDetailPresenter.updateCareDetail(GlobleConstant.getgDeviceId(),updateBean);
  }

  private void initView() {
    initSpecialAttentionTitleBar("儿童关怀");

    mStartTime = new String[2];
    mStopTime = new String[2];

    Button[] btnArr = {mBinding.btn1,mBinding.btn2,mBinding.btn3,mBinding.btn4,mBinding.btn5,mBinding.btn6,mBinding.btn7};

    weekBeanList = new ArrayList<>();

    for (int i = 0; i < 7; i++) {
      //初始化
      WeekDayStatusBean bean = new WeekDayStatusBean();
      bean.setBtn(btnArr[i]);
      bean.setFlag(false);
      weekBeanList.add(bean);
    }

    mTitleBar.setRightAs("保存", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(getChildDetail().getAdd()){
          //新增
          PostChildCareDetailRequest bean = new PostChildCareDetailRequest();

          StringBuffer buffer = new StringBuffer();
          for (int i = 0; i < 7; i++) {
            if(weekBeanList.get(i).getFlag()){
              buffer.append((i+1)+" ");
            }
          }
          if(buffer.length() > 0){
            bean.setWeekdays(buffer.deleteCharAt(buffer.length()-1).toString());
          }else{
            bean.setWeekdays("");
          }

          bean.setStart_time(mBinding.tvStartTime.getText().toString());
          bean.setStop_time(mBinding.tvEndTime.getText().toString());

          bean.setMacaddr(getChildDetail().getMacaddr());

          if(isTimeLimit){
            bean.setEnabled(1);

            if((getText(mBinding.tvStartTime)).equals(getText(mBinding.tvEndTime))){
              Toast.makeText(ChildCareDetailActivity.this, "开始时间和结束时间不能相同", Toast.LENGTH_SHORT).show();
              return;
            }

            if(buffer.length() == 0){
              Toast.makeText(ChildCareDetailActivity.this, "请选择重复周期", Toast.LENGTH_SHORT).show();
              return;
            }

          }else {
            bean.setEnabled(0);
          }

          postChildCareDetail(bean);
        }else {
          //修改
          UpdateChildCareDetailRequest updateBean = new UpdateChildCareDetailRequest();

          updateBean.setNewmacaddr(getChildDetail().getMacaddr());

          updateBean.setNewstarttime(mBinding.tvStartTime.getText().toString());
          updateBean.setNewstoptime(mBinding.tvEndTime.getText().toString());


          StringBuffer buffer = new StringBuffer();
          for (int i = 0; i < 7; i++) {
            if(weekBeanList.get(i).getFlag()){
              buffer.append((i+1)+" ");
            }
          }
          if(buffer.length() > 0){
            updateBean.setNewweekdays(buffer.deleteCharAt(buffer.length()-1).toString());
          }else{
            updateBean.setNewweekdays("");
          }

          updateBean.setOldenabled(getChildDetail().getIsEnable());
          updateBean.setOldweekdays(getChildDetail().getWeekdays());
          updateBean.setOldstoptime(getChildDetail().getStop_time());
          updateBean.setOldmacaddr(getChildDetail().getMacaddr());
          updateBean.setOldstarttime(getChildDetail().getStart_time());

          if(isTimeLimit){
            updateBean.setNewenabled(1);

            if((getText(mBinding.tvStartTime)).equals(getText(mBinding.tvEndTime))){
              Toast.makeText(ChildCareDetailActivity.this, "开始时间和结束时间不能相同", Toast.LENGTH_SHORT).show();
              return;
            }

            if(buffer.length() == 0){
              Toast.makeText(ChildCareDetailActivity.this, "请选择重复周期", Toast.LENGTH_SHORT).show();
              return;
            }

          }else {
            updateBean.setNewenabled(0);
          }

          updateChildCareDetail(updateBean);
        }
      }
    });

    mTitleBar.getRightBtn().setTextColor(getResources().getColor(R.color.white));

  }

  private void initData() {
    mBinding.imgNetLimitSwitch.setOnClickListener(this);
    mBinding.rlStartTime.setOnClickListener(this);
    mBinding.rlEndTime.setOnClickListener(this);

    for (int i = 0; i < 7; i++) {
      weekBeanList.get(i).getBtn().setOnClickListener(this);
    }

    if(getChildDetail().getIsEnable() == 1){
      updateOldData();
    }else {
      isTimeLimit = false;
      mBinding.imgNetLimitSwitch.setBackgroundResource(R.drawable.ic_off);
      mBinding.llChildlimitTime.setVisibility(View.GONE);
    }

    mBinding.tvHeadTitle.setText(getChildDetail().getStaname());

  }

  private void updateOldData(){
    //打开
    isTimeLimit = true;
    mBinding.imgNetLimitSwitch.setBackgroundResource(R.drawable.ic_open);
    mBinding.llChildlimitTime.setVisibility(View.VISIBLE);
    // 打开的逻辑展示
    mBinding.tvEndTime.setText(getChildDetail().getStop_time());
    mBinding.tvStartTime.setText(getChildDetail().getStart_time());

    mStartTime = getChildDetail().getStart_time().split(":");
    mStopTime = getChildDetail().getStop_time().split(":");

    if(!TextUtils.isEmpty(getChildDetail().getWeekdays())){

      String[] weekDays = getChildDetail().getWeekdays().split(" ");

      for (int i = 0; i < 7; i++) {
        for (int j = 0; j < weekDays.length; j++) {
          if((i+1) == Integer.parseInt(weekDays[j])){
            weekBeanList.get(i).setFlag(true);
            break;
          }
        }
      }

      for (int i = 0; i < 7; i++) {
        if(weekBeanList.get(i).getFlag()){
          weekBeanList.get(i).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(i).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }else {
          weekBeanList.get(i).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(i).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }
      }

    }

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.img_netLimitSwitch:
        if(isTimeLimit){
          isTimeLimit = false;
          mBinding.imgNetLimitSwitch.setBackgroundResource(R.drawable.ic_off);
          mBinding.llChildlimitTime.setVisibility(View.GONE);
        }else {
          isTimeLimit = true;
          mBinding.imgNetLimitSwitch.setBackgroundResource(R.drawable.ic_open);
          mBinding.llChildlimitTime.setVisibility(View.VISIBLE);
          //更新数据
          if(getChildDetail().getAdd()){
            //默认00:00
            mStartTime[0] = "00";
            mStartTime[1] = "00";

            mStopTime[0] = "00";
            mStopTime[1] = "00";
          }else{
            updateOldData();
          }
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
        },mStartTime[0],mStartTime[1]);
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
        },mStopTime[0],mStopTime[1]);
        break;

      case R.id.btn1:
        if(weekBeanList.get(0).getFlag()){
          weekBeanList.get(0).setFlag(false);
          weekBeanList.get(0).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(0).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(0).setFlag(true);
          weekBeanList.get(0).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(0).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn2:
        if(weekBeanList.get(1).getFlag()){
          weekBeanList.get(1).setFlag(false);
          weekBeanList.get(1).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(1).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(1).setFlag(true);
          weekBeanList.get(1).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(1).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn3:
        if(weekBeanList.get(2).getFlag()){
          weekBeanList.get(2).setFlag(false);
          weekBeanList.get(2).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(2).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(2).setFlag(true);
          weekBeanList.get(2).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(2).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn4:
        if(weekBeanList.get(3).getFlag()){
          weekBeanList.get(3).setFlag(false);
          weekBeanList.get(3).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(3).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(3).setFlag(true);
          weekBeanList.get(3).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(3).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn5:
        if(weekBeanList.get(4).getFlag()){
          weekBeanList.get(4).setFlag(false);
          weekBeanList.get(4).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(4).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(4).setFlag(true);
          weekBeanList.get(4).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(4).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn6:
        if(weekBeanList.get(5).getFlag()){
          weekBeanList.get(5).setFlag(false);
          weekBeanList.get(5).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(5).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(5).setFlag(true);
          weekBeanList.get(5).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(5).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn7:
        if(weekBeanList.get(6).getFlag()){
          weekBeanList.get(6).setFlag(false);
          weekBeanList.get(6).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(6).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }else{
          weekBeanList.get(6).setFlag(true);
          weekBeanList.get(6).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(6).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;

      default:
        break;
    }
  }

  private ChildCareBean getChildDetail() {
    return (ChildCareBean) getIntent().getSerializableExtra("ChildDetail");
  }

  @Override
  public void postCareDetail(PostChildCareDetailResponse response) {
    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    setResult(5);
    finish();
  }

  @Override
  public void updateCareDetail(PostChildCareDetailResponse response) {
    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    setResult(5);
    finish();
  }
}
