package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FileBean;

import java.util.ArrayList;
import java.util.List;

/**
 * samba自动备份照片 信息缓存
 *
 * @author shaojun
 * @name LockManager
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class PictureCacheManager {

  /**
  * 已上传的照片的url
  *
  * @param
  * @return
  */
  public static void saveUrls(Context context, List<String> response) {
    if (response == null) response = new ArrayList<>();
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_URLS;
    ObjectUtil.saveObject(context, response, fileName);
  }

  public static List<String> fetchUrls(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_URLS;
    Object o = ObjectUtil.readObject(context, fileName);
    if (o == null) return null;
    return (List<String>) o;
  }

 /* public static void delete(Context context) {
    List<FileBean> pictures = fetchPictures(context);
    if(pictures.size() > 0){
      pictures.clear();
    }
    savePictures(context, pictures);
  }*/
}
