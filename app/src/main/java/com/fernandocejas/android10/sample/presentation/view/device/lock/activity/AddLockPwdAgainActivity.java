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
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddLockPwdAgainBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.rsp.BleAddLockPwdResponse;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.ADD_PWD;
import static com.blueflybee.blelibrary.BleRequest.CmdType.ADD_PWD_CONFIRM;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddLockPwdAgainActivity extends BaseActivity implements AddLockPwdView {
  private static final String TAG = AddLockPwdAgainActivity.class.getSimpleName();

  private ActivityAddLockPwdAgainBinding mBinding;

  @Inject
  AddLockPwdPresenter mAddLockPwdPresenter;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_lock_pwd_again);

    initializeInjector();

    initPresenter();
    initData();

    initView();

  }

  private void initView() {
    initTitleBar("添加密码");
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setLength(6);
    watcher.addEt(mBinding.etPwd, condition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnNext.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnNext.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnNext.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  public void nextStep(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    if (pwd().equals(getText(mBinding.etPwd))) {
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
          pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
          pkg.setUserId(PrefConstant.getUserIdInHexString());
          pkg.setSeq("00");
          BleBody body = new BleBody();
          body.setCmd(BleMapper.BLE_CMD_ADD_PWD_BLE);
          body.setPayload(BleHexUtil.str2HexStr(getText(mBinding.etPwd)).replace(" ", "").toLowerCase());
          pkg.setBody(body);
          sendBlePkg(pkg, ADD_PWD);
        }
      }, 2000);
    } else {
      mBinding.tvTips.setText("两次输入密码不一致");
    }
  }

  private void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    mBle = ((AndroidApplication) getApplication()).getIBle();
    if (mBle == null || TextUtils.isEmpty(mDeviceAddress)) return;
    mBle.requestConnect(mDeviceAddress, cmdType, 30);

  }

  private String pwd() {
    String data = getIntent().getStringExtra(Navigator.EXTRA_ADD_LOCK_PWD);
    return data == null ? "" : data;
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
    setUserUsingBle(false, mDeviceAddress);
  }

  private void initPresenter() {
    mAddLockPwdPresenter.setView(this);
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
  public void showAddLockPwdSuccess(String pwdId) {
    showLoading();
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0e");
    pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_ADD_PWD_CONFIRM);
    body.setPayload(pwdId);
    pkg.setBody(body);

    if (mBle == null) return;
    BleGattService service = mBle.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      mCurrentPkg = pkg;
      mCurrentCmdType = ADD_PWD_CONFIRM;
      mBle.requestConnect(mDeviceAddress, ADD_PWD_CONFIRM, 30);
      return;
    }

    mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    mBle.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
    String data = new BleMapper().pkgToReqString(pkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    mBle.requestWriteCharacteristicToLock(ADD_PWD_CONFIRM, mDeviceAddress, mCharacteristic, data, true);
  }

  private boolean isAddPwdOnly() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_ADD_PWD_ONLY, false);
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
  public void onBackPressed() {
    super.onBackPressed();
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
        handleBleDisconnect(cmdType != ADD_PWD && cmdType != ADD_PWD_CONFIRM, false, mDeviceAddress);
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
        mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true);

      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        handleBleFail(intent, cmdType != ADD_PWD && cmdType != ADD_PWD_CONFIRM, false, mDeviceAddress);
      }

      ////////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_ADD_PWD.equals(action)) {

        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        BleAddLockPwdResponse pwdResponse = bleMapper.addLockPwd(blePkg);

        String code = pwdResponse.getCode();

        if (!bleRspFail(code, true, mDeviceAddress)) {
          String pwdId = pwdResponse.getPwdId();
          mAddLockPwdPresenter.addLockPwd(mBleLock.getId(), "默认密码", pwdId);
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(code)) {
          hideLoading();
          setUserUsingBle(false, mDeviceAddress);
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
          });
        } else {
          hideLoading();
          setUserUsingBle(false, mDeviceAddress);
        }

      } else if (BleService.BLE_ADD_PWD_CONFIRM.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

        String payload = blePkg.getBody().getPayload();

        if (!bleRspFail(payload, true, mDeviceAddress)) {
          Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
          applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
          sendBroadcast(applyKeyIntent);
          startBleTimer();
          DialogUtil.showSuccessDialog(getContext(), "设置成功", () -> {
            if (isAddPwdOnly()) {
              Intent addPwdOnlyIntent = new Intent();
              addPwdOnlyIntent.putExtra(Navigator.EXTRA_LOCK_PAGE, 1);
              mNavigator.navigateExistAndClearTop(getContext(), LockMainActivity.class, addPwdOnlyIntent);
            } else {
              Intent anIntent = new Intent();
              anIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
              if (AppConstant.isgFirstAddFingerPrint()) {
                mNavigator.navigateTo(getContext(), BindRouterToLockActivity.class, anIntent);
              } else {
                mNavigator.navigateTo(getContext(), KeepOnAddFingerprintActivity.class, anIntent);
              }
            }
          });
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
          });
        } else {
          ToastUtils.showShort("密码添加失败");
        }

      }
    }
  };
}
