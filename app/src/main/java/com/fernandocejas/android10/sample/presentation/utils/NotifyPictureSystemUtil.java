package com.fernandocejas.android10.sample.presentation.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;

import java.io.File;

/**
 * @author
 * @name VersionUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date
 * 下载照片完成后通知相册
 */
public class NotifyPictureSystemUtil {

  public static void sendNotifyToPictureSystem(Context context,String path) {
    try{
      Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
      Uri uri = Uri.fromFile(new File(path));
      intent.setData(uri);
      context.sendBroadcast(intent); // 发送广播通知
    }catch (Exception e){
      e.printStackTrace();
    }
  }

}
