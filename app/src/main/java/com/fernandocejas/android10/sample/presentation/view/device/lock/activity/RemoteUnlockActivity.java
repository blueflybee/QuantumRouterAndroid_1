package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCatEyeDoorbellBinding;
import com.fernandocejas.android10.sample.presentation.databinding.DialogPasswordInputBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CatEyeMainActivity;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.message.data.CatEyePushData;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;

import javax.inject.Inject;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class RemoteUnlockActivity extends Activity implements RemoteLockOperationView {

  private DialogPasswordInputBinding mBinding;

  @Inject
  RemoteLockOperationPresenter mRemoteLockOperationPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.dialog_password_input);
    setFinishOnTouchOutside(false);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mRemoteLockOperationPresenter.setView(this);
  }

  private void initView() {
    mBinding.rlClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    mBinding.tvTitle.setText("请输入登录密码用于远程开锁");

    mBinding.btnConfirm.setOnClickListener(v -> {
      remoteUnlock();
    });
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(6, 20));
    watcher.addEt(mBinding.etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnConfirm.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnConfirm.setBackgroundColor(getContext().getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnConfirm.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

  }


  @Override
  public void onBackPressed() {}

  public void ignore(View view) {
    finish();
  }

  public void goDetail(View view) {
    GlobleConstant.setgCatEyeId(getPushData().getBody().getBusinessData().getCatSerialNo());
    new Navigator().navigateTo(this, CatEyeMainActivity.class);
    finish();
  }

  public void unlock(View view) {
    remoteUnlock();
  }

  private void remoteUnlock() {
    String userPwd = PrefConstant.getUserPwd();
    String inputPwd = EncryptUtils.encryptMD5ToString(pwd()).toLowerCase();
    if (!inputPwd.equals(userPwd)) {
      ToastUtils.showShort("密码错误");
      return;
    }
    showLoading();
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0c");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE);
    body.setPayload("");
    pkg.setBody(body);
    String lockSerialNo = getPushData().getBody().getBusinessData().getLockSerialNo();
    String routerSerialNo = getPushData().getBody().getBusinessData().getRouterSerialNo();

    mRemoteLockOperationPresenter.remoteOpt(routerSerialNo, lockSerialNo, pkg);
  }

  private String pwd() {
    return mBinding.etPwd.getText().toString().trim();
  }

  private CatEyePushData getPushData() {
    CatEyePushData data = (CatEyePushData) getIntent().getSerializableExtra(Navigator.EXTRA_CAT_EYE_DOORBELL_PUSH_DATA);
    return data;
  }

  private ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }


  @Override
  public void showLoading() {
    DialogUtil.showProgress(this);
  }

  @Override
  public void hideLoading() {
    DialogUtil.hideProgress();
  }

  @Override
  public void onError(String message) {
    hideLoading();
    if (!TextUtils.isEmpty(message)) {
      ToastUtils.showShort(message);
    }
  }

  @Override
  public Context getContext() {
    return this;
  }

  @Override
  public void showLoginInvalid() {
    DialogUtil.showOkAlertDialogWithMessage(
        getContext(),
        "登录失效", "您的登录已失效，请重新登录",
        v -> new Navigator().navigateNewAndClearTask(getContext(), LoginActivity.class));
  }

  @Override
  public void showRemoteLockOptFailed(String cmdType) {
    switch (cmdType) {
      case BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE:
      case BleMapper.BLE_CMD_UNLOCK_REMOTE:
        hideLoading();
        ToastUtils.showShort("开门失败，请重试");
        break;
      default:
        break;
    }
  }

  @Override
  public void showRemoteLockOptSuccess(BlePkg blePkg, String cmdType) {

    switch (cmdType) {

      case BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE_REMOTE:
        String payload = blePkg.getBody().getPayload();
        if (TextUtils.isEmpty(payload) || payload.length() < 2) return;
        String rspCode = payload.substring(0, 2);
        if (checkBleRsp(rspCode)) return;
        String randomCode = payload.substring(2);
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("00");
        pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UNLOCK);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_UNLOCK_REMOTE);
        body.setPayload(randomCode);
        pkg.setBody(body);

        String lockSerialNo = getPushData().getBody().getBusinessData().getLockSerialNo();
        String routerSerialNo = getPushData().getBody().getBusinessData().getRouterSerialNo();
        mRemoteLockOperationPresenter.remoteOpt(routerSerialNo, lockSerialNo, pkg);
        break;

      case BleMapper.BLE_CMD_UNLOCK_REMOTE:
        if (checkBleRsp(blePkg.getBody().getPayload())) return;
        hideLoading();
        ToastUtils.showShort("已开门");
        finish();
        break;

      default:
        break;
    }
  }

  @Override
  public void onRefreshComplete() {}

  @Override
  public void onRefreshStart() {}

  @Override
  public void onRefreshError() {}

  protected boolean checkBleRsp(String code) {
    if (!BleBody.RSP_OK.equals(code)) {
      ToastUtils.showShort("门锁操作失败");
      return true;
    }
    return false;
  }
}