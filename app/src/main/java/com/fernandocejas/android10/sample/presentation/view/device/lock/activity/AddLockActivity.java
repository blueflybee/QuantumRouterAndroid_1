package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest.CmdType;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAddLockBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.utils.MD5Util;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.lock.model.rsp.BleCheckLockAdminPwdResponse;
import com.qtec.lock.model.rsp.BleGetLockInfoResponse;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.CHECK_ADMIN_PASSWORD;
import static com.blueflybee.blelibrary.BleRequest.CmdType.CLEAR_FACTORY_RESET_FLAG;
import static com.blueflybee.blelibrary.BleRequest.CmdType.GET_LOCK_INFO;
import static com.blueflybee.blelibrary.BleRequest.CmdType.UPDATE_USER_INFO_OF_LOCK;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AddLockActivity extends BaseActivity implements AddLockView {
  private static final String TAG = AddLockActivity.class.getName();
  private static final String MD5_STR = "abcdefgh";
  private ActivityAddLockBinding mBinding;

  @Inject
  AddLockPresenter mAddLockPresenter;

  private String mDeviceAddress;

  private BleGattCharacteristic mCharacteristic;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private CmdType mCurrentCmdType;
  private BleLock mBleLock;

  private boolean mIsClearFactoryResetFlag;
  private BleGetLockInfoResponse mLockInfo;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_lock);

    initializeInjector();

    initPresenter();

    initView();

    initData();

  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  private void initView() {
    initTitleBar("添加门锁");

    mBinding.tvLockName.setText(bleName());
  }

  public void addLock(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    DialogUtil.showLockAdminPasswordDialog(getContext(), "请输入管理员密码", new DialogUtil.OnPasswordInputFinishedListener() {
      @Override
      public void onFinished(View view, String pwd) {
        showLoading();
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("0c");
        pkg.setKeyId(BleMapper.NO_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_CHECK_ADMIN_PASSWORD);
        body.setPayload(MD5Util.encryption(pwd + MD5_STR));
        pkg.setBody(body);
        sendBlePkg(pkg, CHECK_ADMIN_PASSWORD);
      }
    });

  }

  private void initPresenter() {
    mAddLockPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .lockModule(new LockModule())
        .build()
        .inject(this);
  }

  @Override
  public void onAddLockSuccess(CommitAddRouterInfoResponse response) {
    DialogUtil.showSuccessDialog(getContext(), "添加成功", () -> {
      final Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
      mNavigator.navigateTo(getContext(), LockMatchPinActivity.class, intent);
    });
    mAddLockPresenter.getUsersOfLock(mAddLockPresenter.getLockId());
  }

  @Override
  public void onGetUsersOfLockSuccess(String userIds) {
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("00");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_UPDATE_USER_INFO_OF_LOCK);
    body.setPayload(userIds);
    pkg.setBody(body);
    sendBlePkg(pkg, UPDATE_USER_INFO_OF_LOCK);
  }

  @Override
  public void onFactoryResetSuccess(LockFactoryResetResponse response) {
    showLoading();
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("00");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_CLEAR_FACTORY_RESET_FLAG);
    body.setPayload("");
    pkg.setBody(body);
    sendBlePkg(pkg, CLEAR_FACTORY_RESET_FLAG);
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
    mAddLockPresenter.destroy();
  }

  private String bleName() {
    return getIntent().getStringExtra(Navigator.EXTRA_BLE_DEVICE_NAME);
  }


  private void getLockInfo() {
    showLoading();
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
    sendBlePkg(pkg, GET_LOCK_INFO);
  }

  private void sendBlePkg(BlePkg pkg, CmdType cmdType) {
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
    String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    mBle.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true);
  }


  private void addLock(BleGetLockInfoResponse lockInfo) {
    GetDevTreeResponse<List<DeviceBean>> lock = new GetDevTreeResponse<>();
    lock.setMac(mDeviceAddress);
    lock.setDeviceSerialNo(lockInfo.getDeviceId());
    lock.setDeviceModel(lockInfo.getModel());
    lock.setDeviceName(lockInfo.getName());
    lock.setDeviceType(AppConstant.DEVICE_TYPE_LOCK);
    lock.setDeviceVersion(lockInfo.getVersion());

    LockManager.add(getContext(), lock);
    mAddLockPresenter.addLockCloud(lockInfo, mDeviceAddress, bleName());
    mAddLockPresenter.setLockId(lockInfo.getDeviceId());
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
        handleBleDisconnect(getBleCmdType(intent) != GET_LOCK_INFO
            && getBleCmdType(intent) != CHECK_ADMIN_PASSWORD
            && getBleCmdType(intent) != CLEAR_FACTORY_RESET_FLAG);

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
        handleBleFail(intent,
            getBleCmdType(intent) != GET_LOCK_INFO
                && getBleCmdType(intent) != CHECK_ADMIN_PASSWORD
                && getBleCmdType(intent) != CLEAR_FACTORY_RESET_FLAG);
      }
      ////////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_GET_LOCK_INFO.equals(action)) {
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        mLockInfo = bleMapper.getLockInfo(bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId()));
        System.out.println("lockInfo = " + mLockInfo);
        if (!bleRspFail(mLockInfo.getRspCode(), true, mDeviceAddress)) {

          if (!mIsClearFactoryResetFlag) {
            addLock(mLockInfo);
          } else {
            mAddLockPresenter.lockFactoryReset(mLockInfo.getDeviceId());
          }
        } else {
          ToastUtils.showLong("门锁信息获取失败");
        }


      } else if (BleService.BLE_CHECK_ADMIN_PASSWORD.equals(action)) {
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg pkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        System.out.println("pkg36 = " + pkg);
        BleCheckLockAdminPwdResponse response = bleMapper.checkLockAdminPwd(pkg);
        if (response == null) {
          ToastUtils.showShort("校验管理员密码失败");
          return;
        }

        System.out.println("response = " + response);
        if (!bleRspFail(response.getRspCode(), true, mDeviceAddress)) {
          mIsClearFactoryResetFlag = response.isClearFactoryResetFlag();
          getLockInfo();
        } else {
          ToastUtils.showLong("校验管理员密码失败");
        }


      } else if (BleService.BLE_CLEAR_FACTORY_RESET_FLAG.equals(action)) {
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg pkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

        String payload = bleMapper.payload(pkg);
        if (TextUtils.isEmpty(payload)) {
          ToastUtils.showShort("清除门锁出厂设置失败");
          return;
        }

        System.out.println("payload = " + payload);
        if (!bleRspFail(payload, true, mDeviceAddress)) {
          addLock(mLockInfo);
        } else {
          ToastUtils.showLong("清除门锁出厂设置失败");
        }


      }
    }
  };


}
