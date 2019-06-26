package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddTimeIntervalBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeLimitBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.TimeUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare.WeekDayStatusBean;
import com.qtec.router.model.req.AddWifiDataRequest;
import com.qtec.router.model.req.DeleteWifiSwitchRequest;
import com.qtec.router.model.req.ModifyWifiDataRequest;
import com.qtec.router.model.rsp.DeleteWifiSwitchResponse;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;
import com.qtec.router.model.rsp.SetWifiDataResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : 添加时段
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddTimeIntervalActivity extends BaseActivity implements View.OnClickListener, IDeleteWifiSwitchView, ISetWifiDataView {
  private ActivityAddTimeIntervalBinding mBinding;
  private Boolean isTimeLimit = false;
  private List<WeekDayStatusBean> weekBeanList;
  private String mStartHour = "", mStartMin = "", mStopHour = "", mStopMin = "";
  private Boolean isHasSameTimeInterval = false;//判断是否有重叠区间
  // 周日的Index才是1，周六为7
  private int mWeek = 0,mHour = 0,mMinute = 0;
  
  @Inject
  DeleteWiFiSwitchPresenter mDeleteWifiSwitchPresenter;
  @Inject
  SetWifiDataPresenter mSetWifiDataPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_time_interval);
    initializeInjector();

    initPresenter();

    initView();
    initData();

    mBinding.btnDeleteTimeInterval.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //删除开关

        deleteWifiSwitchRequest();
      }
    });

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
    mDeleteWifiSwitchPresenter.setView(this);
    mSetWifiDataPresenter.setView(this);
  }

  private void deleteWifiSwitchRequest() {
    DeleteWifiSwitchRequest bean = new DeleteWifiSwitchRequest();
    bean.setId(getRuleDetail().getId());

    mDeleteWifiSwitchPresenter.deleteWIfiSwitch(GlobleConstant.getgDeviceId(), bean);
  }

  private void setWifiDataRequest(Boolean isAdd) {

    // 周日的Index才是1，周六为7
    mWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
    mHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);//时
    mMinute = Calendar.getInstance().get(Calendar.MINUTE);//分

    System.out.println("wifiTimeSwitch mWeek = " + mWeek);
    System.out.println("wifiTimeSwitch mHour = " + mHour);
    System.out.println("wifiTimeSwitch mMinute = " + mMinute);

    /*long nowTimeMil = TimeUtils.string2Millis(new SimpleDateFormat("HH:mm").format(TimeUtils.getNowDate()),new SimpleDateFormat("HH:mm"));*/

    System.out.println("wifiTimeSwitch tvStartTime = " + mBinding.tvStartTime.getText().toString().trim());
    System.out.println("wifiTimeSwitch tvEndTime = " + mBinding.tvEndTime.getText().toString().trim());

    // 参数校验 排除了全是空格的问题
    if (TextUtils.isEmpty(getText(mBinding.etIntervalName).trim())) {
      Toast.makeText(this, "描述信息不能为空", Toast.LENGTH_SHORT).show();
      return;
    }

    if ((getText(mBinding.tvStartTime)).equals(getText(mBinding.tvEndTime))) {
      Toast.makeText(this, "开始时间和结束时间不能相同", Toast.LENGTH_SHORT).show();
      return;
    }

    if (isAdd) {
      //新增
      AddWifiDataRequest bean = new AddWifiDataRequest();
      bean.setName(getText(mBinding.etIntervalName));

      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < 7; i++) {
        if (weekBeanList.get(i).getFlag()) {
          if (i == 6) {
            buffer.append("0 ");
          } else {
            buffer.append((i + 1) + ",");
          }
        }
      }
      if (buffer.length() > 0) {
        bean.setWeek_day(buffer.deleteCharAt(buffer.length() - 1).toString());
      } else {
        bean.setWeek_day("");
      }

      System.out.println("wifiTimeSwitch isHasSameTimeInterval =" + isHasSameTimeInterval);

      String[] startTime = getText(mBinding.tvStartTime).split(":");
      String[] endTime = getText(mBinding.tvEndTime).split(":");

      bean.setRule_enable(1);
      bean.setStart_hour(Integer.parseInt(startTime[0].trim()));
      bean.setStart_min(Integer.parseInt(startTime[1].trim()));
      bean.setStop_hour(Integer.parseInt(endTime[0].trim()));
      bean.setStop_min(Integer.parseInt(endTime[1].trim()));

      if (TextUtils.isEmpty(bean.getWeek_day())) {
        Toast.makeText(this, "重复周期不能为空", Toast.LENGTH_SHORT).show();
        return;
      }

      //五分钟之内不能设置成功
      int startMins = Integer.parseInt(startTime[0].trim())*60+Integer.parseInt(startTime[1].trim());//开始的分钟数
      int endMins = Integer.parseInt(endTime[0].trim())*60+Integer.parseInt(endTime[1].trim());//结束的分钟数

      if(Math.abs(startMins-endMins) < 6){
        //配置的时间段是否小于5min
        Toast.makeText(this, "开始和结束时间必须大于5分钟", Toast.LENGTH_SHORT).show();
        return;
      }

      /**判断设置的时间是否在当前时间范围内*/
      String weekStr = "";// 0 1 2 3 4 5 6

      if(mWeek == 1){
        weekStr = 0+"";
      }else{
        weekStr = (mWeek-1)+"";
      }

      judgeIsContainsNowTime(bean,weekStr);

      /**判断设置的时间是否在当前时间范围内*/

      mSetWifiDataPresenter.setWiFiData(GlobleConstant.getgDeviceId(), bean);

    } else {
      //修改
      ModifyWifiDataRequest bean = new ModifyWifiDataRequest();
      bean.setId(getRuleDetail().getId());
      bean.setName(getText(mBinding.etIntervalName));

      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < 7; i++) {
        if (weekBeanList.get(i).getFlag()) {
          if (i == 6) {
            buffer.append("0,");
          } else {
            buffer.append((i + 1) + ",");
          }
        }
      }
      if (buffer.length() > 0) {
        bean.setWeek_day(buffer.deleteCharAt(buffer.length() - 1).toString());
      } else {
        bean.setWeek_day("");
      }

      System.out.println("wifiTimeSwitch isHasSameTimeInterval=" + isHasSameTimeInterval);

      String[] startTime = getText(mBinding.tvStartTime).split(":");
      String[] endTime = getText(mBinding.tvEndTime).split(":");

      bean.setRule_enable(getRuleDetail().getRule_enable());

      bean.setStart_hour(Integer.parseInt(startTime[0].trim()));
      bean.setStart_min(Integer.parseInt(startTime[1].trim()));
      bean.setStop_hour(Integer.parseInt(endTime[0].trim()));
      bean.setStop_min(Integer.parseInt(endTime[1].trim()));

      /**判断设置的时间是否在当前时间范围内*/

      /**判断设置的时间是否在当前时间范围内*/

      if (TextUtils.isEmpty(bean.getWeek_day())) {
        Toast.makeText(this, "重复周期不能为空", Toast.LENGTH_SHORT).show();
        return;
      }

      //五分钟之内不能设置成功
      int startMins = Integer.parseInt(startTime[0].trim())*60+Integer.parseInt(startTime[1].trim());//开始的分钟数
      int endMins = Integer.parseInt(endTime[0].trim())*60+Integer.parseInt(endTime[1].trim());//结束的分钟数

      if(Math.abs(startMins-endMins) < 5){
        //配置的时间段是否小于5min
        Toast.makeText(this, "开始和结束时间必须大于5分钟", Toast.LENGTH_SHORT).show();
        return;
      }

      String weekStr = "";// 0 1 2 3 4 5 6

      if(mWeek == 1){
        weekStr = 0+"";
      }else{
        weekStr = (mWeek-1)+"";
      }

      if(bean.getWeek_day().indexOf(weekStr)!=-1){
        System.out.println("WiFiTimeSwitch 包含设置当天");

        if(bean.getStart_hour() < mHour && mHour < bean.getStop_hour()){
          isHasSameTimeInterval = true;
        }

        if((bean.getStart_hour()==mHour) && mHour < bean.getStop_hour()){
          if(bean.getStart_min() <= mMinute){
            isHasSameTimeInterval = true;
          }else{
            isHasSameTimeInterval = false;
          }
        }

        if(bean.getStart_hour() < mHour && (mHour == bean.getStop_hour())){
          if(bean.getStop_min() >= mMinute){
            isHasSameTimeInterval = true;
          }else{
            isHasSameTimeInterval = false;
          }
        }

        if(bean.getStart_hour()<=mHour && bean.getStop_hour() <= bean.getStart_hour() ){
          //第二天
          isHasSameTimeInterval = true;
        }


      }else{
        isHasSameTimeInterval = false;
        System.out.println("WiFiTimeSwitch 不包含");
      }

      mSetWifiDataPresenter.setWiFiData(GlobleConstant.getgDeviceId(), bean);
    }
  }

  /**
  * 判断当前时间是否在设置期限内
  *
  * @param
  * @return
  */
  private void judgeIsContainsNowTime(AddWifiDataRequest bean, String weekstr) {
    if(bean.getWeek_day().indexOf(weekstr)!=-1){
      System.out.println("WiFiTimeSwitch 包含设置当天");

      if(bean.getStart_hour() < mHour && mHour < bean.getStop_hour()){
        isHasSameTimeInterval = true;
      }

      if((bean.getStart_hour()==mHour) && mHour < bean.getStop_hour()){
        if(bean.getStart_min() <= mMinute){
          isHasSameTimeInterval = true;
        }else{
          isHasSameTimeInterval = false;
        }
      }

      if(bean.getStart_hour() < mHour && (mHour == bean.getStop_hour())){
        if(bean.getStop_min() >= mMinute){
          isHasSameTimeInterval = true;
        }else{
          isHasSameTimeInterval = false;
        }
      }

      if(bean.getStart_hour()<=mHour && bean.getStop_hour() <= bean.getStart_hour() ){
        //第二天
        isHasSameTimeInterval = true;
      }


    }else{
      isHasSameTimeInterval = false;
      System.out.println("WiFiTimeSwitch 不包含");
    }
  }

  private void initView() {
    initTitleBar("WIFI定时开关");

    Button[] btnArr = {mBinding.btn1, mBinding.btn2, mBinding.btn3, mBinding.btn4, mBinding.btn5, mBinding.btn6, mBinding.btn7};

    weekBeanList = new ArrayList<>();

    for (int i = 0; i < 7; i++) {
      //初始化
      WeekDayStatusBean bean = new WeekDayStatusBean();
      bean.setBtn(btnArr[i]);
      bean.setFlag(false);
      weekBeanList.add(bean);
    }


    if ("0".equals(getAddFlag())) {
      //新增
      mBinding.btnDeleteTimeInterval.setVisibility(View.GONE);
    } else {
      mBinding.btnDeleteTimeInterval.setVisibility(View.VISIBLE);
    }

    mTitleBar.setRightAs("保存", new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if ("0".equals(getAddFlag())) {
          setWifiDataRequest(true);// 新增
        } else {
          setWifiDataRequest(false);// 修改
        }

      }
    });


  }

  private void initData() {
    mBinding.rlStartTime.setOnClickListener(this);
    mBinding.rlEndTime.setOnClickListener(this);

    for (int i = 0; i < 7; i++) {
      weekBeanList.get(i).getBtn().setOnClickListener(this);
    }

    watchEditTextNoClear(mBinding.etIntervalName);//不能输入表情
    //监听wifi名称设置
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition1 = new InputWatcher.WatchCondition();
    condition1.setByteRange(new InputWatcher.InputByteRange(1, 48));

    watcher.addEt(mBinding.etIntervalName, condition1);

    /*InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition ssidCondition = new InputWatcher.WatchCondition();
    ssidCondition.setLength(20);
    watcher.addEt(mBinding.etIntervalName, ssidCondition);*/

    if ("1".equals(getAddFlag())) {
      // 修改
      mStartHour = TimeUtil.getStringTime(getRuleDetail().getStart_hour());
      mStartMin = TimeUtil.getStringTime(getRuleDetail().getStart_min());
      mStopHour = TimeUtil.getStringTime(getRuleDetail().getStop_hour());
      mStopMin = TimeUtil.getStringTime(getRuleDetail().getStop_min());

      mBinding.tvEndTime.setText(TimeUtil.getStringTime(getRuleDetail().getStop_hour()) + ":" + TimeUtil.getStringTime(getRuleDetail().getStop_min()));
      mBinding.tvStartTime.setText(TimeUtil.getStringTime(getRuleDetail().getStart_hour()) + ":" + TimeUtil.getStringTime(getRuleDetail().getStart_min()));

      String[] weekDays = getRuleDetail().getWeek_day().split(",");
      //0表示星期日，1-6表示星期一至六

      for (int i = 0; i < weekDays.length; i++) {
        System.out.println("weekBeanList weekDays[i] = " + weekDays[i]);
        for (int j = 0; j < 7; j++) {
          if (("" + j).equals(weekDays[i])) {
            if ("0".equals(weekDays[i])) {
              weekBeanList.get(6).setFlag(true);
            } else {
              weekBeanList.get(j - 1).setFlag(true);
            }
            break;
          }
        }
      }

      for (int i = 0; i < 7; i++) {
        System.out.println("weekBeanList = " + weekBeanList.get(i).getFlag());
        if (weekBeanList.get(i).getFlag()) {
          weekBeanList.get(i).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(i).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        } else {
          weekBeanList.get(i).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(i).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        }
      }

      if (!"".equals(getRuleDetail().getName())) {
        mBinding.etIntervalName.setText(getRuleDetail().getName());
        //当号码返回时光标放在最后
        Selection.setSelection(mBinding.etIntervalName.getText(), mBinding.etIntervalName.getText().length());
      }
    } else {
      //新增
      mStartHour = "00";
      mStartMin = "00";
      mStopHour = "00";
      mStopMin = "00";

      mBinding.tvStartTime.setText("00:00");
      mBinding.tvEndTime.setText("00:00");
    }

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_startTime:
        DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
          @Override
          public void onTimeSelected(String[] times) {
            mBinding.tvStartTime.setText(times[0] + ":" + times[1]);
          }

          @Override
          public void onTimeSelectedInInt(int[] times) {

          }
        }, mStartHour, mStartMin);
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
        }, mStopHour, mStopMin);
        break;

      case R.id.btn1:
        if (weekBeanList.get(0).getFlag()) {
          weekBeanList.get(0).setFlag(false);
          weekBeanList.get(0).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(0).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(0).setFlag(true);
          weekBeanList.get(0).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(0).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn2:
        if (weekBeanList.get(1).getFlag()) {
          weekBeanList.get(1).setFlag(false);
          weekBeanList.get(1).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(1).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(1).setFlag(true);
          weekBeanList.get(1).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(1).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn3:
        if (weekBeanList.get(2).getFlag()) {
          weekBeanList.get(2).setFlag(false);
          weekBeanList.get(2).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(2).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(2).setFlag(true);
          weekBeanList.get(2).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(2).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn4:
        if (weekBeanList.get(3).getFlag()) {
          weekBeanList.get(3).setFlag(false);
          weekBeanList.get(3).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(3).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(3).setFlag(true);
          weekBeanList.get(3).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(3).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn5:
        if (weekBeanList.get(4).getFlag()) {
          weekBeanList.get(4).setFlag(false);
          weekBeanList.get(4).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(4).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(4).setFlag(true);
          weekBeanList.get(4).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(4).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn6:
        if (weekBeanList.get(5).getFlag()) {
          weekBeanList.get(5).setFlag(false);
          weekBeanList.get(5).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(5).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(5).setFlag(true);
          weekBeanList.get(5).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(5).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;
      case R.id.btn7:
        if (weekBeanList.get(6).getFlag()) {
          weekBeanList.get(6).setFlag(false);
          weekBeanList.get(6).getBtn().setTextColor(getResources().getColor(R.color.gray_999999));
          weekBeanList.get(6).getBtn().setBackgroundResource(R.drawable.bottom_week_normal);
        } else {
          weekBeanList.get(6).setFlag(true);
          weekBeanList.get(6).getBtn().setTextColor(getResources().getColor(R.color.white));
          weekBeanList.get(6).getBtn().setBackgroundResource(R.drawable.bottom_week_pressed);
        }
        break;

      default:
        break;
    }
  }

  private GetWifiTimeConfigResponse.WifiTimeConfig getRuleDetail() {
    return (GetWifiTimeConfigResponse.WifiTimeConfig) getIntent().getSerializableExtra("DETAIL");
  }

  private String getAddFlag() {
    return getIntent().getStringExtra("FLAG");
  }

  @Override
  public void deleteWifiSwitch(DeleteWifiSwitchResponse response) {
    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    setResult(3);
    finish();
  }

  @Override
  public void setWifiData(SetWifiDataResponse response) {

    if(isHasSameTimeInterval){
      isHasSameTimeInterval = false;
      Toast.makeText(this, "保存成功，但当前时间在时间段内，需到下个时间段才能生效", Toast.LENGTH_SHORT).show();
    }else {
      Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    setResult(3);
    finish();
  }
  
}
