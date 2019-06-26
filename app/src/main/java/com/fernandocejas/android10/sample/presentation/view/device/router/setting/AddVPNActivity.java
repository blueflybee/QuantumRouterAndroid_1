package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddVpnBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.router.model.req.AddVpnRequest;
import com.qtec.router.model.req.ModifyVpnRequest;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.DeleteVpnResponse;
import com.qtec.router.model.rsp.GetVpnListResponse;
import com.qtec.router.model.rsp.ModifyVpnResponse;

import java.util.regex.Pattern;

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
public class AddVPNActivity extends BaseActivity implements AddVpnView, DeleteVpnView, ModifyVpnView, View.OnClickListener {
  private ActivityAddVpnBinding mBinding;
  private String mode = "l2tp";//默认

  @Inject
  AddVpnPresenter mAddVpnPresenter;
  @Inject
  DeleteVpnPresenter mDeleteVpnPresenter;
  @Inject
  ModifyVpnPresenter mModifyVpnPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_vpn);

    initializeInjector();
    initPresenter();
    initTitleBar("添加VPN");
    initView();

    mTitleBar.setRightAs("完成", v -> {

      if(TextUtils.isEmpty(mBinding.etDesc.getText().toString().trim())){
        Toast.makeText(this, "描述信息不能为空", Toast.LENGTH_SHORT).show();
        return;
      }

      if(TextUtils.isEmpty(mBinding.etServerAddress.getText().toString().trim())){
        Toast.makeText(this, "服务地址不能为空", Toast.LENGTH_SHORT).show();
        return;
      }
      //ip地址支持域名设置,全为数字和.则为IP，其他为域名
      if(InputUtil.judgeContainsLetter(mBinding.etServerAddress.getText().toString().trim())){
        //域名：包含字母
        System.out.println("vpn 域名匹配");
        if(!Pattern.compile(InputUtil.REGULAR_DOMAIN).matcher(mBinding.etServerAddress.getText().toString().trim()).matches()){
          Toast.makeText(this, "服务地址错误", Toast.LENGTH_SHORT).show();
          mBinding.etServerAddress.getText().clear();
          return;
        }
      }else {
        System.out.println("vpn ip匹配");
        if(!Pattern.compile(InputUtil.REGULAR_IP).matcher(mBinding.etServerAddress.getText().toString().trim()).matches()){
          Toast.makeText(this, "服务地址错误", Toast.LENGTH_SHORT).show();
          mBinding.etServerAddress.getText().clear();
          return;
        }
      }

      if(TextUtils.isEmpty(mBinding.etAccount.getText().toString().trim())){
        Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        return;
      }

      if(TextUtils.isEmpty(mBinding.et5gWifiPwd.getText().toString().trim())){
        Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
        return;
      }

      if ("0".equals(getFlag())) {
        //新增
        if (getVpnListSize() == 8) {
          Toast.makeText(this, "最多只能添加8个VPN", Toast.LENGTH_SHORT).show();
          return;
        }

        AddVpnRequest bean = new AddVpnRequest();
        bean.setDescription(getText(mBinding.etDesc));
        bean.setMode(mode);
        bean.setPassword(getText(mBinding.et5gWifiPwd));
        bean.setUsername(getText(mBinding.etAccount));
        bean.setServer_ip(getText(mBinding.etServerAddress));

        addVpnRequest(bean);
      } else {
        // 修改
        ModifyVpnRequest bean = new ModifyVpnRequest();
        bean.setDescription(getText(mBinding.etDesc));
        bean.setMode(mode);
        bean.setPassword("");//因为网关不返回密码了
        bean.setUsername(getText(mBinding.etAccount));
        bean.setServer_ip(getText(mBinding.etServerAddress));
        bean.setIfname(getVpnDetail().getIfname());

        modifyVpnRequest(bean);
      }

    });
  }

  private void initView() {

    if ("0".equals(getFlag())) {
      // 新增
      mBinding.btnDeleteVpn.setVisibility(View.GONE);
    } else {
      mBinding.btnDeleteVpn.setVisibility(View.VISIBLE);
      initData();
    }

    mBinding.rlL2tp.setOnClickListener(this);
    mBinding.rlPptp.setOnClickListener(this);

    //控制表情输入
    watchEditTextNoClear(mBinding.etServerAddress);
    watchEditTextNoClear(mBinding.et5gWifiPwd);
    watchEditTextNoClear(mBinding.etAccount);
    watchEditTextNoClear(mBinding.etDesc);

    InputUtil.inhibitSpace(mBinding.etDesc);//禁止输入空格
    InputUtil.inhibitSpace(mBinding.etServerAddress);//禁止输入空格
    InputUtil.inhibit(mBinding.etAccount, InputUtil.REGULAR_CHINESE_AND_SPACE, 32);

    InputWatcher watcher = new InputWatcher();

    InputWatcher.WatchCondition ipCondition = new InputWatcher.WatchCondition();
    ipCondition.setInputRegular(new InputWatcher.InputRegular(InputUtil.REGULAR_IP));

    InputWatcher.WatchCondition descCondition = new InputWatcher.WatchCondition();
    descCondition.setMinLength(1);

    InputWatcher.WatchCondition accountCondition = new InputWatcher.WatchCondition();
    accountCondition.setMinLength(1);

    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputByteRange(1,32));

    watcher.addEt(mBinding.etDesc, descCondition);
    watcher.addEt(mBinding.etServerAddress, ipCondition);//IP正则表达式

    watcher.addEt(mBinding.etAccount, accountCondition);
    watcher.addEt(mBinding.et5gWifiPwd, pwdCondition);

 /*   watcher.setInputListener(isEmpty -> {
      mTitleBar.getRightBtn().setClickable(!isEmpty);
      if (isEmpty) {
        mTitleBar.setRightEnable(false, getResources().getColor(R.color.gray_cdcdcd));
      } else {
        mTitleBar.setRightEnable(true, getResources().getColor(R.color.black_424242));
      }
    });*/

  }

  private void initData() {
    mBinding.etDesc.setText(getVpnDetail().getDescription());
    //当号码返回时光标放在最后
    Selection.setSelection(mBinding.etDesc.getText(), mBinding.etDesc.getText().length());

    /*mBinding.et5gWifiPwd.setText("");*/

    mBinding.etAccount.setText(getVpnDetail().getUsername());
    mBinding.etServerAddress.setText(getVpnDetail().getServer_ip());
    if ("l2tp".equals(getVpnDetail().getMode())) {
      mode = "l2tp";
      mBinding.tvL2tp.setTextColor(getResources().getColor(R.color.blue_2196f3));
      mBinding.imgL2tp.setVisibility(View.VISIBLE);
      mBinding.tvPptp.setTextColor(R.color.black_424242);
      mBinding.imgPptp.setVisibility(View.GONE);
    } else {
      mode = "pptp";
      mBinding.tvL2tp.setTextColor(R.color.black_424242);
      mBinding.imgL2tp.setVisibility(View.GONE);
      mBinding.tvPptp.setTextColor(getResources().getColor(R.color.blue_2196f3));
      mBinding.imgPptp.setVisibility(View.VISIBLE);
    }

    mBinding.btnDeleteVpn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        deleteVpnRequest(getVpnDetail().getIfname());
      }
    });

  }


  public void showHidePwd(View view) {
    InputUtil.showHidePwd(mBinding.et5gWifiPwd);
  }


  private void initPresenter() {
    mAddVpnPresenter.setView(this);
    mDeleteVpnPresenter.setView(this);
    mModifyVpnPresenter.setView(this);
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
  public void addVpn(AddVpnResponse response) {
    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    setResult(6);
    finish();
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.rl_l2tp:
        mode = "l2tp";
        mBinding.tvL2tp.setTextColor(getResources().getColor(R.color.blue_2196f3));
        mBinding.imgL2tp.setVisibility(View.VISIBLE);
        mBinding.tvPptp.setTextColor(R.color.black_424242);
        mBinding.imgPptp.setVisibility(View.GONE);
        break;

      case R.id.rl_pptp:
        mode = "pptp";
        mBinding.tvL2tp.setTextColor(R.color.black_424242);
        mBinding.imgL2tp.setVisibility(View.GONE);
        mBinding.tvPptp.setTextColor(getResources().getColor(R.color.blue_2196f3));
        mBinding.imgPptp.setVisibility(View.VISIBLE);
        break;

      default:
        break;
    }
  }


  private void addVpnRequest(AddVpnRequest bean) {
    mAddVpnPresenter.addVpn(GlobleConstant.getgDeviceId(), bean);
  }

  private void deleteVpnRequest(String key) {
    mDeleteVpnPresenter.deleteVpn(GlobleConstant.getgDeviceId(), key);
  }

  private void modifyVpnRequest(ModifyVpnRequest bean) {
    mModifyVpnPresenter.modifyVpn(GlobleConstant.getgDeviceId(), bean);
  }

  private GetVpnListResponse.VpnBean getVpnDetail() {
    return (GetVpnListResponse.VpnBean) getIntent().getSerializableExtra("VPNINFO");
  }

  private int getVpnListSize() {
    return getIntent().getIntExtra("VpnListSize", 0);
  }

  private String getFlag() {
    return getIntent().getStringExtra("FLAG");
  }

  @Override
  public void deleteVpn(DeleteVpnResponse response) {
    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    setResult(6);
    finish();

  }

  @Override
  public void modifyVpn(ModifyVpnResponse response) {
    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
    setResult(6);
    finish();
  }
}
