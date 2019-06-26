package com.fernandocejas.android10.sample.presentation.view.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.keystore.IKeystoreRepertory;
import com.blueflybee.keystore.KeystoreRepertory;
import com.blueflybee.keystore.callback.KeyStateCallback;
import com.blueflybee.keystore.data.LZKey;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.data.net.CloudRestApiImpl;
import com.fernandocejas.android10.sample.data.net.RouterRestApi;
import com.fernandocejas.android10.sample.data.net.RouterRestApiImpl;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMainBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMainComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.MainComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.KeyModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MainModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.fragment.DeviceFragment;
import com.fernandocejas.android10.sample.presentation.view.device.keystore.KeyInvalidActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.HooliganActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockInjectKeyView;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockManageTipActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.LoginConfig;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.SambaUtils;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.utils.UploadUtils;
import com.fernandocejas.android10.sample.presentation.view.logcat.LogcatView;
import com.fernandocejas.android10.sample.presentation.view.message.mqtt.MQTTManager;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.IUploadMsgIdView;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.UploadMsgIDPresenter;
import com.fernandocejas.android10.sample.presentation.view.mine.MineFragment;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.VersionInfoPresenter;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.VersionInfoView;
import com.fernandocejas.android10.sample.presentation.view.safe.SafeFragment;
import com.google.android.sambadocumentsprovider.ShareManager;
import com.google.android.sambadocumentsprovider.TaskManager;
import com.google.android.sambadocumentsprovider.base.OnTaskFinishedCallback;
import com.google.android.sambadocumentsprovider.cache.DocumentCache;
import com.google.android.sambadocumentsprovider.document.DocumentMetadata;
import com.google.android.sambadocumentsprovider.mount.MountServerTask;
import com.google.android.sambadocumentsprovider.nativefacade.SmbClient;
import com.jovision.JniUtil;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.CheckAppVersionResponse;
import com.qtec.mapp.model.rsp.UploadMsgDeviceIDResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.APPLY_KEYS;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class MainActivity extends BaseActivity implements HasComponent<MainComponent>, IUploadMsgIdView, MainView,
    AddRouterView_NetChange, VersionInfoView, LockInjectKeyView, LogcatView {
  public static final String ACTION_CAN_APPLY_KEY = "android.intent.action.lock.key.canapplykey";
  private static final String TAG = MainActivity.class.getSimpleName();
  private NetChangedReceiver mNetChangeReceiver;

  private SmbClient mClient;
  private DocumentCache mCache;
  private TaskManager mTaskManager;
  private ShareManager mShareManager;

  @Inject
  UploadMsgIDPresenter mUploadMsgIdPresenter;

  @Inject
  MainPresenter mMainPresenter;

  @Inject
  VersionInfoPresenter mVersionInfoPresenter;

//  @Inject
//  LogcatPresenter mLogcatPresenter;

  @Inject
  static AddRouterPresenter_NetChange mAddRouterPresenter;

  private MainComponent mMainComponent;
  private ActivityMainBinding mBinding;
  private KeyInvalidReceiver mKeyInvalidReceiver;

  private String mDeviceAddress;

  protected IBle mBle;

  private BlePkg mCurrentPkg;
  private BleRequest.CmdType mCurrentCmdType;

  private BleGattCharacteristic mCharacteristic;

  private LZKey mLZKey;

  private static boolean mIsKeyLow = false;

  static {
    System.loadLibrary("gnustl_shared");
    System.loadLibrary("stlport_shared");
    System.loadLibrary("tools");
    System.loadLibrary("nplayer");
    System.loadLibrary("alu");
    System.loadLibrary("play");
    System.loadLibrary("HisiLink");

    System.loadLibrary("elianjni");
    System.loadLibrary("elianjni-v7a");
    System.out.println("catEye: static加载.so文件");
  }

  private AppBroadcastReceiver mAppBroadcastReceiver;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    initializeInjector();

    initView();

    initPresenter();

    initKeystoreRepo();

    // 判断消息设备ID是否上传成功
    mUploadMsgIdPresenter.isMsgDeviceIdEmpty();

    initReceivers();

    checkAppVersion();

    ((AndroidApplication) getApplication()).bindAndStartBleService();

    initSettings();

    initUi();

    MQTTManager.instance().init(getContext());

//    uploadLogcat();

  }

