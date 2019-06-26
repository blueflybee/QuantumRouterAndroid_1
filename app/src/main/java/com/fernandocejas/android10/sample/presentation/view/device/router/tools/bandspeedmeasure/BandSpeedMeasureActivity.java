package com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySpeedMeasureBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.qtec.router.model.rsp.GetBandSpeedResponse;
import com.qtec.router.model.rsp.OpenBandSpeedResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 宽带测速
 *      version: 1.0
 * </pre>
 */

public class BandSpeedMeasureActivity extends BaseActivity implements View.OnClickListener, IBandSpeedMeasureView, IOpenSpeedMeasureView {
  private ActivitySpeedMeasureBinding mBinding;
  private int mCount = 0;
  private Boolean isTesting = false; //正在测速
  private Boolean isStop = false;
  float mMaxSpeed = 0f;
  private DatabaseUtil dataBase;

  @Inject
  GetBandSpeedPresenter mGetBandSpeedPresenter;
  @Inject
  OpenBandSpeedPresenter mOpenBandSpeedPresenter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_speed_measure);
    initSpecialAttentionTitleBar("宽带测速");
    initializeInjector();
    initPresenter();
    initData();
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
    mGetBandSpeedPresenter.setView(this);
    mOpenBandSpeedPresenter.setView(this);
  }

  private void initData() {
    dataBase = new DatabaseUtil(this);
    mBinding.btnHistorySpeed.setOnClickListener(this);
    mBinding.btnMeasureSpeed.setOnClickListener(this);
    updateDrawBandSpeed("0.0",false);//初始化

/*    mBinding.seekBar.setMax(100);

    mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mBinding.instrumentView.setProgress(progress);
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });*/
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_measureSpeed:

        if (isTesting) {
          isTesting = false;
          isStop = true;
          openBandSpeedRequest(0);// 0代表取消测速
        } else {
          isStop = false;
          mBinding.tvPrompt.setText("宽带测速网关自动完成"+"\n"+"不会消耗手机流量");
          openBandSpeedRequest(1);// 1代表开启测速
        }

        break;

      case R.id.btn_historySpeed:
       /* Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_ROUTER_ID,getRouterId());*/
        startActivity(new Intent(this, BandSpeedMeasureHisActivity.class));
        break;

      default:
        break;
    }
  }

  @Override
  public void bandSpeedMeasure(GetBandSpeedResponse response) {

    if(isStop){
      isStop = false;
      return;
    }

    mBinding.btnMeasureSpeed.setText("取消测速");

    if (response.getSpeedtest() == 0) {
      isTesting = true;
      mBinding.tvTestState.setVisibility(View.VISIBLE);

      try {
        int progress = (Double.valueOf(response.getDownspeed().trim()).intValue() / (1024 * 1024));
        float downSpeed = ((Float.parseFloat(response.getDownspeed().trim())) / (1024 * 1024));

        if(mMaxSpeed <= downSpeed){
          mMaxSpeed = downSpeed;//保留最大值
        }


        // 保护机制：1/10直接舍去
        if (mMaxSpeed > downSpeed*10) {

        } else {
          if (progress < 100) {
            mBinding.instrumentView.setProgress(Double.valueOf(response.getDownspeed().trim()) / (1024 * 1024));
            updateDrawBandSpeed(String.format("%.1f", downSpeed),false);
          } else {
            mBinding.instrumentView.setProgress(100.0);
          }

        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      mCount++;
      if (mCount <= 50) {
        //延时几秒再去
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            getBandSpeedRequest();
          }
        },500);

      } else {
        Toast.makeText(this, "测速超时，请重试", Toast.LENGTH_SHORT).show();
        finish();
      }

    } else if (response.getSpeedtest() == 1) {
      mCount = 0;
      mBinding.btnMeasureSpeed.setText("重新测速");
      System.out.println("speedResult response.getDownload = " + response.getDownload());
      //存入数据库
      String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());

      BandSpeedHisBean bean = new BandSpeedHisBean();
      bean.setDate(date);
      bean.setSpeed(String.format("%.1f", Float.parseFloat(response.getDownload().trim())/8));//保留一位小数
      dataBase.insertBandSpeedHistory(bean);

      isTesting = false;

      //测速完成
      mBinding.tvTestState.setVisibility(View.GONE);
      Toast.makeText(BandSpeedMeasureActivity.this, "测速完成", Toast.LENGTH_SHORT).show();
      String speedResult = String.format("%.1f", Float.parseFloat(response.getDownload().trim())/8);
      System.out.println("speedResult finished = " + speedResult);
      this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          System.out.println("speedResult finished runOnUiThread = " + speedResult);
          mBinding.instrumentView.setProgress(Double.valueOf(response.getDownload().trim()) / 8);
          updateDrawBandSpeed(speedResult,true);
        }
      });

      mBinding.tvPrompt.setText("相当于 "+convertSpeed(Float.parseFloat(speedResult)*8)+" 宽带"+"\n"+"         ");

    }

  }

  /**
   * 网速转换区间
   *
   * @param
   * @return
   */
  private String convertSpeed(float speed) {
    String result = "";

    if (speed < 10.0) {
      result = "<10M";
    } else if (speed <= 30.0) {
      result = "20M";
    } else if (speed <= 70.0) {
      result = "50M";
    } else if (speed <= 200.0) {
      result = "100~200M";
    }else if(speed <= 500.0){
      result = "200~500M";
    }else{
      result = ">500M";
    }

    return result;
  }

  /**
   * 动态绘制网速
   *
   * @param
   * @return
   */
  private void updateDrawBandSpeed(String speed,Boolean isTestDone) {
    mBinding.instrumentView.setOnDrawBandSpeed(new InstrumentView.OnDrawBandSpeed() {
      @Override
      public void drawBandSpeed(Canvas canvas) {
        mBinding.instrumentView.drawCurrentProgressSpeed(speed,isTestDone,canvas);
      }
    });
  }

  private void getBandSpeedRequest() {
    mGetBandSpeedPresenter.getBandSpeed(GlobleConstant.getgDeviceId());
  }

  /**
   * 开启测速
   *
   * @param
   * @return
   */
  private void openBandSpeedRequest(int action) {
    mOpenBandSpeedPresenter.openBandSpeed(GlobleConstant.getgDeviceId(),action);
  }

  @Override
  public void openSpeedMeasure(OpenBandSpeedResponse response) {
    if(isStop){
      /*isStop = false;*/
      mCount = 0;
      Toast.makeText(this, "测速已取消", Toast.LENGTH_SHORT).show();
      //取消测速后归零
      mBinding.instrumentView.setProgress(0.0);
      updateDrawBandSpeed("0.0",false);//初始化

      // finish();
      isTesting = false;
      mBinding.btnMeasureSpeed.setText("一键测速");
      mBinding.tvPrompt.setText("宽带测速网关自动完成"+"\n"+"不会消耗手机流量");
      mBinding.tvTestState.setVisibility(View.GONE);

      return;
    }

    getBandSpeedRequest();//请求测速
  }

}
