package com.fernandocejas.android10.sample.presentation.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.blelibrary.AndroidLockBle;
import com.blueflybee.blelibrary.BleRequest;
import com.blueflybee.blelibrary.BleService;
import com.blueflybee.blelibrary.IBle;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.fernandocejas.android10.sample.presentation.view.ViewServer;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockCheckVersionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockFactoryResetActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.IBleOperable;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.lock.model.core.BleBody;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

//import com.umeng.analytics.MobclickAgent;

/**
 * Base {@link Activity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity implements LoadDataView {

  @Inject
  protected Navigator mNavigator;

  protected TitleBar mTitleBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getApplicationComponent().inject(this);

    ViewServer.get(this).addWindow(this);
  }

  /**
   * Adds a {@link Fragment} to this activity's file_paths.
   *
   * @param containerViewId The container view to where add the fragment.
   * @param fragment        The fragment to be added.
   */
  protected void addFragment(int containerViewId, Fragment fragment) {
    FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(containerViewId, fragment);
    fragmentTransaction.commit();
  }

  /**
   * Get the Main Application component for dependency injection.
   *
   * @return {@link ApplicationComponent}
   */
  protected ApplicationComponent getApplicationComponent() {
    return ((AndroidApplication) getApplication()).getApplicationComponent();
  }

  /**
   * Get an Activity module for dependency injection.
   *
   * @return {@link ActivityModule}
   */
  protected ActivityModule getActivityModule() {
    return new ActivityModule(this);
  }

  public Navigator getNavigator() {
    return mNavigator;
  }


  @Override
  protected void onResume() {
    super.onResume();
    MobclickAgent.onPageStart("SplashScreen");//统计页面
    MobclickAgent.onResume(this); //统计时长
    ViewServer.get(this).setFocusedWindow(this);
  }


  @Override
  protected void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd("SplashScreen");
    MobclickAgent.onPause(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    ViewServer.get(this).removeWindow(this);
  }

  protected void initTitleBar(String title) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) findViewById(R.id.title_bar);
    }
    mTitleBar.setTitleCenter(title);
    setSupportActionBar(mTitleBar.getToolbar());
    mTitleBar.setLeftAsBackButton();
  }

  /**
   * 特别关注
   *
   * @param
   * @return
   */
  protected void initSpecialAttentionTitleBar(String title) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) findViewById(R.id.title_bar);
    }
    mTitleBar.setTitleCenter(title);
    setSupportActionBar(mTitleBar.getToolbar());
    mTitleBar.setLeftAsBackButton(R.drawable.arrow_white);

    mTitleBar.getTitleView().setTextColor(getResources().getColor(R.color.white));
    mTitleBar.getToolbar().setBackgroundColor(getResources().getColor(R.color.blue_2196f3));
    mTitleBar.getDividerView().setVisibility(View.GONE);
  }

  protected void initMoveFileTitleBar(String title) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) findViewById(R.id.title_bar);
    }
    mTitleBar.setTitleCenter(title);
    setSupportActionBar(mTitleBar.getToolbar());
    mTitleBar.setLeftAsBackButtonForSamba();
  }

  /**
   * samba主界面用
   *
   * @param
   * @return
   */
  protected void initCenterTitle(String title, View.OnClickListener listener) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) findViewById(R.id.title_bar);
    }
    mTitleBar.setCenterAs(title, listener);
    setSupportActionBar(mTitleBar.getToolbar());
    //mTitleBar.setLeftAsBackButton();
    mTitleBar.setLeftAsBackButtonForSamba();
  }

  @Override
  public void showLoading() {
    DialogUtil.showProgress(this);
  }

  @Override
  public void hideLoading() {
    DialogUtil.hideProgress();
  }

  protected void hideSoftKeyBoard() {
    new Handler().postDelayed(
        () -> KeyboardUtils.hideSoftInput(this), 200);
  }

  @Override
  public Context getContext() {
    return this;
  }

  /*
    * 为子类提供权限检查方法
    * */
  protected boolean hasPermission(String... permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(this, permission)
          != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }


  @Override
  public void onError(String message) {
    if (!TextUtils.isEmpty(message)) {
      ToastUtils.showShort(message);
    }

  }

  @Override
  public void showLoginInvalid() {
    DialogUtil.showOkAlertDialogWithMessage(
        getContext(),
        "登录失效", "您的登录已失效，请重新登录",
        v -> mNavigator.navigateNewAndClearTask(BaseActivity.this.getContext(), LoginActivity.class));
  }


  protected void changeTitleRightBtnStyle(String input) {
    changeTitleRightBtnStyle(TextUtils.isEmpty(input));
  }

  protected void changeTitleRightBtnStyle(boolean empty) {
    boolean isModify = !empty;
    int color = isModify ? R.color.black_424242 : R.color.gray_cdcdcd;
    mTitleBar.setRightEnable(isModify, getResources().getColor(color));
  }

  @NonNull
  protected String getText(TextView tv) {
    return tv.getText().toString().trim();
  }

  /**
   * 限制 EditText 输入表情,同时显示clear按钮
   *
   * @param
   * @return
   */
  protected void watchEditText(EditText editView, ImageView clearView) {
    editView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
          clearView.setVisibility(View.GONE);
        } else {
          clearView.setVisibility(View.VISIBLE);
          clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              editView.getText().clear();
            }
          });
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {
        int index = editView.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(editable.charAt(index))) {
            Editable edit = editView.getText();
            if (index == 1) {
              edit.clear();
            } else {
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }

  /**
   * 限制 EditText 输入表情,无clear按钮
   *
   * @param
   * @return
   */
  protected void watchEditTextNoClear(EditText editView) {
    editView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
   /*     if(TextUtils.isEmpty(s)){
          clearView.setVisibility(View.GONE);
        }else{
          clearView.setVisibility(View.VISIBLE);
          clearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              editView.getText().clear();
            }
          });
        }*/
      }

      @Override
      public void afterTextChanged(Editable editable) {
        int index = editView.getSelectionStart() - 1;
        if (index > 0) {
          if (isEmojiCharacter(editable.charAt(index))) {
            Editable edit = editView.getText();
            if (index == 1) {
              edit.clear();
            } else {
              edit.delete(index, index + 1);
            }
          }
        }
      }
    });
  }

  /**
   * 判断是否是表情
   *
   * @param
   * @return
   */
  private static boolean isEmojiCharacter(char codePoint) {
    return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
  }

  /**
   * EditText光标移动到最后
   *
   * @param
   * @return
   */
  protected static void moveEditCursorToEnd(EditText editText) {
    Selection.setSelection(editText.getText(), editText.getText().length());
  }


  ////////////////////////////////////////////////////ble///////////////////////////////////////////////////////////////////////////
  public void setUserUsingBle(boolean userUsingBle, String lockMac) {
    IBleOperable bleLockService = ((AndroidApplication) getApplication()).getBleLockService();
    if (bleLockService != null) {
      if (!userUsingBle) close(lockMac);
      bleLockService.setUserUsingBle(userUsingBle);
    }
  }

  public void startBleTimer() {
    IBleOperable bleLockService = ((AndroidApplication) getApplication()).getBleLockService();
    if (bleLockService != null) {
      bleLockService.startTimer();
    }
  }

  public void pauseBleTimer() {
    IBleOperable bleLockService = ((AndroidApplication) getApplication()).getBleLockService();
    if (bleLockService != null) {
      bleLockService.pauseTimer();
    }
  }

  protected void stopBleTimer() {
    IBleOperable bleLockService = ((AndroidApplication) getApplication()).getBleLockService();
    if (bleLockService != null) {
      bleLockService.stopTimer();
    }
  }

  protected void restartBle() {
    IBle ble = getBle();
    if (ble == null) return;
    ble.disable();
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        ble.enable();
      }
    }, 2000);
  }

  protected BleRequest.RequestType getBleRequestType(Intent intent) {
    return (BleRequest.RequestType) intent.getSerializableExtra(BleService.EXTRA_REQUEST);
  }

  protected BleRequest.FailReason getBleFailReason(Intent intent) {
    return (BleRequest.FailReason) intent.getSerializableExtra(BleService.EXTRA_REASON);
  }

  public BleRequest.CmdType getBleCmdType(Intent intent) {
    return (BleRequest.CmdType) intent.getSerializableExtra(BleService.EXTRA_CMD_TYPE);
  }

  protected int getBleStatus(Intent intent) {
    return intent.getIntExtra(BleService.EXTRA_STATUS, -100);
  }

  protected void handleBleDisconnect(boolean incorrectCmdType) {
    if (incorrectCmdType) return;
    hideLoading();
    DialogUtil.showBleTipsDialog(getContext(), "蓝牙连接失败");
  }

  public void handleBleDisconnect(boolean incorrectCmdType, boolean userUsingBle, String lockMac) {
    if (incorrectCmdType) return;
    hideLoading();
    setUserUsingBle(userUsingBle, lockMac);
    DialogUtil.showBleTipsDialog(getContext(), "蓝牙连接失败");
  }

  protected void handleBleFail(Intent intent, boolean incorrectCmdType) {
    if (incorrectCmdType) return;
    hideLoading();
    BleRequest.FailReason reason = getBleFailReason(intent);
    BleRequest.RequestType requestType = getBleRequestType(intent);
    if (reason == BleRequest.FailReason.TIMEOUT && requestType == BleRequest.RequestType.CONNECT_GATT) {
      DialogUtil.showBleTipsDialog(getContext(), "蓝牙连接超时");
    } else if (reason == BleRequest.FailReason.TIMEOUT && requestType == BleRequest.RequestType.WRITE_CHARACTERISTIC_TO_LOCK) {
      DialogUtil.showBleTipsDialog(getContext(), "蓝牙通信超时");
    } else {
      DialogUtil.showBleTipsDialog(getContext(), "蓝牙通信失败");
    }
  }

  public void handleBleFail(Intent intent, boolean incorrectCmdType, boolean userUsingBle, String lockMac) {
    if (incorrectCmdType) return;
    hideLoading();
    setUserUsingBle(userUsingBle, lockMac);
    BleRequest.FailReason reason = getBleFailReason(intent);
    BleRequest.RequestType requestType = getBleRequestType(intent);
    if (reason == BleRequest.FailReason.TIMEOUT && requestType == BleRequest.RequestType.CONNECT_GATT) {
      DialogUtil.showBleTipsDialog(getContext(), "蓝牙连接超时");
    } else if (reason == BleRequest.FailReason.TIMEOUT && requestType == BleRequest.RequestType.WRITE_CHARACTERISTIC_TO_LOCK) {
      DialogUtil.showBleTipsDialog(getContext(), "蓝牙通信超时");
    } else {
      DialogUtil.showBleTipsDialog(getContext(), "蓝牙通信失败");
    }
  }

  public IBle getBle() {
    return ((AndroidApplication) getApplication()).getIBle();
  }


  protected void close(String address) {
    IBle ble = getBle();
    if (ble != null && !TextUtils.isEmpty(address)) {
      ble.close(address);
    }
  }

  protected void disconnect(String address) {
    IBle ble = getBle();
    if (ble != null && !TextUtils.isEmpty(address)) {
      ble.disconnect(address);
    }
  }

  protected void closeAll() {
    IBle ble = getBle();
    if (ble != null) {
      ble.closeAll();
    }
  }

  protected void cancel(BleRequest.CmdType... cmdTypes) {
    IBle ble = getBle();
    if (ble != null) {
      ble.cancel(cmdTypes);
    }
  }

  public void clearBleRequest() {
    IBle ble = ((AndroidApplication) getApplication()).getIBle();
    if (ble != null) {
      if (ble instanceof AndroidLockBle) {
        ((AndroidLockBle) ble).clearBleRequest();
      }
    }
  }

  public boolean bleRspFail(String code, boolean show, String lockMacAddress) {
    if (!BleBody.RSP_OK.equals(code)) {
      String msg;
      switch (code) {
        case BleBody.RSP_INVALID_USER:
          msg = "无效的用户id";
          LockManager.deleteKeyRepo(getContext(), lockMacAddress);
          mNavigator.navigateExistAndClearTop(getContext(), MainActivity.class);
          break;

        case BleBody.RSP_PWD_DUPLICATE:
          msg = "密码已存在";
          break;

        case BleBody.RSP_PWD_IS_FULL:
          msg = "密码达到上限";
          break;

        case BleBody.RSP_FP_IS_FULL:
          msg = "指纹达到上限";
          break;

        case BleBody.RSP_CARD_IS_FULL:
          msg = "卡片达到上限";
          break;

        case BleBody.RSP_LOCK_KEY_INVALID:
        case BleBody.RSP_ILLEGAL_CMD:
          msg = "门锁密钥失效";
          Intent intent = new Intent();
          intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress);
          intent.setAction(BleMapper.ACTION_BLE_LOCK_KEY_INVALID);
          sendBroadcast(intent);
          break;

        case BleBody.RSP_NO_ZIGBEE:
        case BleBody.RSP_NO_MAC_ADDRESS:
          msg = "门锁硬件错误，请联系售后支持";
          break;

        case BleBody.RSP_SYS_LOCKED:
          msg = "系统锁定，请稍候再试";
          break;

        case BleBody.RSP_UPDATING:
          msg = "门锁升级中";
          break;

        case BleBody.RSP_NO_SUCH_CMD:
          msg = "";
          show = false;
          DialogUtil.showConfirmCancelDialog(
              getContext(),
              "该门锁暂未支持该功能",
              "您可对该门锁进行固件升级，以获得我们的更新内容",
              "前往升级", "跳过",
              v -> {
                Intent updateIntent = new Intent();
                updateIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress);
                mNavigator.navigateTo(getContext(), LockCheckVersionActivity.class, updateIntent);
              }, null);
          break;

        case BleBody.RSP_LOCK_FACTORY_RESET:
          BleLock lock = LockManager.getLock(getContext(), lockMacAddress);
          msg = "";
          if (lock != null) {
            msg = "门锁已经恢复出厂设置";
            Intent lockIntent = new Intent();
            lockIntent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress);
            mNavigator.navigateTo(getContext(), LockFactoryResetActivity.class, lockIntent);
          }
          break;

        default:
          msg = "";
          break;
      }
      if (!show) return true;
      if (!TextUtils.isEmpty(msg)) {
        ToastUtils.showShort(msg);
      }
      return true;
    }
    return false;
  }

  protected boolean checkBleRsp(String code) {
    if (!BleBody.RSP_OK.equals(code)) {
      ToastUtils.showShort("门锁操作失败");
      return true;
    }
    return false;
  }

  protected String lockMacAddress() {
    Intent intent = getIntent();
    if (intent == null) return "";
    return intent.getStringExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS);
  }
}
