package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockUpdateFirmwareBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.data.LockFirmwarePkg;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.blueflybee.blelibrary.BleRequest.CmdType.UPDATE_FIRMWARE_END;
import static com.blueflybee.blelibrary.BleRequest.CmdType.UPDATE_FIRMWARE_START;
import static com.blueflybee.blelibrary.BleRequest.CmdType.UPDATE_FIRMWARE_TRANS;
import static com.blueflybee.blelibrary.BleRequest.FailReason.START_FAILED;
import static com.blueflybee.blelibrary.BleRequest.FailReason.TIMEOUT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.DISCOVER_SERVICE;
import static com.blueflybee.blelibrary.BleRequest.RequestType.WRITE_CHARACTERISTIC_TO_LOCK;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class LockUpdateFirmwareActivity extends BaseActivity {
  private static final String TAG = LockUpdateFirmwareActivity.class.getSimpleName();
  public static final int SINGLE_PKG_BYTES_LENGTH = 256;

  private ActivityLockUpdateFirmwareBinding mBinding;

  private String mDeviceAddress;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;

  private List<LockFirmwarePkg> mFilePkgs = new ArrayList<>();

  private String mCheckSum = "";
  private float mFilePkgCount = 0f;

  private BleMapper mBleMapper = new BleMapper();
  private String mTransData;
  private BleLock mBleLock;

  private boolean mUpdateComplete = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_update_firmware);

    initFirmwareData();

    initData();

    initView();

    startUpdate();
  }

  private void initFirmwareData() {
    byte[] bytes = FileUtils.readFile2Bytes(getFirmwareFilePath());
    if (bytes != null) {
      mFilePkgs.clear();
      String hexFileStr = BleHexUtil.byte2HexStr(bytes).replace(" ", "").toLowerCase();
//      System.out.println("hexFileStr = " + hexFileStr);
//      System.out.println("hexFileStr.length() = " + hexFileStr.length());
      int remainder = hexFileStr.length() % SINGLE_PKG_BYTES_LENGTH;
      if (remainder != 0) {
        int supplyLength = SINGLE_PKG_BYTES_LENGTH - remainder;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < supplyLength; i++) {
          sb.append("0");
        }
        hexFileStr = hexFileStr + sb.toString();
      }
      int pkgCount = hexFileStr.length() / SINGLE_PKG_BYTES_LENGTH;
//      System.out.println("pkgCount = " + pkgCount);
      for (int i = 0; i < pkgCount; i++) {
        String subHexFileStr = "";
        if (i == pkgCount - 1 && remainder != 0) {
          subHexFileStr = hexFileStr.substring(i * SINGLE_PKG_BYTES_LENGTH);
        } else {
          subHexFileStr = hexFileStr.substring(i * SINGLE_PKG_BYTES_LENGTH, (i + 1) * SINGLE_PKG_BYTES_LENGTH);
        }
        LockFirmwarePkg firmwarePkg = new LockFirmwarePkg();
        firmwarePkg.setHexStr(subHexFileStr);
        firmwarePkg.setSeq(i);
//        System.out.println("firmwarePkg = " + firmwarePkg);
        mFilePkgs.add(firmwarePkg);
      }
    }
