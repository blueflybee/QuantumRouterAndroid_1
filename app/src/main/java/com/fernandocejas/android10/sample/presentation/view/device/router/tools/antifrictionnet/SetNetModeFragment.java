package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.FragmentSetNetModeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.RouterComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.*;
import com.fernandocejas.android10.sample.presentation.view.fragment.BaseFragment;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.GetAntiFritNetStatusResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;
import com.qtec.router.model.rsp.GetAntiFritSwitchResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.0
 * </pre>
 */
public class SetNetModeFragment extends BaseFragment implements View.OnClickListener, AntiFritSwitchView ,IEnableAntiFritNetView,AntiFritNetStatusView,IGetAntiFritQuestionView{

  private FragmentSetNetModeBinding mBinding;
  private Boolean isLimitVisitRouter = false;
  private Boolean isLimitVisitComputer = false;
  private String mFlag = "";
  private Boolean isCloseAnti = false;
  private GetAntiFritNetStatusResponse mAntiStatus;

  @Inject
  AntiFritSwitchPresenter mAntiFritSwitchPresenter;
  @Inject
  EnableAntiFritNetPresenter mEnableNetPresenter;
  @Inject
  AntiFritNetStatusPresenter mAntiFritNetStatusPresenter;
  @Inject
  GetAntiFritQuestionPresenter mGetQuestionPresenter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(RouterComponent.class).inject(this);
  }

  public static SetNetModeFragment newInstance() {
    SetNetModeFragment fragment = new SetNetModeFragment();
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_net_mode, container, false);
    return mBinding.getRoot();
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initPresenter();
    initView();
    queryAntiFritNetStatus();
  }

  private void initView() {
    mBinding.rlSetQuestions.setOnClickListener(this);
    mBinding.imgLimitComputer.setOnClickListener(this);
    mBinding.imgLimitRouter.setOnClickListener(this);
    mBinding.btnCloseAntiFrit.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_set_questions:
        Intent intent = new Intent(getActivity(), SetQuestionActivity.class);
        intent.putExtra("FLAG","1");//非首次设置
        intent.putExtra("Anti",mAntiStatus.getEnable());
        startActivityForResult(intent,1);
        break;

      case R.id.img_limit_router:
        int status1 = convertSwitchFlag(isLimitVisitComputer);
        mFlag = "router";
        if (isLimitVisitRouter) {
          mAntiFritSwitchPresenter.postAntiFritSwitch(GlobleConstant.getgDeviceId(),0,status1);
        } else {
          mAntiFritSwitchPresenter.postAntiFritSwitch(GlobleConstant.getgDeviceId(),1,status1);
        }
        break;

      case R.id.img_limit_computer:
        int status2 = convertSwitchFlag(isLimitVisitRouter);
        mFlag = "computer";
        if (isLimitVisitComputer) {
          mAntiFritSwitchPresenter.postAntiFritSwitch(GlobleConstant.getgDeviceId(),status2,0);
        } else {
          mAntiFritSwitchPresenter.postAntiFritSwitch(GlobleConstant.getgDeviceId(),status2,1);
        }
        break;

      case R.id.btn_closeAntiFrit:
        if(mAntiStatus.getEnable() == 1){
          //关闭
          isCloseAnti = true;
          mEnableNetPresenter.enableAntiFritNet(GlobleConstant.getgDeviceId(),0,"","");
        }else{
          //开启，要带上问题:先查询问题
          getQuestionRequest();
        }

        break;

      default:
        break;


    }
  }

  private void getQuestionRequest(){
    mGetQuestionPresenter.getAntiFritQuestion(GlobleConstant.getgDeviceId());
  }

  private void initPresenter() {
    mAntiFritSwitchPresenter.setView(this);
    mEnableNetPresenter.setView(this);
    mAntiFritNetStatusPresenter.setView(this);
    mGetQuestionPresenter.setView(this);
  }

  private int convertSwitchFlag(Boolean flag){
    int status = 0;
    if(flag){
      status = 1;
    }else{
      status = 0;
    }
    return status;
  }

  @Override
  public void postAntiFritnetSwitch(GetAntiFritSwitchResponse response) {
    Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
    if("computer".equals(mFlag)){
      if (isLimitVisitComputer) {
        isLimitVisitComputer = false;
        mBinding.imgLimitComputer.setBackgroundResource(R.drawable.ic_off);
      } else {
        isLimitVisitComputer = true;
        mBinding.imgLimitComputer.setBackgroundResource(R.drawable.ic_open);
      }
    }else if("router".equals(mFlag)){
      if (isLimitVisitRouter) {
        isLimitVisitRouter = false;
        mBinding.imgLimitRouter.setBackgroundResource(R.drawable.ic_off);
        PrefConstant.IS_ANTI_LIMIT_NET = false;
      } else {
        isLimitVisitRouter = true;
        mBinding.imgLimitRouter.setBackgroundResource(R.drawable.ic_open);
        PrefConstant.IS_ANTI_LIMIT_NET = true;
      }

      System.out.println("防蹭网 postAntiFritnetSwitch IS_ANTI_LIMIT_NET = " + PrefConstant.IS_ANTI_LIMIT_NET);
    }

  }

  @Override
  public void getAntiFritNetStatus(EnableAntiFritNetResponse response) {
    //PrefConstant.IS_LOCAL_DEVICE_NOT_AUTHED

    if(isCloseAnti){
      isCloseAnti = false;
      System.out.println("防蹭网已关闭");

      PrefConstant.IS_ANTI_SWITCH = false;

      Toast.makeText(getActivity(), "防蹭网已关闭", Toast.LENGTH_SHORT).show();
      queryAntiFritNetStatus();
    }else{

      PrefConstant.IS_ANTI_SWITCH = true;

      System.out.println("防蹭网已开启");
      Toast.makeText(getActivity(), "防蹭网已开启", Toast.LENGTH_SHORT).show();
      queryAntiFritNetStatus();
    }

    System.out.println("防蹭网 EnableAntiFritNetResponse IS_ANTI_SWITCH = " + PrefConstant.IS_ANTI_SWITCH);

  }

  @Override
  public void getAntiFritnetStatus(GetAntiFritNetStatusResponse response) {
    mAntiStatus = response;
    mBinding.btnCloseAntiFrit.setVisibility(View.VISIBLE);

    if (response.getLan_dev_access() == 0) {
      isLimitVisitComputer = false;
      mBinding.imgLimitComputer.setBackgroundResource(R.drawable.ic_off);
    } else {
      isLimitVisitComputer = true;
      mBinding.imgLimitComputer.setBackgroundResource(R.drawable.ic_open);
    }

    if (response.getRouter_access() == 0) {
      isLimitVisitRouter = false;
      mBinding.imgLimitRouter.setBackgroundResource(R.drawable.ic_off);
      PrefConstant.IS_ANTI_LIMIT_NET = false;
    } else {
      isLimitVisitRouter = true;
      mBinding.imgLimitRouter.setBackgroundResource(R.drawable.ic_open);
      PrefConstant.IS_ANTI_LIMIT_NET = true;
    }

    System.out.println("防蹭网 getAntiFritnetStatus IS_ANTI_LIMIT_NET = " + PrefConstant.IS_ANTI_LIMIT_NET);

    if(response.getEnable() == 1){
      mBinding.btnCloseAntiFrit.setText("关闭防蹭网");
      PrefConstant.IS_ANTI_SWITCH = true;
    }else{
      mBinding.btnCloseAntiFrit.setText("开启防蹭网");
      PrefConstant.IS_ANTI_SWITCH = false;
    }

    System.out.println("防蹭网 getAntiFritnetStatus IS_ANTI_SWITCH = " + PrefConstant.IS_ANTI_SWITCH);
  }

  /**
   * 防蹭网状态 请求
   *
   * @param
   * @return
   */
  private void queryAntiFritNetStatus() {
    mAntiFritNetStatusPresenter.queryAntiFritNetStatus(GlobleConstant.getgDeviceId());
  }

  @Override
  public void getAntiFritQuestion(GetAntiFritQuestionResponse response) {
    //开启防蹭网
    mEnableNetPresenter.enableAntiFritNet(GlobleConstant.getgDeviceId(),1,response.getQuestion(),response.getAnswer());
  }
}