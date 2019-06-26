package com.fernandocejas.android10.sample.data.constant;

import com.fernandocejas.android10.sample.data.data.BleLock;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GlobleConstant {

  public static final String NO_ID = "no_id";
  private static String gDeviceId = NO_ID;
  private static String gCatEyeId = NO_ID;
  private static String gCameraId = NO_ID;
  private static String gCameraPwd = "";//视频流的账号密码
  private static String gCatPwd = "";
  private static List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> mDeviceTrees;
  private static List<String> mPicturePaths;

  public static int gPictureRestoredCount = 0;//已备份图片数
  public static int gPictureRestoredSumCount = 0;//总备份的总数
  public static int gPictureRestoreIndex = 0;//备份上传下标
  public static int gBatchOperateCount = 0;//统计批量操作的数目

  public static long gPictureTotalSize = 0;//文件大小
  public static long gPictureUploadedSize = 0;//已上传的大小
  public static String gPictureUploadSpeed = "";//上传网速
  public static Boolean isLastPicRestored = true;//标注上次的照片是否备份完成
  public static Boolean isFinishedPicRestored = false;//标注是否备份完成
  public static Boolean isStopRestorePicture = false; //标志是否暂停上传
  public static Boolean isCatBinded = false; //标志是否绑定过
  public static Boolean isSambaExtranetAccess = false; //标志安全存储是否是外网访问

  public static Boolean isTimeZero = false;//记录无线中继是否倒计时为0

  public static String getgKeyRepoId() {
    return gKeyRepoId;
  }

  public static void setgKeyRepoId(String gKeyRepoId) {
    GlobleConstant.gKeyRepoId = gKeyRepoId;
  }

  private static String gKeyRepoId;
  private static String gDeviceType;

  public static String getgDeviceId() {
    return gDeviceId;
  }

  public static void setgDeviceId(String gDeviceId) {
    GlobleConstant.gDeviceId = gDeviceId;
  }

  public static void setgCatEyeId(String gCatEyeId) {
    GlobleConstant.gCatEyeId = gCatEyeId;
  }

  public static void setgCameraId(String gCameraId) {
    GlobleConstant.gCameraId = gCameraId;
  }

  public static void setgCameraPwd(String gCameraPwd) {
    GlobleConstant.gCameraPwd = gCameraPwd;
  }

  public static void setgCatPwd(String gCatPwd) {
    GlobleConstant.gCatPwd = gCatPwd;
  }

  public static String getgCatEyeId() {
    return gCatEyeId;
  }

  public static String getgCameraId() {
    return gCameraId;
  }

  public static String getgCameraPwd() {
    return gCameraPwd;
  }

  public static String getgCatPwd() {
    return gCatPwd;
  }

  public static String getgDeviceType() {
    return gDeviceType;
  }

  public static void setgDeviceType(String gDeviceType) {
    GlobleConstant.gDeviceType = gDeviceType;
  }

  public static void setDeviceTrees(List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> responses) {
    mDeviceTrees = new ArrayList<>();
    mDeviceTrees = responses;
  }

  /**
  * 存放图片备份的路径
  *
  * @param
  * @return
  */
  public static List<String> getGlobleArraylist() {
    if(mPicturePaths == null){
      mPicturePaths = new ArrayList<>();
    }

    return mPicturePaths;
  }

  public static List<String> getPicturePathList() {
    return mPicturePaths;
  }

  public static void setgPictureRestoredCount(int gPictureRestoredCount) {
    GlobleConstant.gPictureRestoredCount = gPictureRestoredCount;
  }

  public static void setBatchOperateCount(int gBatchOperateCount) {
    GlobleConstant.gBatchOperateCount = gBatchOperateCount;
  }

  public static void setgPictureRestoredSumCount(int gPictureRestoredSumCount) {
    GlobleConstant.gPictureRestoredSumCount = gPictureRestoredSumCount;
  }

  public static int getgPictureRestoredCount() {
    return gPictureRestoredCount;
  }

  public static int getBatchOperateCount() {
    return gBatchOperateCount;
  }

  public static int getgPictureRestoredSumCount() {
    return gPictureRestoredSumCount;
  }

  public static List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> getDeviceTrees() {
    return mDeviceTrees;
  }
}
