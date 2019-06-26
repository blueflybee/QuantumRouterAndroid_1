package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.app.AlertDialog;
import android.app.Dialog;
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

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
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
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class TimeZoneSettingActivity extends BaseCatActivity implements AdapterView.OnItemClickListener {
  private ActivityTimeZoneSettingBinding mBinding;
  private TimeZoneSettingAdapter adapter;
  private List<String> timeZoneList;
  private String[] content = {"东十二区", "东十一区", "东十区", "东九区", "东八区", "东七区", "东六区", "东五区"
      , "东四区", "东三区", "东二区", "东一区", "中时区",
      "西一区", "西二区", "西三区", "西四区", "西五区", "西六区", "西七区", "西八区", "西九区", "西十区", "西十一区", "西十二区"};
  private int checkedItemPosition = 0;
  private int REQUEST_CODE_FOR_TIME_ZONE_SET = 0x13;
  private int timeZone;//时区-12~12 表示东12-西12
  //intent
  private int window = 0;

  public static TimeZoneSettingActivity newInstance() {
    TimeZoneSettingActivity fragment = new TimeZoneSettingActivity();
    return fragment;
  }

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_time_zone_setting);

    initView();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private String getTimeZone() {
    return getIntent().getStringExtra("TimeZoneSet");
  }

  private void initView() {
    initTitleBar("时区设置");
    timeZoneList = new ArrayList<>();
    for (int i = 0; i < content.length; i++) {
      timeZoneList.add(content[i]);
    }

    getTimeZoneSettingIndex(getTimeZone());
    mBinding.tvTimeZoneChoosed.setText(getTimeZone());

    adapter = new TimeZoneSettingAdapter(this, timeZoneList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvLanguageType.setAdapter(adapter);
    mBinding.lvLanguageType.setOnItemClickListener(this);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(TimeZoneSettingActivity.this);

        if(AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())){
          SovUtil.setStreamCatZone(window,
              String.format(AppConsts.FORMATTER_TIME_ZONE_STREAM, 12 - checkedItemPosition));
        }else if(AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())){
          SovUtil.setStreamZone(window,
              String.format(AppConsts.FORMAT_TIME_ZONE, 12 - checkedItemPosition));
        }

      }
    });
  }

  private void getTimeZoneSettingIndex(String question) {
    switch (question) {
      case "东十二区":
        checkedItemPosition = 0;
        break;
      case "东十一区":
        checkedItemPosition = 1;
        break;
      case "东十区":
        checkedItemPosition = 2;
        break;
      case "东九区":
        checkedItemPosition = 3;
        break;
      case "东八区":
        checkedItemPosition = 4;
        break;
      case "东七区":
        checkedItemPosition = 5;
        break;
      case "东六区":
        checkedItemPosition = 6;
        break;
      case "东五区":
        checkedItemPosition = 7;
        break;
      case "东四区":
        checkedItemPosition = 8;
        break;
      case "东三区":
        checkedItemPosition = 9;
        break;
      case "东二区":
        checkedItemPosition = 10;
        break;
      case "东一区":
        checkedItemPosition = 11;
        break;
      case "中时区":
        checkedItemPosition = 12;
        break;
      case "西一区":
        checkedItemPosition = 13;
        break;
      case "西二区":
        checkedItemPosition = 14;
        break;
      case "西三区":
        checkedItemPosition = 15;
        break;
      case "西四区":
        checkedItemPosition = 16;
        break;
      case "西五区":
        checkedItemPosition = 17;
        break;
      case "西六区":
        checkedItemPosition = 18;
        break;
      case "西七区":
        checkedItemPosition = 19;
        break;
      case "西八区":
        checkedItemPosition = 20;
        break;
      case "西九区":
        checkedItemPosition = 21;
        break;
      case "西十区":
        checkedItemPosition = 22;
        break;
      case "西十一区":
        checkedItemPosition = 23;
        break;
      case "西十二区":
        checkedItemPosition = 24;
        break;

      default:
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvLanguageType.getCheckedItemPosition();

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
        if (arg2 != JVNetConst.CCONNECTTYPE_CONNOK) {
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
        /*    case JVNetConst.SRC_PARAM_ALL: {
              analyzeAll(JSONObject.parseObject(data));
              break;
            }*/
            //2.设备时间相关
            case JVNetConst.SRC_TIME: {
              switch (nPacketType) {
                //修改设备时区回调
                case JVNetConst.SRC_EX_SETTIME_ZONE: {
                  if (result == 0) {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
                    break;
                  } else {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("CHOOSE_TIME_ZONG_SET", content[checkedItemPosition]);
                    setResult(REQUEST_CODE_FOR_TIME_ZONE_SET, intent);
                    finish();
                  }
                  break;
                }

              }
              break;
            }

            default: {
              break;
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
          makeText(this, "Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }

  }
}