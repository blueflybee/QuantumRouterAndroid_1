package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.Downloader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * samba文件下载器 信息缓存
 *
 * @author shaojun
 * @name LockManager
 * @package com.fernandocejas.android10.sample.presentation.utils
 * @date 15-11-10
 */
public class DownloadCacheManager {

  /**
  *
  *
  * @param
  * @return
  */
  public synchronized static void saveDownloaders(Context context, Map<String, Downloader> response) {
    if (response == null) response = new HashMap<>();
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_DOWNLOADERS;
    ObjectUtil.saveObject(context, response, fileName);
    System.out.println("下载器 存文件：==》 saveDownloaders");
  }

  public  synchronized static void putDownloader(Context context,String url, Downloader downloader) {
    Map<String, Downloader> response = fetchDownloaders(context);
    if(response != null){
      response.put(url, downloader);
      System.out.println("下载器 存文件：==》 put Downloader");
      saveDownloaders(context,response);
    }
  }

  public synchronized static void updateDownloaderInfos(Context context, String url ,Downloader downloader) {
    Map<String, Downloader> response = fetchDownloaders(context);
    if(response != null){
      Iterator it = response.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        String key;
        key = (String) it.next();
        if(url.equals(key)){
          response.get(url).setCompeleteSize(downloader.getCompeleteSize());
          response.get(url).setFileSize(downloader.getFileSize());
          System.out.println("下载器 存文件：==》 更新下载信息");
          saveDownloaders(context,response);
          break;
        }
      }
    }

  }

  public synchronized static void delete(Context context, String url) {
    Map<String, Downloader> response = fetchDownloaders(context);
    if(response != null){
      Iterator it = response.keySet().iterator();//获取所有的健值
      while (it.hasNext()) {
        String key;
        key = (String) it.next();
        if(url.equals(key)){
          response.get(url).reset();
          response.remove(url);
          System.out.println("下载器 存文件：==》 下载完成 删除文件下载器");
          saveDownloaders(context,response);
          break;
        }
      }
    }

  }

  public synchronized static Map<String, Downloader> fetchDownloaders(Context context) {
    String fileName = PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_ROUTER) + "_" + ObjectUtil.SAMBA_DOWNLOADERS;
    Object o = ObjectUtil.readObject(context, fileName);
    if (o == null) return null;
    return (Map<String, Downloader>) o;
  }

}
