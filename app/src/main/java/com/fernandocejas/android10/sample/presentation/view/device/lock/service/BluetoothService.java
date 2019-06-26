package com.fernandocejas.android10.sample.presentation.view.device.lock.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.blelibrary.utils.BleBase;
import com.blueflybee.blelibrary.utils.ParsedAd;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DateUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockFactoryResetActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.constant.LockConstant;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static com.blueflybee.blelibrary.BleRequest.CmdType.APPLY_KEYS;
import static com.blueflybee.blelibrary.BleRequest.CmdType.GET_UNLOCK_RANDOM_CODE;
import static com.blueflybee.blelibrary.BleRequest.CmdType.UNLOCK;
import static com.blueflybee.blelibrary.BleRequest.FailReason.START_FAILED;
import static com.blueflybee.blelibrary.BleRequest.FailReason.TIMEOUT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.CONNECT_GATT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.WRITE_CHARACTERISTIC_TO_LOCK;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/12/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BluetoothService extends Service implements IBleOperable {

  protected static final int BLE_GATT_STATUS_133 = 133;
  private static final int BLE_GATT_STATUS_34 = 34;

  private static final long SCAN_PERIOD = 4000;
  public static final int NOTIFY_ID = 10;

  private BluetoothBinder mBinder = new BluetoothBinder();
  private String mDeviceAddress;
  private BlePkg mCurrentPkg;
  private BleGattCharacteristic mCharacteristic;
  private Timer mTimer;
  private TimerTask mTimerTask;
  private boolean mBleUsing = false;
  private Handler mBleTimerHandler;
  private Handler mScanHandler = new Handler();
  private boolean mScanning;
  private boolean mIsUserUsingBle = false;
  private UUID[] mServiceUUids = new UUID[]{UUID.fromString(BleMapper.SERVICE_UUID)};
  private Timer mBleUserUsingTimer;
  private TimerTask mBleUserUsingTask;
  private Timer mBleUsingTimer;
  private TimerTask mBleUsingTask;
  private PowerManager.WakeLock mWakeLock = null;

  private BleController mBleController;

  private Runnable mBleTimerRunnable = new Runnable() {
    @Override
    public void run() {
      if (mIsUserUsingBle) {
        System.out.println(tag() + " start timer delayed ++++++++++++++++++++++++++++++++++");
        mBleTimerHandler.postDelayed(mBleTimerRunnable, 6000);
      } else {
        mBleTimerHandler = null;
        startTimer();
        System.out.println("ble timer auto start..............................................................");
      }

    }
  };
  private String mUnlockData;
  private BleLock mBleLock;
  private LinkedHashMap<String, BleLock> mBleLocks;

  @Override
  public void onCreate() {
    System.out.println(tag() + ".onCreate+++++++++++++++++++++++++++++++++++");
    mBleController = new BleController(this);
    acquireWakeLock();
    startTimer();
    registerReceiver(getBleReceiver(), BleService.getIntentFilter());
//    startForeground();
  }

  @Override
  public void onDestroy() {
    releaseWakeLock();
    super.onDestroy();
    System.out.println(tag() + ".onDestroy+++++++++++++++++++++++++");
    mBleController = null;
    stopTimer();
    unregisterReceiver(getBleReceiver());
  }

  @Override
  public IBinder onBind(Intent intent) {
    System.out.println(tag() + ".onBind+++++++++++++++++++++++++");
    return mBinder;
  }

  @Override
  public boolean onUnbind(Intent intent) {
    System.out.println(tag() + ".onUnbind+++++++++++++++++++++++++++++++++");
    stopTimer();
//    unregisterReceiver(mBleReceiver);
    return true;
  }

  @Override
  public void onRebind(Intent intent) {
    System.out.println(tag() + ".onRebind+++++++++++++++++++++++++++++++++++++");
    startTimer();
//    registerReceiver(mBleReceiver, BleService.getIntentFilter());
    super.onRebind(intent);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
//    System.out.println("BluetoothService.onStartCommand++++++++++++++++++++++++++++++++++++");
    return START_STICKY;
  }

  private void startForeground() {
    Intent serviceIntent = new Intent(this, MainActivity.class);
    PendingIntent pi = PendingIntent.getActivity(this, 0, serviceIntent, 0);
    Notification notification = new NotificationCompat.Builder(this)
        .setContentTitle("This is content title")
        .setContentText("This is content text")
        .setWhen(System.currentTimeMillis())
        .setSmallIcon(R.mipmap.ic_launcher)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
        .setContentIntent(pi)
        .build();
    startForeground(NOTIFY_ID, notification);
  }

  private void startScanMonitor() {
    if (mBleController != null) {
      mBleController.startScanMonitor();
    }
  }

  private void releaseWakeLock() {
    if (mWakeLock != null) {
      mWakeLock.release();
      mWakeLock = null;
    }
  }

  private void acquireWakeLock() {
    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getContext().getClass().getName());
    mWakeLock.acquire();
  }

  private void closeAll() {
    IBle ble = getBle();
    if (ble != null) {
      ble.closeAll();
    }
  }

  private void closeLocksHasKeyRepo() {
    if (mBleLocks == null || mBleLocks.isEmpty()) return;
    Set<String> addresses = mBleLocks.keySet();
    for (String address : addresses) {
      close(address);
    }
  }

  private void close(String address) {
    IBle ble = getBle();
    if (ble != null && !TextUtils.isEmpty(address)) {
      ble.close(address);
    }
  }

  @Override
  public void startTimer() {
    if (mTimer == null) {
      mTimer = new Timer();
    }

    if (mTimerTask == null) {
      mTimerTask = new TimerTask() {
        @Override
        public void run() {
          System.out.println(tag() + " timer running+++++++++++++++++++++++++++++++++");
//          testLogToFile("bluetoothService.txt", TimeUtils.getNowString() + "   Hello! I'm BluetoothService");
          boolean unlockWithoutBle = mBleController.unlockWithoutBle();
          System.out.println("unlockWithoutBle = " + unlockWithoutBle);
          if (unlockWithoutBle) return;
          System.out.println("mBleUsing = " + mBleUsing);
          if (mBleUsing) return;
          if (mBleLocks != null) {
            mBleLocks.clear();
            mBleLocks = null;
          }
          mBleLocks = LockManager.getUnlockableLocks(getContext());
          printUsableLocks();
          if (mBleLocks == null || mBleLocks.isEmpty()) return;
          scanLeDevice(true);
          startScanMonitor();
        }
      };
    }

    if (mTimer != null && mTimerTask != null) {

      try {
        mTimer.schedule(mTimerTask, 1500, 6000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    setServiceUsingBle(false, 0);
  }

  private void testLogToFile(String fileName, String content) {
    String filePath;
    if (Environment.getExternalStorageState().equals(
        Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
      filePath = Environment.getExternalStorageDirectory()
          .getAbsolutePath() + File.separator + "QtecLog" + File.separator + fileName;

    } else {// 如果SD卡不存在，就保存到本应用的目录下
      filePath = getFilesDir().getAbsolutePath()

          + File.separator + "QtecLog" + File.separator + fileName;
    }
    FileUtils.writeFileFromString(filePath, content + "\n", true);
  }

  private void printUsableLocks() {
    System.out.println("---------------------------------------------usableLocks-----------------------------------------------");
    if (mBleLocks == null || mBleLocks.isEmpty()) {
      System.out.println("mBleLocks = " + mBleLocks);
      System.out.println("---------------------------------------------usableLocks-----------------------------------------------");
      return;
    }
    Set<String> addresses = mBleLocks.keySet();
    for (String address : addresses) {
      BleLock bleLock = mBleLocks.get(address);
      System.out.println("bleLock = " + bleLock);
    }
    System.out.println("---------------------------------------------usableLocks-----------------------------------------------");
  }

  @Override
  public void pauseTimer() {
    scanLeDevice(false);
    if (mTimer != null) {
      mTimer.cancel();
      mTimer = null;
    }
    if (mTimerTask != null) {
      mTimerTask.cancel();
      mTimerTask = null;
    }

    if (mBleTimerHandler == null) {
      mBleTimerHandler = new Handler();
    } else {
      mBleTimerHandler.removeCallbacks(mBleTimerRunnable);
    }
    mBleTimerHandler.postDelayed(mBleTimerRunnable, 6000);
  }

  @Override
  public void stopTimer() {
    scanLeDevice(false);
    if (mTimer != null) {
      mTimer.cancel();
      mTimer = null;
    }
    if (mTimerTask != null) {
      mTimerTask.cancel();
      mTimerTask = null;
    }
  }

  private void scanLeDevice(final boolean enable) {
    IBle ble = getBle();
    if (ble == null) {
      return;
    }
    if (enable) {
      // Stops scanning after a pre-defined scan period.
      mScanHandler.postDelayed(new Runnable() {
        @Override
        public void run() {
          mScanning = false;
          if (ble != null) {
            ble.stopScan();
          }
        }
      }, SCAN_PERIOD);

      mScanning = true;
      if (ble != null) {
        ble.startScan(UNLOCK, null);
      }
    } else {
      mScanning = false;
      if (ble != null) {
        ble.stopScan();
      }
    }
  }

  @Override
  public void setUserUsingBle(boolean userUsingBle) {
    mIsUserUsingBle = userUsingBle;
    if (mIsUserUsingBle) {
      postDelayBleUserUsingFalg();
    } else {
//      closeAll();
      if (mBleUserUsingTimer != null) {
        mBleUserUsingTimer.cancel();
        mBleUserUsingTimer = null;
      }

      if (mBleUserUsingTask != null) {
        mBleUserUsingTask.cancel();
        mBleUserUsingTask = null;
      }
    }
  }

  protected void setServiceUsingBle(boolean usingBle, long delay) {
    mBleUsing = usingBle;
    if (mBleUsing) {
      postDelayBleUsingFalg(delay);
    } else {
      if (mBleUsingTimer != null) {
        mBleUsingTimer.cancel();
        mBleUsingTimer = null;
      }

      if (mBleUsingTask != null) {
        mBleUsingTask.cancel();
        mBleUsingTask = null;
      }
    }
  }


  private void postDelayBleUsingFalg(long delay) {
    if (mBleUsingTimer != null) {
      mBleUsingTimer.cancel();
      mBleUsingTimer = null;
    }

    if (mBleUsingTask != null) {
      mBleUsingTask.cancel();
      mBleUsingTask = null;
    }
    mBleUsingTimer = new Timer();
    mBleUsingTask = new TimerTask() {
      @Override
      public void run() {
        mBleUsing = false;
      }
    };
    mBleUsingTimer.schedule(mBleUsingTask, delay);
  }

  private void postDelayBleUserUsingFalg() {
    if (mBleUserUsingTimer != null) {
      mBleUserUsingTimer.cancel();
      mBleUserUsingTimer = null;
    }

    if (mBleUserUsingTask != null) {
      mBleUserUsingTask.cancel();
      mBleUserUsingTask = null;
    }
    mBleUserUsingTimer = new Timer();
    mBleUserUsingTask = new TimerTask() {
      @Override
      public void run() {
        mIsUserUsingBle = false;
      }
    };
    mBleUserUsingTimer.schedule(mBleUserUsingTask, 30000);
  }

  protected void connect(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;

    ble.requestConnect(mDeviceAddress, cmdType, 30);
  }

  private IBle getBle() {
    return ((AndroidApplication) getApplication()).getIBle();
  }

  private Context getContext() {
    return this;
  }

  private String tag() {
    return getContext().getClass().getSimpleName();
  }

  private BroadcastReceiver getBleReceiver() {
    return mBleReceiver;
  }

  private int getStatus(Intent intent) {
    return intent.getIntExtra(BleService.EXTRA_STATUS, -100);
  }

  protected BleRequest.RequestType getBleRequestType(Intent intent) {
    return (BleRequest.RequestType) intent.getSerializableExtra(BleService.EXTRA_REQUEST);
  }

  protected BleRequest.FailReason getBleFailReason(Intent intent) {
    return (BleRequest.FailReason) intent.getSerializableExtra(BleService.EXTRA_REASON);
  }

  protected BleRequest.CmdType getBleCmdType(Intent intent) {
    return (BleRequest.CmdType) intent.getSerializableExtra(BleService.EXTRA_CMD_TYPE);
  }

  @Nullable
  private BlePkg createUnlockPkg(String payload) {
    String randomCode = payload.substring(2);
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("00");
    pkg.setKeyId(BleMapper.DEFAULT_ENCRYPTION_WHEN_UNLOCK);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_UNLOCK_APP_BESIDE);
    body.setPayload(randomCode);
    pkg.setBody(body);
    return pkg;
  }

  @NonNull
  protected BlePkg createRandomCodePkg() {
    BlePkg pkg = new BlePkg();
    pkg.setHead("aa");
    pkg.setLength("0c");
    pkg.setKeyId(BleMapper.NO_ENCRYPTION);
    pkg.setUserId(PrefConstant.getUserIdInHexString());
    pkg.setSeq("00");
    BleBody body = new BleBody();
    body.setCmd(BleMapper.BLE_CMD_GET_UNLOCK_RADOM_CODE);
    body.setPayload("");
    pkg.setBody(body);
    return pkg;
  }

  @Override
  public void restartBle() {
    IBle ble = getBle();
    if (ble == null) return;
    ble.disable();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        ble.enable();
      }
    }, 2600);
  }

  @Override
  public void setCanConnect(boolean canConnect) {
    if (mBleController != null) {
      mBleController.setCanConnect(canConnect);
    }
  }

  @Override
  public void resetFactoryConnectCount() {
    if (mBleController != null) {
      mBleController.resetFactoryConnectCount();
    }
  }

  @Override
  public void setUnlockWithoutBle(boolean unlockWithoutBle) {
    if (mBleController != null) {
      mBleController.setUnlockWithoutBle(unlockWithoutBle);
    }
  }

  public class BluetoothBinder extends AbsBluetoothBinder {
    public IBleOperable getService() {
      return (IBleOperable) getContext();
    }
  }

  private void onDeviceFound(Intent intent) {
    BleRequest.CmdType cmdType = (BleRequest.CmdType) intent.getSerializableExtra(BleService.EXTRA_CMD_TYPE);
    if (cmdType != UNLOCK) return;
    // device found
    byte[] scanRecord = intent.getByteArrayExtra(BleService.EXTRA_SCAN_RECORD);
    ParsedAd parsedAd = BleBase.parseScanRecord(scanRecord);
    if (!parsedAd.uuids.contains(mServiceUUids[0])) return;


    final BluetoothDevice device = intent.getParcelableExtra(BleService.EXTRA_DEVICE);
    String address = device.getAddress();
    if (mBleLocks == null) return;
    if (!mBleLocks.containsKey(address)) return;

    mBleController.scanSuccess();

    if (!mBleController.isCanConnect()) {
//      testLogToFile("bleConnect.txt", TimeUtils.getNowString() + "   connect stopped!");
      return;
    }

    System.out.println("parsedAd = " + parsedAd);
//    if (mBleController.unlockWithoutBle(parsedAd)) return;
    mBleController.setUnlockMode4V16(mBleLocks.get(address), parsedAd);
    int rssi = intent.getIntExtra(BleService.EXTRA_RSSI, -1000);
    if (mBleController.isRssiTooLow(rssi)) return;
    scanLeDevice(false);
    mDeviceAddress = address;
    mBleLock = mBleLocks.get(mDeviceAddress);
    if (mBleUsing) return;

    setServiceUsingBle(true, 30000);
//    ToastUtils.showShort("[" + mBleLock.getDeviceName() + "  " + String.valueOf(rssi) + "]  阈值=" + mBleController.getRssiThreshold());
    System.out.println("send pkg +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    connect(createRandomCodePkg(), GET_UNLOCK_RANDOM_CODE);
  }

  private void onConnected(Intent intent) {
//    testLogToFile("bleConnect.txt", TimeUtils.getNowString() + "   connected!");
    BleRequest.CmdType cmdType = getBleCmdType(intent);
    if (cmdType == GET_UNLOCK_RANDOM_CODE) {
      System.out.println(tag() + " connected +++++++++++++++++++++++++++++++");
      setServiceUsingBle(true, 14000);
    }
  }

  private void onServiceDiscoveredOfGetRandomCode() {
    setServiceUsingBle(true, 4000);
    System.out.println("ble_service_discovered_when_get_unlock_random_code+++++++++++++++++++++");
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      System.out.println("ble service is null!!!");
      setServiceUsingBle(true, 30000);
      connect(createRandomCodePkg(), GET_UNLOCK_RANDOM_CODE);
      return;
    }
    mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));
    ble.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
    String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(GET_UNLOCK_RANDOM_CODE, mDeviceAddress, mCharacteristic, data, true, 4f);
  }

  private void onGetRandomCode(Bundle extras) {
    setServiceUsingBle(true, 4000);
    ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
//        System.out.println("get_unlock_random_code_values++++++++++++++++++++++++++++++ = " + values);
    BlePkg blePkg = new BleMapper().stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());

    String payload = blePkg.getBody().getPayload();

    if (!checkPayloadSuccess(payload)) return;

    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
    if (service == null) {
      return;
    }
    mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));
    ble.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
    BlePkg pkg = createUnlockPkg(payload);
    mUnlockData = new BleMapper().pkgToReqString(pkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    ble.requestWriteCharacteristicToLock(UNLOCK, mDeviceAddress, mCharacteristic, mUnlockData, true, 4f);


  }

  private void onUnlockWrite4V16() {
    BleLock bleLock = mBleLocks.get(mDeviceAddress);
    if (bleLock == null) return;
    if (LockConstant.LOCK_VERSION_16.compareToIgnoreCase(bleLock.getVersion()) < 0) return;

    System.out.println("BluetoothService.onUnlockWrite4V16");

//    ToastUtils.showShort("press to unlock");

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        // TODO: 17-11-8 should close ?
        System.out.println("mIsUserUsingBle = " + mIsUserUsingBle);
        if (!mIsUserUsingBle) {
          closeLocksHasKeyRepo();
        }
        Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
        applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
        sendBroadcast(applyKeyIntent);
        setServiceUsingBle(false, 0);
      }
    }, 80);

    startInRangeMonitor();
  }

  private boolean checkPayloadSuccess(String payload) {
    if (TextUtils.isEmpty(payload)) return false;
    if (payload.length() < 2) return false;
    String rspCode = payload.substring(0, 2);
    if (BleBody.RSP_LOCK_FACTORY_RESET.equals(rspCode)) {
      BleLock lock = LockManager.getLock(getContext(), mDeviceAddress);
      if (lock == null) return false;
      Intent lockIntent = new Intent();
      lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      lockIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
      new Navigator().navigateTo(getContext(), LockFactoryResetActivity.class, lockIntent);
      closeLocksHasKeyRepo();
      setServiceUsingBle(false, 0);
      return false;
    }
    if (!BleBody.RSP_OK.equals(rspCode)) return false;
    return true;
  }

  private void onUnlock(Bundle extras) {
    System.out.println("ble_characteristic_write_to_lock_when_unlock+++++++++++++++++++++");
//    ToastUtils.showShort("press to unlock");
//    if (!mIsUserUsingBle) {
//      closeLocksHasKeyRepo();
//    }
//    Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
//    applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
//    sendBroadcast(applyKeyIntent);
//    setServiceUsingBle(false, 0);

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        // TODO: 17-11-8 should close ?
        System.out.println("mIsUserUsingBle = " + mIsUserUsingBle);
//        if (!mIsUserUsingBle) {
        System.out.println("closeLocksHasKeyRepo ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
        closeLocksHasKeyRepo();
//        }
        Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
        applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
        sendBroadcast(applyKeyIntent);
        setServiceUsingBle(false, 0);
      }
    }, 80);

    startInRangeMonitor();

    ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
    BlePkg blePkg = new BleMapper().stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
    System.out.println("blePkg = " + blePkg);
    if (blePkg == null || blePkg.getBody() == null) return;
    mBleController.setUnlockMode(blePkg);
    checkKeyInvalid(blePkg);
  }

  private void checkKeyInvalid(BlePkg blePkg) {
    if (blePkg == null || blePkg.getBody() == null) return;
    String payload = blePkg.getBody().getPayload();
    if (TextUtils.isEmpty(payload)) return;
    if (BleBody.RSP_LOCK_KEY_INVALID.equals(payload)
        || BleBody.RSP_ILLEGAL_CMD.equals(payload)) {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
      intent.setAction(BleMapper.ACTION_BLE_LOCK_KEY_INVALID);
      sendBroadcast(intent);
    }
  }

  private void startInRangeMonitor() {
    if (mBleController != null) {
      mBleController.startInRangeMonitor();
    }
  }

  private void onDisconnected(Intent intent) {
    System.out.println(tag() + " disconnected +++++++++++++++++++++++++++++++");
    int status = getStatus(intent);
    System.out.println("ble disconnected status = " + status);
//    setServiceUsingBle(false, 0);
  }

  protected void onBleFailed(Intent intent, int disconnectStatus) {
    BleRequest.CmdType cmdType = getBleCmdType(intent);
    BleRequest.FailReason reason = getBleFailReason(intent);
    BleRequest.RequestType requestType = getBleRequestType(intent);

    //write GET_UNLOCK_RANDOM_CODE fail start
    System.out.println("ble request failed : cmdType = " + cmdType + ","
        + "reason = " + reason + ","
        + "requestType = " + requestType + ","
        + "disconnectStatus = " + disconnectStatus);

//    ble request failed : cmdType = GET_UNLOCK_RANDOM_CODE,reason = TIMEOUT,requestType = CONNECT_GATT,disconnectStatus = -100
    //连接超时后蓝牙不可用，重启蓝牙
    if (reason == TIMEOUT && cmdType == GET_UNLOCK_RANDOM_CODE && requestType == CONNECT_GATT) {
      setServiceUsingBle(false, 0);
      restartBle();
    } else if (reason == TIMEOUT && cmdType == APPLY_KEYS && requestType == CONNECT_GATT) {
      setServiceUsingBle(false, 0);
      restartBle();
    }
    //19包第二个包没有写成功，需要重启蓝牙
    else if (reason == START_FAILED && cmdType == GET_UNLOCK_RANDOM_CODE && requestType == WRITE_CHARACTERISTIC_TO_LOCK) {
      setServiceUsingBle(false, 0);
      restartBle();
    }
    //连接失败，返回133等错误代码，尝试重连
//    ble request failed : cmdType = GET_UNLOCK_RANDOM_CODE,reason = RESULT_FAILED,requestType = CONNECT_GATT,disconnectStatus = 133
    else if (disconnectStatus == BLE_GATT_STATUS_133 && cmdType == GET_UNLOCK_RANDOM_CODE) {
      setServiceUsingBle(true, 30000);
      connect(createRandomCodePkg(), GET_UNLOCK_RANDOM_CODE);
    }
//    ble request failed : cmdType = APPLY_KEYS,reason = RESULT_FAILED,requestType = CONNECT_GATT,disconnectStatus = 133
    else if (disconnectStatus == BLE_GATT_STATUS_133 && cmdType == APPLY_KEYS && requestType == CONNECT_GATT) {
      setServiceUsingBle(false, 0);
      restartBle();
    }
//    ble request failed : cmdType = null,reason = TIMEOUT,requestType = DISCOVER_SERVICE,disconnectStatus = -100
//    else if (reason == START_FAILED && cmdType == GET_UNLOCK_RANDOM_CODE && requestType == WRITE_CHARACTERISTIC_TO_LOCK) {
//      setServiceUsingBle(false, 0);
//      restartBle();
//    }
    //其它失败情况，开启
    else {
      setServiceUsingBle(false, 0);
    }
  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {

    /**
     * 收集蓝牙连接断开回调被执行时的异常状态code
     */
    private int disconnectStatus = -100;

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle extras = intent.getExtras();
      String action = intent.getAction();
      if (BleService.BLE_DEVICE_FOUND.equals(action)) {
        onDeviceFound(intent);
        return;
      }
      if (TextUtils.isEmpty(mDeviceAddress) || !mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR)))
        return;
      if (BleService.BLE_GATT_CONNECTED.equals(action)) {
        onConnected(intent);
      } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
        disconnectStatus = getStatus(intent);
        onDisconnected(intent);
      } else if (BleService.BLE_SERVICE_DISCOVERED_WHEN_GET_UNLOCK_RANDOM_CODE.equals(action)) {
        onServiceDiscoveredOfGetRandomCode();
      } else if (BleService.BLE_GET_UNLOCK_RANDOM_CODE.equals(action)) {
        onGetRandomCode(extras);
      } else if (BleService.BLE_UNLOCK.equals(action)) {
        onUnlock(extras);
      }
      //兼容版本16门锁
      else if (BleService.BLE_CHARACTERISTIC_WRITE_TO_LOCK_WHEN_UNLOCK.equals(action)) {
        onUnlockWrite4V16();
      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        onBleFailed(intent, disconnectStatus);
        disconnectStatus = -100;
      }
    }
  };


}
