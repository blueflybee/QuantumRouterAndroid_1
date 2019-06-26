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
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockUserManageDetailBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockUserModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.UNBIND_LOCK;

/**
 * <pre>
 *      author: wusj
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/08
 *      desc: 门锁用户列表
 *      version: 1.0
 * </pre>
 */
public class LockUserManageDetailActivity extends BaseActivity implements LockUserView{
  private static final String TAG = LockUserManageDetailActivity.class.getSimpleName();

  private ActivityLockUserManageDetailBinding mBinding;

  @Inject
  LockUserPresenter mLockUserPresenter;

  private String mDeviceAddress;
  private BleLock mBleLock;
  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;


  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_user_manage_detail);

    initializeInjector();
    initPresenter();

    initData();

    initView();
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .lockUserModule(new LockUserModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mLockUserPresenter.setView(this);
  }

  private void initData() {
    mDeviceAddress = getLock().getMac();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  private void initView() {
    initTitleBar("用户管理");
    mBinding.tvNickName.setText(lockUser().getUserNickName());
    mBinding.tvPhone.setText(lockUser().getUserPhone());
    GlideUtil.loadCircleHeadImage(getContext(), lockUser().getUserHeadImage(), mBinding.imgHead, R.drawable.default_avatar);

    boolean isAdmin = GetUserRoleResponse.ADMIN.equals(lockUser().getUserRole());
    mBinding.btnLogoff.setVisibility(isAdmin ? View.GONE : View.VISIBLE);
  }

  private GetLockUsersResponse lockUser() {
    GetLockUsersResponse data = (GetLockUsersResponse) getIntent().getSerializableExtra(Navigator.EXTRA_LOCK_USER);
    return data == null ? new GetLockUsersResponse() : data;
  }

  public void logoff(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    DialogUtil.showLogoffLockUserDialog(getContext(), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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
            body.setCmd(BleMapper.BLE_CMD_UNBIND_LOCK);
            body.setPayload(getUserIdInHexString(lockUser().getUserId()));
            pkg.setBody(body);
            sendBlePkg(pkg, UNBIND_LOCK);
          }
        }, 2000);
      }
    });
  }

  private String getUserIdInHexString(String userId) {
    int anInt = 0;
    try {
      anInt = Integer.parseInt(userId);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    StringBuilder hexUserId = new StringBuilder(Integer.toHexString(anInt));
    int hexStringLength = hexUserId.length();
    int zeroize = 8 - hexStringLength;
    zeroize = zeroize < 0 ? 0 : zeroize;
    for (int i = 0; i < zeroize; i++) {
      hexUserId.insert(0, "0");
    }
    return hexUserId.toString();
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
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
    mLockUserPresenter.destroy();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setUserUsingBle(false, mDeviceAddress);
  }

  private void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    IBle ble = getBle();

    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      ble.requestConnect(mDeviceAddress, cmdType, 30);
      return;
    }
    BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

    ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
    String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, characteristic, data, true);

  }

  @Override
  public void showUserRole(GetUserRoleResponse response) {}

  @Override
  public void showLockUsers(List<GetLockUsersResponse> response) {}

  @Override
  public void onDeleteLockUserSuccess(DeleteLockUserResponse response) {
    ToastUtils.showShort("删除用户成功");
    finish();
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
        handleBleDisconnect(getBleCmdType(intent) != UNBIND_LOCK, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        IBle ble = getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          ble.requestConnect(mDeviceAddress, mCurrentCmdType, 30);
          return;
        }
        BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, characteristic, data, true);
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        handleBleFail(intent, getBleCmdType(intent) != UNBIND_LOCK, false, mDeviceAddress);
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_UNBIND_LOCK.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

        String payload = bleMapper.unbindUser(blePkg);
        if (TextUtils.isEmpty(payload)) {
          ToastUtils.showShort("删除用户失败");
          return;
        }

        if (!bleRspFail(payload, true, mDeviceAddress)) {
          startBleTimer();
          mLockUserPresenter.deleteLockUser(getLock().getDeviceSerialNo(), lockUser().getUserUniqueKey());
        } else {
          ToastUtils.showShort("删除用户失败");
        }
      }

    }
  };

}