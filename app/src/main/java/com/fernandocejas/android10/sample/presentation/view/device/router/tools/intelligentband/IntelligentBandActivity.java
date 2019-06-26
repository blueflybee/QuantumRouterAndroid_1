package com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityIntelligentBandBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.router.model.req.PostQosInfoRequest;
import com.qtec.router.model.rsp.GetQosInfoResponse;
import com.qtec.router.model.rsp.PostQosInfoResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc: 智能宽带
 *      version: 1.0
 * </pre>
 */

public class IntelligentBandActivity extends BaseActivity implements View.OnClickListener, GetQosInfoView, PostQosInfoView {
  private ActivityIntelligentBandBinding mBinding;
  private Boolean isBandOpen = false;
  private int mChooseMode = 0;
  private GetQosInfoResponse mQosResponse;
  private String uploadSpeed = "";
  private String downloadSpeed = "";
  private Boolean isGetInfoRequest = false; //get请求不成功才显示断网视图

  @Inject
  GetQosInfoPresenter mGetQosInfoPresenter;
  @Inject
  PostQosInfoPresenter mPostQosInfoPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intelligent_band);
    initSpecialAttentionTitleBar("智能宽带");
    initializeInjector();
    initPresenter();
    initData();

    findViewById(R.id.ll_offline).setVisibility(View.GONE);
    mBinding.rlSwitch.setVisibility(View.VISIBLE);
    getQosInfoRequest();
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
    mGetQosInfoPresenter.setView(this);
    mPostQosInfoPresenter.setView(this);
  }

  private void initData() {
    mBinding.rlBandMode.setOnClickListener(this);
    mBinding.rlBandSpeed.setOnClickListener(this);

    mBinding.switchBtnBand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
          isBandOpen = true;
          mBinding.llIntelligentBand.setVisibility(View.VISIBLE);
        }else {
          isBandOpen = false;
          mBinding.llIntelligentBand.setVisibility(View.GONE);
        }
      }
    });

    mTitleBar.setRightAs("保存", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //更新数据
        try{

          PostQosInfoRequest bean = new PostQosInfoRequest();

          if (isBandOpen) {
            bean.setEnabled(1);
          } else {
            bean.setEnabled(0);
          }

          bean.setQosmode(mChooseMode);

          if(TextUtils.isEmpty(downloadSpeed)){
            bean.setDownload(0);
          }else {
            if("0".equals(downloadSpeed)){
              bean.setDownload(0);
            }else {
              bean.setDownload(Integer.parseInt(downloadSpeed) * 1024);
            }
          }

          if(TextUtils.isEmpty(uploadSpeed)){
            bean.setUpload(0);
          }else {
            if("0".equals(uploadSpeed)){
              bean.setUpload(0);
            }else {
              bean.setUpload(Integer.parseInt(uploadSpeed) * 1024);
            }
          }

          postQosInfoRequest(bean);

        }catch (Exception e){
          e.printStackTrace();
        }
      }
    });

    mTitleBar.getRightBtn().setTextColor(getResources().getColor(R.color.white));
  }

  private void getQosInfoRequest() {
    isGetInfoRequest = true;
    mGetQosInfoPresenter.getQosInfo(GlobleConstant.getgDeviceId());
  }

  private void postQosInfoRequest(PostQosInfoRequest bean) {
    mPostQosInfoPresenter.postQosInfo(GlobleConstant.getgDeviceId(), bean);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_band_mode:
        Intent intent1 = new Intent(this, ModeChooseActivity.class);
        intent1.putExtra("MODE", mBinding.tvSpeedMode.getText().toString().trim());
        startActivityForResult(intent1, 3);
        break;

      case R.id.rl_band_speed:
        Intent intent = new Intent(IntelligentBandActivity.this, BandSpeedLimitActivity.class);
        intent.putExtra("SPEEDINFO", mQosResponse);
        startActivityForResult(intent, 4);
        break;

      default:
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data == null) {
      return;
    }

    if (requestCode == 3) {
      String modeChoosed = data.getStringExtra("CHOOSEDMODE");
      mBinding.tvSpeedMode.setText(modeChoosed);
      switch (modeChoosed) {
        case "自动模式":
          mChooseMode = 0;
          break;

        case "游戏优先":
          mChooseMode = 1;
          break;

        case "网页优先":
          mChooseMode = 2;
          break;

        case "视频优先":
          mChooseMode = 3;
          break;

        default:
          break;
      }
    }

    if (resultCode == 4) {

      downloadSpeed = data.getStringExtra("DownloadSpeed");
      uploadSpeed = data.getStringExtra("UploadSpeed");

      mQosResponse.setDownload(data.getIntExtra("DownloadSpeed_Int",0)*1024);
      mQosResponse.setUpload(data.getIntExtra("UploadSpeed_Int",0)*1024);

    }
  }

  @Override
  public void getQosInfo(GetQosInfoResponse response) {
    mQosResponse = response;

    downloadSpeed = response.getDownload()/1024+"";

    uploadSpeed = response.getUpload()/1024+"";

    if (response.getEnabled() == 1) {

      isBandOpen = true;

      mBinding.switchBtnBand.setChecked(true);
      mBinding.llIntelligentBand.setVisibility(View.VISIBLE);

      switch (response.getQosmode()) {
        case 0:
          mBinding.tvSpeedMode.setText("自动模式");
          break;
        case 1:
          mBinding.tvSpeedMode.setText("游戏优先");
          break;
        case 2:
          mBinding.tvSpeedMode.setText("网页优先");
          break;
        case 3:
          mBinding.tvSpeedMode.setText("视频优先");
          break;

        default:
          break;
      }

    } else {
      isBandOpen = false;
      mBinding.switchBtnBand.setChecked(false);
      mBinding.llIntelligentBand.setVisibility(View.GONE);
    }

  }

  @Override
  public void postQosInfo(PostQosInfoResponse response) {

    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();

    if (isBandOpen) {
      getQosInfoRequest();
    } else {
      mBinding.switchBtnBand.setChecked(false);
      mBinding.llIntelligentBand.setVisibility(View.GONE);
    }

  }

  @Override
  public void onError(String message) {
    if(!TextUtils.isEmpty(message)){
      super.onError(message);
    }

    if(isGetInfoRequest){
      isGetInfoRequest = false;

      //网络异常
      findViewById(R.id.ll_offline).setVisibility(View.VISIBLE);
      mBinding.rlSwitch.setVisibility(View.GONE);
      findViewById(R.id.ll_offline).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          findViewById(R.id.ll_offline).setVisibility(View.GONE);
          mBinding.rlSwitch.setVisibility(View.VISIBLE);
          getQosInfoRequest();
        }
      });
    }
  }
}
