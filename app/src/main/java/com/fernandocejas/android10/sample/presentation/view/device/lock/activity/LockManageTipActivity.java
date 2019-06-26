package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.blueflybee.keystore.KeystoreRepertory;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockFactoryResetBinding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLockManageTipBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.ApplicationComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/22
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class LockManageTipActivity extends Activity {

  public static final int LOCK_TIPS_TYPE_DELETED_BY_ADMIN = 0;
  public static final int LOCK_TIPS_TYPE_ADMIN_TRANSFER = 1;
  public static final int LOCK_TIPS_TYPE_RESET_LOCK = 2;
  private ActivityLockManageTipBinding mBinding;

  public static final String ACTION_LOCK_MANAGE_TIPS = "android.intent.action.action_lock_manage_tips";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_lock_manage_tip);
    setFinishOnTouchOutside(false);

    initView();

  }

  private void initView() {

    switch (getTipsType()) {
      case LOCK_TIPS_TYPE_DELETED_BY_ADMIN:
        mBinding.tvContent.setText("您已经被门锁管理员移除访问权限");
        mBinding.tvTips.setText("提示：如果您希望继续使用，可以重新绑定或者联系管理员");
        break;

      case LOCK_TIPS_TYPE_ADMIN_TRANSFER:
        mBinding.tvContent.setText("您已经被指定为门锁管理员");
        mBinding.tvTips.setVisibility(View.GONE);
        break;

      case LOCK_TIPS_TYPE_RESET_LOCK:
        mBinding.tvContent.setText("您绑定的门锁已经被恢复出厂设置");
        mBinding.tvTips.setText("提示：门锁将从您的设备中移除，需要重新绑定");
        break;

      default:
        finish();
        break;
    }
  }

  @Override
  public void onBackPressed() {}

  public void closeDialog(View view) {
    finish();
    new Navigator().navigateExistAndClearTop(this, MainActivity.class);
  }

  public void iKnow(View view) {
    finish();
    new Navigator().navigateExistAndClearTop(this, MainActivity.class);
  }

  private int getTipsType() {
    return getIntent().getIntExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_TYPE, -1);
  }

  private String getLockId() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_MANAGE_TIPS_DEVICE_ID);
  }


}