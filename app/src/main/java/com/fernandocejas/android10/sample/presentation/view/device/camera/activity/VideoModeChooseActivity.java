package com.fernandocejas.android10.sample.presentation.view.device.camera.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityVideoModeChooseBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.camera.adapter.SafePeriodSetingAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.jovision.JVNetConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public class VideoModeChooseActivity extends BaseCatActivity implements AdapterView.OnItemClickListener {
  private ActivityVideoModeChooseBinding mBinding;
  private SafePeriodSetingAdapter adapter;
  private List<String> timeTypeList;
  private String[] content = {"停止录像", "全天录像","报警录像","缩时录像"};
  private int checkedItemPosition = 0;
  private int REQUEST_CODE_FOR_SCREEN_TIME = 0x11;
  private int wakeTime = -1;//唤醒时长
  private int REQUEST_CODE_FOR_STORAGE = 0x55;
  //intent
  private int window = 0;
  private static final String TAG = "ScreenOffTimeActivity";

  public static VideoModeChooseActivity newInstance() {
    VideoModeChooseActivity fragment = new VideoModeChooseActivity();
    return fragment;
  }

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_mode_choose);

    initView();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private int getVideoMode() {
    return getIntent().getIntExtra("VideoMode",0);
  }

  private void initView() {
    initTitleBar("录像模式");
    timeTypeList = new ArrayList<>();
    for (int i = 0; i < content.length; i++) {
      timeTypeList.add(content[i]);
    }

    checkedItemPosition = getVideoMode();

    adapter = new SafePeriodSetingAdapter(this, timeTypeList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvSafePeriod.setAdapter(adapter);
    mBinding.lvSafePeriod.setOnItemClickListener(this);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String sendData = "";
        if (0 == checkedItemPosition) {
          sendData = String.format(Locale.CHINA, AppConsts.FORMAT_STORAGETYPE, checkedItemPosition);
        } else if (1 == checkedItemPosition) {
          sendData = String.format(Locale.CHINA, AppConsts.FORMAT_STORAGETYPE, checkedItemPosition);
        } else if (2 == checkedItemPosition) {
          sendData = String.format(Locale.CHINA, AppConsts.FORMAT_STORAGETYPE, checkedItemPosition);
        } else if (3 == checkedItemPosition) {
          sendData = String.format(Locale.CHINA, AppConsts.FORMAT_STORAGETYPE_FRAME, checkedItemPosition, 4);
        }

        DialogUtil.showProgress(VideoModeChooseActivity.this);

        SovUtil.setStreamRecordMode(window, sendData);
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    checkedItemPosition = mBinding.lvSafePeriod.getCheckedItemPosition();

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
          Log.e(TAG, "nPacketType=" + nPacketType + "；nCmd=" + nCmd + "；obj=" + obj.toString());

          switch (nCmd) {
            //配置信息全的回调
          /*  case JVNetConst.SRC_PARAM_ALL: {
              analyzeAll(JSONObject.parseObject(data));
              break;
            }*/
            //4.存储管理回调
            case JVNetConst.SRC_STORAGE: {
              switch (nPacketType) {
                case JVNetConst.SRC_EX_STORAGE_RECORDMODE:
                  if (result == 1) {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("VideoModeIndex",checkedItemPosition);
                    setResult(REQUEST_CODE_FOR_STORAGE,intent);
                    finish();
                  } else {
                    Toast.makeText(this, "设置失败", Toast.LENGTH_SHORT).show();
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
          makeText(this, "Json数据解析错误", LENGTH_SHORT).show();
        }
        break;
      }
    }
  }

}