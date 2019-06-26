package com.fernandocejas.android10.sample.presentation.view.device.lock.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.blueflybee.blelibrary.utils.ParsedAd;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ConnectLockTooLongActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.UnlockModeActivityForV15;
import com.fernandocejas.android10.sample.presentation.view.device.lock.constant.LockConstant;
import com.qtec.lock.model.core.BlePkg;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/02/01
 *     desc   : A ble controller to optimize and control ble service operations
 *     version: 1.0
 * </pre>
 */
class BleController {

  /**
   * 开锁方式为指纹加蓝牙
   */
  private static final String UNLOCK_WITHOUT_BLE = "10";
  private final int DEFAULT_RSSI_THRESHOLD = -68;
  private final int LOW_RSSI_THRESHOLD = -100;

  private final int IN_RANGE_TIMER_PERIOD = 3 * 60 * 1000;
  private final int BLE_MAX_COMMUNICATE_COUNT = 15;
  private static final int NOTIFICATION_ID = 0;

  private final int SCAN_MONITOR_DELAY = 4000;

  private int mRssiThreshold;
  private BluetoothService mBleService;
  private Timer mTimer;
  private TimerTask mTimerTask;

  private Timer mScanTimer;
  private TimerTask mScanTimerTask;

  private boolean mIsInRangeMonitorStart = false;
  private boolean mCanConnect = true;

  private int mBleCommunicateCount;

  private int mScanSuccessCount = 0;

  /**
   * 用于检测门锁是否已经恢复出厂设置的连接次数，每次应用启动时恢复并检测
   */
  private int mFactoryResetConnectCount = 2;


  private boolean mUnlockWithoutBle = true;

  /**
   * 开锁方式为指纹加蓝牙
   */
  private static final int UNLOCK_WITH_FP_AND_BLE = 48;

  BleController(BluetoothService bleService) {
    mBleService = bleService;
    mRssiThreshold = DEFAULT_RSSI_THRESHOLD;
  }

  int getRssiThreshold() {
    return mRssiThreshold;
  }

  public void setRssiThreshold(int rssiThreshold) {
    this.mRssiThreshold = rssiThreshold;
  }

  boolean isRssiTooLow(int rssi) {
    return rssi <= mRssiThreshold;
  }

