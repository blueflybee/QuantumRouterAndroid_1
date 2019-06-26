package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterRestartBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.TimeUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.req.SetRouterTimerRequest;
import com.qtec.router.model.rsp.GetTimerTaskResponse;
import com.qtec.router.model.rsp.SetRouterTimerResponse;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RestartRouterActivity extends BaseActivity implements RestartRouterView {
  private ActivityRouterRestartBinding mBinding;
  private String[] stringTime;

  @Inject
  RestartRouterPresenter mRestartRouterPresenter;

  private SetRouterTimerRequest mSetRouterTimerRequest = new SetRouterTimerRequest();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_router_restart);

    initializeInjector();

    initPresenter();

    initView();

    mRestartRouterPresenter.getTimerTask();

  }

  private void initView() {
    initTitleBar("网关定时重启");

    mTitleBar.setRightAs("完成", v -> {

      //参数校验
      //关闭时不做校验,参数校验
      if(mBinding.switchRestart.isChecked()){
        //  getTimerDays().substring(0,getTimerDays().length()-1) 去掉最后一个,号
        if(TextUtils.isEmpty(getTimerDays())){
          Toast.makeText(this, "请选择重复周期", Toast.LENGTH_SHORT).show();
          return;
        }
      }

      mSetRouterTimerRequest.setTasktype(2);

      if(TextUtils.isEmpty(getTimerDays())){
        mSetRouterTimerRequest.setDay("");
      }else {
        mSetRouterTimerRequest.setDay(getTimerDays().substring(0,getTimerDays().length()-1));//去掉最后一个,
      }

      mRestartRouterPresenter.setRouterTimer(mSetRouterTimerRequest);
    });

   // changeTitleRightBtnStyle(!mBinding.switchRestart.isChecked());

    mBinding.switchRestart.setOnCheckedChangeListener((buttonView, isChecked) -> {
      mBinding.rlRestartTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      mBinding.rlRepeatCycle.setVisibility(isChecked ? View.VISIBLE : View.GONE);
      mSetRouterTimerRequest.setEnable(isChecked ? 1 : 0);
     // changeTitleRightBtnStyle(!isChecked);
    });

  }

  public void pickTime(View view) {
    DialogUtil.showTimePick(getContext(), new DialogUtil.OnTimeSelectedListener() {
      @Override
      public void onTimeSelected(String[] times) {
        mBinding.tvTime.setText(times[0] + ":" + times[1]);
      }

      @Override
      public void onTimeSelectedInInt(int[] times) {
        mSetRouterTimerRequest.setHour(times[0]);
        mSetRouterTimerRequest.setMinute(times[1]);
      }
    },stringTime[0],stringTime[1]);
  }


  private void initPresenter() {
    mRestartRouterPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void getTimerTaskSuccess(GetTimerTaskResponse response) {
    mBinding.setRsp(response);
    stringTime = TimeUtil.getStringTime(new int[]{response.getHour(), response.getMinute()});
    mBinding.tvTime.setText(stringTime[0] + ":" + stringTime[1]);

    mSetRouterTimerRequest.setEnable(mBinding.switchRestart.isChecked() ? 1 : 0);
    mSetRouterTimerRequest.setHour(response.getHour());
    mSetRouterTimerRequest.setMinute(response.getMinute());
  }

  @Override
  public void setRouterTimerSuccess(SetRouterTimerResponse response) {
    ToastUtils.showShort("网关定时重启设置成功");
    finish();
  }

  private String getTimerDays() {
    StringBuilder sb = new StringBuilder();
    sb.append(mBinding.cbZero.isChecked() ? "0," : "");
    sb.append(mBinding.cbOne.isChecked() ? "1," : "");
    sb.append(mBinding.cbTwo.isChecked() ? "2," : "");
    sb.append(mBinding.cbThree.isChecked() ? "3," : "");
    sb.append(mBinding.cbFour.isChecked() ? "4," : "");
    sb.append(mBinding.cbFive.isChecked() ? "5," : "");
    sb.append(mBinding.cbSix.isChecked() ? "6," : "");
    return sb.toString();
  }

}
