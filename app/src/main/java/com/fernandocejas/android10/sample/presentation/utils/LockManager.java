package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.IBleOperable;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * lock存储工具类
 *
 * @author shaojun
 * @name LockManager
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class LockManager {

  private static LinkedHashMap<String, BleLock> mLocks;

  public static void saveLocksLocal(Context context, List<GetDevTreeResponse<List<DeviceBean>>> responses) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    Object o = ObjectUtil.readObject(context, fileName);
    LinkedHashMap<String, BleLock> savedLocks = o == null ? new LinkedHashMap<>() : (LinkedHashMap<String, BleLock>) o;

    for (GetDevTreeResponse response : responses) {
      if (!AppConstant.DEVICE_TYPE_LOCK.equals(response.getDeviceType())) continue;
      if (savedLocks.containsKey(response.getMac())) {
        BleLock bleLock = savedLocks.get(response.getMac());
        if (response.getDeviceSerialNo().equals(bleLock.getId())
            && bleLock.getMac().equals(response.getMac())
            && response.getDeviceVersion().equals(bleLock.getVersion())) {
          continue;
        }
      }
      BleLock bleLock = new BleLock();
      bleLock.setId(response.getDeviceSerialNo());
      bleLock.setBindRouterId(response.getRouterSerialNo());
      bleLock.setMac(response.getMac());
      bleLock.setDeviceName(response.getDeviceName());
      bleLock.setModel(response.getDeviceModel());
      bleLock.setKeyRepoId(response.getDeviceSerialNo() + "_" + PrefConstant.getUserUniqueKey("1"));
      bleLock.setType(AppConstant.DEVICE_TYPE_LOCK);
      bleLock.setVersion(response.getDeviceVersion());
      savedLocks.put(bleLock.getMac(), bleLock);
    }
    ObjectUtil.saveObject(context, savedLocks, fileName);
  }


  public static void delete(Context context, String lockMac) {
    if (TextUtils.isEmpty(lockMac)) return;
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    Object o = ObjectUtil.readObject(context, fileName);
    if (o == null) return;
    LinkedHashMap<String, BleLock> savedLocks = (LinkedHashMap<String, BleLock>) o;
    if (!savedLocks.containsKey(lockMac)) return;
    savedLocks.remove(lockMac);
    ObjectUtil.saveObject(context, savedLocks, fileName);
  }

  public static void deleteById(Context context, String id) {
    BleLock bleLock = getLockById(context, id);
    if (bleLock == null) return;
    delete(context, bleLock.getMac());
  }

  public static void add(Context context, GetDevTreeResponse<List<DeviceBean>> lock) {
    if (lock == null) return;
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    Object o = ObjectUtil.readObject(context, fileName);
    LinkedHashMap<String, BleLock> savedLocks = o == null ? new LinkedHashMap<>() : (LinkedHashMap<String, BleLock>) o;
    if (savedLocks.containsKey(lock.getMac())) return;

    BleLock bleLock = new BleLock();
    bleLock.setId(lock.getDeviceSerialNo());
    bleLock.setBindRouterId(lock.getRouterSerialNo());
    bleLock.setMac(lock.getMac());
    bleLock.setDeviceName(lock.getDeviceName());
    bleLock.setModel(lock.getDeviceModel());
    bleLock.setKeyRepoId(lock.getDeviceSerialNo() + "_" + PrefConstant.getUserUniqueKey("1"));
    bleLock.setType(AppConstant.DEVICE_TYPE_LOCK);
    bleLock.setVersion(lock.getDeviceVersion());
    savedLocks.put(bleLock.getMac(), bleLock);

    ObjectUtil.saveObject(context, savedLocks, fileName);

  }

  /**
   * 筛选满足解锁要求的集合
   *
   * @param context
   * @return
   */
  public static LinkedHashMap<String, BleLock> getUnlockableLocks(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    LinkedHashMap<String, BleLock> savedLocks = (LinkedHashMap<String, BleLock>) ObjectUtil.readObject(context, fileName);
    if (savedLocks == null || savedLocks.isEmpty()) return null;
    Set<String> addresses = savedLocks.keySet();
    LinkedHashMap<String, BleLock> result = new LinkedHashMap<>();
    for (String address : addresses) {
      if (TextUtils.isEmpty(address)) continue;
      BleLock bleLock = savedLocks.get(address);
      if (bleLock == null) continue;
      if (TextUtils.isEmpty(bleLock.getMac())) continue;
      if (!AppConstant.DEVICE_TYPE_LOCK.equals(bleLock.getType())) continue;
      if (!KeystoreRepertory.getInstance().has(bleLock.getKeyRepoId())) continue;
      result.put(address, bleLock);
    }
    return result;
  }

  public static BleLock getLock(Context context, String mac) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    LinkedHashMap<String, BleLock> savedLocks = (LinkedHashMap<String, BleLock>) ObjectUtil.readObject(context, fileName);
    if (savedLocks == null || savedLocks.isEmpty()) return null;
    return savedLocks.get(mac);
  }

  public static BleLock getLockById(Context context, String id) {
    if (TextUtils.isEmpty(id)) return null;
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    LinkedHashMap<String, BleLock> savedLocks = (LinkedHashMap<String, BleLock>) ObjectUtil.readObject(context, fileName);
    if (savedLocks == null || savedLocks.isEmpty()) return null;
    Set<String> addresses = savedLocks.keySet();
    for (String address : addresses) {
      if (TextUtils.isEmpty(address)) continue;
      BleLock bleLock = savedLocks.get(address);
      if (!id.equals(bleLock.getId())) continue;
      return bleLock;
    }
    return null;
  }

  public static void deleteKeyRepo(Context context, String mac) {
    BleLock bleLock = LockManager.getLock(context, mac);
    if (bleLock != null) {
      KeystoreRepertory.getInstance().clear(bleLock.getKeyRepoId());
    }
  }

  public static void loadLocks(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.LOCK_FILE_NAME;
    mLocks = (LinkedHashMap<String, BleLock>) ObjectUtil.readObject(context, fileName);
    if (mLocks == null) {
      mLocks = new LinkedHashMap<>();
    }
    System.out.println("mLocks = " + mLocks);
  }

  /**
   * 获取云端最新开锁方式信息并配置本地方式
   *
   * @param context
   * @param response
   */
  public static void setUnlockMode(Context context, List<GetDevTreeResponse<List<DeviceBean>>> response) {
    if (response == null || response.isEmpty()) return;
    boolean unlockWithoutBle = true;
    for (GetDevTreeResponse<List<DeviceBean>> device : response) {
      if (!AppConstant.DEVICE_TYPE_LOCK.equals(device.getDeviceType())) continue;
      String deviceDetail = device.getDeviceDetail();
      if (TextUtils.isEmpty(deviceDetail)) continue;
      try {
        JSONObject jsonDeviceDetail = new JSONObject(deviceDetail);
        String openConfig = jsonDeviceDetail.getString("openConfig");
        String pwdOpenConfig = jsonDeviceDetail.getString("passwordOpenConfig");
        String cardOpenConfig = jsonDeviceDetail.getString("doorcardOpenConfig");
        if ("1".equals(openConfig) || "1".equals(pwdOpenConfig) || "1".equals(cardOpenConfig)) {
          unlockWithoutBle = false;
          break;
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
    IBleOperable bleLockService = ((AndroidApplication) context.getApplicationContext()).getBleLockService();
    if (bleLockService == null) return;
    bleLockService.setUnlockWithoutBle(unlockWithoutBle);
  }

  public static void updateLockVersion(Context context, String address, String newVersion) {
    BleLock bleLock = getLock(context, address);
    if (bleLock == null) return;
    bleLock.setVersion(newVersion);
  }
}
