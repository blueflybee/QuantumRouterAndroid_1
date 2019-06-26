/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.internal.di.HasComponent;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.TitleBar;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.qtec.lock.model.core.BleBody;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

//import com.umeng.analytics.MobclickAgent;

/**
 * Base {@link Fragment} class for every fragment in this application.
 */
public abstract class BaseFragment extends Fragment implements LoadDataView {

  @Inject
  protected Navigator mNavigator;

  protected TitleBar mTitleBar;
  protected boolean mIsMoreThanOneUser;
  protected GetLockUsersResponse mSelectedLockUser;
  protected boolean mIsAdmin;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  /**
   * Shows a {@link Toast} message.
   *
   * @param message An string representing a message to be shown.
   */
  protected void showToastMessage(String message) {
    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
  }


  /**
   * Gets a component for dependency injection by its type.
   */
  @SuppressWarnings("unchecked")
  protected <C> C getComponent(Class<C> componentType) {
    return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
  }

  protected Navigator getNavigator() {
    return ((BaseActivity) getActivity()).getNavigator();
  }

  @Override
  public void onPause() {
    super.onPause();
        MobclickAgent.onPageEnd("MainScreen");//统计时长
  }

  @Override
  public void onResume() {
    super.onResume();
       MobclickAgent.onPageStart("MainScreen");
  }

  protected void initTitleBar(String title) {
    if (mTitleBar == null) {
      mTitleBar = (TitleBar) getActivity().findViewById(R.id.title_bar);
    }
    mTitleBar.setTitleCenter(title);
    ((AppCompatActivity) getActivity()).setSupportActionBar(mTitleBar.getToolbar());

    mTitleBar.setLeftAsBackButton();
  }

  @Override
  public void showLoading() {
    DialogUtil.showProgress((AppCompatActivity) getActivity());

  }

  @Override
  public void hideLoading() {
    DialogUtil.hideProgress();

  }

  @Override
  public Context getContext() {
    return super.getContext();
  }

  @Override
  public void onError(String message) {
    if(!TextUtils.isEmpty(message)){
      ToastUtils.showShort(message);
    }

  }

  @Override
  public void showLoginInvalid() {
    DialogUtil.showOkAlertDialogWithMessage(
        getContext(),
        "登录失效", "您的登录已失效，请重新登录",
        v -> mNavigator.navigateNewAndClearTask(getContext(), LoginActivity.class));
  }
  /*
    * 为子类提供权限检查方法
    * */
  protected boolean hasPermission(String... permissions) {
    for (String permission : permissions) {
      if (ContextCompat.checkSelfPermission(getContext(), permission)
          != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }
  /**
   * 隐藏或显示密码
   * @param et
   */
  protected void showHidePwd(EditText et) {
    int selectionStart = et.getSelectionStart();
    int hide = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
    if (et.getInputType() == hide) {
      et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    } else {
      et.setInputType(hide);
    }
    et.setSelection(selectionStart);
  }

  protected void changeTitleRightBtnStyle(String input) {
    boolean isModify = !TextUtils.isEmpty(input);
    int color = isModify ? R.color.black_424242 : R.color.gray_cdcdcd;
    mTitleBar.setRightEnable(isModify, getResources().getColor(color));
  }

  @NonNull
  protected String getText(TextView tv) {
    return tv.getText().toString().trim();
  }


  protected boolean checkBleRsp(String code) {
    if (!BleBody.RSP_OK.equals(code)) {
      ToastUtils.showShort("门锁操作失败");
      return true;
    }
    return false;
  }

  protected BaseActivity getBaseActivity() {
    return (BaseActivity) getActivity();
  }


}
