/*
 *
 *  * Copyright (C) 2014 Antonio Leiva Gordillo.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.keystore.data.LZKey;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityInjectKeyBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.FIRST_GET_KEYS;

public class LockInjectKeyActivity extends BaseActivity implements LockInjectKeyView {

  private static final String TAG = LockInjectKeyActivity.class.getSimpleName();

  @Inject
  LockInjectKeyPresenter mLockInjectKeyPresenter;


  private ActivityInjectKeyBinding mBinding;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;

  private BleMapper bleMapper = new BleMapper();
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inject_key);

    initializeInjector();

    initPresenter();

    initData();

    initView();

    injectKey();

  }

  private void initView() {
    initTitleBar("密钥同步");
  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  public void reloadKey(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    onKeyInjectStart();
    injectKey();
  }

  private void injectKey() {
    if (!BleLockUtils.isBleEnable()) return;
    showLoading();
    pauseBleTimer();
    setUserUsingBle(true, mDeviceAddress);
    clearBleRequest();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("12");
        pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WITH_PIN_KEY);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_APPLY_KEYS);
        body.setPayload("");
        pkg.setBody(body);
        sendBlePkg(pkg, FIRST_GET_KEYS);
      }
    }, 2500);
  }

  private void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    mBle = ((AndroidApplication) getApplication()).getIBle();
    if (mBle == null || TextUtils.isEmpty(mDeviceAddress)) return;

    BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      mBle.requestConnect(mDeviceAddress, cmdType, 30);
      return;
    }

    mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    mBle.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
    byte[] keyByteOfLockPin = BleHexUtil.hexStringToBytes(pinMd5());
    bleMapper.setKeyByteOfLockPin(keyByteOfLockPin);
    String data = bleMapper.pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true, 15);
  }

  public void injectFinish(View view) {

    if (isOnlyInjectKey()) {
      mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
    } else {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
      mNavigator.navigateExistAndClearTop(getContext(), SetLockActivity.class, intent);
    }
  }

  private void initPresenter() {
    mLockInjectKeyPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .lockModule(new LockModule())
        .build()
        .inject(this);
  }

  @Override
  public void onKeyInjectStart() {
    mBinding.tvProgress.setText("0");
    mBinding.tvProgress.setVisibility(View.VISIBLE);
    mBinding.tvTips.setVisibility(View.INVISIBLE);

    mBinding.btnReloadKey.setVisibility(View.GONE);
    mBinding.btnLoadFinish.setVisibility(View.GONE);

    mBinding.progressLoadKey.setProgress(0);
    mBinding.progressLoadKey.setBackgroundColor(getResources().getColor(R.color.gray_fafafa));
    mBinding.rlKeyProgress.setBackgroundResource(R.drawable.key_contour);
    mBinding.ivKey.setBackgroundResource(R.drawable.key_key);
  }

  @Override
  public void onKeyInjectSuccess(String routerId) {
    close(mDeviceAddress);
    mBinding.btnReloadKey.setVisibility(View.GONE);
    mBinding.btnLoadFinish.setVisibility(View.VISIBLE);
    mBinding.tvTips.setVisibility(View.VISIBLE);
    mBinding.tvTips.setText("密钥注入完成！");
    mBinding.tvTips.setTextColor(getResources().getColor(R.color.blue_2196f3));
  }

  @Override
  public void onKeyInjectFail() {
    mBinding.tvTips.setVisibility(View.VISIBLE);
    mBinding.tvTips.setText("密钥获取失败");
    mBinding.tvTips.setTextColor(getResources().getColor(R.color.red_fc7b8a));
    mBinding.tvProgress.setVisibility(View.INVISIBLE);
    mBinding.btnReloadKey.setVisibility(View.VISIBLE);
    mBinding.btnLoadFinish.setVisibility(View.GONE);
    mBinding.rlKeyProgress.setBackgroundResource(R.drawable.key_failcontour);
    mBinding.ivKey.setBackgroundResource(R.drawable.key_failkey);
    mBinding.progressLoadKey.setProgress(0);
    mBinding.progressLoadKey.setBackgroundColor(getResources().getColor(R.color.red_ff68a4));
  }

  @Override
  public void onKeyInjectProgress(int progress) {
    mBinding.progressLoadKey.setProgress(progress);
    mBinding.tvProgress.setText(progress + "%");
  }

  @Override
  public void onKeyInjectCancel(String routerId) {
//    close();
  }

  private boolean isOnlyInjectKey() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_ONLY_INJECT_KEY, false);
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(mBleReceiver, BleService.getIntentFilter());
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(mBleReceiver);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mLockInjectKeyPresenter.destroy();
  }

  private String pinMd5() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_PIN_MD5);
  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle extras = intent.getExtras();
      if (!mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR))) {
        return;
      }
      String action = intent.getAction();
      if (BleService.BLE_GATT_CONNECTED.equals(action)) {
        System.out.println(TAG + " connected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
        handleBleDisconnect(getBleCmdType(intent) != FIRST_GET_KEYS, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED_WHEN_FIRST_GET_KEYS.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        if (mBle == null) return;
        BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          mBle.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
          return;
        }
        mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        mBle.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
        byte[] keyByteOfLockPin = BleHexUtil.hexStringToBytes(pinMd5());
        bleMapper.setKeyByteOfLockPin(keyByteOfLockPin);
        String data = bleMapper.pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true, 15);

      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        handleBleFail(intent, getBleCmdType(intent) != FIRST_GET_KEYS, false, mDeviceAddress);
        if (getBleCmdType(intent) == FIRST_GET_KEYS) {
          onKeyInjectFail();
        }
      }

      ////////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_FIRST_GET_KEYS.equals(action)) {
        hideLoading();
        setUserUsingBle(false, mDeviceAddress);
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        if (values == null || values.size() < 60) {
          onKeyInjectFail();
          return;
        }
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        if (blePkg == null || blePkg.getBody() == null) {
          onKeyInjectFail();
          return;
        }
        String payload = blePkg.getBody().getPayload();
        if (payload.length() < 2) {
          onKeyInjectFail();
          return;
        }
        String code = payload.substring(0, 2);
        String keysPayload = payload.substring(2);
        if (!bleRspFail(code, true, mDeviceAddress)) {
          startBleTimer();
          bleMapper.setKeyByteOfLockPin(null);
          blePkg.getBody().setPayload(keysPayload);
          List<LZKey> keys = bleMapper.getKeys(blePkg);
          mLockInjectKeyPresenter.injectKey(keys, mBleLock.getKeyRepoId());
        } else {
          onKeyInjectFail();
        }

      }
    }
  };
}
