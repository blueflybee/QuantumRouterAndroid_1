

package com.fernandocejas.android10.sample.presentation.view.login.login;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.net.CloudUrlPath;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLogin1Binding;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityLoginCritterBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLoginComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LoginModule;
import com.fernandocejas.android10.sample.presentation.utils.ClickUtil;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdGetIdCodeActivity;
import com.fernandocejas.android10.sample.presentation.view.login.register.RegisterGetIdCodeActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.selectconfig.SelectLockEncryptionActivity;
import com.fernandocejas.android10.sample.presentation.view.selectconfig.ServerActivity;
import com.qtec.mapp.model.rsp.LoginResponse;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginView {
  public static final int PERMISSION_REQUEST_CODE = 0;

  private static final String TAG = LoginActivity.class.getName();

  @Inject
  LoginPresenter mLoginPresenter;

  private ActivityLoginCritterBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (!isTaskRoot()) {
      finish();
      return;
    }
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login_critter);

    initializeInjector();

    initPresenter();

    initView();

    mBinding.etName.setText(PrefConstant.getUserPhone());

    addTextWatcherNameEt();

    /*mBinding.etPwd.setText("123456");*/
    requestPermissions();

    checkAutoLogin();

  }

  private void checkAutoLogin() {
    String token = PrefConstant.getAppToken();
    if (!TextUtils.isEmpty(token)) {
      CloudUrlPath.setToken(token);
      mNavigator.navigateTo(getContext(), MainActivity.class);
      finish();
    }

  }

  private void initView() {
    mBinding.viewCritter.setMaxWatchLength(11);
    mBinding.etName.setOnTouchListener(new View.OnTouchListener() {
      int touchCount = 0;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        touchCount++;
        if (touchCount == 2) {
          touchCount = 0;
          mBinding.viewCritter.applyActive();
          Object tag = mBinding.etName.getTag();
          if (tag == null) {
            new Handler().postDelayed(() -> mBinding.viewCritter.applyWatch(username().length()), 450);
          }
        }
        return false;
      }
    });

    mBinding.etPwd.setOnTouchListener(new View.OnTouchListener() {
      int touchCount = 0;

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        touchCount++;
        if (touchCount == 2) {
          touchCount = 0;
          if (mBinding.ivShowPwd.isChecked()) {
            mBinding.viewCritter.applyShy();
          } else {
            mBinding.viewCritter.applyPeek();
          }
        }
        return false;
      }
    });

    mBinding.rlLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mBinding.viewCritter.applyNeutral();
        KeyboardUtils.hideSoftInput(getContext(), mBinding.etName);
      }
    });
    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition nameCondition = new InputWatcher.WatchCondition();
    nameCondition.setLength(11);
    InputWatcher.WatchCondition pwdCondition = new InputWatcher.WatchCondition();
    pwdCondition.setRange(new InputWatcher.InputRange(6, 20));
    watcher.addEt(mBinding.etName, nameCondition);
    watcher.addEt(mBinding.etPwd, pwdCondition);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnLogin.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnLogin.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnLogin.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    InputUtil.inhibit(mBinding.etPwd, InputUtil.REGULAR_CHINESE_AND_SPACE, 20);
    watchEditTextNoClear(mBinding.etPwd);

  }

  private void addTextWatcherNameEt() {
    mBinding.etName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        mBinding.etName.setTag(true);
        mBinding.viewCritter.applyWatch(s.length());
      }
    });
  }


  private void initPresenter() {
    mLoginPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLoginComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .loginModule(new LoginModule())
        .build()
        .inject(this);
  }

  public void login(View view) {
    if (ClickUtil.isFastClick()) return;
    mLoginPresenter.login(username(), password());
  }

  public void forgetPwd(View view) {
    mNavigator.navigateTo(this, ResetPwdGetIdCodeActivity.class, null);
  }

  public void register(View view) {
    mNavigator.navigateTo(this, RegisterGetIdCodeActivity.class, null);
  }

  public void clearUsername(View view) {
    mBinding.etName.setText("");
  }

  public void changeInputType(View view) {
    if (mBinding.ivShowPwd.isChecked()) {
      mBinding.viewCritter.applyShy();
    } else {
      mBinding.viewCritter.applyPeek();
    }
    InputUtil.showHidePwd(mBinding.etPwd);
  }

  @NonNull
  private String password() {
    return getText(mBinding.etPwd);
  }

  @NonNull
  private String username() {
    return getText(mBinding.etName);
  }

  @Override
  public void openMain(LoginResponse response) {
    mNavigator.navigateTo(getContext(), MainActivity.class);
    finish();
  }

  @Override
  public void showUsernameEmp() {
    ToastUtils.showShort("请输入用户名");
  }

  @Override
  public void showPasswordEmp() {
    ToastUtils.showShort("请输入密码");
  }

  @Override
  public void showPasswordErrorThreeTimes(Throwable e) {

    DialogUtil.showPwdErrThreeTimesDialog(getContext(), new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mNavigator.navigateTo(getContext(), ResetPwdGetIdCodeActivity.class);
      }
    });
  }

  @Override
  public void showPasswordErrorMoreTimes(Throwable e) {
    DialogUtil.showCancelConfirmWithTipsDialog(getContext(),
        "提示", "短时间内登录次数过多，请半小时后重试",
        "您可以重置密码立即登录", "知道了", "重置密码", new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            mNavigator.navigateTo(getContext(), ResetPwdGetIdCodeActivity.class);
          }
        });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mLoginPresenter != null) {
      mLoginPresenter.destroy();
    }
  }

  @Override
  public void onError(String message) {
    mBinding.tvTips.setText(message);
  }

  public void selectServer(View view) {
    mNavigator.navigateTo(getContext(), ServerActivity.class);
  }

  public void selectEncryption(View view) {
    mNavigator.navigateTo(getContext(), SelectLockEncryptionActivity.class);
  }

  private void requestPermissions() {
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        , Manifest.permission.RECORD_AUDIO};
    PermissionUtils.requestPermissions(getContext(), PERMISSION_REQUEST_CODE, perms, new PermissionUtils.OnPermissionListener() {
      @Override
      public void onPermissionGranted() {
        Log.i(TAG, "允许权限");
      }

      @Override
      public void onPermissionDenied(String[] deniedPermissions) {
        Log.i(TAG, "拒绝权限");
      }
    });
  }

}