  /**
   * 启动监控，监控在蓝牙可扫描连接和通信的范围中，3分钟内连接次数统计
   */
  void startInRangeMonitor() {
    if (mIsInRangeMonitorStart) {
      mBleCommunicateCount++;
      System.out.println("total ble communicate count = " + mBleCommunicateCount);
      return;
    }
    if (mTimer == null) {
      mTimer = new Timer();
    }

    if (mTimerTask == null) {
      mTimerTask = new TimerTask() {
        @Override
        public void run() {
          System.out.println("total ble communicate count = " + mBleCommunicateCount);
          //指定时间段内，通信次数小于预订值，判断为通信不频繁，重新启动监控
          //如果通信次数大于等于预订值，判断为设备在门锁附近通信太频繁，关闭蓝牙的连接通信，但不关闭扫描
          if (mBleCommunicateCount < BLE_MAX_COMMUNICATE_COUNT) {
            stopInRangeMonitor();
            startInRangeMonitor();
          } else {
            stopInRangeMonitor();
            mCanConnect = false;
            boolean appBackground = ((AndroidApplication) mBleService.getApplication()).isAppBackground();
            if (!appBackground) {
              showConnectTooManyDialog();
            } else {
              showNotification();
            }
          }
        }
      };
    }

    if (mTimer != null && mTimerTask != null) {
      try {
        mIsInRangeMonitorStart = true;
        mTimer.schedule(mTimerTask, IN_RANGE_TIMER_PERIOD);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void showConnectTooManyDialog() {
    Intent intent = new Intent();
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    new Navigator().navigateTo(mBleService, ConnectLockTooLongActivity.class, intent);
  }

  private void showNotification() {
    NotificationManager notifyManager = (NotificationManager) mBleService.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(mBleService);
    String appName = mBleService.getString(R.string.app_name);
    builder.setContentTitle(appName)
        .setSmallIcon(R.drawable.app_icon)
        .setContentText("您的手机在门前时间过长，系统已停止密钥校验以节省电量");

    PendingIntent pIntent = PendingIntent.getActivity(mBleService, 1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setContentIntent(pIntent);
    builder.setAutoCancel(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      builder.setVisibility(Notification.VISIBILITY_PUBLIC);
      // 关联PendingIntent
      builder.setFullScreenIntent(pIntent, false);
    }
    Notification notification = builder.build();

    notifyManager.notify(NOTIFICATION_ID, notification);

  }

  private void stopInRangeMonitor() {
    mIsInRangeMonitorStart = false;
    mBleCommunicateCount = 0;
    if (mTimer != null) {
      mTimer.cancel();
      mTimer = null;
    }
    if (mTimerTask != null) {
      mTimerTask.cancel();
      mTimerTask = null;
    }
  }

  boolean isCanConnect() {
    return mCanConnect;
  }

  void setCanConnect(boolean canConnect) {
    mCanConnect = canConnect;
  }

  /**
   * 启动扫描监控，动态调整rssi阈值
   */
  void startScanMonitor() {
    try {
//    System.out.println("start scan monitor");
      if (mScanTimer == null) {
        mScanTimer = new Timer();
      }

      if (mScanTimerTask == null) {
        mScanTimerTask = new TimerTask() {
          @Override
          public void run() {
            stopScanMonitor();
            adjustRssi();
            adjustInRangeMonitor();
          }
        };
      }

      if (mScanTimer != null && mScanTimerTask != null) {

        mScanTimer.schedule(mScanTimerTask, SCAN_MONITOR_DELAY);

      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  private void adjustInRangeMonitor() {
    if (mRssiThreshold <= LOW_RSSI_THRESHOLD) {
      stopInRangeMonitor();
      this.mCanConnect = true;
    }

  }

  private void adjustRssi() {
    System.out.println("mScanSuccessCount = " + mScanSuccessCount);
    System.out.println("mRssiThreshold = " + mRssiThreshold);
    if (mScanSuccessCount > 0) {
      mRssiThreshold += 2;
      if (mRssiThreshold >= DEFAULT_RSSI_THRESHOLD) {
        mRssiThreshold = DEFAULT_RSSI_THRESHOLD;
      }

    } else {
      mRssiThreshold -= 4;
      if (mRssiThreshold <= LOW_RSSI_THRESHOLD) {
        mRssiThreshold = LOW_RSSI_THRESHOLD;
      }
    }
    mScanSuccessCount = 0;
  }

  void stopScanMonitor() {
    if (mScanTimer != null) {
      mScanTimer.cancel();
      mScanTimer = null;
    }
    if (mScanTimerTask != null) {
      mScanTimerTask.cancel();
      mScanTimerTask = null;
    }
  }

  void scanSuccess() {
    mScanSuccessCount++;
  }

  void resetFactoryConnectCount() {
    this.mFactoryResetConnectCount = 2;
  }

  void setUnlockWithoutBle(boolean unlockWithoutBle) {
    mUnlockWithoutBle = unlockWithoutBle;
  }

  boolean unlockWithoutBle() {
    if (mFactoryResetConnectCount >= 0) {
      mFactoryResetConnectCount--;
      return false;
    }
    return mUnlockWithoutBle;
  }

  //兼容门锁16版本而设置
  void setUnlockMode4V16(BleLock bleLock, ParsedAd parsedAd) {
    if (LockConstant.LOCK_VERSION_16.compareToIgnoreCase(bleLock.getVersion()) >= 0) {
      mUnlockWithoutBle = parsedAd.manufacturer != UNLOCK_WITH_FP_AND_BLE;
    }
  }

  void setUnlockMode(BlePkg pkg) {
    if (pkg == null || pkg.getBody() == null) return;
    String payload = pkg.getBody().getPayload();
    if (TextUtils.isEmpty(payload)) return;
    mUnlockWithoutBle = UNLOCK_WITHOUT_BLE.equals(payload);
  }
}
