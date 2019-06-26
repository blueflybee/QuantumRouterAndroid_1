/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.presentation;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.IBinder;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.keystore.IKeystoreRepertory;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.net.CloudRestApi;
import com.fernandocejas.android10.sample.data.net.CloudRestApiImpl;
import com.fernandocejas.android10.sample.data.net.ConnectionCreator;
import com.fernandocejas.android10.sample.data.net.IPostConnection;
import com.fernandocejas.android10.sample.data.net.RouterRestApi;
import com.fernandocejas.android10.sample.data.net.RouterRestApiImpl;
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ApplicationModule;
import com.fernandocejas.android10.sample.presentation.utils.CrashLogOutUtil;
import com.fernandocejas.android10.sample.presentation.utils.DownloadCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.UploadCacheManager;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.IHandlerLikeNotify;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.BluetoothService;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.IBleOperable;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.Downloader;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.LoginConfig;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.dbhelper.DatabaseUtil;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.google.android.sambadocumentsprovider.SambaConfiguration;
import com.google.android.sambadocumentsprovider.ShareManager;
import com.google.android.sambadocumentsprovider.TaskManager;
import com.google.android.sambadocumentsprovider.browsing.NetworkBrowser;
import com.google.android.sambadocumentsprovider.cache.DocumentCache;
import com.google.android.sambadocumentsprovider.nativefacade.CredentialCache;
import com.google.android.sambadocumentsprovider.nativefacade.SambaMessageLooper;
import com.google.android.sambadocumentsprovider.nativefacade.SmbFacade;
import com.hss01248.glidepicker.GlideIniter;
import com.hss01248.photoouter.PhotoUtil;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;


/**
 * Android Main Application
 */
public class AndroidApplication extends Application implements Thread.UncaughtExceptionHandler, IHandlerLikeNotify {

  public static final String APP = "RT";
  private static final String TAG = "Init";
  private final static float TARGET_HEAP_UTILIZATION = 0.70f;
  public static Boolean IS_HAS_NEW_MSG = false;//全局控制是否有新的消息，便于刷新UI

  private ApplicationComponent applicationComponent;
  public static Context mApplicationContext;
  public static CloudPushService pushService;
  private static Map<String, Downloader> downloaders;
  private static Map<String, Downloader> uploaders;
  private static DatabaseUtil mDBUtil, mUploadDBUtil;

  private IBle mBle;
  private IBleOperable mIBleOperable;
  private ServiceConnection mBleServiceConnection;

  private static AndroidApplication instance;
  private IHandlerLikeNotify currentNotify;
  private Stack<Activity> activityStack;

  private boolean mIsAppBackground = false;

  ////
  public static final int PERMISSION_REQUEST_CODE = 0;

  private final DocumentCache mCache = new DocumentCache();
  private final TaskManager mTaskManager = new TaskManager();

  private SmbFacade mSambaClient;
  private ShareManager mShareManager;
  private NetworkBrowser mNetworkBrowser;


  @Override
  public void onCreate() {
    super.onCreate();
    Utils.init(getApplicationContext());
    this.initializeInjector();
    initContext();

    initCSamba(this);

    initCatEyeVariables();

    initSmbDownloaders();

    initializeLogger();

    initBle();

//        initImageLoader(getApplicationContext());

    initRealConnection();

    initKeystoreRepertory();

    initImagePhotoUtil();

    /* 此处要保证initCrashLogUtil 在initUmengAnalytics 之前被初始化 */
//    initCrashLogUtil();

    initUmengAnalytics();

    initCloudChannel(this);

//    initLog();

    UploadUtils.getLocalPhoneList(this);//筛选出照片

    registerActivityLifecycleCallbacks();

  }

  private void initCSamba(AndroidApplication androidApplication) {

  }

  private void registerActivityLifecycleCallbacks() {
    registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

      private int mFinalCount;


      @Override
      public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

      }

      @Override
      public void onActivityStarted(Activity activity) {
        mFinalCount++;
        //如果mFinalCount ==1，说明是从后台到前台
        if (mFinalCount == 1) {
          //说明从后台回到了前台
//          System.out.println("appstate = forgeround");
          restartConnect();
          mIsAppBackground = false;
        }
      }

      @Override
      public void onActivityResumed(Activity activity) {

      }

