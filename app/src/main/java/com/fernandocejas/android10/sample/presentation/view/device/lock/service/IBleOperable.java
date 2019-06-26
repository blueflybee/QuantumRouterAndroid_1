package com.fernandocejas.android10.sample.presentation.view.device.lock.service;

import android.os.Binder;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/12/05
 *     desc   : 门锁蓝牙操作接口
 *     version: 1.0
 * </pre>
 */
public interface IBleOperable {
  void startTimer();

  void pauseTimer();

  void stopTimer();

  void setUserUsingBle(boolean userUsingBle);

  void restartBle();

  void setCanConnect(boolean canConnect);

  void resetFactoryConnectCount();

  void setUnlockWithoutBle(boolean unlockWithoutBle);

  abstract class AbsBluetoothBinder extends Binder {
    public abstract IBleOperable getService();
  }
}
