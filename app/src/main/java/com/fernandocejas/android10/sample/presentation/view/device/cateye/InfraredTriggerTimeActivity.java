package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityIntriggerTimeBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTimeZoneSettingBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

/**
 * <pre>
 *      author:
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc: 红外触发时间
 *      version: 1.0
 * </pre>
 */
public class InfraredTriggerTimeActivity extends BaseCatActivity implements AdapterView.OnItemClickListener {
  private ActivityIntriggerTimeBinding mBinding;
  private TimeZoneSettingAdapter adapter;
  private List<String> triggerTimeList;
  private String[] content = {"实时","5s","10s","15s"};
  private int checkedItemPosition = 0;
  private int REQUEST_CODE_FOR_TRIGGER_TIME = 0x15;
  //intent
  private int window = 0;
  private int mPirTime;//红外触发时间
  private static final String TAG = "InfraredTriggerTimeActivity";

  public static InfraredTriggerTimeActivity newInstance() {
    InfraredTriggerTimeActivity fragment = new InfraredTriggerTimeActivity();
    return fragment;
  }

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intrigger_time);

    initView();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private String getTriggerTime() {
    return getIntent().getStringExtra("TrigerTime");
  }

  private void initView() {
    initTitleBar("红外触发时间");
    triggerTimeList = new ArrayList<>();
    for (int i = 0; i < content.length; i++) {
      triggerTimeList.add(content[i]);
    }

    getTimeZoneSettingIndex(getTriggerTime());

    adapter = new TimeZoneSettingAdapter(this, triggerTimeList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvIntriggerTime.setAdapter(adapter);
    mBinding.lvIntriggerTime.setOnItemClickListener(this);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(InfraredTriggerTimeActivity.this);

        if(checkedItemPosition == 0){
          SovUtil.setStreamPirTime(window,
              String.format(AppConsts.FORMATTER_BPIRTIME,0));
        }else {
          SovUtil.setStreamPirTime(window,
              String.format(AppConsts.FORMATTER_BPIRTIME, convertPositionToTime(checkedItemPosition) / 5 - 1));
        }
      }
    });
  }

  private void getTimeZoneSettingIndex(String question) {
    switch (question) {

      case "实时":
        checkedItemPosition = 0;
        break;

      case "5s":
        checkedItemPosition = 1;
        break;

      case "10s":
        checkedItemPosition = 2;
        break;

      case "15s":
        checkedItemPosition = 3;
        break;

      default:
        break;
    }
  }

  private int convertPositionToTime(int position){

    switch (position){
      case 0:
        mPirTime = 0;
        break;
      case 1:
        mPirTime = 5;
        break;
      case 2:
        mPirTime = 10;
        break;
      case 3:
        mPirTime = 15;
        break;
      default:
        break;
    }

    return mPirTime;

  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvIntriggerTime.getCheckedItemPosition();

    ((TextView) view.findViewById(R.id.tv_content)).setTextColor(getResources().getColor(R.color.blue_2196f3));
    ((ImageView) view.findViewById(R.id.img_chooseLogo)).setVisibility(View.VISIBLE);

    adapter.initOtherItem(checkedItemPosition);
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

          switch (nCmd) {
            //配置信息全的回调
          /*  case JVNetConst.SRC_PARAM_ALL: {
              analyzeAll(JSONObject.parseObject(data));
              break;
            }*/

            //3.智能设置相关
            case JVNetConst.SRC_INTELLIGENCE: {
              switch (nPacketType) {
                //红外感应开关设置回调
                //红外感应时间设置回调
                case JVNetConst.SRC_EX_INTELLIGENCE_PIR_TIME:
                  if (result == 0) {
                    makeText(this, "设置失败", LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("CHOOSE_TRIGGER_TIME", content[checkedItemPosition]);
                    setResult(REQUEST_CODE_FOR_TRIGGER_TIME, intent);
                    finish();
                  }
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
}