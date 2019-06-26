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
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLanguageTypeBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityScreenOffTimeBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySetCustomQuestionBinding;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.adapter.CustomQuestionListAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.data.WaitAuthBean;
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
public class LanguageTypeActivity extends BaseCatActivity implements AdapterView.OnItemClickListener {
  private ActivityLanguageTypeBinding mBinding;
  private LanguageTypeAdapter adapter;
  private List<String> languageTypeList;
  private String[] content = {"简体中文", "英文"};
  private int checkedItemPosition = 0;//语言：0：中文，1：英文
  private int REQUEST_CODE_FOR_LANGUAGE_TYPE = 0x12;
  private static final String TAG = "LanguageTypeActivity";
  //intent
  private int window = 0;

  public static LanguageTypeActivity newInstance() {
    LanguageTypeActivity fragment = new LanguageTypeActivity();
    return fragment;
  }

  @Override
  protected void initSettings() {

  }

  @Override
  protected void initUi() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_language_type);

    initView();
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {

  }

  private String getScreenOffTime() {
    return getIntent().getStringExtra("LanguageType");
  }

  private void initView() {
    initTitleBar("语言类型");
    languageTypeList = new ArrayList<>();
    for (int i = 0; i < content.length; i++) {
      languageTypeList.add(content[i]);
    }

    getScreenOffTimeIndex(getScreenOffTime());

    adapter = new LanguageTypeAdapter(this, languageTypeList, R.layout.item_custom_question, checkedItemPosition);
    mBinding.lvLanguageType.setAdapter(adapter);
    mBinding.lvLanguageType.setOnItemClickListener(this);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogUtil.showProgress(LanguageTypeActivity.this);
        SovUtil.setStreamLanguage(window,
            String.format(AppConsts.FORMATTER_LANGUAGE, checkedItemPosition));
      }
    });
  }

  private void getScreenOffTimeIndex(String question) {
    switch (question) {
      case "简体中文":
        checkedItemPosition = 0;
        break;

      case "英文":
        checkedItemPosition = 1;
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
          /*  case JVNetConst.SRC_PARAM_ALL: {
              analyzeAll(JSONObject.parseObject(data));
              break;
            }*/
            //1.显示设置回调
            case JVNetConst.SRC_DISPLAY: {
              switch (nPacketType) {
                //语言设置回调
                case JVNetConst.SRC_EX_DISPLAY_LANGUAGE: {
                  if (result == 0) {
                    makeText(this,"设置失败", LENGTH_SHORT).show();
                  } else {
                    Toast.makeText(this, "设置成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("CHOOSE_LANGUAGE_TYPE", content[checkedItemPosition]);
                    setResult(REQUEST_CODE_FOR_LANGUAGE_TYPE, intent);
                    finish();
                  }
                  break;
                }
              }
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