//    System.out.println("mFilePkgs.size() = " + mFilePkgs.size());
    if (mFilePkgs.isEmpty()) {
      setResult(RESULT_CANCELED);
      finish();
    }

    this.mFilePkgCount = mFilePkgs.size();
    calculateCheckSum();

  }

  private void calculateCheckSum() {
    StringBuilder sb = new StringBuilder();
    for (LockFirmwarePkg filePkg : mFilePkgs) {
      sb.append(filePkg.getCheckSum());
    }
    byte byteSum = 0;
    byte[] bytes = BleHexUtil.hexStringToBytes(sb.toString());
    for (byte aByte : bytes) {
      byteSum += aByte;
    }
    byte sum = (byte) (byteSum & 0XFF);
    byte[] b = new byte[1];
    b[0] = sum;
    this.mCheckSum = BleHexUtil.byte2HexStr(b).toLowerCase();
//    System.out.println("mCheckSum = " + mCheckSum);
  }

  private void initView() {
    initTitleBar("固件升级");
    mTitleBar.hideNavigation();
  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  private void startUpdate() {
    if (!BleLockUtils.isBleEnable()) return;
    showLoading();
    stopBleTimer();
    clearBleRequest();

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("00");
        pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UPDATE_FIRMWARE);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_UPDATE_FIRMWARE_START);
        body.setPayload("");
        pkg.setBody(body);
        connect(pkg, UPDATE_FIRMWARE_START);
      }
    }, 2000);
  }

  private void connect(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    ble.requestConnect(mDeviceAddress, cmdType, 30);
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(mBleReceiver, BleService.getIntentFilter());
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(mBleReceiver);
    close(mDeviceAddress);
  }

  @Override
  public void onBackPressed() {
//    super.onBackPressed();
  }

  private void onUpdateFailed(Intent intent) {
    BleRequest.CmdType cmdType = getBleCmdType(intent);
    if (cmdType != UPDATE_FIRMWARE_START && cmdType != UPDATE_FIRMWARE_TRANS && cmdType != UPDATE_FIRMWARE_END)
      return;
    hideLoading();
    startBleTimer();
    setResult(RESULT_CANCELED);
    finish();
  }

  private void onUpdateFailed() {
    hideLoading();
    startBleTimer();
    setResult(RESULT_CANCELED);
    finish();
  }

  private void transFirmware() {
    if (mFilePkgs.isEmpty()) {
      sendEndPkg();
    } else {
      sendTransPkg();
    }
    showUpdateProgress();
  }

  private void showUpdateProgress() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        int progress = (int) ((1 - (mFilePkgs.size() / mFilePkgCount)) * 100);
        mBinding.tvProgress.setText(progress + "%");
        mBinding.progressFirmwareUpdate.setProgress(progress);
      }
    });

  }

  private void sendTransPkg() {
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("00");
    pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UPDATE_FIRMWARE);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_UPDATE_FIRMWARE_TRANS);
    body.setPayload(mFilePkgs.remove(0).pkgToString());
    pkg.setBody(body);

    IBle ble = getBle();
    if (ble == null) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) return;
    BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
    mTransData = mBleMapper.pkgToReqString(pkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(UPDATE_FIRMWARE_TRANS, mDeviceAddress, characteristic, mTransData, 35, true, 15);
  }

  private void sendEndPkg() {
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("00");
    pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UPDATE_FIRMWARE);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_UPDATE_FIRMWARE_END);
    body.setPayload(mCheckSum);
    pkg.setBody(body);

    IBle ble = getBle();
    if (ble == null) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) return;
    BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
    String data = mBleMapper.pkgToReqString(pkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(UPDATE_FIRMWARE_END, mDeviceAddress, characteristic, data, true);
  }

  private String getFirmwareFilePath() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_FIRMWARE_PATH);
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
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);
        BleRequest.FailReason reason = getBleFailReason(intent);
        System.out.println(TAG + " connect BleService.BLE_GATT_DISCONNECTED = " + BleService.BLE_GATT_DISCONNECTED);
        System.out.println(TAG + " connect BleService.cmdType = " + cmdType);
        System.out.println(TAG + " connect BleService.requestType = " + requestType);
        System.out.println(TAG + " connect BleService.reason = " + reason);
        if (mUpdateComplete) return;
        onUpdateFailed(intent);
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");
        IBle ble = getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          ble.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
          return;
        }
        mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
        String data = mBleMapper.pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true);

      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {

//        LockUpdateFirmwareActivity cmdType = UPDATE_FIRMWARE_TRANS
//        LockUpdateFirmwareActivity requestType = WRITE_CHARACTERISTIC_TO_LOCK
//        LockUpdateFirmwareActivity reason = START_FAILED

//        LockUpdateFirmwareActivity BLE_REQUEST_FAILED = com.blueflybee.blelibrary.request_failed
//        LockUpdateFirmwareActivity cmdType = UPDATE_FIRMWARE_START
//        LockUpdateFirmwareActivity requestType = DISCOVER_SERVICE
//        06-08 16:17:03.592 26215-26215/com.qtec.router I/System.out: LockUpdateFirmwareActivity reason = START_FAILED
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);
        BleRequest.FailReason reason = getBleFailReason(intent);
        System.out.println(TAG + " BLE_REQUEST_FAILED = " + BleService.BLE_REQUEST_FAILED);
        System.out.println(TAG + " cmdType = " + cmdType);
        System.out.println(TAG + " requestType = " + requestType);
        System.out.println(TAG + " reason = " + reason);

        if (cmdType != UPDATE_FIRMWARE_START && cmdType != UPDATE_FIRMWARE_TRANS && cmdType != UPDATE_FIRMWARE_END)
          return;

        if (cmdType == UPDATE_FIRMWARE_TRANS && requestType == WRITE_CHARACTERISTIC_TO_LOCK && reason == START_FAILED) {
          onUpdateFailed();
          return;
        }

        if (cmdType == UPDATE_FIRMWARE_TRANS && requestType == WRITE_CHARACTERISTIC_TO_LOCK && reason == TIMEOUT) {
          onUpdateFailed();
          return;
        }

        if (cmdType == UPDATE_FIRMWARE_START && requestType == DISCOVER_SERVICE && reason == START_FAILED) {
          onUpdateFailed();
          return;
        }
