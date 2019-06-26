package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.BleGattCharacteristic;
import com.blueflybee.blelibrary.BleGattService;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.bumptech.glide.util.Util;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityTempPwdBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.utils.BleLockUtils;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.lock.model.core.BlePkg;
import com.qtec.mapp.model.rsp.AddTempPwdResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.QueryTempPwdResponse;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.blueflybee.blelibrary.BleRequest.CmdType.GET_TEMP_PWD;
import static com.blueflybee.blelibrary.BleRequest.FailReason.TIMEOUT;
import static com.blueflybee.blelibrary.BleRequest.RequestType.DISCOVER_SERVICE;


public class TempPwdActivity extends BaseActivity implements TempPwdView {

  private static final String TAG = TempPwdActivity.class.getSimpleName();
  public static final String UTF_8 = "UTF-8";

  private ActivityTempPwdBinding mBinding;

  //  AppID: wxdf42c23e01fbccd7
//  AppSecret: cf23e7ff72350ea51ce56f187ba1e94b
  private static final String APP_ID = "wxdf42c23e01fbccd7";    //这个APP_ID就是注册APP的时候生成的
  private static final String APP_SECRET = "cf23e7ff72350ea51ce56f187ba1e94b";
  private static final int IMAGE_LENGTH_LIMIT = 6291456;

  @Inject
  TempPwdPresenter mTempPwdPresenter;

  private String mDeviceAddress;
  private BlePkg mCurrentPkg;
  private BleLock mBleLock;

