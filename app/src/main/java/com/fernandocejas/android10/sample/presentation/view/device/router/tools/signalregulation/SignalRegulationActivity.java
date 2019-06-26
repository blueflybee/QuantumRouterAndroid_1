package com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySignalRegulationBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 信号调节
 *      version: 1.0
 * </pre>
 */

public class SignalRegulationActivity extends BaseActivity implements View.OnClickListener, IPostSignalInfoView ,IGetSignalModeView{
  private ActivitySignalRegulationBinding mBinding;
  @Inject
  PostSignalInfoPresenter mPostSignalInfoPresenter;
  @Inject
  GetSignalModePresenter mGetSignalModePresenter;
  private String mClickMode = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signal_regulation);
    /*initSpecialAttentionTitleBar("信号调节");*/
    initializeInjector();
    initPresenter();
    initData();

    //querySignalModeRequest();

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
    mPostSignalInfoPresenter.setView(this);
    mGetSignalModePresenter.setView(this);
  }

  private void initData() {
    mBinding.flPregnantMode.setOnClickListener(this);
    mBinding.flBalanceMode.setOnClickListener(this);
    mBinding.flEnhanceMode.setOnClickListener(this);
    mBinding.rlBack.setOnClickListener(this);

    switch (getIntent().getIntExtra("MODE",0)){
      case 0:
        mBinding.flHeadBg.setBackground(getResources().getDrawable(R.drawable.signal_minbg));
        mBinding.tvPrompt.setText("低辐射，让准妈妈安全省心");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.signal_min));
        /*mBinding.imgPregnantMode.setBackground(getResources().getDrawable(R.drawable.signal_minic));*/
        mBinding.imgPregnantMode.startWaveAnimation(R.drawable.signal_minic);
        mBinding.imgBalanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_mid));
        mBinding.imgEnhanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_max));
        break;

      case 1:
        mBinding.flHeadBg.setBackground(getResources().getDrawable(R.drawable.signal_midbg));
        mBinding.tvPrompt.setText("均衡，工作生活两不误");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.signal_mid));

        mBinding.imgPregnantMode.setBackground(getResources().getDrawable(R.drawable.signalic_min));
        /*mBinding.imgBalanceMode.setBackground(getResources().getDrawable(R.drawable.signal_midic));*/
        mBinding.imgBalanceMode.startWaveAnimation(R.drawable.signal_midic);
        mBinding.imgEnhanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_max));
        break;

      case 2:
        mBinding.flHeadBg.setBackground(getResources().getDrawable(R.drawable.signal_maxbg));
        mBinding.tvPrompt.setText("增强模式，适合大户型");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.signal_max));

        mBinding.imgPregnantMode.setBackground(getResources().getDrawable(R.drawable.signalic_min));
        mBinding.imgBalanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_mid));
        /*mBinding.imgEnhanceMode.setBackground(getResources().getDrawable(R.drawable.signal_maxic));*/
        mBinding.imgEnhanceMode.startWaveAnimation(R.drawable.signal_maxic);
        break;

      default:
        break;
    }
  }

  /**
   * 设置信号调节模式
   *
   * @param
   * @return
   */
  private void postSignalRegulationRequest(int mode) {
    mPostSignalInfoPresenter.setSignalRegulation(GlobleConstant.getgDeviceId(),mode);
  }

  private void querySignalModeRequest() {
    mGetSignalModePresenter.getSignalMode(GlobleConstant.getgDeviceId());
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.fl_pregnant_mode:

        mClickMode = "0";
        postSignalRegulationRequest(0);
        break;

      case R.id.fl_balance_mode:

        mClickMode = "1";
        postSignalRegulationRequest(1);
        break;

      case R.id.fl_enhance_mode:

        mClickMode = "2";
        postSignalRegulationRequest(2);
        break;

      case R.id.rl_back:
        finish();
        break;

      default:
        break;
    }
  }

  /**
   * 信号调节返回
   *
   * @param
   * @return
   */
  @Override
  public void postSignalRelationInfo(PostSignalRegulationResponse response) {

    switch (mClickMode){
      case "0":
        mBinding.flHeadBg.setBackground(getResources().getDrawable(R.drawable.signal_minbg));
        mBinding.tvPrompt.setText("低辐射，让准妈妈安全省心");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.signal_min));
        /*mBinding.imgPregnantMode.setBackground(getResources().getDrawable(R.drawable.signal_minic));*/

        mBinding.imgBalanceMode.stopWaveAnimation();
        mBinding.imgEnhanceMode.stopWaveAnimation();
        mBinding.imgPregnantMode.startWaveAnimation(R.drawable.signal_minic);

        mBinding.imgBalanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_mid));
        mBinding.imgEnhanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_max));
        break;

      case "1":
        mBinding.flHeadBg.setBackground(getResources().getDrawable(R.drawable.signal_midbg));
        mBinding.tvPrompt.setText("均衡，工作生活两不误");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.signal_mid));

        mBinding.imgPregnantMode.setBackground(getResources().getDrawable(R.drawable.signalic_min));
        /*mBinding.imgBalanceMode.setBackground(getResources().getDrawable(R.drawable.signal_midic));*/

        mBinding.imgPregnantMode.stopWaveAnimation();
        mBinding.imgEnhanceMode.stopWaveAnimation();
        mBinding.imgBalanceMode.startWaveAnimation(R.drawable.signal_midic);

        mBinding.imgEnhanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_max));
        break;

      case "2":
        mBinding.flHeadBg.setBackground(getResources().getDrawable(R.drawable.signal_maxbg));
        mBinding.tvPrompt.setText("增强模式，适合大户型");
        mBinding.imgHead.setBackground(getResources().getDrawable(R.drawable.signal_max));

        mBinding.imgPregnantMode.setBackground(getResources().getDrawable(R.drawable.signalic_min));
        mBinding.imgBalanceMode.setBackground(getResources().getDrawable(R.drawable.signalic_mid));
        /*mBinding.imgEnhanceMode.setBackground(getResources().getDrawable(R.drawable.signal_maxic));*/

        mBinding.imgPregnantMode.stopWaveAnimation();
        mBinding.imgBalanceMode.stopWaveAnimation();
        mBinding.imgEnhanceMode.startWaveAnimation(R.drawable.signal_maxic);
        break;

      default:
        break;
    }
  }

  @Override
  public void getSignalMode(GetSignalRegulationResponse response) {
    System.out.println("response = " + response);


  }
}
