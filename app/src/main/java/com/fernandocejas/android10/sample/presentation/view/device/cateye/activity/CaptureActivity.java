package com.fernandocejas.android10.sample.presentation.view.device.cateye.activity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.net.CloudRestApi;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerDeviceComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.IntentUtil;
import com.fernandocejas.android10.sample.presentation.utils.HttpConnectionUtils;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.AboutCatEyePopupWindow;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.AddCatPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.AddCatView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.InputCatEyeNumActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.QueryLockedCatPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.QueryLockedCatView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.WaitConfigActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.camera.CameraManager;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.component.ViewfinderView;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.decoding.CaptureActivityHandler;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.decoding.InactivityTimer;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.PairWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.QueryIsOnlineResponse;
import com.qtec.mapp.model.rsp.QueryLockedCatResponse;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback, QueryLockedCatView,AddCatView {

  private CaptureActivityHandler handler;
  private ViewfinderView viewfinderView;
  private boolean hasSurface;
  private Vector<BarcodeFormat> decodeFormats;
  private String characterSet;
  private InactivityTimer inactivityTimer;
  private MediaPlayer mediaPlayer;
  private boolean playBeep;
  private static final float BEEP_VOLUME = 0.10f;
  private boolean vibrate;
  private AboutCatEyePopupWindow mPopWindow;
  @Inject
  QueryLockedCatPresenter mQueryLockedCatPresenter;
  @Inject
  AddCatPresenter mAddCatPresenter;

  /**
   * Called when the activity is first created.
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_capture);
    //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
    CameraManager.init(getApplication());
    viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);

    /*initTitleBar("扫描二维码");*/

    hasSurface = false;
    inactivityTimer = new InactivityTimer(this);

    findViewById(R.id.rl_close).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    initializeInjector();

    initPresenter();
  }

  /**
   * 判断设备是否在线
   *
   * @param
   * @return
   */
  class isOnlineAsyncTask extends AsyncTask<String, Void, String> {

    private String companyName, deviceGuidArr, url, sig;
    private int type;

    public isOnlineAsyncTask(String companyName, String deviceGuidArr) {
      this.companyName = companyName;
      this.deviceGuidArr = deviceGuidArr;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();

      type = (int) ((Math.random() * 9 + 1) * 1000000); // 七位随机整数
      System.out.println("随机数 七位 = --------------" + type);
      int random = (type + type * 21 + type % 18) ^ 16755421;
      String qtecKey = "@L#@WLXhlw4hR60*";
      System.out.println("随机数 七位 random = --------------" + random);
      sig = EncryptUtils.encryptMD5ToString(companyName + deviceGuidArr + String.valueOf(random) + qtecKey);

      //拼接请求体  注意：多设备时用,隔开 deviceGuidArr=C1035023,C200558849
      url = CloudRestApi.URL_ZHONG_WEI + "ThirdService/device/deviceOnlineN?" + "companyName=" + companyName + "&" + "deviceGuidArr=" + deviceGuidArr + "&" + "sig=" + sig.toLowerCase() + "&" + "type=" + type;
      System.out.println("查询猫眼摄像头在线状态 request url = --------------" + url);
    }

    @Override
    protected String doInBackground(String... params) {
      // "http://xwthird.cloudsee.net/ThirdService/device/deviceOnlineN?companyName=jzlz&deviceGuidArr=C1035023,C200558849&sig=fab0fd5b2a5f4cbbc481733851bbfbde&type=6543210"
      // String url=params[0];
      String json = HttpConnectionUtils.check_j(url);
      return json;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);

      try {
        //在线状态 1-在线，0-离线
        // String newww = "{\"msg\":\"fail！\",\"result\":\"false\",\"data\":[],\"errorCode\":\"102\"}";
        System.out.println("查询猫眼摄像头在线状态 response json = --------------" + result);

        Gson g = new Gson();
        QueryIsOnlineResponse myNews = g.fromJson(result, QueryIsOnlineResponse.class);

        switch (myNews.getErrorCode()) {
          case "000":
            List<QueryIsOnlineResponse.Data> deviceStatusList = myNews.getData();
            for (int i = 0; i < deviceStatusList.size(); i++) {
              System.out.println("deviceStatusList name = " + deviceStatusList.get(i).getDeviceGuid() + " status = " + deviceStatusList.get(i).getOnline());
              if (deviceGuidArr.equals(deviceStatusList.get(i).getDeviceGuid())) {
                // 扫码后，通过api获取设备在线状态，如果在线直接添加到服务器，不在线 走配网流程
                if (deviceStatusList.get(i).getOnline() == 1) {
                  //在线 直接添加到服务器
                  if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
                    System.out.println("猫眼 上传服务器 GlobleConstant.getgCatEyeId() = " + GlobleConstant.getgCatEyeId());
                    mAddCatPresenter.addCatCloud(GlobleConstant.getgCatEyeId());
                  } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
                    System.out.println("摄像机 上传服务器 GlobleConstant.getgCameraId() = " + GlobleConstant.getgCameraId());
                    mAddCatPresenter.addCatCloud(GlobleConstant.getgCameraId());
                  }
                } else {
                  //离线
                  if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
                    mQueryLockedCatPresenter.queryLockedCat(GlobleConstant.getgCatEyeId());
                  } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
                    mQueryLockedCatPresenter.queryLockedCat(GlobleConstant.getgCameraId());
                  }
                }
                break;
              }
            }
            break;

          case "102":
            Toast.makeText(getContext(), "参数错误，请重试", Toast.LENGTH_SHORT).show();
            break;

          case "104":
            Toast.makeText(getContext(), "服务异常，请重试", Toast.LENGTH_SHORT).show();
            break;

          case "212":
            Toast.makeText(getContext(), "该设备不存在，请重试", Toast.LENGTH_SHORT).show();
            break;

          default:
            break;
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void initializeInjector() {
    DaggerDeviceComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .deviceModule(new DeviceModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
    mQueryLockedCatPresenter.setView(this);
    mAddCatPresenter.setView(this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    SurfaceHolder surfaceHolder = surfaceView.getHolder();
    if (hasSurface) {
      initCamera(surfaceHolder);
    } else {
      surfaceHolder.addCallback(this);
      surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    decodeFormats = null;
    characterSet = null;

    playBeep = true;
    AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
    if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
      playBeep = false;
    }
    initBeepSound();
    vibrate = true;

  }

  @Override
  protected void onPause() {
    super.onPause();
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    CameraManager.get().closeDriver();
  }

  @Override
  protected void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }

  /**
   * 处理扫描结果
   *
   * @param result
   * @param barcode
   */
  public void handleDecode(Result result, Bitmap barcode) {
    inactivityTimer.onActivity();
    playBeepSoundAndVibrate();
    String resultString = result.getText();

    if (resultString.equals("")) {
      Toast.makeText(getContext(), "扫描失败，请重试", Toast.LENGTH_SHORT).show();
      return;
    }

    //规则校验：猫眼摄像头都要截取12位，然后去掉前面的0
    if (resultString.length() < 12) {
      Toast.makeText(getContext(), "二维码不符合规则，请检查", Toast.LENGTH_SHORT).show();
      return;
    }
    System.out.println("扫描 扫描结果 = " + resultString);
    String codeText = resultString.substring(0, 12);
    System.out.println("扫描 截取前12位 = " + codeText);


    if (IntentUtil.getSerialExtra(this, Navigator.EXTRA_BIND_LITE_GATEWAY) != null) {
      if (TextUtils.isEmpty(codeText) || codeText.length() != 12) {
        ToastUtils.showShort("识别失败");
        return;
      }
      LiteGateway liteGateway = (LiteGateway) IntentUtil.getSerialExtra(this, Navigator.EXTRA_BIND_LITE_GATEWAY);
      liteGateway.setId(codeText);
      mNavigator.navigateTo(getContext(), PairWifiActivity.class, getIntent());
      return;
    }

    //mD5校验
    String before_ = resultString.substring(0, resultString.indexOf("-"));
    String apendStr = before_ + "+xwjzlz";
    String md5Str = EncryptUtils.encryptMD5ToString(apendStr);
    String md50_6 = md5Str.substring(0, 6);
    String after_ = resultString.substring(resultString.indexOf("-") + 1);

    System.out.println("扫描 扫描结果 resultString = " + resultString);
    System.out.println("扫描 扫描结果 before_ = " + before_);
    System.out.println("扫描 扫描结果 apendStr = " + apendStr);
    System.out.println("扫描 扫描结果 md5Str = " + md5Str);
    System.out.println("扫描 扫描结果 md50_6 = " + md50_6);
    System.out.println("扫描 扫描结果 after_ = " + after_);


    if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
      //
      if (!md50_6.equalsIgnoreCase(after_)) {
        System.out.println("扫描 扫描结果 md5校验不通过");

        mPopWindow = new AboutCatEyePopupWindow(this, 4, false);
        mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
        mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
          @Override
          public void onPositiveClick(int index) {
            finish();
          }
        });

        return;
      }
    }

    String qrCode = codeText.replaceFirst("^0*", "");
    System.out.println("扫描 去掉前面的0 = " + qrCode);

    if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
      GlobleConstant.setgCatEyeId(qrCode);
      mQueryLockedCatPresenter.queryLockedCat(GlobleConstant.getgCatEyeId());

