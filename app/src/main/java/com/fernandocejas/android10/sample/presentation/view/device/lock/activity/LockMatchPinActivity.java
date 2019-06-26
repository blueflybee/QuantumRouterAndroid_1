
package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockMatchPinBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.utils.MD5Util;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;

import java.util.ArrayList;
import java.util.UUID;

import static com.blueflybee.blelibrary.BleRequest.CmdType.APPLY_KEYS_INPUT_PIN;

public class LockMatchPinActivity extends BaseActivity {

  private static final String TAG = LockMatchPinActivity.class.getSimpleName();

  private ActivityLockMatchPinBinding mBinding;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_match_pin);

    initData();

    initView();

    sendPin();
  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  private void sendPin() {
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
        pkg.setLength("0c");
        pkg.setKeyId(BleMapper.NO_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_FIRST_GET_KEY_SEND_PIN);
        body.setPayload(MD5Util.encryption(pin() + "abcdefghij"));
        pkg.setBody(body);
        sendBlePkg(pkg, APPLY_KEYS_INPUT_PIN);
      }
    }, 1000);

  }

  private void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    mBle = ((AndroidApplication) getApplication()).getIBle();
    if (mBle == null || TextUtils.isEmpty(mDeviceAddress)) return;

    BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      mBle.requestConnect(mDeviceAddress, cmdType, 35);
      return;
    }
    BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    mBle.requestCharacteristicNotification(mDeviceAddress, characteristic);
    String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, characteristic, data, true, 30);
    hideLoading();
  }

  private void initView() {
    initTitleBar("密钥同步");
    mBinding.tvPin.setText(createPin());
    mBinding.tvPin.setSpacing(30);
  }

  private String pin() {
    return getText(mBinding.tvPin);
  }

  public void skip(View view) {
    DialogUtil.showSkipKeyInjectDialog(getContext(),
        "跳过量子密钥同步会导致门锁功能无法使用，仍然要跳过吗？",
        v -> {
          setUserUsingBle(false, mDeviceAddress);
          startBleTimer();
          close(mDeviceAddress);
          mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
        });
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

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
    private BleMapper bleMapper = new BleMapper();

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
        handleBleDisconnect(getBleCmdType(intent) != APPLY_KEYS_INPUT_PIN, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED_WHEN_APPLY_KEYS_INPUT_PIN.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");
        if (mBle == null) return;
        BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          mBle.requestConnect(mDeviceAddress, mCurrentCmdType, 32);
          return;
        }
        BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        mBle.requestCharacteristicNotification(mDeviceAddress, characteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, characteristic, data, true, 30);
        hideLoading();
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        if (getBleCmdType(intent) != APPLY_KEYS_INPUT_PIN) return;
        hideLoading();
        setUserUsingBle(false, mDeviceAddress);
        BleRequest.FailReason reason = getBleFailReason(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);
        if (reason == BleRequest.FailReason.TIMEOUT && requestType == BleRequest.RequestType.CONNECT_GATT) {
          DialogUtil.showConfirmDialog(getContext(), "超时", "蓝牙连接超时，请重新生成密码", -1, "重新生成", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              mBinding.tvPin.setText(createPin());
              mBinding.tvPin.setSpacing(30);
              sendPin();
            }
          });
        } else if (reason == BleRequest.FailReason.TIMEOUT && requestType == BleRequest.RequestType.WRITE_CHARACTERISTIC_TO_LOCK) {
          DialogUtil.showConfirmDialog(getContext(), "超时", "当前密码已经超时，请重新生成", -1, "重新生成", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              mBinding.tvPin.setText(createPin());
              mBinding.tvPin.setSpacing(30);
              sendPin();
            }
          });
        } else {
          DialogUtil.showConfirmDialog(getContext(), "蓝牙通信失败", "蓝牙通信失败，请重新生成", -1, "重新生成", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              mBinding.tvPin.setText(createPin());
              mBinding.tvPin.setSpacing(30);
              sendPin();
            }
          });
        }
      }
      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_APPLY_KEYS_INPUT_PIN.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        System.out.println("lock match pin blePkg = " + blePkg);
        String payload = blePkg.getBody().getPayload();
        if (!bleRspFail(payload, true, mDeviceAddress)) {
          startBleTimer();
          Intent injectKeyIntent = new Intent();
          injectKeyIntent.putExtra(Navigator.EXTRA_LOCK_PIN_MD5, MD5Util.encryption(pin()));
          injectKeyIntent.putExtra(Navigator.EXTRA_ONLY_INJECT_KEY, isOnlyInjectKey());
          injectKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
          mNavigator.navigateTo(getContext(), LockInjectKeyActivity.class, injectKeyIntent);
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mBinding.tvPin.setText(createPin());
              mBinding.tvPin.setSpacing(30);
              sendPin();
            }
          });
        } else {
          DialogUtil.showConfirmDialog(getContext(), "蓝牙通信失败", "蓝牙通信失败，请重新生成", -1, "重新生成", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              mBinding.tvPin.setText(createPin());
              mBinding.tvPin.setSpacing(30);
              sendPin();
            }
          });
        }


      }
    }
  };

  @NonNull
  private String createPin() {
    return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
  }
}