//
//        if (cmdType == UPDATE_FIRMWARE_TRANS && requestType == WRITE_CHARACTERISTIC_TO_LOCK) {
//          IBle ble = getBle();
//          if (ble == null) return;
//          BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
//          if (service == null) {
//            System.out.println(TAG + " service is null = " + service);
//            onUpdateFailed(intent);
//            return;
//          }
//
//          BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));
//
//          ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
//          ble.requestWriteCharacteristicToLock(UPDATE_FIRMWARE_TRANS, mDeviceAddress, characteristic, mTransData, 35, true, 15);
//        } else {
//          hideLoading();
//          startBleTimer();
//          LockUpdateFirmwareActivity.this.setResult(RESULT_CANCELED);
//          finish();
//        }

      }

      ////////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_UPDATE_FIRMWARE_START.equals(action)) {
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = mBleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        String payload = blePkg.getBody().getPayload();
        if (!bleRspFail(payload, true, mDeviceAddress)) {
          transFirmware();
        } else {
          onUpdateFailed(intent);
        }
      } else if (BleService.BLE_UPDATE_FIRMWARE_TRANS.equals(action)) {
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = mBleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        String payload = blePkg.getBody().getPayload();
        if (!bleRspFail(payload, true, mDeviceAddress)) {
          transFirmware();
        } else {
          onUpdateFailed(intent);
        }

      } else if (BleService.BLE_UPDATE_FIRMWARE_END.equals(action)) {
        mUpdateComplete = true;
        close(mDeviceAddress);
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = mBleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        String payload = blePkg.getBody().getPayload();
        if (!bleRspFail(payload, true, mDeviceAddress)) {
          stopBleTimer();
          mBinding.tvStatus.setText("门锁正在重启，请等待");
          new TimeCounter(80000, 1000).start();
        } else {
          onUpdateFailed(intent);
        }
      }
    }

  };

  private class TimeCounter extends CountDownTimer {

    TimeCounter(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
      int progress = (int) ((1 - millisUntilFinished / 80000f) * 100);
      mBinding.tvProgress.setText(progress + "%");
      mBinding.progressFirmwareUpdate.setProgress(progress);
    }

    @Override
    public void onFinish() {
      startBleTimer();
      setResult(RESULT_OK);
      finish();
    }
  }

}
