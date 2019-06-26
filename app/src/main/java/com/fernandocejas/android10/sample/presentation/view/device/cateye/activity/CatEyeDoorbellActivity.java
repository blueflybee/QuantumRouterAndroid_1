package com.fernandocejas.android10.sample.presentation.view.device.cateye.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.DeviceUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCatEyeDoorbellBinding;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.AppConsts;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.CommonUtil;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.test.SovUtil;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.RemoteUnlockActivity;
import com.fernandocejas.android10.sample.presentation.view.message.data.CatEyePushData;
import com.jovision.JVNetConst;
import com.jovision.JniUtil;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

import java.util.List;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class CatEyeDoorbellActivity extends CatEyeBaseActivity {

  private static final String TAG = "CatEyeDoorbellActivity";

  private ActivityCatEyeDoorbellBinding mBinding;

  private boolean isConnected = false;//是否已连接
  private int window = 0;

  private String mFileFullPath;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cat_eye_doorbell);
    setFinishOnTouchOutside(false);

    initView();
    connectToVideo();
  }

  private void initView() {
    initPushDataUI();
  }

  private void initPushDataUI() {
    CatEyePushData pushData = getPushData();
    if (pushData == null || pushData.getBody() == null || pushData.getBody().getBusinessData() == null)
      return;
    configCatEyeConnectInfo(pushData);
    String messageCode = pushData.getBody().getBusinessData().getMessageCode();
    mBinding.tvTitle.setText(pushData.getMsgByType(messageCode));
    GlideUtil.loadImage(this, mFileFullPath, R.drawable.bg_bell, mBinding.ivContent);
    if (CatEyePushData.CAT_EYE_DOORBELL.equals(messageCode)) {
      initDoorbellUI(pushData);
    } else if (CatEyePushData.CAT_EYE_INFRARED.equals(messageCode)
        || CatEyePushData.CAT_EYE_MOTION_DETECTION.equals(messageCode)) {
      mBinding.btnUnlock.setVisibility(View.GONE);
    } else {
      mBinding.llBottomControl.setVisibility(View.GONE);
    }
  }

  private void configCatEyeConnectInfo(CatEyePushData pushData) {
    System.out.println(TAG + ".pushData = " + pushData);
    CatEyePushData.DataBody.BusinessData businessData = pushData.getBody().getBusinessData();
    String catSerialNo = businessData.getCatSerialNo();
    GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> catEye = DeviceCacheManager.getDeviceById(this, catSerialNo);
    System.out.println(TAG + ".catEye = " + catEye);
    if (catEye ==  null) return;
    String pwd = catEye.parseDeviceDetail("devicePass");
    System.out.println(TAG + ".catSerialNo = " + catSerialNo);
    System.out.println(TAG + ".pwd = " + pwd);
    GlobleConstant.setgCatEyeId(catSerialNo);
    GlobleConstant.setgCatPwd(pwd);
  }

  private void initDoorbellUI(CatEyePushData pushData) {
    CatEyePushData.DataBody.BusinessData businessData = pushData.getBody().getBusinessData();
    String catSerialNo = businessData.getCatSerialNo();
    String lockSerialNo = businessData.getLockSerialNo();
    String routerSerialNo = businessData.getRouterSerialNo();
    if (TextUtils.isEmpty(catSerialNo)) {
      mBinding.llBottomControl.setVisibility(View.GONE);
      return;
    }

    String lockKeyRepoId = lockSerialNo + "_" + PrefConstant.getUserUniqueKey(AppConstant.DEVICE_TYPE_LOCK);
    if (TextUtils.isEmpty(lockSerialNo)
        || TextUtils.isEmpty(routerSerialNo)
        || !KeystoreRepertory.getInstance().has(lockKeyRepoId)) {
      mBinding.btnUnlock.setVisibility(View.GONE);
    }
  }

  @Override
  public void onBackPressed() {
  }

  public void ignore(View view) {
    finish();
  }

  public void goDetail(View view) {
    GlobleConstant.setgCatEyeId(getCatSerialNo());
    new Navigator().navigateTo(this, CatEyeMainActivity.class);
    finish();
  }

  public void unlock(View view) {
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_CAT_EYE_DOORBELL_PUSH_DATA, getPushData());
    new Navigator().navigateTo(this, RemoteUnlockActivity.class, intent);
    finish();
  }

  private CatEyePushData getPushData() {
    CatEyePushData data = (CatEyePushData) getIntent().getSerializableExtra(Navigator.EXTRA_CAT_EYE_DOORBELL_PUSH_DATA);
    return data;
  }

  private void connectToVideo() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        Looper.prepare();
        String group = CommonUtil.getGroup(getCatSerialNo());
        int ystNo = CommonUtil.getYST(getCatSerialNo());
        Surface surface = new SurfaceView(CatEyeDoorbellActivity.this).getHolder().getSurface();
        String devPwd = GlobleConstant.getgCatPwd();
        System.out.println(TAG + ".devPwd = " + devPwd);
        SovUtil.connect(group, ystNo, "admin", devPwd, false, surface);
      }
    }).start();
  }

  @Override
  public void onNotify(int what, int arg1, int arg2, Object obj) {
    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
  }

  @Override
  protected void initSettings() {
    SovUtil.disconnect(window);
    SovUtil.addYSTNOS(new String[]{getCatSerialNo()});
  }

  @Override
  protected void initUi() {
  }

  @Override
  protected void saveSettings() {
  }

  @Override
  protected void freeMe() {
  }

  @Override
  protected void onResume() {
    SovUtil.enableStreamCatVideoData(window, true);
    super.onResume();
  }

  @Override
  protected void onPause() {
    if (isConnected) {
      SovUtil.enableStreamCatVideoData(window, false);
      JniUtil.pauseSurface(window);
    }
    SovUtil.disconnect(window);
    super.onPause();
  }

  private String getCatSerialNo() {
    return getPushData().getBody().getBusinessData().getCatSerialNo();
  }

  @Override
  public void onHandler(int what, int arg1, int arg2, Object obj) {
    switch (what) {
      case JVNetConst.CALL_CATEYE_CONNECTED:
        handleCatEyeConnect(arg2);
        break;
      case JVNetConst.CALL_NORMAL_DATA:
        isConnected = true;
        break;
      case JVNetConst.CALL_DOWNLOAD:
        loadImage(arg2, obj);
        break;
      default:
        System.out.println(TAG + ".onHandler default");
        break;
    }
  }

  private void handleCatEyeConnect(int arg2) {
    System.out.println(TAG + ".handleCatEyeConnect");
    switch (arg2) {
      case JVNetConst.CCONNECTTYPE_CONNOK:
        downloadCatPic();
        break;
      case JVNetConst.CCONNECTTYPE_DISCONNE:
        System.out.println(TAG + ".CCONNECTTYPE_DISCONNE 连接失败");
        isConnected = false;
//        SovUtil.disconnect(window);
        break;
      //其他都是连接失败
      default:
        System.out.println(TAG + ".handleCatEyeConnect 其他连接失败");
        isConnected = false;
        break;
    }
  }

  private void downloadCatPic() {
    System.out.println(TAG + ".downloadCatPic.................");
    mFileFullPath = AppConsts.DOWNLOAD_PATH + System.currentTimeMillis() + ".jpg";
    CatEyePushData.DataBody.BusinessData businessData = getPushData().getBody().getBusinessData();
    System.out.println(TAG + ".mFileFullPath = " + mFileFullPath);
    System.out.println(TAG + ".businessData = " + businessData);
    SovUtil.setDownloadFilePath(window, mFileFullPath);
    SovUtil.startStreamCatDownload(window, businessData.getPicturePath());
  }

  private void loadImage(int arg2, Object obj) {
    System.out.println(TAG + ".loadImage arg2 = [" + arg2 + "], obj = [" + obj + "]");
    if (arg2 != JVNetConst.DT_FILE_TAIL) return;
    com.alibaba.fastjson.JSONObject data = JSON.parseObject(obj.toString());
    if (data.getInteger("result") != 1) return;
    System.out.println(TAG + ".loadImage mFileFullPath = " + mFileFullPath);
    GlideUtil.loadImage(this, mFileFullPath, R.drawable.bg_bell, mBinding.ivContent);
  }
}