package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.Downloader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * samba文件上传器 信息缓存
 *
 * @author shaojun
 * @name LockManager
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class UploadCacheManager {

  /**
  *
  *
  * @param
  * @return
  */
  public synchronized static void saveUploaders(Context context, Map<String, Downloader> response) {
    if (response == null) response = new HashMap<>();
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_UPLOADERS;
    ObjectUtil.saveObject(context, response, fileName);
    System.out.println("上传器 存文件：==》 saveDownloaders");
  }

  public synchronized static void putUploader(Context context,String url, Downloader downloader) {
    Map<String, Downloader> response = fetchUploaders(context);
    if(response != null && downloader != null){
      response.put(url, downloader);
      System.out.println("上传器 存文件：==》 put Uploader");
      saveUploaders(context,response);
    }
  }

  public synchronized static void updateUploaderInfos(Context context, String url ,Downloader downloader) {
    Map<String, Downloader> response = fetchUploaders(context);
    if(response != null && downloader != null){
      Iterator it = response.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        String key;
        key = (String) it.next();
        if(url.equals(key)){
          response.get(url).setCompeleteSize(downloader.getCompeleteSize());
          response.get(url).setFileSize(downloader.getFileSize());
          System.out.println("上传器 存文件：==》 更新上传信息");
          saveUploaders(context,response);
          break;
        }
      }
    }

  }

  public synchronized static void delete(Context context, String url) {
    Map<String, Downloader> response = fetchUploaders(context);
    if(response != null && context != null){
      Iterator it = response.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        String key;
        key = (String) it.next();
        if(url.equals(key)){
          response.get(url).reset();
          response.remove(url);
          System.out.println("上传器 存文件：==》 上传完成 删除文件上传器");
          saveUploaders(context,response);
          break;
        }
      }
    }
  }

  public synchronized static Map<String, Downloader> fetchUploaders(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_UPLOADERS;
    Object o = ObjectUtil.readObject(context, fileName);
    if (o == null) return null;
    return (Map<String, Downloader>) o;
  }

}
