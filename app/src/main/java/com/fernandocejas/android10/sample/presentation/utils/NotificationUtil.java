package com.fernandocejas.android10.sample.presentation.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.fernandocejas.android10.sample.presentation.R;

/**
 * @author shaojun
 * @name VersionUtil
 * @package com.fernandocejas.android10.sample.presentation.view.utils
 * @date 15-11-27
 */
public class NotificationUtil {

  private static final int NOTIFICATION_ID = 0;

  public static void showNotification(Context context, String title, String content, Intent pendingIntent) {
    NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
    String appName = context.getString(R.string.app_name);
    builder.setContentTitle(TextUtils.isEmpty(title) ? appName : title)
        .setSmallIcon(R.drawable.app_icon)
        .setContentText(content);

    PendingIntent pIntent = PendingIntent.getActivity(context, 1, pendingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    builder.setContentIntent(pIntent);
    builder.setAutoCancel(true);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      builder.setVisibility(Notification.VISIBILITY_PUBLIC);
      // 关联PendingIntent
      builder.setFullScreenIntent(pIntent, false);
    }
    Notification notification = builder.build();

    /**
     * sound属性是一个 Uri 对象。 可以在通知发出的时候播放一段音频，这样就能够更好地告知用户有通知到来.
     * 如：手机的/system/media/audio/ringtones 目录下有一个 Basic_tone.ogg音频文件，
     * 可以写成： Uri soundUri = Uri.fromFile(new
     * File("/system/media/audio/ringtones/Basic_tone.ogg"));
     * notification.sound = soundUri; 我这里为了省事，就去了手机默认设置的铃声
     */
    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    notification.sound = uri;
    /**
     * 手机处于锁屏状态时， LED灯就会不停地闪烁， 提醒用户去查看手机,下面是绿色的灯光一 闪一闪的效果
     */
    notification.ledARGB = Color.GREEN;// 控制 LED 灯的颜色，一般有红绿蓝三种颜色可选
    notification.ledOnMS = 1000;// 指定 LED 灯亮起的时长，以毫秒为单位
    notification.ledOffMS = 1000;// 指定 LED 灯暗去的时长，也是以毫秒为单位
    notification.flags = Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;// 指定通知的一些行为，其中就包括显示

    /**
     * vibrate属性是一个长整型的数组，用于设置手机静止和振动的时长，以毫秒为单位。
     * 参数中下标为0的值表示手机静止的时长，下标为1的值表示手机振动的时长， 下标为2的值又表示手机静止的时长，以此类推。
     */
    long[] vibrates = { 0, 1000, 1000, 1000 };
    notification.vibrate = vibrates;


    notifyManager.notify(NOTIFICATION_ID, notification);

  }

}
