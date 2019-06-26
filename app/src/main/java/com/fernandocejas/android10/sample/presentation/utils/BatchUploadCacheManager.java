package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileUploadBean;

import java.util.List;

/**
 * samba批量上传
 *
 * @author shaojun
 * @name LockManager
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class BatchUploadCacheManager {

  /**
  *
  *
  * @param
  * @return
  */
  public static void saveBatchUploadList(Context context, List<FileUploadBean> response) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_BATCH_UPLOAD;
    ObjectUtil.saveObject(context, response, fileName);
    System.out.println("批量上传 存上传列表：==》 saveBatchUploadList");
  }

  public static List<FileUploadBean> fetchBatchUploadList(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_BATCH_UPLOAD;
    Object o = ObjectUtil.readObject(context, fileName);
    if (o == null) return null;
    return (List<FileUploadBean>) o;
  }

}
