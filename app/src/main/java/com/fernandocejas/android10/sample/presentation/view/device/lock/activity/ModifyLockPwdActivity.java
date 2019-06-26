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
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModifyLockPwdBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.DEL_SINGLE_PWD;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyLockPwdActivity extends BaseActivity implements ModifyLockPwdView {
  private static final String TAG = ModifyLockPwdActivity.class.getSimpleName();

  protected ActivityModifyLockPwdBinding mBinding;

  @Inject
  protected ModifyLockPwdPresenter mModifyLockPwdPresenter;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_lock_pwd);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  protected void initView() {
    initTitleBar("三点安全智能门锁");
    mTitleBar.setRightAs("完成", v -> {
      pwd().setPasswordName(getText(mBinding.etModifyProperty));
      mModifyLockPwdPresenter.modifyLockPwd(pwd());
    });

    changeTitleRightBtnStyle(pwd().getPasswordName());

    InputWatcher watcher = new InputWatcher();
    watcher.addEt(mBinding.etModifyProperty);
    watcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean empty) {
        changeTitleRightBtnStyle(empty);
      }
    });
    InputUtil.allowLetterNumberChinese(mBinding.etModifyProperty, 16);

    mBinding.etModifyProperty.setHint("请输入密码名称");
    mBinding.etModifyProperty.setText(pwd().getPasswordName());
  }

  public void clearText(View view) {
    mBinding.etModifyProperty.setText("");
  }

  public void deletePwd(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    DialogUtil.showCancelConfirmDialog(getContext(), "删除密码", "您确定要删除密码吗？", v -> {

      showLoading();
      pauseBleTimer();
      setUserUsingBle(true, mDeviceAddress);
      clearBleRequest();
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          BlePkg pkg = new BlePkg();
          pkg.setHead("aa");
          pkg.setLength("0e");
          pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
          pkg.setUserId(PrefConstant.getUserIdInHexString());
          pkg.setSeq("00");
          BleBody body = new BleBody();
          body.setCmd(BleMapper.BLE_CMD_DEL_SINGLE_PWD);
          int anInt = 0;
          try {
            anInt = Integer.parseInt(pwd().getPasswordSerialNo());
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
          String hexPwdId = Integer.toHexString(anInt);
          int i = hexPwdId.length();
          if (i == 1) {
            hexPwdId = "000" + hexPwdId;
          } else if (i == 2) {
            hexPwdId = "00" + hexPwdId;
          }
          body.setPayload(hexPwdId);
          pkg.setBody(body);
          sendBlePkg(pkg, DEL_SINGLE_PWD);
        }
      }, 2000);

    });

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
    String data = new BleMapper().pkgToReqString(pkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    mBle.requestWriteCharacteristicToLock(cmdType, mDeviceAddress, mCharacteristic, data, true);

  }

  private GetPasswordsResponse pwd() {
    GetPasswordsResponse data = (GetPasswordsResponse) getIntent().getSerializableExtra(Navigator.EXTRA_LOCK_PWD);
    return data == null ? new GetPasswordsResponse() : data;
  }

  @Override
  public void showModifyPwdNameSuccess() {
    ToastUtils.showShort("密码名称修改成功");
    finish();
  }

  @Override
  public void showDeletePwdSuccess() {
    DialogUtil.showSuccessDialog(getContext(), "密码删除成功", new DialogUtil.DialogCallback() {
      @Override
      public void onDismiss() {
        finish();
      }
    });
  }

  private void initPresenter() {
    mModifyLockPwdPresenter.setView(this);
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
  protected void onStop() {
    super.onStop();
    boolean appBackground = ((AndroidApplication) getApplicationContext()).isAppBackground();
    if (!appBackground) unregisterReceiver(mBleReceiver);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mModifyLockPwdPresenter.destroy();
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

        handleBleDisconnect(getBleCmdType(intent) != DEL_SINGLE_PWD, false, mDeviceAddress);

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
        handleBleFail(intent, getBleCmdType(intent) != DEL_SINGLE_PWD, false, mDeviceAddress);
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_DEL_SINGLE_PWD.equals(action)) {
        hideLoading();
        setUserUsingBle(false, mDeviceAddress);
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        String payload = blePkg.getBody().getPayload();

        if (!bleRspFail(payload, true, mDeviceAddress)) {
          startBleTimer();
          Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
          applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
          sendBroadcast(applyKeyIntent);
          mModifyLockPwdPresenter.deleteLockPwd(pwd().getDeviceSerialNo(), pwd().getPasswordSerialNo());
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("密码删除失败");
        }

      }

    }
  };

}
