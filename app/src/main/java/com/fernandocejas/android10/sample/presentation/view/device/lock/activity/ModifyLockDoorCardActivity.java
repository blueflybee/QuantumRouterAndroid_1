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
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModifyLockDoorCardBinding;
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
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;

import java.util.ArrayList;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.DEL_SINGLE_DOOR_CARD;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyLockDoorCardActivity extends BaseActivity implements ModifyLockDoorCardView {
  private static final String TAG = ModifyLockDoorCardActivity.class.getSimpleName();

  private ActivityModifyLockDoorCardBinding mBinding;

  @Inject
  protected ModifyLockDoorCardPresenter mModifyLockDoorCardPresenter;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;
  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_lock_door_card);

    initializeInjector();

    initPresenter();

    initData();

    initView();

  }

  protected void initView() {
    initTitleBar("三点安全智能门锁");
    mTitleBar.setRightAs("完成", v -> {
      doorCard().setDoorcardName(getText(mBinding.etModifyProperty));
      mModifyLockDoorCardPresenter.modifyLockDoorCard(doorCard());
    });
    changeTitleRightBtnStyle(getText(mBinding.etModifyProperty));


    InputWatcher watcher = new InputWatcher();
    watcher.addEt(mBinding.etModifyProperty);
    watcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean empty) {
        changeTitleRightBtnStyle(empty);
      }
    });
    InputUtil.allowLetterNumberChinese(mBinding.etModifyProperty, 16);

    mBinding.etModifyProperty.setText(doorCard().getDoorcardName());
    mBinding.etModifyProperty.setHint("请输入门卡名称");
  }

  private void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  public void clearText(View view) {
    mBinding.etModifyProperty.setText("");
  }

  public void deleteDoorCard(View view) {
    if (!BleLockUtils.isBleEnable()) return;
    DialogUtil.showCancelConfirmDialog(getContext(), "删除门卡", "您确定要删除门卡吗？", v -> {

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
          body.setCmd(BleMapper.BLE_CMD_DEL_SINGLE_DOOR_CARD);
          int anInt = 0;
          try {
            anInt = Integer.parseInt(doorCard().getDoorcardSerialNo());
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
          String hexCardId = Integer.toHexString(anInt);
          int i = hexCardId.length();
          if (i == 1) {
            hexCardId = "000" + hexCardId;
          } else if (i == 2) {
            hexCardId = "00" + hexCardId;
          }
          body.setPayload(hexCardId);
          pkg.setBody(body);
          sendBlePkg(pkg, DEL_SINGLE_DOOR_CARD);
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

  private GetDoorCardsResponse doorCard() {
    GetDoorCardsResponse data = (GetDoorCardsResponse) getIntent().getSerializableExtra(Navigator.EXTRA_LOCK_DOOR_CARD);
    return data == null ? new GetDoorCardsResponse() : data;
  }

  private void initPresenter() {
    mModifyLockDoorCardPresenter.setView(this);
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
  public void showModifyDoorCardNameSuccess() {
    ToastUtils.showShort("门卡名称修改成功");
    finish();
  }

  @Override
  public void showDeleteDoorCardSuccess() {
    DialogUtil.showSuccessDialog(getContext(), "门卡删除成功", new DialogUtil.DialogCallback() {
      @Override
      public void onDismiss() {
        finish();
      }
    });

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
    mModifyLockDoorCardPresenter.destroy();
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
        handleBleDisconnect(getBleCmdType(intent) != DEL_SINGLE_DOOR_CARD, false, mDeviceAddress);
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

        handleBleFail(intent, getBleCmdType(intent) != DEL_SINGLE_DOOR_CARD, false, mDeviceAddress);
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_DEL_SINGLE_DOOR_CARD.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        String payload = blePkg.getBody().getPayload();

        if (!bleRspFail(payload, true, mDeviceAddress)) {
          startBleTimer();
          Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
          applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
          sendBroadcast(applyKeyIntent);
          mModifyLockDoorCardPresenter.deleteLockDoorCard(doorCard().getDeviceSerialNo(), doorCard().getDoorcardSerialNo());
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("门卡删除失败");
        }

      }

    }
  };

}