      @Override
      public void onActivityPaused(Activity activity) {

      }

      @Override
      public void onActivityStopped(Activity activity) {
        mFinalCount--;
        //如果mFinalCount ==0，说明是前台到后台
        if (mFinalCount == 0) {
          //说明从前台回到了后台
//          System.out.println("appstate = background");
          mIsAppBackground = true;
        }
      }

      @Override
      public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

      }

      @Override
      public void onActivityDestroyed(Activity activity) {

      }

      private void restartConnect() {
        IBleOperable bleLockService = getBleLockService();
        if (bleLockService != null) {
          bleLockService.setCanConnect(true);
          bleLockService.resetFactoryConnectCount();
        }
      }
    });
  }

  public boolean isAppBackground() {
    return mIsAppBackground;
  }

  public static AndroidApplication getApplication() {
    return instance;
  }

  /**
   * 猫眼初始化
   *
   * @param
   * @return
   */
  private void initCatEyeVariables() {
    activityStack = new Stack<>();
    System.out.println("打印日志 = " + TAG);

    currentNotify = null;

    instance = this;
  }


//  private void initLog() {
//    LogcatUtil.start(this, "QtecLog", "blelog.txt");
//  }


  /**
   * 打印日志到本地文件: QtecLog
   */
  private void initCrashLogUtil() {
    CrashLogOutUtil myCrashHandler = CrashLogOutUtil.getInstance();
    myCrashHandler.init(getApplicationContext());
  }

  private void initImagePhotoUtil() {
    PhotoUtil.init(getApplicationContext(), new GlideIniter());
  }

  private void initBle() {
    BleService.setBleSdkType(BleService.BLE_SDK_TYPE_ANDROID_LOCK);
    Intent bindIntent = new Intent(this, BleService.class);
    bindService(bindIntent, new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName className,
                                     IBinder binder) {
        BleService service = ((BleService.LocalBinder) binder).getService();
        mBle = service.getBle();

        if (mBle != null && !mBle.adapterEnabled()) {
          // TODO: enalbe adapter
        }
      }

      @Override
      public void onServiceDisconnected(ComponentName classname) {
      }
    }, Context.BIND_AUTO_CREATE);


  }

  private void initSmbDownloaders() {

    mDBUtil = new DatabaseUtil(this);
    mUploadDBUtil = new DatabaseUtil(this);

    try {
      //下载器初始化 从文件中获取
      Map<String, Downloader> downloaderMap = DownloadCacheManager.fetchDownloaders(this);
      if (downloaderMap == null) {
        downloaders = new HashMap<>();
        System.out.println("下载器 = " + "AndroidApplication 新建");
        DownloadCacheManager.saveDownloaders(this, downloaders);//下载器 存到文件中
      } else {
        System.out.println("下载器 = " + "AndroidApplication 从文件读取");
        downloaders = downloaderMap;
      }

      //上传器初始化
      Map<String, Downloader> uploaderMap = UploadCacheManager.fetchUploaders(this);
      if (uploaderMap == null) {
        uploaders = new HashMap<>();
        System.out.println("上传器 = " + "AndroidApplication 新建");
        UploadCacheManager.saveUploaders(this, uploaders);//上传器 存到文件中
      } else {
        uploaders = uploaderMap;
        System.out.println("上传器 = " + "AndroidApplication 从文件读取 size= " + uploaders.size());
      }


      //把所有的状态恢复成暂停状态
      if (downloaders.size() > 0) {
        //启动下一个
        Iterator it = downloaders.keySet().iterator();//获取所有的健值
        while (it.hasNext()) {
          try {
            String key;
            key = (String) it.next();

            downloaders.get(key).setState(Downloader.PAUSE);
            DownloadCacheManager.saveDownloaders(this, downloaders);

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      //把所有的状态恢复成暂停状态
      if (uploaders.size() > 0) {
        Iterator it = uploaders.keySet().iterator();//获取所有的健值
        while (it.hasNext()) {
          try {
            String key;
            key = (String) it.next();

            uploaders.get(key).setState(Downloader.PAUSE);
            UploadCacheManager.saveUploaders(this, uploaders);

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private void initContext() {
    mApplicationContext = getApplicationContext();
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  private void initUmengAnalytics() {
    //MobclickAgent.openActivityDurationTrack(false); // 禁止默认的页面统计方式，这样将不会再自动统计Activity。
    MobclickAgent.setScenarioType(getApplicationContext(), MobclickAgent.EScenarioType.E_UM_NORMAL);
    MobclickAgent.setDebugMode(true);//调试模式
    MobclickAgent.setSessionContinueMillis(40000);
  }

  private void initializeLogger() {
    Logger.init(APP);
  }

  private void initializeInjector() {
    this.applicationComponent =
        DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
  }

  private void initKeystoreRepertory() {
    IKeystoreRepertory repertory = KeystoreRepertory.getInstance();
    repertory.init(getApplicationContext());
  }

  private void initRealConnection() {
    //  PrefConstant.getCloudUrl()

    IPostConnection cloudConnection = ConnectionCreator.create(ConnectionCreator.CLOUD_CONNECTION, CloudRestApi.URL_GATEWAY_3_CARETEC);
    CloudRestApiImpl.setApiPostConnection(cloudConnection);

    IPostConnection routerConnection =
        ConnectionCreator.create(ConnectionCreator.ROUTER_CONNECTION, RouterRestApi.URL_DEBUG);
    RouterRestApiImpl.setApiPostConnection(routerConnection);
  }

  public ApplicationComponent getApplicationComponent() {
    return this.applicationComponent;
  }

  public static void exit() {
//        MobclickAgent.onKillProcess(mApplicationContext);
    android.os.Process.killProcess(android.os.Process.myPid());
  }

  /**
   * 初始化阿里云推送通道及移动数据分析
   */
  private void initCloudChannel(Context applicationContext) {
    /*** 阿里云移动推送  ***/
    PushServiceFactory.init(applicationContext);
    pushService = PushServiceFactory.getCloudPushService();

    pushService.register(applicationContext, new CommonCallback() {
      @Override
      public void onSuccess(String response) {
        Log.d(TAG, "init cloudchannel success");
        // PrefConstant.MSG_DEVICE_ID = pushService.getDeviceId();
        String tmpMsgID = pushService.getDeviceId();
        if (TextUtils.isEmpty(tmpMsgID)) return;
        SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
        spUtils.put(PrefConstant.MSG_DEVICE_ID, tmpMsgID);
      }

      @Override
      public void onFailed(String errorCode, String errorMessage) {
        Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
      }
    });

    // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
    HuaWeiRegister.register(applicationContext);

  }

  @Override
  public void uncaughtException(Thread thread, Throwable ex) {
    ex.printStackTrace();
    thread.destroy();
  }

  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public static CloudPushService getPushService() {
    return pushService;
  }

  public void setDownloaders(Map<String, Downloader> downloaders) {
    this.downloaders = downloaders;
  }

  public static Map<String, Downloader> getDownloaders() {
    return downloaders;
  }

  public static Map<String, Downloader> getUploaders() {
    return uploaders;
  }

  public static DatabaseUtil getDBUtil() {
    return mDBUtil;
  }

  public static DatabaseUtil getUploadDBUtil() {
    return mUploadDBUtil;
  }

  public IBle getIBle() {
    return mBle;
  }

  public IBleOperable getBleLockService() {
    return mIBleOperable;
  }

  public void bindAndStartBleService() {
    try {
      String manufacturer = DeviceUtils.getManufacturer();
      String model = DeviceUtils.getModel();
      System.out.println("manufacturer = " + manufacturer);
      System.out.println("model = " + model);
      Class<?> serviceClass;
//      if (PhoneConstant.isGalaxyS8(manufacturer, model)) {
//        serviceClass = BluetoothServiceForSpecial.class;
//      } else {
      serviceClass = BluetoothService.class;
//      }
      Intent intent = new Intent(getApplicationContext(), serviceClass);
      if (mBleServiceConnection == null) {
        mBleServiceConnection = new ServiceConnection() {
          @Override
          public void onServiceConnected(ComponentName name, IBinder binder) {
            IBleOperable service = ((IBleOperable.AbsBluetoothBinder) binder).getService();
            mIBleOperable = service;
//            service.restartBle();
          }

          @Override
          public void onServiceDisconnected(ComponentName name) {
            mIBleOperable = null;
          }
        };
      }
      bindService(intent, mBleServiceConnection, Context.BIND_AUTO_CREATE);

      startService(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void unbindAndStopBleService() {
    try {
      if (mBleServiceConnection != null) {
        unbindService(mBleServiceConnection);
      }
      if (mIBleOperable != null) {
        Intent intent = new Intent(getApplicationContext(), mIBleOperable.getClass());
        stopService(intent);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void unbindBleService() {
    try {
      if (mBleServiceConnection != null) {
        unbindService(mBleServiceConnection);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /////////////////cateye
  public static AndroidApplication getInstance() {
    return instance;
  }


  /**
   * 修改当前显示的 Activity 引用
   *
   * @param currentNotify
   */
  public void setCurrentNotify(IHandlerLikeNotify currentNotify) {
    this.currentNotify = currentNotify;
  }

  public IHandlerLikeNotify getCurrentNotify() {
    return currentNotify;
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    /*currentNotify.onNotify(what, arg1, arg2,obj);*/
  }

  /**
   * 底层所有的回调接口
   *
   * @param what
   * @param uchType
   * @param channel
   * @param obj
   */
  public synchronized void onJniNotify(int what, int uchType, int channel, Object obj) {
    if (null != currentNotify) {
      currentNotify.onNotify(what, uchType, channel, obj);
    } else {
      Log.e(TAG, "currentNotify is null!");
    }

  }

  /**
   * activity 入栈
   *
   * @param activity
   */
  public void push(Activity activity) {
    activityStack.push(activity);
  }

  /**
   * activity 出栈
   */
  public Activity pop() {
    return (false == activityStack.isEmpty()) ? activityStack.pop() : null;

  }

  /////////////////////////////////


  private void initialize(Context context) {
    if (mSambaClient != null) {
      // Already initialized.
      return;
    }

    initializeSambaConf(context);

    final SambaMessageLooper looper = new SambaMessageLooper();
    CredentialCache credentialCache = looper.getCredentialCache();
    mSambaClient = looper.getClient();

    mShareManager = new ShareManager(context, credentialCache);

    mNetworkBrowser = new NetworkBrowser(mSambaClient, mTaskManager);

    registerNetworkCallback(context);
  }

  private void initializeSambaConf(Context context) {
    final File home = context.getDir("home", MODE_PRIVATE);
    final File share = context.getExternalFilesDir(null);
    final SambaConfiguration sambaConf = new SambaConfiguration(home, share);

    final SambaConfiguration.OnConfigurationChangedListener listener = new SambaConfiguration.OnConfigurationChangedListener() {
      @Override
      public void onConfigurationChanged() {
        if (mSambaClient != null) {
          mSambaClient.reset(LoginConfig.OS_PORT + "");
        }
      }
    };

    // Sync from external folder. The reason why we don't use external folder directly as HOME is
    // because there are cases where external storage is not ready, and we don't have an external
    // folder at all.
    if (sambaConf.syncFromExternal(listener)) {
      if (BuildConfig.DEBUG) Log.d(TAG, "Syncing smb.conf from external folder. No need to try "
          + "flushing default config.");
      return;
    }

    sambaConf.flushDefault(listener);
  }

  private void registerNetworkCallback(Context context) {
    final ConnectivityManager manager =
        (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
    manager.registerNetworkCallback(
        new NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build(),
        new ConnectivityManager.NetworkCallback() {
          @Override
          public void onAvailable(Network network) {
            mSambaClient.reset(LoginConfig.OS_PORT + "");
          }
        });
  }

  public static void init(Context context) {
    getApplication(context).initialize(context);
  }

  public static ShareManager getServerManager(Context context) {
    return getApplication(context).mShareManager;
  }

  public static SmbFacade getSambaClient(Context context) {
    return getApplication(context).mSambaClient;
  }

  public static SmbFacade getSambaClient() {
    return instance.mSambaClient;
  }

  public static DocumentCache getDocumentCache(Context context) {
    return getApplication(context).mCache;
  }

  public static TaskManager getTaskManager(Context context) {
    return getApplication(context).mTaskManager;
  }

  public static NetworkBrowser getNetworkBrowser(Context context) {
    return getApplication(context).mNetworkBrowser;
  }

  private static AndroidApplication getApplication(Context context) {
    return ((AndroidApplication) context.getApplicationContext());
  }


}
