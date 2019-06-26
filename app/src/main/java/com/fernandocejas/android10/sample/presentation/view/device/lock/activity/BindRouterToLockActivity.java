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
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityBindRouterToLockBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMainComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MainModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.fragment.QDevPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.fragment.QDevView;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.SearchRouterOwnedAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.BIND_ROUTER_ZIGBEE;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BindRouterToLockActivity extends BaseActivity implements QDevView, BindRouterToLockView {

  protected static final String BIND_LOCK_SUCCESS = "0000";
  private static final String TAG = BindRouterToLockActivity.class.getSimpleName();

  protected ActivityBindRouterToLockBinding mBinding;

  @Inject
  protected
  QDevPresenter mQDevPresenter;

  protected SearchRouterOwnedAdapter mAdapter;

  @Inject
  BindRouterToLockPresenter mBindRouterToLockPresenter;

  protected GetDevTreeResponse mDevTreeResponse;

  protected String mDeviceAddress;
  protected IBle mBle;
  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;
  private BleGattCharacteristic mCharacteristic;
  protected BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_bind_router_to_lock);

    initializeInjector();

    initPresenter();

    initView();

    initData();

    getDevTree();

  }

  protected void getDevTree() {
    mQDevPresenter.getDevTree("0,4");
  }

  protected void initView() {
    initTitleBar("设备绑定");
    mTitleBar.setRightTextAs("以后再说", getResources().getColor(R.color.blue_2196f3), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
      }
    });

    mAdapter = new SearchRouterOwnedAdapter(getContext());
    mBinding.lvRouters.setAdapter(mAdapter);
    mAdapter.setOnBindBtnClickListener((view, device) -> {
      if (!BleLockUtils.isBleEnable()) return;
      showLoading();
      mDevTreeResponse = device;
      pauseBleTimer();
      setUserUsingBle(true, mDeviceAddress);
      clearBleRequest();
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          BlePkg pkg = new BlePkg();
          pkg.setHead("aa");
          pkg.setLength("28");
          pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
          pkg.setUserId(PrefConstant.getUserIdInHexString());
          pkg.setSeq("00");
          BleBody body = new BleBody();
          body.setCmd(BleMapper.BLE_CMD_BIND_ROUTER_ZIGBEE);
          body.setPayload("");
          pkg.setBody(body);
          sendBlePkg(pkg, BIND_ROUTER_ZIGBEE);
        }
      }, 2000);

    });

  }

  protected void initData() {
    mDeviceAddress = lockMacAddress();
    mBleLock = LockManager.getLock(getContext(), mDeviceAddress);
  }

  protected void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
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

  public void bind(View view) {
    mNavigator.navigateTo(getContext(), AddRouterActivity.class);
  }

  public void skip(View view) {
    mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
  }

  protected void initPresenter() {
    mQDevPresenter.setView(this);
    mBindRouterToLockPresenter.setView(this);
  }

  protected void initializeInjector() {
    DaggerMainComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mainModule(new MainModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void showDevTree(List<GetDevTreeResponse<List<DeviceBean>>> response) {
    mAdapter.update(response);
  }

  @Override
  public void showNoDevice() {
    mBinding.llNoOwnedRouters.setVisibility(View.VISIBLE);
    mBinding.llHasOwnedRouters.setVisibility(View.GONE);
  }

  @Override
  public void showNoDeviceGuide() {}

  @Override
  public void showDeviceGuide() {}

  @Override
  public void showNotifyBindSuccess(BindRouterToLockResponse response) {
    DialogUtil.showQueryBindRouterDialog(getContext(), "正在绑定网关", "绑定网关可能需要1分钟，请耐心等待", 60000, () -> {
      mBindRouterToLockPresenter.queryBindRouter(mDevTreeResponse.getDeviceSerialNo(), mBleLock.getId());
    });
  }

  protected void showBindSuccess() {
    if (bindRouterOnly()) {
      DialogUtil.showSuccessDialog(getContext(), "绑定成功", () -> {
        Intent data = new Intent();
        data.putExtra(Navigator.EXTRA_BINDED_ROUTER_ID, mDevTreeResponse.getDeviceSerialNo());
        setResult(RESULT_OK, data);
        finish();
      });

    } else {
      DialogUtil.showSuccessDialog(getContext(), "绑定成功", () ->
          mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class));
    }
  }

  protected void showBindFail() {
    DialogUtil.showSuccessDialog(getContext(), "绑定失败", () -> {
    });
  }

  @Override
  public void onQuery(QueryBindRouterToLockResponse response) {
    if (AppConstant.DEVICE_TYPE_ROUTER.equals(mDevTreeResponse.getDeviceType())) {
      if (response.getContained() == 1) {
        showBindSuccess();
      } else {
        showBindFail();
      }
    } else if (LiteGateway.DEVICE_TYPE_LITE_GATEWAY.equals(mDevTreeResponse.getDeviceType())) {
      if (BIND_LOCK_SUCCESS.equals(response.getEncrypdata())) {
        showBindSuccess();
      } else {
        showBindFail();
      }
    }
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
    mQDevPresenter.destroy();
    mBindRouterToLockPresenter.destroy();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setUserUsingBle(false, mDeviceAddress);
  }

  private boolean bindRouterOnly() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_BIND_ROUTER_TO_LOCK_ONLY, false);
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
        handleBleDisconnect(getBleCmdType(intent) != BIND_ROUTER_ZIGBEE, false, mDeviceAddress);
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
        handleBleFail(intent, getBleCmdType(intent) != BIND_ROUTER_ZIGBEE, false, mDeviceAddress);
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_BIND_ROUTER_ZIGBEE.equals(action)) {
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
          mBindRouterToLockPresenter.bindRouter(mDevTreeResponse.getDeviceSerialNo(), mBleLock.getId());
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(payload)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("门锁绑定网关失败");
        }
      }

    }
  };
}
