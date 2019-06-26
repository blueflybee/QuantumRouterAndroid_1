/*
 * Copyright 2018 Google Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.google.android.sambadocumentsprovider.mount;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03null
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class AppConstant {
  public static final String PLATFORM_ANDROID = "ANDROID";
  private static Map<String, String> mLockStatus = new HashMap<>();
  private static Map<String, String> mFirmUpdateStatus = new HashMap<>();

  private static boolean gFirstAddFingerPrint = false;

  public static final String KEY_LOCK_STATUS_0 = "0";
  public static final String KEY_LOCK_STATUS_1 = "1";

  public static final String DEVICE_TYPE_ROUTER = "0";
  public static final String DEVICE_TYPE_LOCK = "1";
  public static final String DEVICE_TYPE_CAT = "2";
  public static final String DEVICE_TYPE_CAMERA = "3";

  public static final String IMG_UPLOADING = "IMG UPLOADING";
  public static final String IMG_CHECKING = "IMG CHECKING";
  public static final String IMG_CHECK_FAILED = "IMG CHECK FAILED";
  public static final String IMG_FLASHING = "IMG FLASHING";
  public static final String IMG_UPLOADING_FAILED = "IMG UPLOADING FAILED";

  public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
  public static final String APP_PATH = SDCARD_PATH + "Qtec" + File.separator;
  public static final String SAMBA_DOWNLOAD_PATH = APP_PATH + "QtecSamba" + File.separator; //Samba下载路径
  public static final String SAMBA_DOWNLOAD_CACHE_PATH = APP_PATH + "QtecSambaCache" + File.separator; //Samba预览缓存路径

  static {
    mLockStatus.put(KEY_LOCK_STATUS_0, "未使用");
    mLockStatus.put(KEY_LOCK_STATUS_1, "使用中");

    mFirmUpdateStatus.put(IMG_UPLOADING, "正在下载中...");
    mFirmUpdateStatus.put(IMG_CHECKING, "正在校验中...");
    mFirmUpdateStatus.put(IMG_CHECK_FAILED, "校验失败");
    mFirmUpdateStatus.put(IMG_FLASHING, "正在升级中...");
    mFirmUpdateStatus.put(IMG_UPLOADING_FAILED, "下载固件失败...");
  }


  public static String getLockStatus(String key) {
    return mLockStatus.get(key);
  }

  public static String getFirmUpdateStatus(String key) {
    return mFirmUpdateStatus.get(key);
  }


  public static boolean isgFirstAddFingerPrint() {
    return gFirstAddFingerPrint;
  }

  public static void setgFirstAddFingerPrint(boolean gFirstAddFingerPrint) {
    AppConstant.gFirstAddFingerPrint = gFirstAddFingerPrint;
  }

  /**
   * 获取本机mac地址(手机)
   *
   * @param
   * @return
   */
  public static String getMacAddress(Context context) {
    String address = null;
    // 把当前机器上的访问网络接口的存入 Enumeration集合中
    Enumeration<NetworkInterface> interfaces = null;
    try {
      interfaces = NetworkInterface.getNetworkInterfaces();
      while (interfaces.hasMoreElements()) {
        NetworkInterface netWork = interfaces.nextElement();
        // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
        byte[] by = netWork.getHardwareAddress();
        if (by == null || by.length == 0) {
          continue;
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : by) {
          builder.append(String.format("%02X:", b));
        }
        if (builder.length() > 0) {
          builder.deleteCharAt(builder.length() - 1);
        }
        String mac = builder.toString();
        // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
        if (netWork.getName().equals("wlan0")) {
          address = mac;
        }
      }

    } catch (SocketException e) {
      e.printStackTrace();
    }

    return address;

  }

}