  private IWXAPI mMWxApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_temp_pwd);

    registerWx();
    initData();
    initializeInjector();
    initPresenter();
    initView();
  }

  private void registerWx() {
    mMWxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
    mMWxApi.registerApp(APP_ID);
  }

  private void initView() {
    initTitleBar("授权开门");
  }

  private void initData() {
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> lock = getLock();
    mDeviceAddress = lock == null ? "" : lock.getMac();
    mBleLock = LockManager.getLock(getContext(), lock.getMac());
    mBleLock.setBleName(lock.parseDeviceDetail("bluetoothName"));
  }

  private void connect(BlePkg pkg, BleRequest.CmdType cmdType) {
    mCurrentPkg = pkg;
    IBle ble = getBle();
    if (ble == null || TextUtils.isEmpty(mDeviceAddress)) return;
    ble.requestConnect(mDeviceAddress, cmdType, 30);
  }

  private void initPresenter() {
    mTempPwdPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .lockModule(new LockModule())
        .build()
        .inject(this);
  }

  public void getTempPwd(View view) {
    mTempPwdPresenter.getTempPwd(mBleLock.getId(), mBleLock.getBleName());
  }

  private void getTempPwd() {
    if (!BleLockUtils.isBleEnable()) return;
    showLoading();
    pauseBleTimer();
    setUserUsingBle(true, mDeviceAddress);
    clearBleRequest();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        BlePkg pkg = new BlePkg();
        pkg.setHead("aa");
        pkg.setLength("0d");
        pkg.setKeyId(BleMapper.KEY_STORTE_ENCRYPTION);
        pkg.setUserId(PrefConstant.getUserIdInHexString());
        pkg.setSeq("00");
        BleBody body = new BleBody();
        body.setCmd(BleMapper.BLE_CMD_GET_TEMP_PWD);
        body.setPayload("");
        pkg.setBody(body);
        connect(pkg, GET_TEMP_PWD);
      }
    }, 2000);
  }

  @Override
  protected void onResume() {
    super.onResume();
    registerReceiver(mBleReceiver, BleService.getIntentFilter());
  }

  @Override
  protected void onStop() {
    super.onStop();
    boolean appBackground = ((AndroidApplication) getApplicationContext()).isAppBackground();
    if (!appBackground) unregisterReceiver(mBleReceiver);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setUserUsingBle(false, mDeviceAddress);
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
  }

  private void showActionSheet(String text) {
    setTheme(R.style.ActionSheetStyleiOS7);
    ActionSheet.createBuilder(this, getSupportFragmentManager())
        .setCancelButtonTitle("取消")
        .setOtherButtonTitles("通过短信进行通知", "通过微信进行通知")
        .setCancelableOnTouchOutside(true)
        .setListener(new ActionSheet.ActionSheetListener() {
          @Override
          public void onDismiss(ActionSheet actionSheet, boolean isCancel) {}

          @Override
          public void onOtherButtonClick(ActionSheet actionSheet, int index) {
            switch (index) {
              case 0:
                shareBySms(text);
                break;
              case 1:
                shareByWx(text);
                break;
              default:
                break;
            }
          }
        }).show();
  }

  private void shareByWx(String text) {
    if (!mMWxApi.isWXAppInstalled()) {
      ToastUtils.showShort("请先安装微信客户端");
      return;
    }
    WXTextObject textObject = new WXTextObject();
    textObject.text = text;
    //用WXTextObject对象初始化一个WXMediaMessage对象
    WXMediaMessage msg = new WXMediaMessage();
    msg.mediaObject = textObject;
    msg.description = text;

    SendMessageToWX.Req req = new SendMessageToWX.Req();
//WXSceneTimeline朋友圈    WXSceneSession聊天界面
//    req.scene = isTimeLineCb ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;//聊天界面
    req.scene = SendMessageToWX.Req.WXSceneSession;//聊天界面
    req.message = msg;
    req.transaction = String.valueOf(System.currentTimeMillis());
    mMWxApi.sendReq(req);
  }

  private ByteArrayOutputStream getWXThumb(int resource) {
    Bitmap thumb = ImageUtils.getBitmap(getResources(), resource, 144, 144);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    int quality = 90;
    int realLength = Util.getBitmapByteSize(thumb.getWidth(), thumb.getHeight(), Bitmap.Config.ARGB_8888);
    if (realLength > IMAGE_LENGTH_LIMIT) {
      quality = (int) (IMAGE_LENGTH_LIMIT * 1f / realLength * 100);
    }
    if (quality < 75) {
      quality = 75;
    }
    thumb.compress(Bitmap.CompressFormat.PNG, quality, output);
    output.reset();
    thumb.compress(Bitmap.CompressFormat.PNG, 85, output);
    return output;
  }

  private void shareBySms(String text) {
    startActivity(IntentUtils.getSendSmsIntent("", text));
  }

  @Override
  public void onAddTempPwdSuccess(AddTempPwdResponse response) {
    ToastUtils.showShort("门锁密码生成成功");
  }

  @Override
  public void onQueryTempPwd(QueryTempPwdResponse response) {}

  @Override
  public void onGetTempPwdFail() {
    DialogUtil.showConfirmCancelDialog(
        getContext(),
        "授权失败", "请先用蓝牙连接至锁具生成临时密码",
        "生成密码",
        "取消", v1 -> {
          getTempPwd();
        }, null);
  }

  @Override
  public void onGetTempPwdSuccess(String pwd) {
    showActionSheet(PrefConstant.getUserPhone() + "已经授权您三点安全门锁的一次性临时密码：" + pwd);
  }

  private final BroadcastReceiver mBleReceiver = new BroadcastReceiver() {
    private BleMapper bleMapper = new BleMapper();

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle extras = intent.getExtras();
      if (!mDeviceAddress.equals(extras.getString(BleService.EXTRA_ADDR))) {
        return;
      }
      String action = intent.getAction();
      if (BleService.BLE_GATT_CONNECTED.equals(action)) {
        System.out.println(TAG + " connected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_GATT_DISCONNECTED.equals(action)) {
        handleBleDisconnect(getBleCmdType(intent) != GET_TEMP_PWD, false, mDeviceAddress);
        System.out.println(TAG + " disconnected +++++++++++++++++++++++++++++++");
      } else if (BleService.BLE_SERVICE_DISCOVERED.equals(action)) {
        System.out.println(TAG + " service discovered +++++++++++++++++++++++++++++++");

        IBle ble = getBle();
        if (ble == null) return;
        BleGattService service = ble.getService(mDeviceAddress, UUID.fromString(BleMapper.SERVICE_UUID));
        if (service == null) {
          ble.requestConnect(mDeviceAddress, GET_TEMP_PWD, 30);
          return;
        }
        BleGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(BleMapper.CHARACTERISTIC_UUID));

        ble.requestCharacteristicNotification(mDeviceAddress, characteristic);
        String data = new BleMapper().pkgToReqString(mCurrentPkg, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        ble.requestWriteCharacteristicToLock(GET_TEMP_PWD, mDeviceAddress, characteristic, data, true);

      } else if (BleService.BLE_REQUEST_FAILED.equals(action)) {
        BleRequest.CmdType cmdType = getBleCmdType(intent);
        BleRequest.FailReason reason = getBleFailReason(intent);
        BleRequest.RequestType requestType = getBleRequestType(intent);
        if (cmdType == GET_TEMP_PWD && reason == TIMEOUT && requestType == DISCOVER_SERVICE) {
          connect(mCurrentPkg, GET_TEMP_PWD);
        } else {
          handleBleFail(intent, getBleCmdType(intent) != GET_TEMP_PWD, false, mDeviceAddress);
        }
      }

      //////////////////////////////////////////////////////////////////////////////////////////////
      else if (BleService.BLE_GET_TEMP_PWD.equals(action)) {
        setUserUsingBle(false, mDeviceAddress);
        hideLoading();
        ArrayList<String> values = extras.getStringArrayList(BleService.EXTRA_VALUE);
        BlePkg blePkg = bleMapper.stringValuesToPkg(values, mBleLock == null ? "" : mBleLock.getKeyRepoId());
        System.out.println("blePkg = " + blePkg);

        String payload = blePkg.getBody().getPayload();
//        String payload = "00080508010202000306020008000504050403040504020506010302040107060001010704040404030901080508010202080508010202080508010202";
//        System.out.println("payload.length() = " + payload.length());

        if (TextUtils.isEmpty(payload) || payload.length() < 2) {
          ToastUtils.showShort("获取门锁密码数据异常");
          return;
        }
        String code = payload.substring(0, 2);
        if (!bleRspFail(code, true, mDeviceAddress)) {
          if (payload.length() != 122) {
            ToastUtils.showShort("获取门锁密码数据异常");
            return;
          }
          Intent applyKeyIntent = new Intent(MainActivity.ACTION_CAN_APPLY_KEY);
          applyKeyIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, mDeviceAddress);
          sendBroadcast(applyKeyIntent);
          startBleTimer();
          String[] tempPwds = bleMapper.getTempPwds(payload);
          mTempPwdPresenter.addTempPwd(mBleLock.getId(), tempPwds, mBleLock.getBleName());
        } else if (BleBody.RSP_CONFIG_MODE_NOT_OPEN.equals(code)) {
          DialogUtil.showConfirmDialog(getContext(), "门锁未在配置状态", "请触碰门锁背后的按钮开启配置状态。", R.drawable.ic_buttonlock, null);
        } else {
          ToastUtils.showShort("门锁密码获取失败");
        }
      }

    }
  };
}