package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchListener;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.task.__IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.EspAES;
import com.espressif.iot.esptouch.util.EspNetUtil;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityConnectWifiBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.IntentUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.utils.EspUtils;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class PairWifiActivity extends BaseActivity implements TextToSpeech.OnInitListener, AddGwView {
  private static final String TAG = AddLockActivity.class.getSimpleName();

  private static final boolean AES_ENABLE = false;
  private static final String AES_SECRET_KEY = "1234567890123456"; // TODO modify your own key
  private static final String DEVICE_COUNT = "1";
  public static final String PAIR_WIFI_FAILD = "组网失败，请重试";
  public static final String PAIR_WIFI_SUCCESS = "组网成功";

  private ActivityConnectWifiBinding mBinding;
  private TextToSpeech mTextToSpeech; // TTS对象

  private EsptouchAsyncTask4 mTask;
  private String mBSsid = "";

  @Inject
  AddLiteGwPresenter mAddLiteGwPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_connect_wifi);

    initializeInjector();

    initPresenter();

    initData();

    initView();

    mTextToSpeech = new TextToSpeech(this, this);//语音播报
  }

  private void initData() {
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
          mTextToSpeech.setPitch(0.7f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
          mTextToSpeech.speak("请输入无线密码然后点击配置按钮",
              TextToSpeech.QUEUE_FLUSH, null);
        }
      }
    }, 1000);

    IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    registerReceiver(mReceiver, filter);
  }

  private void initView() {
    initTitleBar("配对网络");

    String wifiName = getWifiSsid().replace("\"", "");
    mBinding.etWifiName.setText(wifiName);
    moveEditCursorToEnd(mBinding.etWifiName);

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition nameCondition = new InputWatcher.WatchCondition();
    nameCondition.setByteRange(new InputWatcher.InputByteRange(1, 32));
    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputRange(6, 20));
    watcher.addEt(mBinding.etWifiName, nameCondition);
    watcher.addEt(mBinding.etPwd, pwdCondition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnConfig.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnConfig.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnConfig.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });
  }

  private void initPresenter() {
    mAddLiteGwPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  public void changeInputType(View view) {
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  private String getWifiSsid() {
    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    return wifiInfo.getSSID();
  }

  public void configWifi(View view) {
    LiteGateway liteGateway = (LiteGateway) IntentUtil.getSerialExtra(this, Navigator.EXTRA_BIND_LITE_GATEWAY);
    if (liteGateway == null) return;
    System.out.println("liteGateway = " + liteGateway);

    if ((Boolean) mBinding.btnConfig.getTag()) {
      ToastUtils.showShort(R.string.wifi_5g_message);
      return;
    }

    byte[] ssid = mBinding.etWifiName.getTag() == null ? ByteUtil.getBytesByString(getText(mBinding.etWifiName))
        : (byte[]) mBinding.etWifiName.getTag();
    byte[] password = ByteUtil.getBytesByString(getText(mBinding.etPwd));
    byte[] bssid = EspNetUtil.parseBssid2bytes(mBSsid);
    byte[] deviceCount = DEVICE_COUNT.getBytes();

    System.out.println("ssid = " + getText(mBinding.etWifiName));
    System.out.println("password = " + getText(mBinding.etPwd));
    System.out.println("mBSsid = " + mBSsid);
    System.out.println("DEVICE_COUNT = " + DEVICE_COUNT);


    if (mTask != null) {
      mTask.cancelEsptouch();
    }
    mTask = new EsptouchAsyncTask4(this);
    mTask.execute(ssid, bssid, password, deviceCount);
  }

  @Override
  public void onInit(int status) {
    if (status == TextToSpeech.SUCCESS) {
      int result = mTextToSpeech.setLanguage(Locale.CHINESE);
      if (result == TextToSpeech.LANG_MISSING_DATA
          || result == TextToSpeech.LANG_NOT_SUPPORTED) {
        System.out.println("语音播报 onInit() 数据丢失或不支持");
      }
    }
  }

  private void onWifiChanged(WifiInfo info) {
    if (info == null) {
      mBinding.etWifiName.setText("");
      mBinding.etWifiName.setTag(null);
      mBSsid = "";
      mBinding.btnConfig.setTag(null);

      if (mTask != null) {
        mTask.cancelEsptouch();
        mTask = null;
      }
    } else {
      String ssid = info.getSSID();
      System.out.println("ssid = " + ssid);
      if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
        ssid = ssid.substring(1, ssid.length() - 1);
      }
      mBinding.etWifiName.setText(ssid);
      mBinding.etWifiName.setTag(ByteUtil.getBytesByString(ssid));
      byte[] ssidOriginalData = EspUtils.getOriginalSsidBytes(info);
      mBinding.etWifiName.setTag(ssidOriginalData);

      mBSsid = info.getBSSID();

      mBinding.btnConfig.setTag(Boolean.FALSE);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        int frequence = info.getFrequency();
        if (frequence > 4900 && frequence < 5900) {
          // Connected 5G wifi. Device does not support 5G
          mBinding.btnConfig.setTag(Boolean.TRUE);
        }
      }
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    mTextToSpeech.stop(); // 不管是否正在朗读TTS都被打断
    mTextToSpeech.shutdown(); // 关闭，释放资源
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(mReceiver);
  }

  @Override
  public void onAddGwSuccess(CommitAddRouterInfoResponse response) {
    DialogUtil.showSuccessDialog(getContext(), "绑定成功", new DialogUtil.DialogCallback() {
      @Override
      public void onDismiss() {
        finish();
        mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
      }
    });
  }

  private class EsptouchAsyncTask4 extends AsyncTask<byte[], Void, List<IEsptouchResult>> {
    private WeakReference<PairWifiActivity> mActivity;

    // without the lock, if the user tap confirm and cancel quickly enough,
    // the bug will arise. the reason is follows:
    // 0. task is starting created, but not finished
    // 1. the task is cancel for the task hasn't been created, it do nothing
    // 2. task is created
    // 3. Oops, the task should be cancelled, but it is running
    private final Object mLock = new Object();
    private Dialog mProgressDialog;
    private IEsptouchTask mEsptouchTask;

    EsptouchAsyncTask4(PairWifiActivity activity) {
      mActivity = new WeakReference<>(activity);
    }

    void cancelEsptouch() {
      cancel(true);
      if (mProgressDialog != null) {
        mProgressDialog.dismiss();
      }
      synchronized (mLock) {
        if (__IEsptouchTask.DEBUG) {
          Log.i(TAG, "task canceled");
        }
        if (mEsptouchTask != null) {
          mEsptouchTask.interrupt();
        }
      }
    }

    @Override
    protected void onPreExecute() {
      Activity activity = mActivity.get();
      mProgressDialog = new Dialog(activity, R.style.CustomProgressDialog);
      mProgressDialog.setContentView(R.layout.app_progress);
      mProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
//      mProgressDialog.setMessage("组网中，请稍候...");
      mProgressDialog.setCanceledOnTouchOutside(false);
      mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
          synchronized (mLock) {
            if (__IEsptouchTask.DEBUG) {
              Log.i(TAG, "progress dialog back pressed canceled");
            }
            if (mEsptouchTask != null) {
              mEsptouchTask.interrupt();
            }
          }
        }
      });
      mProgressDialog.show();
    }

    @Override
    protected List<IEsptouchResult> doInBackground(byte[]... params) {
      PairWifiActivity activity = mActivity.get();
      int taskResultCount;
      synchronized (mLock) {
        // !!!NOTICE
        byte[] apSsid = params[0];
        byte[] apBssid = params[1];
        byte[] apPassword = params[2];
        byte[] deviceCountData = params[3];
        taskResultCount = deviceCountData.length == 0 ? -1 : Integer.parseInt(new String(deviceCountData));
        Context context = activity.getApplicationContext();
        if (AES_ENABLE) {
          byte[] secretKey = AES_SECRET_KEY.getBytes();
          EspAES aes = new EspAES(secretKey);
          mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, aes, context);
        } else {
          mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, null, context);
        }
        mEsptouchTask.setEsptouchListener(activity.myListener);
      }
      return mEsptouchTask.executeForResults(taskResultCount);
    }

    @Override
    protected void onPostExecute(List<IEsptouchResult> results) {
      PairWifiActivity activity = mActivity.get();
      mProgressDialog.dismiss();
      if (results == null) {
        ToastUtils.showShort(PAIR_WIFI_FAILD);
        return;
      }

      IEsptouchResult firstResult = results.get(0);
      // check whether the task is cancelled and no results received
      if (!firstResult.isCancelled()) {
//        int count = 0;
        // max results to be displayed, if it is more than maxDisplayCount,
        // just show the count of redundant ones
//        final int maxDisplayCount = 5;
        // the task received some results including cancelled while
        // executing before receiving enough results
        if (firstResult.isSuc()) {
//          2c3ae818d65e
          LiteGateway gw = (LiteGateway) IntentUtil.getSerialExtra(PairWifiActivity.this, Navigator.EXTRA_BIND_LITE_GATEWAY);
          String bssid = null;
          for (IEsptouchResult result : results) {
            bssid = result.getBssid();
            String id = gw.getId();
            if (bssid.equals(id)) break;
          }
          if (bssid.equals(gw.getId())) {
            if (gw.isBind()) {
              ToastUtils.showShort(PAIR_WIFI_SUCCESS);
              activity.mTask = null;
              finish();
            } else {
              mAddLiteGwPresenter.addGw(gw);
            }
          } else {
            ToastUtils.showShort("设备序列号输入错误");
          }

        } else {
          ToastUtils.showShort(PAIR_WIFI_FAILD);
        }
      }

      activity.mTask = null;
    }
  }

  private IEsptouchListener myListener = new IEsptouchListener() {

    @Override
    public void onEsptouchResultAdded(final IEsptouchResult result) {
      onEsptoucResultAddedPerform(result);
    }
  };

  private void onEsptoucResultAddedPerform(final IEsptouchResult result) {
    runOnUiThread(new Runnable() {

      @Override
      public void run() {
      }
    });
  }

  private BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (action == null) return;
      switch (action) {
        case WifiManager.NETWORK_STATE_CHANGED_ACTION:
          onWifiChanged(intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO));
          break;
      }
    }
  };

}
