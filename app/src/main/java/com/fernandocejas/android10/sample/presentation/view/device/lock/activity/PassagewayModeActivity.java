package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.CompoundButton;

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
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockPassagewayModeBinding;
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
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetPassagewayModeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.CONFIG_PASSAGEWAY_MODE;
import static com.blueflybee.blelibrary.BleRequest.FailReason.TIMEOUT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.DISCOVER_SERVICE;


public class PassagewayModeActivity extends BaseActivity implements PassagewayModeView {

  private static final String TAG = PassagewayModeActivity.class.getSimpleName();

  private ActivityLockPassagewayModeBinding mBinding;

  @Inject
  PassagewayModePresenter mPassagewayModePresenter;

  private String mDeviceAddress;
  private BlePkg mCurrentPkg;
  private BleLock mBleLock;

  private boolean mPassagewayOpen = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_passageway_mode);

    initData();

    initializeInjector();
    initPresenter();
    initView();

    mPassagewayModePresenter.getPassagewayMode(getLock().getDeviceSerialNo());
  }

  private void initView() {
    initTitleBar("通道模式");
    mTitleBar.setRightAs("完成", v -> {
      configPassagewayMode();
    });

    mBinding.switchPassageway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mPassagewayOpen = isChecked;
      }
    });
  }

  private void initData() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> lock = getLock();
    mDeviceAddress = lock == null ? "" : lock.getMac();
    mBleLock = LockManager.getLock(getContext(), lock.getMac());
  }

  private void configPassagewayMode() {
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
        pkg.setLength("0d");
        pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_CONFIG_PASSAGEWAY);
        body.setPayload(mPassagewayOpen ? "01" : "00");
        pkg.setBody(body);
        connect(pkg, CONFIG_PASSAGEWAY_MODE);
      }
    }, 2000);
  }

  private void connect(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    ble.requestConnect(mDeviceAddress, cmdType, 30);
  }

  private void initPresenter() {
    mPassagewayModePresenter.setView(this);
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
  public void onBackPressed() {
    super.onBackPressed();
    setUserUsingBle(false, mDeviceAddress);
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
  }


  @Override
  public void showPassageway(GetPassagewayModeResponse response) {
    boolean checked = !GetPassagewayModeResponse.CHANNEL_CLOSED.equals(response.getChannelConfig());
    mBinding.switchPassageway.setChecked(checked);
  }


  @Override
  public void showModifyPassagewaySuccess() {
    ToastUtils.showShort("通道模式设置成功");
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
        handleBleDisconnect(getBleCmdType(intent) != CONFIG_PASSAGEWAY_MODE, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        IBle ble = getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          ble.requestConnect(mDeviceAddress, CONFIG_PASSAGEWAY_MODE, 30);
          return;
        }
        BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(CONFIG_PASSAGEWAY_MODE, mDeviceAddress, characteristic, data, true);

      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        BleRequest.FailReason reason = getBleFailReason(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);
        if (cmdType == CONFIG_PASSAGEWAY_MODE && reason == TIMEOUT && requestType == DISCOVER_SERVICE) {
          connect(mCurrentPkg, CONFIG_PASSAGEWAY_MODE);
        } else {
          handleBleFail(intent, getBleCmdType(intent) != CONFIG_PASSAGEWAY_MODE, false, mDeviceAddress);
        }
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_CONFIG_PASSAGEWAY_MODE.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

        String payload = blePkg.getBody().getPayload();

        if (!bleRspFail(payload, true, mDeviceAddress)) {
          Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
          applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
          sendBroadcast(applyKeyIntent);
          startBleTimer();
          mPassagewayModePresenter.modifyPassagewayMode(getLock().getDeviceSerialNo(), mPassagewayOpen);
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("门锁通道模式配置失败");
        }
      }

    }
  };

}