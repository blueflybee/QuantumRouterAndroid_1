package com.fernandocejas.android10.sample.presentation.service.polling;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.annotation.NonNull;

/**
 * Polling service
 *
 * @Author Ryan
 * @Create 2013-7-13 上午10:18:44
 */
public class PollingService extends Service {

  public static final String ACTION = "com.ai.service.PollingService";
  /**
   * Polling thread
   *
   * @Author Ryan
   * @Create 2013-7-13 上午10:18:34
   */
  int count = 0;
  private Notification mNotification;
  private NotificationManager mManager;

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onCreate() {
  }

  @Override
  public void onStart(Intent intent, int startId) {
    new PollingThread().start();
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    System.out.println("Service:onDestroy");
  }

  class PollingThread extends Thread {
    @Override
    public void run() {
//      String username =
//          PreferencesUtils.getString(getApplicationContext(), PrefConstant.KEY_USERNAME);
//      String password =
//          PreferencesUtils.getString(getApplicationContext(), PrefConstant.KEY_PASSWORD);
//      YZ0001Request request = new YZ0001Request();
//      request.setUsercode(username);
//      request.setPwd(password);
//      request.setDeviceType("aPhone");
//
//      YZ0001Response response = (YZ0001Response) requestService(request, BizCode.YZ0001);
//
//      String sessionIdNew = response.getSessionId();
//
//      IPostConnection connection = CloudRestApiImpl.getApiPostConnection();
//      String sessionIdOld = connection.getSessionId();
//      if (!TextUtils.isEmpty(sessionIdNew) && !sessionIdNew.equals(sessionIdOld)) {
//        connection.setSessionId(sessionIdNew);
//      }
    }
  }
}
