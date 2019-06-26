package com.fernandocejas.android10.sample.presentation.view.device.lock.data;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2018/04/25
 *     desc   : 用于开门方式管理
 *     version: 1.0
 * </pre>
 */
public class UnlockModeManager {

  public UnlockModeManager() {
  }

  public List<UnlockMode> getFpUnlockModes() {
    List<UnlockMode> results = new ArrayList<>();
    UnlockMode unlockMode = new UnlockMode();
    unlockMode.setName("仅指纹开锁");
    unlockMode.setBleCode(UnlockMode.UNLOCK);
    unlockMode.setUnlockModeCloud("0");
    results.add(unlockMode);

    unlockMode = new UnlockMode();
    unlockMode.setName("手机连接蓝牙后指纹开锁");
    unlockMode.setBleCode(UnlockMode.UNLOCK_BLE);
    unlockMode.setUnlockModeCloud("1");
    results.add(unlockMode);

    return results;
  }

  public List<UnlockMode> getPwdUnlockModes() {
    List<UnlockMode> results = new ArrayList<>();
    UnlockMode unlockMode = new UnlockMode();
    unlockMode.setName("仅密码开锁");
    unlockMode.setBleCode(UnlockMode.UNLOCK);
    unlockMode.setUnlockModeCloud("0");
    results.add(unlockMode);

    unlockMode = new UnlockMode();
    unlockMode.setName("手机连接蓝牙后密码开锁");
    unlockMode.setBleCode(UnlockMode.UNLOCK_BLE);
    unlockMode.setUnlockModeCloud("1");
    results.add(unlockMode);

    return results;
  }

  public List<UnlockMode> getCardUnlockModes() {
    List<UnlockMode> results = new ArrayList<>();
    UnlockMode unlockMode = new UnlockMode();
    unlockMode.setName("仅门卡开锁");
    unlockMode.setBleCode(UnlockMode.UNLOCK);
    unlockMode.setUnlockModeCloud("0");
    results.add(unlockMode);

    unlockMode = new UnlockMode();
    unlockMode.setName("手机连接蓝牙后门卡开锁");
    unlockMode.setBleCode(UnlockMode.UNLOCK_BLE);
    unlockMode.setUnlockModeCloud("1");
    results.add(unlockMode);

    return results;
  }
}
