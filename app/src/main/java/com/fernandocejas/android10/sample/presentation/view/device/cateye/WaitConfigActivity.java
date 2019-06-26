package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWaitConfigBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

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
public class WaitConfigActivity extends BaseActivity implements TextToSpeech.OnInitListener{
  private static final String TAG = WaitConfigActivity.class.getName();
  private ActivityWaitConfigBinding mBinding;

  private TextToSpeech mTextToSpeech; // TTS对象
  private boolean mIsWaveConfig = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wait_config);

    initView();

    initData();

    mTextToSpeech = new TextToSpeech(this,this);//语音播报
  }

  private void initData() {

    if(AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())){
      mBinding.imgDevice.setBackground(getResources().getDrawable(R.drawable.pic_cat_b));
      mBinding.tvTips.setText("请长按主机\"home\"键\n直至设备发出等待配置提示音");
      mBinding.tvTips.setGravity(Gravity.CENTER_HORIZONTAL);

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
            mTextToSpeech.setPitch(0.7f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTextToSpeech.speak("请长按主机home键直至设备发出等待配置提示音",
                TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      },1000);

    }else if(AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())){
      mBinding.imgDevice.setBackground(getResources().getDrawable(R.drawable.ic_pic_sxs));
      mBinding.tvTips.setText(getSpannableString());
      mBinding.tvTips.setGravity(Gravity.LEFT);

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
            mTextToSpeech.setPitch(0.7f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTextToSpeech.speak("请长按Reset键直到设备发出等待配置提示音",
                TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      },1000);

    }

  }

  private SpannableString getSpannableString() {
    SpannableString spanString = new SpannableString("手机连接Wi-Fi\n智能摄像机通电\n请长按Reset键直到设备发出等待配置提示音");
    ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#1194f6"));
    spanString.setSpan(blueSpan, 21, 26, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    return spanString;
  }

  private void initView() {
    initTitleBar("切换至等待配置");
  }

  public void onNextClick(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_CAT_WAVE_CONFIG, mIsWaveConfig);
    mNavigator.navigateTo(getContext(), ConnectWifiActivity.class, intent);
  }

  /**
   * 用来初始化TextToSpeech引擎
   * status:SUCCESS或ERROR这2个值
   * setLanguage设置语言，帮助文档里面写了有22种
   * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
   * TextToSpeech.LANG_NOT_SUPPORTED:不支持
   */
  @Override
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      int result = mTextToSpeech.setLanguage(Locale.CHINESE);

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
    mTextToSpeech.stop(); // 不管是否正在朗读TTS都被打断
    mTextToSpeech.shutdown(); // 关闭，释放资源
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    System.out.println("WaitConfigActivity.onNewIntent");
    mIsWaveConfig = true;
  }

}
