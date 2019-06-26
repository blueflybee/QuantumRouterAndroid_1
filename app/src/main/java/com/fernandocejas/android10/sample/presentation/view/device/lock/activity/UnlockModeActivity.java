package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.data.net.CloudUrlPath;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityUnlockModeSelectBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.adapter.UnlockModeArrayAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.data.UnlockMode;
import com.fernandocejas.android10.sample.presentation.view.device.lock.data.UnlockModeManager;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.IBleOperable;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetUnlockModeResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.CONFIG_UNLOCK_MODE;
import static com.blueflybee.blelibrary.BleRequest.FailReason.TIMEOUT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.DISCOVER_SERVICE;


public class UnlockModeActivity extends BaseActivity implements UnlockModeView {

  private static final String TAG = UnlockModeActivity.class.getSimpleName();

  private ActivityUnlockModeSelectBinding mBinding;

  @Inject
  UnlockModePresenter mUnlockModePresenter;

  private String mDeviceAddress;
  private BlePkg mCurrentPkg;
  private UnlockMode mFpMode = new UnlockMode();
  private UnlockMode mPwdMode = new UnlockMode();
  private UnlockMode mCardMode = new UnlockMode();
  private BleLock mBleLock;

  private UnlockModeManager mUnlockModeManager = new UnlockModeManager();

