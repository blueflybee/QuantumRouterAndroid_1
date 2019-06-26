package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.text.TextUtils;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * 用户设备列表信息缓存
 *
 * @author shaojun
 * @name LockManager
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class DeviceCacheManager {


  public static void saveDevices(Context context, List<GetDevTreeResponse<List<DeviceBean>>> response) {
    if (response == null) response = new ArrayList<>();
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.USER_DEVICES;
    ObjectUtil.saveObject(context, response, fileName);
  }

  public static List<GetDevTreeResponse<List<DeviceBean>>> fetchDevices(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.USER_DEVICES;
    Object o = ObjectUtil.readObject(context, fileName);
    if (o == null) return null;
    return (List<GetDevTreeResponse<List<DeviceBean>>>) o;
  }

  public static void delete(Context context, String deviceId) {
    if (TextUtils.isEmpty(deviceId)) return;
    List<GetDevTreeResponse<List<DeviceBean>>> devices = fetchDevices(context);
    for (int i = 0; i < devices.size(); i++) {
      GetDevTreeResponse<List<DeviceBean>> device = devices.get(i);
      if (!deviceId.equals(device.getDeviceSerialNo())) continue;
      devices.remove(i);
    }
    saveDevices(context, devices);
  }

  public static void setDeviceUserRole(Context context, String deviceId, String userRole) {
    if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(userRole)) return;
    List<GetDevTreeResponse<List<DeviceBean>>> devices = fetchDevices(context);
    for (int i = 0; i < devices.size(); i++) {
      GetDevTreeResponse<List<DeviceBean>> device = devices.get(i);
      if (!deviceId.equals(device.getDeviceSerialNo())) continue;
      device.setUserRole(userRole);
      devices.set(i, device);
      break;
    }
    saveDevices(context, devices);
  }

  public static GetDevTreeResponse<List<DeviceBean>> getDeviceById(Context context, String deviceId) {
    if (TextUtils.isEmpty(deviceId)) return null;
    List<GetDevTreeResponse<List<DeviceBean>>> devices = fetchDevices(context);
    for (GetDevTreeResponse<List<DeviceBean>> device : devices) {
      if (!deviceId.equals(device.getDeviceSerialNo())) continue;
      return device;
    }
    return null;
  }
}
