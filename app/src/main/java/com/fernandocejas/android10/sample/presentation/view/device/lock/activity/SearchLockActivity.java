package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.blelibrary.utils.BleBase;
import com.blueflybee.blelibrary.utils.ParsedAd;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivitySearchLockBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.SearchLockAdapter;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;

import java.util.UUID;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SearchLockActivity extends BaseActivity {

  private static final String TAG = SearchLockActivity.class.getName();

  private static final int REQUEST_ENABLE_BT = 1;
  private ActivitySearchLockBinding mBinding;
  private SearchLockAdapter mSearchLockAdapter;

  private IBle mBle;
  private Handler mHandler = new Handler();
  private boolean mScanning;

  // Stops scanning after 5 seconds.
  private static final long SCAN_PERIOD = 3000;
  private BluetoothDevice mDevice;
  private UUID[] mServiceUUids = new UUID[]{UUID.fromString(BleMapper.SERVICE_UUID)};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_lock);

    initView();

//    requestBleEnable();
  }

  private void initView() {
    initTitleBar("添加门锁");

    mTitleBar.setRightAs("搜索", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mSearchLockAdapter.clear();
        requestBleEnable();
      }
    });

    mSearchLockAdapter = new SearchLockAdapter(getContext());
    mBinding.lvLocks.setAdapter(mSearchLockAdapter);
    mBinding.lvLocks.setOnItemClickListener((parent, view, position, id) -> {
      if (mBle == null) return;
      mBle.stopScan();

      mDevice = mSearchLockAdapter.getItem(position);
      if (mDevice == null) return;
      showLoading();
      mBle.requestConnect(mDevice.getAddress(), 32);

    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(mBleReceiver, BleService.getIntentFilter());
    requestBleEnable();
  }

  private void requestBleEnable() {
    if (!BleLockUtils.isBleEnable()) return;
    closeAll();
//      if (mDevice != null) {
//        close(mDevice.getAddress());
//      }
    scanLeDevice(true);
  }

  @Override
  protected void onPause() {
    super.onPause();
    unregisterReceiver(mBleReceiver);
    scanLeDevice(false);
    mSearchLockAdapter.clear();
  }

  private void scanLeDevice(final boolean enable) {

    AndroidApplication app = (AndroidApplication) getApplication();
    mBle = app.getIBle();
    if (mBle == null) {
      return;
    }
    if (enable) {
      showLoading();
      // Stops scanning after a pre-defined scan period.
      mHandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          hideLoading();
          mScanning = false;
          if (mBle != null) {
            mBle.stopScan();
          }
        }
      }, SCAN_PERIOD);

      mScanning = true;
      if (mBle != null) {
        mBle.startScan();
      }
    } else {
      hideLoading();
      mScanning = false;
      if (mBle != null) {
        mBle.stopScan();
      }
    }
  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (BleService.BLE_NOT_SUPPORTED.equals(action)) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            ToastUtils.showShort("您的设备不支持蓝牙4.0");
            finish();
          }
        });
      } else if (BleService.BLE_NO_BT_ADAPTER.equals(action)) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            ToastUtils.showShort("您的设备不支持蓝牙4.0");
            finish();
          }
        });
      } else if (BleService.BLE_DEVICE_FOUND.equals(action)) {

        BleRequest.CmdType cmdType = getBleCmdType(intent);
        if (cmdType != null) return;
        // device found
        byte[] scanRecord = intent.getByteArrayExtra(BleService.EXTRA_SCAN_RECORD);
        ParsedAd parsedAd = BleBase.parseScanRecord(scanRecord);
        if (!parsedAd.uuids.contains(mServiceUUids[0])) return;
        Bundle extras = intent.getExtras();
        final BluetoothDevice device = extras.getParcelable(BleService.EXTRA_DEVICE);
        final int rssi = intent.getIntExtra(BleService.EXTRA_RSSI, 0);

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mSearchLockAdapter.update(device, rssi);
          }
        });
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        hideLoading();
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {

        hideLoading();
        final Intent addLockIntent = new Intent();
        addLockIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_NAME, mDevice.getName());
        addLockIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDevice.getAddress());
        mNavigator.navigateTo(getContext(), AddLockActivity.class, addLockIntent);
      }
    }
  };


}
