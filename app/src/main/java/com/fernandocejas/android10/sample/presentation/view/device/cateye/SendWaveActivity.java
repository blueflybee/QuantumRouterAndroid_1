package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySendWaveBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.jovision.JniUtil;

import java.util.Locale;

/**
 * <pre>
 *     author : xiehao
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   : 发送声波
 *     version: 1.0
 * </pre>
 */
public class SendWaveActivity extends BaseActivity implements TextToSpeech.OnInitListener {
  private ActivitySendWaveBinding mBinding;
  private int sendCounts = 3;//默认发送频次
  private int window = 0;
  private int voiceState;
  private TextToSpeech textToSpeech; // TTS对象
  private int language;//语言：0：中文，1：英文
  //  private Boolean isLastSendFinished = false; //判断上一次声波是否发送完毕
  private Boolean isSendingWave = false; //正在发送声波

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_send_wave);

    initView();

    initData();

    textToSpeech = new TextToSpeech(this, this);//语音播报
  }

  private void initView() {
    initTitleBar("发送声波");

    mTitleBar.setRightAs("下一步", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
//        Intent intent = new Intent();
//        intent.putExtra(Navigator.EXTRA_CAT_WAVE_CONFIG, true);
        mNavigator.navigateTo(getContext(), SearchLanCatActivity.class, getIntent());
      }
    });

    mTitleBar.getRightBtn().setTextColor(getResources().getColor(R.color.gray_cdcdcd));

    mTitleBar.getRvRightBtn().setEnabled(false);
    mTitleBar.getRvRightBtn().setClickable(false);

  }

  private void initData() {

    if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
      mBinding.imgDevice.setBackground(getResources().getDrawable(R.drawable.linedraft));
      mBinding.tvWaveTips.setText("将手机靠近猫头，点击发送声波按钮");

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.setPitch(0.7f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.speak("请将手机靠近猫头，点击\"发送声波\"按钮",
                TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      }, 1000);

    } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
      mBinding.imgDevice.setBackground(getResources().getDrawable(R.drawable.pic_camera_ill));
      mBinding.tvWaveTips.setText("将手机靠近设备，点击发送声波按钮");

      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            textToSpeech.setPitch(0.7f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.speak("请将手机靠近设备，点击\"发送声波\"按钮",
                TextToSpeech.QUEUE_FLUSH, null);
          }
        }
      }, 1000);
    }

  }

  private String getWiFiName() {
    return getIntent().getStringExtra("WIFINAME");
  }

  private String getWiFiPwd() {
    return getIntent().getStringExtra("WIFIPWD");
  }

  /**
   * 发送声波
   *
   * @param
   * @return
   */
  public void onSendWaveClick(View view) {

    //发送之前去轮询是否发送完成
    try {
      String param = String.format(AppConsts.SOUND_WAVE_FORMATTER, getWiFiName(), getWiFiPwd());

      mBinding.imgSendWave.setEnabled(false);
      mBinding.imgSendWave.setClickable(false);
      new Thread(new Runnable() {
        @Override
        public void run() {

          JniUtil.newSendSoundWave(param, sendCounts);//耗时操作

          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //更新UI
              System.out.println("声波 声波完成");
              mBinding.tvWaveTips.setText("听到设备发出\"收到网络配置\"，点击下一步");

                /*DialogUtil.hideProgress();*/

              mBinding.imgSendWave.setEnabled(true);
              mBinding.imgSendWave.setClickable(true);

              mTitleBar.getRightBtn().setTextColor(getResources().getColor(R.color.black_424242));
              mTitleBar.getRvRightBtn().setEnabled(true);
              mTitleBar.getRvRightBtn().setClickable(true);
            }
          });

        }
      }).start();


    } catch (Exception e) {
      e.printStackTrace();
    }

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
}