  private boolean mBleRegistered = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_unlock_mode_select);

    initData();

    initializeInjector();
    initPresenter();
    initView();

    mUnlockModePresenter.getUnlockMode(getLock().getDeviceSerialNo());

  }

  private void initView() {
    initTitleBar("开锁方式");
    mTitleBar.setRightAs("完成", v -> {
      configUnlockMode();
    });

    initFpUnlockView();

    initPwdUnlockView();

    initCardUnlockView();
  }

  private void initFpUnlockView() {
    mBinding.fpUnlockList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    UnlockModeArrayAdapter adapter = new UnlockModeArrayAdapter(this, R.layout.item_unlock_mode_list, mUnlockModeManager.getFpUnlockModes());
    mBinding.fpUnlockList.setAdapter(adapter);
    mBinding.fpUnlockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mFpMode = adapter.getItem(position);
      }
    });
  }

  private void initPwdUnlockView() {
    mBinding.pwdUnlockList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    UnlockModeArrayAdapter adapter = new UnlockModeArrayAdapter(this, R.layout.item_unlock_mode_list, mUnlockModeManager.getPwdUnlockModes());
    mBinding.pwdUnlockList.setAdapter(adapter);
    mBinding.pwdUnlockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPwdMode = adapter.getItem(position);
      }
    });
  }

  private void initCardUnlockView() {
    mBinding.cardUnlockList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
    UnlockModeArrayAdapter adapter = new UnlockModeArrayAdapter(this, R.layout.item_unlock_mode_list, mUnlockModeManager.getCardUnlockModes());
    mBinding.cardUnlockList.setAdapter(adapter);
    mBinding.cardUnlockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCardMode = adapter.getItem(position);
      }
    });
  }

  private void initData() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> lock = getLock();
    mDeviceAddress = lock == null ? "" : lock.getMac();
    mBleLock = LockManager.getLock(getContext(), lock.getMac());
  }

  private void configUnlockMode() {
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
        body.setCmd(BleMapper.BLE_CMD_CONFIG_UNLOCK_MODE);
        body.setPayload(mFpMode.getBleCode() + mPwdMode.getBleCode() + mCardMode.getBleCode());
        pkg.setBody(body);
        connect(pkg, CONFIG_UNLOCK_MODE);
      }
    }, 2000);
  }

  private void connect(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    ble.requestConnect(mDeviceAddress, cmdType, 30);
  }

  @Override
  public void showUnlockMode(GetUnlockModeResponse response) {
    showFpMode(response);
    showPwdMode(response);
    showCardMode(response);
  }

  private void showCardMode(GetUnlockModeResponse response) {
    String cardOpenConfig = response.getDoorcardOpenConfig();
    boolean withoutBle = mCardMode.unlockWithoutBleCloud(cardOpenConfig);
    mBinding.cardUnlockList.setItemChecked(withoutBle ? 0 : 1, true);
    mCardMode.setName(withoutBle ? "仅门卡开锁" : "手机连接蓝牙后门卡开锁");
    mCardMode.setBleCode(withoutBle ? UnlockMode.UNLOCK : UnlockMode.UNLOCK_BLE);
    mCardMode.setUnlockModeCloud(cardOpenConfig);
  }

  private void showPwdMode(GetUnlockModeResponse response) {
    String pwdOpenConfig = response.getPasswordOpenConfig();
    boolean withoutBle = mPwdMode.unlockWithoutBleCloud(pwdOpenConfig);
    mBinding.pwdUnlockList.setItemChecked(withoutBle ? 0 : 1, true);
    mPwdMode.setName(withoutBle ? "仅密码开锁" : "手机连接蓝牙后密码开锁");
    mPwdMode.setBleCode(withoutBle ? UnlockMode.UNLOCK : UnlockMode.UNLOCK_BLE);
    mPwdMode.setUnlockModeCloud(pwdOpenConfig);
  }

  private void showFpMode(GetUnlockModeResponse response) {
    String fpOpenConfig = response.getOpenConfig();
    boolean withoutBle = mFpMode.unlockWithoutBleCloud(fpOpenConfig);
    mBinding.fpUnlockList.setItemChecked(withoutBle ? 0 : 1, true);
    mFpMode.setName(withoutBle ? "仅指纹开锁" : "手机连接蓝牙后指纹开锁");
    mFpMode.setBleCode(withoutBle ? UnlockMode.UNLOCK : UnlockMode.UNLOCK_BLE);
    mFpMode.setUnlockModeCloud(fpOpenConfig);
  }

  @Override
  public void showModifyUnlockModeSuccess() {
    IBleOperable bleLockService = ((AndroidApplication) getApplicationContext()).getBleLockService();
    if (mFpMode.unlockWithoutBle() && mPwdMode.unlockWithoutBle() && mCardMode.unlockWithoutBle()) {
      ToastUtils.showShort("您现在不需要蓝牙就可以解锁");
      if (bleLockService != null) {
        bleLockService.setUnlockWithoutBle(true);
      }
    } else {
      ToastUtils.showShort("您现在需要蓝牙才能解锁");
      if (bleLockService != null) {
        bleLockService.setUnlockWithoutBle(false);
      }
    }

  }

  private void initPresenter() {
    mUnlockModePresenter.setView(this);
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
    mBleRegistered = true;
  }

  @Override
  protected void onStop() {
    super.onStop();
    boolean appBackground = ((AndroidApplication) getApplicationContext()).isAppBackground();
    if (!appBackground && mBleRegistered) {
      unregisterReceiver(mBleReceiver);
      mBleRegistered = false;
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setUserUsingBle(false, mDeviceAddress);
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
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
        handleBleDisconnect(getBleCmdType(intent) != CONFIG_UNLOCK_MODE, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        IBle ble = getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          ble.requestConnect(mDeviceAddress, CONFIG_UNLOCK_MODE, 30);
          return;
        }
        BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(CONFIG_UNLOCK_MODE, mDeviceAddress, characteristic, data, true);

      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        BleRequest.FailReason reason = getBleFailReason(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);
//        cmdType = CONFIG_UNLOCK_MODE,reason = TIMEOUT,requestType = DISCOVER_SERVICE,disconnectStatus = -100
//        ble request failed : cmdType = GET_UNLOCK_RANDOM_CODE,reason = RESULT_FAILED,requestType = CONNECT_GATT,disconnectStatus = 133
        if (cmdType == CONFIG_UNLOCK_MODE && reason == TIMEOUT && requestType == DISCOVER_SERVICE) {
          connect(mCurrentPkg, CONFIG_UNLOCK_MODE);
        } else {
          handleBleFail(intent, getBleCmdType(intent) != CONFIG_UNLOCK_MODE, false, mDeviceAddress);
        }
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_CONFIG_UNLOCK_MODE.equals(action)) {
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
          mUnlockModePresenter.modifyUnlockMode(getLock().getDeviceSerialNo(),
              mFpMode.getUnlockModeCloud(), mPwdMode.getUnlockModeCloud(), mCardMode.getUnlockModeCloud(), CloudUrlPath.PATH_MODIFY_UNLOCK_MODE);
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("门锁开锁方式配置失败");
        }
      }

    }
  };

}