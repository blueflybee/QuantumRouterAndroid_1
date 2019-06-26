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
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddLockFingerPrintBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.rsp.BleAddLockFpResponse;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.ADD_FP;
import static com.blueflybee.blelibrary.BleRequest.CmdType.ADD_FP_CONFIRM;
import static com.blueflybee.blelibrary.BleRequest.FailReason.TIMEOUT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.CONNECT_GATT;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddLockFpActivity extends BaseActivity implements AddLockFpView {

  private static final String TAG = AddLockFpActivity.class.getSimpleName();

  private ActivityAddLockFingerPrintBinding mBinding;

  @Inject
  AddLockFpPresenter mAddLockFpPresenter;

  private String mFpId;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_lock_finger_print);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initView() {
    initTitleBar("添加指纹");
  }

  public void startAddFingerprint(View view) {

    if (!BleLockUtils.isBleEnable()) return;

    mBinding.btnStartAdd.setVisibility(View.INVISIBLE);
    mBinding.tvAddTips.setVisibility(View.INVISIBLE);
    mBinding.tvAddTitle.setVisibility(View.INVISIBLE);
    mBinding.tvAdding.setVisibility(View.VISIBLE);
    mBinding.tvPressTips.setVisibility(View.VISIBLE);

    showLoading();
    setUserUsingBle(true, mDeviceAddress);
    pauseBleTimer();
    clearBleRequest();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("0c");
        pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_ADD_FP);
        body.setPayload("");
        pkg.setBody(body);
        sendBlePkg(pkg, ADD_FP);
      }
    }, 4200);

  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  private void initPresenter() {
    mAddLockFpPresenter.setView(this);
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
  public void showAddLockFpSuccess(String fpId) {
    showLoading();
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0e");
    pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_ADD_FP_CONFIRM);
    body.setPayload(fpId);
    pkg.setBody(body);

    if (mBle == null) return;
    BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      mCurrentPkg = pkg;
      mCurrentCmdType = ADD_FP_CONFIRM;
      mBle.requestConnect(mDeviceAddress, ADD_FP_CONFIRM, 30);
      return;
    }

    mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    mBle.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
    String data = new BleMapper().pkgToReqString(pkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    mBle.requestWriteCharacteristicToLock(ADD_FP_CONFIRM, mDeviceAddress, mCharacteristic, data, true, 30);
  }

  private void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    mBle = ((AndroidApplication) getApplication()).getIBle();
    if (mBle == null || TextUtils.isEmpty(mDeviceAddress)) return;
    mBle.requestConnect(mDeviceAddress, cmdType, 30);
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(mBleReceiver, BleService.getIntentFilter());
  }

  @Override
  protected void onStop() {
    super.onStop();
    boolean appBackground = ((AndroidApplication) getApplicationContext()).isAppBackground();
    if (!appBackground) unregisterReceiver(mBleReceiver);
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mAddLockFpPresenter != null) {
      mAddLockFpPresenter.destroy();
    }
    cancel(ADD_FP, ADD_FP_CONFIRM);
  }

  @Override
  public void onError(String message) {
    super.onError(message);
    setUserUsingBle(false, mDeviceAddress);
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
        BleRequest.CmdType cmdType = getBleCmdType(intent);
//        int status = getBleStatus(intent);
//        if (cmdType == ADD_FP || cmdType == ADD_FP_CONFIRM && status == 22) {
//          System.out.println("status = 22 ++++++++++++++++++++++++++++++++++++++");
//          mBle.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
//        } else {
          handleBleDisconnect(cmdType != ADD_FP && cmdType != ADD_FP_CONFIRM, false, mDeviceAddress);
//        }
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        if (mBle == null) return;
        BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          mBle.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
          return;
        }
        mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        mBle.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true, 30);
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {

        BleRequest.CmdType cmdType = getBleCmdType(intent);
        BleRequest.FailReason reason = getBleFailReason(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);

        if (reason == TIMEOUT && requestType == CONNECT_GATT) {
          hideLoading();
          setUserUsingBle(false, mDeviceAddress);
          DialogUtil.showConfirmDialog(getContext(), "指纹录入失败", "蓝牙连接超时，请重新录入", -1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });

        } else if (reason == TIMEOUT && (cmdType == ADD_FP_CONFIRM || cmdType == ADD_FP)) {
          hideLoading();
          setUserUsingBle(false, mDeviceAddress);
          DialogUtil.showConfirmDialog(getContext(), "指纹录入失败", "指纹录入时间过长，请重新录入", -1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });

        } else if (cmdType == ADD_FP_CONFIRM || cmdType == ADD_FP) {
          hideLoading();
          setUserUsingBle(false, mDeviceAddress);
          DialogUtil.showConfirmDialog(getContext(), "指纹录入失败", "指纹录入失败，请重新录入", -1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });
        }
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_ADD_FP.equals(action)) {

        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        BleAddLockFpResponse response = bleMapper.addLockFp(blePkg);

        String code = response.getCode();

        if (!bleRspFail(code, true, mDeviceAddress)) {
          mFpId = response.getFpId();
          mAddLockFpPresenter.addLockFp(mBleLock.getId(), "默认指纹", mFpId);
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(code)) {
          setUserUsingBle(false, mDeviceAddress);
          hideLoading();
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });
        } else {
          setUserUsingBle(false, mDeviceAddress);
          hideLoading();
          DialogUtil.showConfirmDialog(getContext(), "指纹录入失败", "指纹录入失败，请重新录入", -1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });
        }


      }
      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_ADD_FP_CONFIRM.equals(action)) {

        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        String payload = blePkg.getBody().getPayload();
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        if (!bleRspFail(payload, true, mDeviceAddress)) {
          Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
          applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
          sendBroadcast(applyKeyIntent);
          startBleTimer();
          DialogUtil.showSuccessDialog(getContext(), "添加成功", () -> {
            Intent aIntent = getIntent();
            aIntent.putExtra(Navigator.EXTRA_LOCK_FP_ID, mFpId);
            aIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
            mNavigator.navigateTo(getContext(), CreateFingerprintNameActivity.class, aIntent);
          });
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });
        } else {
          DialogUtil.showConfirmDialog(getContext(), "指纹录入失败", "指纹录入失败，请重新录入", -1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              refreshView();
            }
          });
        }


      }
    }
  };

  private void refreshView() {
    mBinding.btnStartAdd.setVisibility(View.VISIBLE);
    mBinding.tvAddTips.setVisibility(View.VISIBLE);
    mBinding.tvAdding.setVisibility(View.INVISIBLE);
    mBinding.tvPressTips.setVisibility(View.INVISIBLE);
    mBinding.tvAddTitle.setVisibility(View.VISIBLE);
  }

}