//      new isOnlineAsyncTask("jzlz", qrCode).execute();

    } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
      GlobleConstant.setgCameraId(qrCode);

      mQueryLockedCatPresenter.queryLockedCat(GlobleConstant.getgCameraId());

//      new isOnlineAsyncTask("jzlz", qrCode).execute();

    }

  }

  private void initCamera(SurfaceHolder surfaceHolder) {
    try {
      CameraManager.get().openDriver(surfaceHolder);
    } catch (IOException ioe) {
      return;
    } catch (RuntimeException e) {
      return;
    }
    if (handler == null) {
      handler = new CaptureActivityHandler(this, decodeFormats,
          characterSet);
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
                             int height) {

  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (!hasSurface) {
      hasSurface = true;
      initCamera(holder);
    }

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;

  }

  public ViewfinderView getViewfinderView() {
    return viewfinderView;
  }

  public Handler getHandler() {
    return handler;
  }

  public void drawViewfinder() {
    viewfinderView.drawViewfinder();

  }

  private void initBeepSound() {
    if (playBeep && mediaPlayer == null) {
      // The volume on STREAM_SYSTEM is not adjustable, and users found it
      // too loud,
      // so we now play on the music stream.
      setVolumeControlStream(AudioManager.STREAM_MUSIC);
      mediaPlayer = new MediaPlayer();
      mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mediaPlayer.setOnCompletionListener(beepListener);

      AssetFileDescriptor file = getResources().openRawResourceFd(
          R.raw.beep);
      try {
        mediaPlayer.setDataSource(file.getFileDescriptor(),
            file.getStartOffset(), file.getLength());
        file.close();
        mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
        mediaPlayer.prepare();
      } catch (IOException e) {
        mediaPlayer = null;
      }
    }
  }

  private static final long VIBRATE_DURATION = 200L;

  private void playBeepSoundAndVibrate() {
    if (playBeep && mediaPlayer != null) {
      mediaPlayer.start();
    }
    if (vibrate) {
      Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
      vibrator.vibrate(VIBRATE_DURATION);
    }
  }

  /**
   * When the beep has finished playing, rewind to queue up another one.
   */
  private final OnCompletionListener beepListener = new OnCompletionListener() {
    public void onCompletion(MediaPlayer mediaPlayer) {
      mediaPlayer.seekTo(0);
    }
  };

  @Override
  public void queryLockedCat(QueryLockedCatResponse response) {
    //:{"bindStatus":"0,未绑定；1,已绑定"}
    if ("0".equals(response.getBindStatus())) {
      GlobleConstant.isCatBinded = false;
      startActivity(new Intent(this, WaitConfigActivity.class));
    } else {
      GlobleConstant.isCatBinded = true;
      mPopWindow = new AboutCatEyePopupWindow(this, 2, false);
      mPopWindow.showAtLocation(mPopWindow.getOuterLayout(), Gravity.BOTTOM, 0, 0);
      mPopWindow.setOnPositiveClickListener(new AboutCatEyePopupWindow.OnPositiveClickListener() {
        @Override
        public void onPositiveClick(int index) {
          startActivity(new Intent(getContext(), WaitConfigActivity.class));
        }
      });
    }
  }

  public void inputSerialNumber(View view) {
    mNavigator.navigateTo(getContext(), InputCatEyeNumActivity.class, getIntent());
  }

  @Override
  public void onAddCatSuccess(CommitAddRouterInfoResponse response) {
    DialogUtil.showConectLockDialog(getContext(), "设备已在线，绑定成功", () -> {
      if (AppConstant.DEVICE_TYPE_CAT.equals(GlobleConstant.getgDeviceType())) {
        if ("0".equals(response.getCatBindStatus())) {
          if ("0".equals(response.getAvailableLockStatus())) {
            //回首页
            new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
          } else if ("1".equals(response.getAvailableLockStatus())) {
            new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
          }
        } else if ("1".equals(response.getCatBindStatus())) {
          //回首页
          new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
        }
      } else if (AppConstant.DEVICE_TYPE_CAMERA.equals(GlobleConstant.getgDeviceType())) {
        //回首页
        new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
      }

    });
  }

  @Override
  public void onAddCatFailed() {
    Toast.makeText(this, "设备已在线且已绑定，请勿重新绑定", Toast.LENGTH_SHORT).show();
    new Navigator().navigateExistAndClearTop(getContext(), MainActivity.class);
  }

}