package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockCheckVersionBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.rsp.BleGetLockInfoResponse;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.GET_LOCK_INFO;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LockCheckVersionActivity extends BaseActivity implements LockCheckVersionView {
  private static final String TAG = LockCheckVersionActivity.class.getSimpleName();

  private static final int REQUEST_CODE_UPDATE_LOCK_FIRMWARE = 1;

  private ActivityLockCheckVersionBinding mBinding;

  @Inject
  LockCheckVersionPresenter mLockCheckVersionPresenter;

  private String mDeviceAddress;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_check_version);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initView() {
    initTitleBar("固件升级");
    showBeforeCheck();
  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  public void checkUpdate(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    showLoading();
    showCheckingFirmwareVersion();
    pauseBleTimer();
    setUserUsingBle(true, mDeviceAddress);
    clearBleRequest();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("0c");
        pkg.setKeyId(BleMapper.NO_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_GET_LOCK_INFO);
        body.setPayload("");
        pkg.setBody(body);
        connect(pkg, GET_LOCK_INFO);
      }
    }, 2000);
  }

  public void update(View view) {
    mLockCheckVersionPresenter.update();
  }

  private void connect(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    ble.requestConnect(mDeviceAddress, cmdType, 30);
  }

  private void initPresenter() {
    mLockCheckVersionPresenter.setView(this);
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
  public void onBackPressed() {
    super.onBackPressed();
    setUserUsingBle(false, mDeviceAddress);
  }

  private void write() {
    IBle ble = getBle();
    if (ble == null) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      ble.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
      return;
    }
    mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    ble.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
    String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true);
  }

  private void showBeforeCheck() {
    mBinding.tvStatus.setVisibility(View.VISIBLE);
    mBinding.tvStatus.setText(getString(R.string.text_update_lock_firmware_check));

    mBinding.btnCheckVersion.setVisibility(View.VISIBLE);
    mBinding.btnCheckVersion.setText("检查更新");
    mBinding.btnCheckVersion.setClickable(true);
    mBinding.btnCheckVersion.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
  }

  private void showCheckingFirmwareVersion() {
    mBinding.tvStatus.setVisibility(View.VISIBLE);
    mBinding.tvStatus.setText("正在检查更新，请稍候...");

    mBinding.btnCheckVersion.setVisibility(View.VISIBLE);
    mBinding.btnCheckVersion.setText("检查更新");
    mBinding.btnCheckVersion.setClickable(false);
    mBinding.btnCheckVersion.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));

    mBinding.tvCurrentVersion.setVisibility(View.GONE);
    mBinding.tvLastVersion.setVisibility(View.GONE);
    mBinding.btnUpdate.setVisibility(View.GONE);
  }

  private void showLockBleCheckFail() {
    mBinding.tvStatus.setVisibility(View.VISIBLE);
    mBinding.tvStatus.setText(getString(R.string.text_lock_ble_check_fail));

    mBinding.btnCheckVersion.setVisibility(View.VISIBLE);
    mBinding.btnCheckVersion.setText("重新检测");
    mBinding.btnCheckVersion.setClickable(true);
    mBinding.btnCheckVersion.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);

    mBinding.tvCurrentVersion.setVisibility(View.GONE);
    mBinding.tvLastVersion.setVisibility(View.GONE);
    mBinding.btnUpdate.setVisibility(View.GONE);
  }

  @Override
  public void showFirmwareVersionCheckFail() {
    mBinding.tvStatus.setVisibility(View.VISIBLE);
    mBinding.tvStatus.setText("门锁检查更新失败");

    mBinding.btnCheckVersion.setVisibility(View.VISIBLE);
    mBinding.btnCheckVersion.setText("重新检测");
    mBinding.btnCheckVersion.setClickable(true);
    mBinding.btnCheckVersion.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);

    mBinding.tvCurrentVersion.setVisibility(View.GONE);
    mBinding.tvLastVersion.setVisibility(View.GONE);
    mBinding.btnUpdate.setVisibility(View.GONE);
  }

  @Override
  public void showHasNewVersion(String currentVersion, String newVersion) {
    mBinding.tvCurrentVersion.setVisibility(View.VISIBLE);
    mBinding.tvCurrentVersion.setText("当前固件版本：" + currentVersion);

    mBinding.tvLastVersion.setVisibility(View.VISIBLE);
    mBinding.tvLastVersion.setText("发现新版本：" + newVersion);

    mBinding.btnUpdate.setVisibility(View.VISIBLE);

    mBinding.tvStatus.setVisibility(View.GONE);
    mBinding.btnCheckVersion.setVisibility(View.GONE);
  }

  @Override
  public void showIsLastedVersion(String currentVersion) {
    mBinding.tvCurrentVersion.setVisibility(View.VISIBLE);
    mBinding.tvCurrentVersion.setText("当前固件版本：" + currentVersion);

    mBinding.tvLastVersion.setVisibility(View.VISIBLE);
    mBinding.tvLastVersion.setText("当前为最新版本");

    mBinding.btnCheckVersion.setVisibility(View.VISIBLE);
    mBinding.btnCheckVersion.setText("重新检测");
    mBinding.btnCheckVersion.setClickable(true);
    mBinding.btnCheckVersion.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);

    mBinding.tvStatus.setVisibility(View.GONE);
    mBinding.btnUpdate.setVisibility(View.GONE);
  }

  @Override
  public void startUpdateFirmware(String filePath) {
    Intent intent = new Intent();
    intent.setClass(getContext(), LockUpdateFirmwareActivity.class);
    intent.putExtra(Navigator.EXTRA_LOCK_FIRMWARE_PATH, filePath);
    intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
    startActivityForResult(intent, REQUEST_CODE_UPDATE_LOCK_FIRMWARE);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_CODE_UPDATE_LOCK_FIRMWARE:
        String currentVersion = mLockCheckVersionPresenter.getCurrentVersion();
        if (resultCode == RESULT_OK) {
          ToastUtils.showShort("升级成功");
          showIsLastedVersion(currentVersion);
          LockManager.updateLockVersion(getContext(), mDeviceAddress, mLockCheckVersionPresenter.getNewVersion());
          mLockCheckVersionPresenter.updateVersion(mBleLock.getId());
        } else {
          ToastUtils.showShort("升级失败，请重试");
          String newVersion = mLockCheckVersionPresenter.getNewVersion();
          showHasNewVersion(currentVersion, newVersion);
        }
        break;
    }
  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
    private BleMapper bleMapper = new BleMapper();

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle extras = intent.getExtras();
      if (!mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR))) return;
      String action = intent.getAction();
      if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
        if (getBleCmdType(intent) != GET_LOCK_INFO) return;
        hideLoading();
        setUserUsingBle(false, mDeviceAddress);
        showLockBleCheckFail();
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");
        write();
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        if (getBleCmdType(intent) != GET_LOCK_INFO) return;
        hideLoading();
        setUserUsingBle(false, mDeviceAddress);
        showLockBleCheckFail();
      }
      ////////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_GET_LOCK_INFO.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BleGetLockInfoResponse lockInfo = bleMapper.getLockInfo(bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId()));
//        System.out.println("lockInfo = " + lockInfo);
        if (!bleRspFail(lockInfo.getRspCode(), true, mDeviceAddress)) {
          mLockCheckVersionPresenter.checkVersion(lockInfo.getVersion(), lockInfo.getModel());
        } else {
          showLockBleCheckFail();
        }
      }
    }
  };
}
