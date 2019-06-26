package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.BaseCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CloudUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.flybluebee.circleprogress.CircleProgress;
import com.hisilicon.hisilinkapi.HisiLibApi;
import com.hisilicon.hisilinkapi.MessageSend;
import com.hisilicon.hisilinkapi.WiFiAdmin;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class SearchLanCatActivity extends BaseCatActivity implements AddCatView {

  private ArrayList<Map<String, String>> broadStrList = null;
  private Boolean isConnecting = false;
  private int mRefreshCount = 0;//120s超时
  private String TAG = "SearchLanCatActivity";
  private int mProgress = 0;

  @Inject
  AddCatPresenter mAddCatPresenter;

  private Handler mHandler = new Handler();
  private Handler mHandler_Refresh = new Handler();

  private Runnable mRunnable = new Runnable() {
    @Override
    public void run() {
      sovLanBroad(); //1s刷一次,共刷60s
      onConnectCatEyeProgress(mProgress++);
      // mHandler_Refresh.postDelayed(mRunnable_Refresh,1000);
      mRefreshCount++;
      System.out.println("catEye: 搜索设备");
      mHandler.postDelayed(this, 3000);//每隔3s轮询一次
    }
  };

  private Runnable mRunnable_Refresh = new Runnable() {
    @Override
    public void run() {
      //1s刷一次UI
      onConnectCatEyeProgress(mProgress++);
    }
  };

  private WiFiAdmin mWiFiAdmin;
  private CountDownTimer mTimer;

  @Override
  protected void initSettings() {
    CloudUtil.startLanSearchServer(CommonUtil.getLocalIpAddress());
    broadStrList = new ArrayList<Map<String, String>>();
  }

  @Override
  protected void initUi() {
    setContentView(R.layout.activity_search_lan_cat_eye);

    initializeInjector();

    initPresenter();

    initTitleBar("连接设备");

    if (!isWaveConfig()) {
      initConfig();
    }

    mTimer = new CountDownTimer(2000, 10000) {
      @Override
      public void onTick(long millisUntilFinished) {}

      @Override
      public void onFinish() {
        findViewById(R.id.iv_stop_scan).setVisibility(View.VISIBLE);
      }
    }.start();

    mHandler.postDelayed(mRunnable, 500);//搜索中
  }

  private void initConfig() {
    System.out.println("getWiFiName() = " + getWiFiName());
    System.out.println("getWiFiPwd() = " + getWiFiPwd());
    if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
      initHisi();
      startHisiConfig(getWiFiName(), getWiFiPwd());
    } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
      JniUtil.initElian();
      JniUtil.sendElian(getWiFiName(), getWiFiPwd(), (byte) 0x03);
      System.out.println("开始发送智联命令");
    }
  }

  private void initHisi() {
    /**********海思WiFi配置模块-start ************/
    mWiFiAdmin = new WiFiAdmin(this);
    //启动线程发送组播消息,MessageSend里面会加载libHisiLink.so动态库
    mMessageSend = new MessageSend(this);
    /**********海思WiFi配置模块-end ************/
  }

  private void initPresenter() {
    mAddCatPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  protected void saveSettings() {

  }

  @Override
  protected void freeMe() {
    CloudUtil.stopLanSearchServer();
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    Log.e(TAG, "onNotify:what=" + what + ";arg1=" + arg1 + ";arg2=" + arg2 + ";obj=" + obj);
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }


  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    switch (what) {
      //流媒体广播回调 0xD9
      case JVNetConst.CALL_CATEYE_SEARCH_DEVICE: {
        analysisStreamData(obj.toString());
        if (broadStrList.size() > 0) {
          isConnecting = true;
          if (isConnecting) {

            Map<String, String> map = new HashMap<String, String>();

            if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
              map.put("deviceNum", GlobleConstant.getgCatEyeId());
            } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
              map.put("deviceNum", GlobleConstant.getgCameraId());
            }

            //扫描序列中是否含该设备
            if (broadStrList.contains(map)) {
              removeHandler();
              //判断是否绑定过
              if (GlobleConstant.isCatBinded) {
                ((CircleProgress) findViewById(R.id.progress_cat_eye)).setProgress(100);
                ((TextView) findViewById(R.id.tv_progress_cat_eye)).setText(100 + "");
                new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    goHome();
                  }
                }, 1000);
              } else {
                // 上传至服务器

                if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
                  System.out.println("猫眼 上传服务器 GlobleConstant.getgCatEyeId() = " + GlobleConstant.getgCatEyeId());
                  SovUtil.addYSTNOS(new String[]{GlobleConstant.getgCatEyeId()});
                  mAddCatPresenter.addCatCloud(GlobleConstant.getgCatEyeId());
                } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
                  System.out.println("摄像机 上传服务器 GlobleConstant.getgCameraId() = " + GlobleConstant.getgCameraId());
                  SovUtil.addYSTNOS(new String[]{GlobleConstant.getgCameraId()});
                  mAddCatPresenter.addCatCloud(GlobleConstant.getgCameraId());
                }
              }
            } else {
              //连接失败
              removeHandler();
              JniUtil.stopElian();
              System.out.println("stopHisiConfig onhandler 不包含 isHisiConfiging = " + isHisiConfiging);
              stopHisiConfig();
              onSearchDeviceFailed();
            }
          }
        } else {
          //连接失败
          removeHandler();
          JniUtil.stopElian();
          stopHisiConfig();
          System.out.println("stopHisiConfig onhhandler 不是这个设备 isHisiConfiging = " + isHisiConfiging);
          onSearchDeviceFailed();
        }
        break;
      }

    }
  }

  public void onConnectCatEyeProgress(int progress) {
    if (progress <= 100) {
      ((CircleProgress) findViewById(R.id.progress_cat_eye)).setProgress(progress);
      ((TextView) findViewById(R.id.tv_progress_cat_eye)).setText(progress + "");
    } else {
      ((CircleProgress) findViewById(R.id.progress_cat_eye)).setProgress(100);
      ((TextView) findViewById(R.id.tv_progress_cat_eye)).setText(100 + "");
    }
  }

  /**
   * 解析流媒体协议的回调数据
   *
   * @param jsonStr
   */
  public void analysisStreamData(String jsonStr) {
//        "privateinfo":"hipc","timeout":2,"ystno":"H30281117"}
    try {
      JSONObject broadJson = new JSONObject(jsonStr);
      String ystNum = broadJson.getString("ystno");
      Map<String, String> map = new HashMap<String, String>();
      map.put("deviceNum", ystNum);
      Log.e(TAG, "key = deviceNum" + " value =" + ystNum);
      broadStrList.clear();
      broadStrList.add(map);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  //流媒体协议局域网广播
  public void sovLanBroad() {
    if (mRefreshCount > 40) {
      mRefreshCount = 0;
      isConnecting = false;
      removeHandler();
      //超时
      onSearchDeviceFailed();
      return;
    }

    broadStrList.clear();
 /*   if (null != adapter) {
      adapter.notifyDataSetChanged();
    }*/
    //猫眼局域网广播回调 0xB7
    SovUtil.searchLanDev();
  }

  private void onSearchDeviceFailed() {
    findViewById(R.id.rl_connect_success).setVisibility(View.GONE);
    findViewById(R.id.rl_connect_failed).setVisibility(View.VISIBLE);
  }

  @Override
  protected void onDestroy() {
    removeHandler();
    JniUtil.stopElian();
    stopHisiConfig();
    System.out.println("stopHisiConfig onDestory isHisiConfiging = " + isHisiConfiging);
    super.onDestroy();

    if (mTimer != null) {
      mTimer.cancel();
    }
  }

  @Override
  protected void onPause() {
    removeHandler();
    stopHisiConfig();
    System.out.println("stopHisiConfig onpause isHisiConfiging = " + isHisiConfiging);
    super.onPause();
  }

  private void removeHandler() {
    if (mRunnable != null) {
      System.out.println("catEye: handler 定时器移除");
      if (mHandler != null) {
        mHandler.removeCallbacks(mRunnable);
      }
    }

    if (mRunnable_Refresh != null) {
      if (mHandler_Refresh != null) {
        System.out.println("catEye: handler_refresh 定时器移除");
        mHandler_Refresh.removeCallbacks(mRunnable_Refresh);
      }
    }
  }

  @Override
  public void onAddCatSuccess(CommitAddRouterInfoResponse response) {
    DialogUtil.showConectLockDialog(getContext(), "绑定成功", () -> {
      goHome();
    });
  }

  private void goHome() {
    new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
    finish();
  }

  @Override
  public void onAddCatFailed() {
    Toast.makeText(this, "设备已在线且已绑定，请勿重新绑定", Toast.LENGTH_SHORT).show();
    goHome();
  }

  /**
   * 判断是否有门锁
   */
  private List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> getLockList() {
    // 获取网关列表
    List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> mLockList = new ArrayList<>();

    // 先分出网关和门锁来
    for (int i = 0; i < GlobleConstant.getDeviceTrees().size(); i++) {
      if ((AppConstant.DEVICE_TYPE_LOCK).equals(GlobleConstant.getDeviceTrees().get(i).getDeviceType())) {
        //门锁
        mLockList.add(GlobleConstant.getDeviceTrees().get(i));
      }
    }
    return mLockList;
  }

  public void stopSearchClick(View view) {
    removeHandler();
    onSearchDeviceFailed();
  }

  public void waveConfig(View view) {
    mNavigator.navigateTo(getContext(), WaitConfigActivity.class);
  }

  private String getWiFiName() {
    return getIntent().getStringExtra("WIFINAME");
  }

  private String getWiFiPwd() {
    return getIntent().getStringExtra("WIFIPWD");
  }

  private boolean isWaveConfig() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_CAT_WAVE_CONFIG, false);
  }

  //TODO *****************************海思WiFi配置模块 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

  //上线消息接收方式
  //上线消息接收方式
  private static final int ONLINE_MSG_BY_TCP = 1;
  private static final int ONLINE_PORT_BY_TCP = 0x3516;
  private MessageSend mMessageSend = null;
  private boolean isHisiConfiging = false;

  /**
   * 开始WiFi配置
   * 2017/11/07
   */
  private void startHisiConfig(String wifiName, String wifiPwd) {
    if (isHisiConfiging) {
      return;
    }

    HisiLibApi.setNetworkInfo(mWiFiAdmin.getSecurity(),
        ONLINE_PORT_BY_TCP,
        ONLINE_MSG_BY_TCP,
        mWiFiAdmin.getWifiIPAdress(),
        wifiName,
        wifiPwd,
        "123456");
    sendMessage();
    isHisiConfiging = true;
  }

  public int sendMessage() {
    mMessageSend.multiCastThread();
    return 0;
  }

  /**
   * 停止海思WiFi配置模块
   */
  private void stopHisiConfig() {
    /*if (isHisiConfiging) {
      mMessageSend.stopMultiCast();
      isHisiConfiging = false;
    }*/
    if (mMessageSend != null) {
      mMessageSend.stopMultiCast();
    }

    isHisiConfiging = false;
  }

  /**
   * TODO *****************************海思WiFi配置模块 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
   * */
}