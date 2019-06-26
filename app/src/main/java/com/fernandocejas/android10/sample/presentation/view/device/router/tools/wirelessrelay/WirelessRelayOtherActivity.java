package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityWirelessRelayOtherBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.req.PostConnectWirelessRequest;
import com.qtec.router.model.rsp.GetWirelessListResponse;
import com.qtec.router.model.rsp.PostConnectWirelessResponse;

import java.util.List;

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
public class WirelessRelayOtherActivity extends BaseActivity implements PostConnectWirelessView   {
  private ActivityWirelessRelayOtherBinding mBinding;

  @Inject
  PostConnectWirelessPresenter mConnectWirelessPresenter;
  /*private MyProgressDialog mMyDialog;*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wireless_relay_other);

    initializeInjector();

    initPresenter();

    initView();

    /*watchEditTextNoClear(mBinding.etPwd);
    watchEditTextNoClear(mBinding.etName);
*/
    mBinding.etPwd.setFilters(InputUtil.emojiFilters);
    mBinding.etName.setFilters(InputUtil.emojiFilters);

  }

  private void initPresenter() {
    mConnectWirelessPresenter.setView(this);
  }

  private void initView() {
    initTitleBar("手动连接");

    InputWatcher watcher = new InputWatcher();
    watcher.addEt(mBinding.etName);

    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputRange(8, 63));

    watcher.addEt(mBinding.etPwd, pwdCondition);

    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    mBinding.etName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        mBinding.tvTips.setText("");
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    mBinding.etPwd.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        mBinding.tvTips.setText("");
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  public void confirm(View view) {
//    mAdminPwdPresenter.checkAdminPwd(getText(mBinding.etPwd));
    //判断ssid是不是在已有的列表中
    Boolean isHasSsid = false;
    int mSearchedIndex = 0;
    
    for (int i = 0; i < getWiFiList().size(); i++) {
      if(getWiFiList().get(i).getSsid().equals(getText(mBinding.etName))){
        isHasSsid = true;
        mSearchedIndex = i;
        break;
      }else{
        isHasSsid = false;
      }
    }
    
    if(isHasSsid){
      PostConnectWirelessRequest bean = new PostConnectWirelessRequest();
      bean.setPassword(getText(mBinding.etPwd));
      bean.setSsid(getText(mBinding.etName));
      bean.setMac(getWiFiList().get(mSearchedIndex).getMac());
      bean.setEncrypt(getWiFiList().get(mSearchedIndex).getEncrypt());
      bean.setMode(getWiFiList().get(mSearchedIndex).getMode());
      connectWirelessRequest(bean);
    }else {
      Toast.makeText(this, "未搜索到该wifi，请重新输入", Toast.LENGTH_SHORT).show();
      mBinding.etName.getText().clear();
      mBinding.etPwd.getText().clear();

      mBinding.etName.setFocusable(true);
      mBinding.etName.setFocusableInTouchMode(true);
      mBinding.etName.requestFocus();

      return;
    }

  }

  private void connectWirelessRequest(PostConnectWirelessRequest bean) {
    mConnectWirelessPresenter.connectWireless(GlobleConstant.getgDeviceId(), bean);
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

//  private void initPresenter() {
//    mAdminPwdPresenter.setView(this);
//  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void postConnectWireless(PostConnectWirelessResponse response) {

    /*mMyDialog.dismiss();*/

  /*  Toast.makeText(this, "设置成功，但您的网络会暂时断开几分钟，请稍候...", Toast.LENGTH_SHORT).show();
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_TO_FRAGMENT, 2);
    new Navigator().navigateTo(this, RouterMainActivity.class, intent);*/

    Intent intent = new Intent();
    intent.putExtra("EXTRA",2);
    this.setResult(55,intent);
    finish();
   // finish();
  }

  private List<GetWirelessListResponse.WirelessBean> getWiFiList(){
    return (List<GetWirelessListResponse.WirelessBean>)getIntent().getSerializableExtra("WIFILIST");
  }
}