//  private void uploadLogcat() {
//    mLogcatPresenter.uploadLogcat();
//  }

  /**
   * 初始化猫眼设置
   *
   * @param
   * @return
   */
  protected void initSettings() {
    CommonUtil.createDirectory(AppConsts.LOG_PATH);
    CommonUtil.createDirectory(AppConsts.CAPTURE_PATH);
    CommonUtil.createDirectory(AppConsts.VIDEO_PATH);
    CommonUtil.createDirectory(AppConsts.DOWNLOAD_PATH);

  }

  /**
   * 初始化m猫眼SDK
   *
   * @param
   * @return
   */
  protected void initUi() {
    try {
      JniUtil.initSDK(getApplication(), AppConsts.LOG_PATH, "");
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * 检查网络情况变化
   *
   * @param
   * @return
   */
  private void initNetChangeReceiver() {
    if (mNetChangeReceiver == null) {
      mNetChangeReceiver = new NetChangedReceiver();
    }

    IntentFilter filter = new IntentFilter();
    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    registerReceiver(mNetChangeReceiver, filter);
    System.out.println("网络情况：NetChangeReceiver注册");
  }

  private void checkAppVersion() {
    if (!VersionInfoPresenter.isChecked()) {
      mVersionInfoPresenter.checkVersion();
      VersionInfoPresenter.setIsChecked(true);
    }
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
  }

  private void initReceivers() {
    mKeyInvalidReceiver = new KeyInvalidReceiver();
    IntentFilter filter = new IntentFilter();
    filter.addAction(CloudRestApiImpl.ACTION_ROUTER_KEY_INVALID);
    filter.addAction(BleMapper.ACTION_BLE_LOCK_KEY_INVALID);
    registerReceiver(mKeyInvalidReceiver, filter);

    registerReceiver(mBleReceiver, BleService.getIntentFilter());

    IntentFilter applyKeyFilter = new IntentFilter();
    applyKeyFilter.addAction(ACTION_CAN_APPLY_KEY);
    registerReceiver(mCanApplyKeyReceiver, applyKeyFilter);

    initNetChangeReceiver();//网络状况的广播

    mAppBroadcastReceiver = new AppBroadcastReceiver();
    filter.addAction(LockManageTipActivity.ACTION_LOCK_MANAGE_TIPS);
    registerReceiver(mAppBroadcastReceiver, filter);

//    IntentFilter screenFilter = new IntentFilter();
//    screenFilter.addAction(Intent.ACTION_SCREEN_ON);
//    screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
//    registerReceiver(new BootCompleteReceiver(), screenFilter);

  }

  private void initKeystoreRepo() {

    KeystoreRepertory.getInstance().setKeyStateCallback(new KeyStateCallback() {
      private long reuseCount = 0;
      private int keyLowCount = 0;

      @Override
      public void onKeyLow(String repoId, String devType, int number, boolean isSaving, LZKey lzKey) {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++onKeyLow");
        System.out.println("repoId = [" + repoId + "], devType = [" + devType + "], number = [" + number + "], isSaving = [" + isSaving + "]");
//        System.out.println("mMainPresenter.isGettingLockKey() = " + mMainPresenter.isGettingLockKey());
        if (IKeystoreRepertory.TYPE_ROUTER.equals(devType) && !isSaving && !mMainPresenter.isGettingRouterKey()) {
          mMainPresenter.getRouterKeys(repoId, 150);
        } else if (IKeystoreRepertory.TYPE_LOCK.equals(devType) && !isSaving) {
          System.out.println("reuseCount++++++++++++++++++++++++++++++++++++++++++++ = " + reuseCount);
          System.out.println("keyLowCount++++++++++++++++++++++++++++++++++++++++++++ = " + keyLowCount);
          reuseCount++;
          if (reuseCount >= 2) {
            keyLowCount++;
            if (keyLowCount <= 3) return;
            mLZKey = lzKey;
            System.out.println("reuse mLZKey+++++++++++++++++++++++++++++ = " + mLZKey);
            mIsKeyLow = true;
            keyLowCount = 0;
            reuseCount = 0;
          }

        }
      }
    });
  }

  private void initPresenter() {
    mUploadMsgIdPresenter.setView(this);
    mMainPresenter.setView(this);
    mVersionInfoPresenter.setView(this);
    mAddRouterPresenter.setView(this);
//    mLogcatPresenter.setView(this);
  }

  private void initView() {
    mBinding.viewpager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager()));
    mBinding.viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tablayout));
    mBinding.tablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewpager));
    mBinding.tablayout.setSmoothScrollingEnabled(true);
    mBinding.viewpager.setCurrentItem(0);

    mClient = AndroidApplication.getSambaClient(this);
    mCache = AndroidApplication.getDocumentCache(this);
    mTaskManager = AndroidApplication.getTaskManager(this);
    mShareManager = AndroidApplication.getServerManager(this);
  }

  private void initializeInjector() {
    this.mMainComponent = DaggerMainComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mainModule(new MainModule())
        .keyModule(new KeyModule())
        .mineModule(new MineModule())
        .routerModule(new RouterModule())
        .build();
    this.mMainComponent.inject(this);
  }

  @Override
  public MainComponent getComponent() {
    return mMainComponent;
  }

  /**
   * 上传消息ID 成功
   */
  @Override
  public void uploadMsgID(UploadMsgDeviceIDResponse response) {
    //Toast.makeText(this, "消息ID上传成功", Toast.LENGTH_SHORT).show();
    System.out.println("设备ID上传服务器 成功！");
  }

  @Override
  public void showVersionInfo(CheckAppVersionResponse response) {
    int newVersion = response.getVersionNum();
    int currentVersion = AppUtils.getAppVersionCode();
    if (newVersion <= currentVersion) return;
    int minVersion = response.getMinVersion();
    DialogUtil.showVersionUpdateDialog(
        getContext(),
        currentVersion < minVersion,
        response.getVersionNo(),
        response.getVersionStatement(),
        v -> mVersionInfoPresenter.update());
  }

  private void getLockKeys() {
    pauseBleTimer();
    setUserUsingBle(true, mDeviceAddress);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("12");
        pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_APPLY_KEYS);
        body.setPayload("");
        pkg.setBody(body);
        sendBlePkg(pkg, APPLY_KEYS);
      }
    }, 2200);

  }

  private void sendBlePkg(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    mCurrentCmdType = cmdType;
    mBle = ((AndroidApplication) getApplication()).getIBle();
    if (mBle == null || TextUtils.isEmpty(mDeviceAddress)) return;
    mBle.requestConnect(mDeviceAddress, cmdType, 32);
  }

  @Override
  public void onKeyInjectStart() {
    System.out.println("MainActivity.onKeyInjectStart");
  }

  @Override
  public void onKeyInjectSuccess(String routerId) {
    System.out.println("MainActivity.onKeyInjectSuccess");
  }

  @Override
  public void onKeyInjectFail() {
  }

  @Override
  public void onKeyInjectProgress(int progress) {
    System.out.println("progress = " + progress);
  }

  @Override
  public void onKeyInjectCancel(String routerId) {
  }

  @Override
  public void showSearchSuccess(SearchRouterResponse response) {
    PrefConstant.ROUTER_ID_CONNECTED_DIRECT = response.getSerialnum();
    System.out.println("唯一ID showSearchSuccessActivity = " + PrefConstant.ROUTER_ID_CONNECTED_DIRECT);

    //图片备份判断是否是直连网络
    if (PrefConstant.getPictureRestoreState()) {
      //判断是否是内网
      if (PrefConstant.getPictureRestoreRouterId().equals(response.getSerialnum())) {
        //非外网访问
        GlobleConstant.isSambaExtranetAccess = false;
        //判断是否有密钥
        if (!KeystoreRepertory.getInstance().has(response.getSerialnum() + "_" + PrefConstant.getUserUniqueKey("0"))) {
          return;
        }

        getSambaAccount();

      } else {
        GlobleConstant.isSambaExtranetAccess = true;
      }
    }
  }

  @Override
  public void showSearchFailed(Throwable e) {
    PrefConstant.ROUTER_ID_CONNECTED_DIRECT = "FFFF";
    System.out.println("唯一ID showSearchFailed = " + PrefConstant.ROUTER_ID_CONNECTED_DIRECT);
  }

  public void getSambaAccount() {
    //解析登录IP
    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("Samba登录信息  解析IP地址= " + InputUtil.convertDomainToIp(RouterRestApi.URL_FOR_SAMBA_IP));
        if (!TextUtils.isEmpty(InputUtil.convertDomainToIp(RouterRestApi.URL_FOR_SAMBA_IP))) {
          LoginConfig.LAN_IP = InputUtil.convertDomainToIp(RouterRestApi.URL_FOR_SAMBA_IP);
        }
      }
    }).start();

    System.out.println("Samba登录信息  解析IP地址= " + GlobleConstant.isSambaExtranetAccess + " isFiniseh = " + GlobleConstant.isFinishedPicRestored);

    if (GlobleConstant.isSambaExtranetAccess) {
      return;
    }

    if (GlobleConstant.isFinishedPicRestored) {
      return;
    }

    LoginConfig.USER_NAME = PrefConstant.getSambaAccount();
    LoginConfig.USER_PWD = PrefConstant.getSambaPwd();

    LoginConfig.LAN_REMOTE_ROOT_IP = "smb://" + LoginConfig.LAN_IP + "/qtec/";

    tryMount();
  }

  /**
   * 连接服务器
   *
   * @param
   * @return
   */
  private void tryMount() {

    final String host = LoginConfig.LAN_IP;
    final String share = LoginConfig.LAN_REMOTE_SHARE_IP;

    final DocumentMetadata metadata = DocumentMetadata.createShare(host, share);

    mCache.put(metadata);

    final OnTaskFinishedCallback<Void> callback = new OnTaskFinishedCallback<Void>() {
      @Override
      public void onTaskFinished(int status, @Nullable Void item, Exception exception) {
        switch (status) {
          case SUCCEEDED:
            new newFolderTask("来自：" + DeviceUtils.getModel()).execute();//已存在的文件夹不会重新新建
            System.out.println("图片备份 首页开启备份 成功");
            break;
          case FAILED:
            mCache.remove(metadata.getUri());
            System.out.println("图片备份 首页开启备份 失败 auth == null");
            break;
        }
      }
    };

    final MountServerTask task = new MountServerTask(
        metadata, LoginConfig.LAN_IP, LoginConfig.USER_NAME, LoginConfig.USER_PWD, mClient, mCache, mShareManager, callback);
    mTaskManager.runTask(metadata.getUri(), task);
  }


  /**
   * 新建文件夹
   *
   * @param
   * @return
   */
  class newFolderTask extends AsyncTask<String, String, Boolean> {
    String newName = "";

    public newFolderTask(String newName) {
      this.newName = newName;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Boolean doInBackground(String... params) {
      Boolean result = SambaUtils.createSambaFolder(mClient, LoginConfig.LAN_REMOTE_ROOT_IP, newName);
      return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
      if (result) {
        System.out.println("\"新建文件夹:\" = " + "新建成功！");
      } else {
        System.out.println("\"新建文件夹:\" = " + "文件夹已存在！");
      }

      if (PrefConstant.getPictureRestoreState()) {
        if (GlobleConstant.getPicturePathList().size() > 0) {
          UploadUtils.startPicRestoreTimer(mClient, getApplicationContext());//开启定时器
        }
      }

    }
  }

  private class MainViewPagerAdapter extends FragmentPagerAdapter {

    MainViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return DeviceFragment.newInstance();
        case 1:
          return SafeFragment.newInstance();
        case 2:
          return MineFragment.newInstance();
        default:
          return MineFragment.newInstance();
      }

    }

    @Override
    public int getCount() {
      return mBinding.tablayout.getTabCount();
    }
  }

  @Override
  public void onBackPressed() {
    DialogUtil.showOkCancelAlertDialogWithMessage(
        getContext(), "退出应用", "您确定要退出应用吗？",
        v -> {
          ((AndroidApplication) getApplication()).unbindAndStopBleService();
          MQTTManager.instance().stop();
          super.onBackPressed();
          AndroidApplication.exit();
        }, null);
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    System.out.println("MainActivity.onDestroy");
    mMainPresenter.destroy();
    mVersionInfoPresenter.destroy();
    mUploadMsgIdPresenter.destroy();
//    mLogcatPresenter.destroy();
    unregisterReceiver(mKeyInvalidReceiver);
    unregisterReceiver(mBleReceiver);
    unregisterReceiver(mCanApplyKeyReceiver);
    unregisterReceiver(mNetChangeReceiver);
    unregisterReceiver(mAppBroadcastReceiver);
//    closeBle();
  }

  /**
   * 处理密钥失效的BroadcastReceiver
   */
  public class KeyInvalidReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (CloudRestApiImpl.ACTION_ROUTER_KEY_INVALID.equals(action)) {
        if (GlobleConstant.NO_ID.equals(GlobleConstant.getgDeviceId())) return;
        String keyInvalidRouterId = intent.getStringExtra(RouterRestApiImpl.KEY_INVALID_ROUTER_ID);
        Intent routerIntent = new Intent();
        routerIntent.putExtra(RouterRestApiImpl.KEY_INVALID_ROUTER_ID, keyInvalidRouterId);
        routerIntent.putExtra(Navigator.EXTRA_KEY_INVALID_TYPE, AppConstant.DEVICE_TYPE_ROUTER);
        mNavigator.navigateTo(context, KeyInvalidActivity.class, routerIntent);
      } else if (BleMapper.ACTION_BLE_LOCK_KEY_INVALID.equals(action)) {
        String lockMacAddress = intent.getStringExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS);
        Intent lockIntent = new Intent();
        lockIntent.putExtra(Navigator.EXTRA_KEY_INVALID_TYPE, AppConstant.DEVICE_TYPE_LOCK);
        lockIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress);
        mNavigator.navigateTo(context, KeyInvalidActivity.class, lockIntent);
      }
    }

  }

  /**
   * 处理其它的app广播的BroadcastReceiver
   */
  public class AppBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (LockManageTipActivity.ACTION_LOCK_MANAGE_TIPS.equals(action)) {
        int tipType = intent.getIntExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_TYPE, -1);
        String lockId = intent.getStringExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_DEVICE_ID);
        Intent lockTipsIntent = new Intent();
        lockTipsIntent.putExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_TYPE, tipType);
        lockTipsIntent.putExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_DEVICE_ID, lockId);
        mNavigator.navigateTo(context, LockManageTipActivity.class, lockTipsIntent);
      }
    }

  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
    private BleMapper bleMapper = new BleMapper();

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle extras = intent.getExtras();
      if (TextUtils.isEmpty(mDeviceAddress) || !mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR))) {
        return;
      }
      String action = intent.getAction();
      if (BleService.BLE_GATT_CONNECTED.equals(action)) {
        System.out.println(TAG + " connected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {

        BleRequest.CmdType cmdType = getBleCmdType(intent);
        if (cmdType != APPLY_KEYS) return;
        setUserUsingBle(false, mDeviceAddress);
        System.out.println(TAG + " dis_dis_dis connected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED_WHEN_APPLY_KEYS.equals(action)) {
        System.out.println(TAG + " service_discovered +++++++++++++++++++++++++++++++");

        IBle ble = getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          mBle.requestConnect(mDeviceAddress, mCurrentCmdType, 32);
          return;
        }

        mCharacteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, mCharacteristic);
        BleMapper bleMapper = new BleMapper();
        bleMapper.setReuseKey(mLZKey);
        BleLock bleLock = LockManager.getLock(getContext(), mDeviceAddress);
        String data = bleMapper.pkgToReqString(mCurrentPkg, bleLock == null ? "" : bleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(mCurrentCmdType, mDeviceAddress, mCharacteristic, data, true);


      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        if (cmdType != APPLY_KEYS) return;
        setUserUsingBle(false, mDeviceAddress);
      }

      ////////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_APPLY_KEYS.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        System.out.println("values = " + values);
        if (values == null || values.size() < 60) return;
        BleLock bleLock = LockManager.getLock(getContext(), mDeviceAddress);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, bleLock == null ? "" : bleLock.getKeyRepoId());
        if (blePkg == null || blePkg.getBody() == null) return;

        String payload = blePkg.getBody().getPayload();
        if (payload.length() < 2) {
          return;
        }
        String code = payload.substring(0, 2);
        String keysPayload = payload.substring(2);

        if (!bleRspFail(code, false, mDeviceAddress)) {
          blePkg.getBody().setPayload(keysPayload);
          List<LZKey> keys = bleMapper.getKeys(blePkg);
          mMainPresenter.injectLockKey(keys, mDeviceAddress);
          startBleTimer();
        }
      }
    }
  };

  public final BroadcastReceiver mCanApplyKeyReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {

      String action = intent.getAction();
      if (ACTION_CAN_APPLY_KEY.equals(action) && mIsKeyLow) {
        System.out.println("MainActivity.mCanApplyKeyReceiver+++++++++++++++++++++++++++++++++++++++++");
        mIsKeyLow = false;
        mDeviceAddress = intent.getStringExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS);
        getLockKeys();
      }
    }
  };

  // TODO: 2017/11/23  static是否可去掉，待优化
  public static class NetChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      System.out.println("网络状态发生变化");
      if (context != null) {
        UploadUtils.getLocalPhoneList(context);//筛选出照片
      }

      //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
      if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取ConnectivityManager对象对应的NetworkInfo对象
        //获取WIFI连接的信息
        NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        //获取移动数据连接的信息
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
          Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
        } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
          Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
        } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
          Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
        } else {
          Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
        }
        //API大于23时使用下面的方式进行网络监听
      } else {

        System.out.println("API level 大于23");
        //获得ConnectivityManager对象
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //获取所有网络连接的信息
        Network[] networks = connMgr.getAllNetworks();
        //用于存放网络连接信息
        StringBuilder sb = new StringBuilder();
        //通过循环将网络信息逐个取出来
        for (int i = 0; i < networks.length; i++) {
          //获取ConnectivityManager对象对应的NetworkInfo对象
          NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);

          if (networkInfo != null) {
            if ("WIFI".equals(networkInfo.getTypeName())) {
              sb.append("WIFI已连接");
            } else if ("MOBILE".equals(networkInfo.getTypeName())) {
              sb.append("WIFI已断开,移动数据已连接");
            } else {
              sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
            }
          }
        }

        if (!TextUtils.isEmpty(sb.toString())) {
          //Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();
          //获取routerID
          if (mAddRouterPresenter != null) {
            mAddRouterPresenter.searchRouter();
          }

        }

      }
    }
  }

//  public class BootCompleteReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//      if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//        HooliganActivity.startHooligan();
//      } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//        HooliganActivity.killHooligan();
//      }
//    }
//  }


}