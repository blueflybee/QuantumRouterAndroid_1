package com.fernandocejas.android10.sample.presentation.view.device.camera.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySafePeriodBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityScreenOffTimeBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.camera.adapter.SafePeriodSetingAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.ScreenOffTimeAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class SafePeriodSetingActivity extends BaseCatActivity implements AdapterView.OnItemClickListener {
  private ActivitySafePeriodBinding mBinding;
  private SafePeriodSetingAdapter adapter;
  private List<String> timeTypeList;
  private String[] content = {"全天", "自定义时间"};
  private int checkedItemPosition = 0;
  private int REQUEST_CODE_FOR_SCREEN_TIME = 0x11;
  private int wakeTime = -1;//唤醒时长
  //intent
  private int window = 0;
  private static final String TAG = "ScreenOffTimeActivity";
  private String[] startTime = {"00","00","00"};//初始化
  private String[] endTime = {"00","00","00"};
  private int REQUEST_FOR_SAFE_PERIOD = 0x88;

  public static SafePeriodSetingActivity newInstance() {
    SafePeriodSetingActivity fragment = new SafePeriodSetingActivity();
    return fragment;
  }

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_safe_period);

    initView();

    initData();
  }

  private void initData() {

    //获取所有配置信息
    SovUtil.getStreamCatDataAll(window);

  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private void initView() {
    initTitleBar("安全防护时间段");
    timeTypeList = new ArrayList<>();
    for (int i = 0; i < content.length; i++) {
      timeTypeList.add(content[i]);
    }

    adapter = new SafePeriodSetingAdapter(this, timeTypeList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvSafePeriod.setAdapter(adapter);
    mBinding.lvSafePeriod.setOnItemClickListener(this);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String startTime = null,endTime = null;

        DialogUtil.showProgress(SafePeriodSetingActivity.this);

        if(checkedItemPosition == 0){
          startTime = "00:00:00";
          endTime = "23:59:59";
        }else {
           startTime = mBinding.tvStartTime.getText().toString();
           endTime = mBinding.tvEndTime.getText().toString();
        }

        Boolean isSuccessed = SovUtil.setStreamAlarmTime(window,
            String.format(AppConsts.FORMAT_ALARM_ALARMTIME0SE,
                startTime + "-"
                    + endTime));
        
        if(isSuccessed){
          DialogUtil.hideProgress();
          Toast.makeText(SafePeriodSetingActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent();
          intent.putExtra("Safe","12");
          setResult(REQUEST_FOR_SAFE_PERIOD,intent);
          finish();
        }else {
          DialogUtil.hideProgress();
          Toast.makeText(SafePeriodSetingActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvSafePeriod.getCheckedItemPosition();

    ((TextView) view.findViewById(R.id.tv_content)).setTextColor(getResources().getColor(R.color.blue_2196f3));
    ((ImageView) view.findViewById(R.id.img_chooseLogo)).setVisibility(View.VISIBLE);

    adapter.initOtherItem(checkedItemPosition);

    refreshChooseTimeUi(position);
  }

  private void refreshChooseTimeUi(int position){
    if(position == 0){
      mBinding.llChooseTime.setVisibility(View.GONE);
    }else {
      mBinding.llChooseTime.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    DialogUtil.hideProgress();

    switch (what) {
      //连接结果,视频异常断开时关闭此界面
      case JVNetConst.CALL_CATEYE_CONNECTED: {
        if(arg2!= JVNetConst.CCONNECTTYPE_CONNOK){
          this.finish();
        }
        break;
      }
      //流媒体猫眼，设置协议回调
      case JVNetConst.CALL_CATEYE_SENDDATA: {
        try {
          org.json.JSONObject object = new org.json.JSONObject(obj.toString());
          String data = object.getString("data");
          int nCmd = object.getInt("nCmd");
          int nPacketType = object.getInt("nPacketType");
          int result = object.getInt("result");
          String reason = object.getString("reason");
          Log.e(TAG, "nPacketType=" + nPacketType + "；nCmd=" + nCmd + "；obj=" + obj.toString());

          switch (nCmd) {
            //配置信息全的回调
            case JVNetConst.SRC_PARAM_ALL: {
              refreshSetUi(JSONObject.parseObject(data));
              break;
            }

            //1.报警设置相关回调
            case JVNetConst.SRC_INTELLIGENCE: {

              switch (nPacketType) {
                case JVNetConst.SRC_EX_INTELLIGENCE_REFRESH://报警设置模块全部参数
                  break;

                default:
                  break;
              }
              break;
            }

            default: {
              break;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          makeText(this,"Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }
  }

  public void onStartTimeClick(View view) {
    DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
      @Override
      public void onTimeSelected(String[] times) {
        mBinding.tvStartTime.setText(times[0] + ":" + times[1] + ":" + times[2]);
      }

      @Override
      public void onTimeSelectedInInt(int[] times) {

      }
    }, startTime[0], startTime[1],startTime[2]);
  }

  public void onEndTimeClick(View view) {
    DialogUtil.showTimePick(this, new DialogUtil.OnTimeSelectedListener() {
      @Override
      public void onTimeSelected(String[] times) {
        mBinding.tvEndTime.setText(times[0] + ":" + times[1] + ":" + times[2]);
      }

      @Override
      public void onTimeSelectedInInt(int[] times) {

      }
    }, endTime[0],endTime[1],endTime[2]);
  }

  /**
   * 根据配置信息刷新UI
   *
   * @param streamJson
   */

  public void refreshSetUi(JSONObject streamJson) {
    // 1.报警时间段
    String alarmTime = streamJson.containsKey(AppConsts.TAG_ALARM_ALARMTIME0SE) ?
        streamJson.getString(AppConsts.TAG_ALARM_ALARMTIME0SE) : AppConsts.ALARM_TIME_ALL_DAY;

    alarmTime = alarmTime.equalsIgnoreCase("-") ? AppConsts.ALARM_TIME_ALL_DAY : alarmTime;

    String[] timeArray = alarmTime.split("-");
    mBinding.tvStartTime.setText(timeArray[0]);
    mBinding.tvEndTime.setText(timeArray[1]);

    try {
      startTime = timeArray[0].split(":");
      endTime = timeArray[1].split(":");
    }catch (Exception e){
      e.printStackTrace();
    }

    if("00:00:00".equals(timeArray[0])&&"23:59:59".equals(timeArray[1])){
      checkedItemPosition = 0;
    }else {
      checkedItemPosition = 1;
    }

    refreshChooseTimeUi(checkedItemPosition);

    if(adapter != null){
      adapter.initOtherItem(checkedItemPosition);
    }

  }
}