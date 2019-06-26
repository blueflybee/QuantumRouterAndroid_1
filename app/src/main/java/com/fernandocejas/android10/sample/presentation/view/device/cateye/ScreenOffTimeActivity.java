package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityScreenOffTimeBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
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
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class ScreenOffTimeActivity extends BaseCatActivity implements AdapterView.OnItemClickListener {
  private ActivityScreenOffTimeBinding mBinding;
  private ScreenOffTimeAdapter adapter;
  private List<String> timeTypeList;
  private String[] content = {"15秒后息屏", "1分钟后息屏", "2分钟息屏", "3分钟后息屏", "永不息屏"};
  private int checkedItemPosition = 0;
  private int REQUEST_CODE_FOR_SCREEN_TIME = 0x11;
  private int wakeTime = -1;//唤醒时长
  //intent
  private int window = 0;
  private static final String TAG = "ScreenOffTimeActivity";

  public static ScreenOffTimeActivity newInstance() {
    ScreenOffTimeActivity fragment = new ScreenOffTimeActivity();
    return fragment;
  }

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_screen_off_time);

    initView();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private String getScreenOffTime() {
    return getIntent().getStringExtra("ScreenOffTime");
  }
  private int getRingId() {
    return getIntent().getIntExtra("ringAndLCD",0);
  }

  private void initView() {
    initMoveFileTitleBar("息屏时间");
    timeTypeList = new ArrayList<>();
    for (int i = 0; i < content.length; i++) {
      timeTypeList.add(content[i]);
    }

    getScreenOffTimeIndex(getScreenOffTime());

    adapter = new ScreenOffTimeAdapter(this, timeTypeList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvScreenOffTime.setAdapter(adapter);
    mBinding.lvScreenOffTime.setOnItemClickListener(this);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(ScreenOffTimeActivity.this);
        SovUtil.setStreamSuspentTime(window,
            String.format(AppConsts.FORMATTER_NLCDSHOWTIME, convertIndexToWakeTime(checkedItemPosition)));
      }
    });

    mTitleBar.setOnSambaBackListener(new TitleBar.OnSambaBackClickListener() {
      @Override
      public void onSambaBackClick() {
        mNavigator.navigateExistAndClearTop(ScreenOffTimeActivity.this,ShowSettingActivity.class);
      }
    });

  }

  private void getScreenOffTimeIndex(String question) {
    switch (question) {
      case "15秒后息屏":
        checkedItemPosition = 0;
        break;

      case "1分钟后息屏":
        checkedItemPosition = 1;
        break;

      case "2分钟息屏":
        checkedItemPosition = 2;
        break;

      case "3分钟后息屏":
        checkedItemPosition = 3;
        break;

      case "永不息屏":
        checkedItemPosition = 4;
        break;

      default:
        break;
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvScreenOffTime.getCheckedItemPosition();

    ((TextView) view.findViewById(R.id.tv_content)).setTextColor(getResources().getColor(R.color.blue_2196f3));
    ((ImageView) view.findViewById(R.id.img_chooseLogo)).setVisibility(View.VISIBLE);

    adapter.initOtherItem(checkedItemPosition);
  }

  private int convertIndexToWakeTime(int position){
    if(position == 0){
      wakeTime = 15;
    }else if(position == 1){
      wakeTime = 60;
    }else if(position == 2){
      wakeTime = 120;
    }else if(position == 3){
      wakeTime = 180;
    }else if(position == 4){
      /*wakeTime = -1;*/
      wakeTime = -2;//-1表示设备不支持此功能，-2表示设备屏幕常亮 2018.1.4
    }
    return wakeTime;
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
            //1.显示设置回调
            case JVNetConst.SRC_DISPLAY: {
              switch (nPacketType) {
                //唤醒时长设置回调
                case JVNetConst.SRC_EX_DISPLAY_SUSPENDTIME: {
                  if (result == 0) {
                    makeText(this,"设置失败", LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(this,"设置成功", Toast.LENGTH_SHORT).show();

                    mNavigator.navigateExistAndClearTop(this,ShowSettingActivity.class);
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
          makeText(this,"Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }
  }

  @Override
  public void onBackPressed() {
    mNavigator.navigateExistAndClearTop(this,ShowSettingActivity.class);
    super.onBackPressed();
  }
}