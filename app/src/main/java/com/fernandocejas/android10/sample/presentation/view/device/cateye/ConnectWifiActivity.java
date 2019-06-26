package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityConnectWifiBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.IntentUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;

import java.util.Locale;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ConnectWifiActivity extends BaseActivity implements TextToSpeech.OnInitListener {
  private ActivityConnectWifiBinding mBinding;
  private int language;//语言：0：中文，1：英文
  private int window = 0;
  private int voiceState;
  private TextToSpeech textToSpeech; // TTS对象

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_wifi);

    initView();

    initData();

    textToSpeech = new TextToSpeech(this, this);//语音播报
  }

  private void initData() {
    String wifiName = getWifiSsid().replace("\"", "");
    mBinding.etWifiName.setText(wifiName);
    moveEditCursorToEnd(mBinding.etWifiName);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
          textToSpeech.setPitch(0.7f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
          textToSpeech.speak("请输入无线密码然后点击配置按钮",
              TextToSpeech.QUEUE_FLUSH, null);
        }
      }
    }, 1000);
  }

  private void initView() {
    initTitleBar("连接无线网");

    InputWatcher watcher = new InputWatcher();
   /* InputWatcher.WatchCondition nameCondition = new InputWatcher.WatchCondition();
    nameCondition.setByteRange(new InputWatcher.InputByteRange(1, 32));*/
    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputRange(6, 20));
    /*watcher.addEt(mBinding.etWifiName, nameCondition);*/
    watcher.addEt(mBinding.etPwd, pwdCondition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnConfig.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnConfig.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnConfig.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  private String getWifiSsid() {
    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    return wifiInfo.getSSID();
  }

  public void configWifi(View view) {
    LiteGateway liteGateway = (LiteGateway) IntentUtil.getSerialExtra(this, Navigator.EXTRA_BIND_LITE_GATEWAY);
    if (liteGateway == null) {
      Intent intent = getIntent();
      intent.putExtra("WIFINAME", mBinding.etWifiName.getText().toString());
      intent.putExtra("WIFIPWD", mBinding.etPwd.getText().toString());
      if (isWaveConfig()) {
        mNavigator.navigateTo(getContext(), SendWaveActivity.class, intent);
      } else {
        mNavigator.navigateTo(getContext(), SearchLanCatActivity.class, intent);
      }
    }
  }

  @Override
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      int result = textToSpeech.setLanguage(Locale.CHINESE);
      if (result == TextToSpeech.LANG_MISSING_DATA
          || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        //Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
        System.out.println("语音播报 onInit() 数据丢失或不支持");
      }
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
    textToSpeech.shutdown(); // 关闭，释放资源
  }

  private boolean isWaveConfig() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_CAT_WAVE_CONFIG, false);
  }

}
