package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCreateFingerprintNameBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;

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
public class CreateFingerprintNameActivity extends BaseActivity implements ModifyLockFpView{
  private ActivityCreateFingerprintNameBinding mBinding;

  @Inject
  ModifyLockFpPresenter mModifyLockFpPresenter;

  private BleLock mBleLock;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_fingerprint_name);

    initializeInjector();

    initPresenter();

    initData();

    initView();
  }

  private void initData() {
    mBleLock = LockManager.getLock(getContext(), lockMacAddress());
  }

  private void initView() {
    initTitleBar("添加指纹");
    mTitleBar.setRightAs("完成", v -> {
      GetFingerprintsResponse response = new GetFingerprintsResponse();
      response.setDeviceSerialNo(mBleLock.getId());
      response.setFingerprintSerialNo(fpId());
      response.setFingerprintName(getText(mBinding.etFingerprintName));
      mModifyLockFpPresenter.modifyLockFp(response);
    });

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(1, 16));
    watcher.addEt(mBinding.etFingerprintName, condition);
    watcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean empty) {
        changeTitleRightBtnStyle(empty);
      }
    });

    InputUtil.allowLetterNumberChinese(mBinding.etFingerprintName, 16);

    View.OnClickListener clickListener =
        v -> mBinding.etFingerprintName.setText(getText((TextView) v));
    mBinding.tvOne.setOnClickListener(clickListener);
    mBinding.tvTwo.setOnClickListener(clickListener);
    mBinding.tvThree.setOnClickListener(clickListener);
    mBinding.tvFour.setOnClickListener(clickListener);
    mBinding.tvFive.setOnClickListener(clickListener);
  }

  private void initPresenter() {
    mModifyLockFpPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerLockComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .lockModule(new LockModule())
        .build()
        .inject(this);
  }

  @Override
  public void showModifyFpNameSuccess() {
    DialogUtil.showSuccessDialog(getContext(), "设置成功", () -> {
      if (isAddFpOnly()) {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_LOCK_PAGE, 1);
        mNavigator.navigateExistAndClearTop(getContext(), LockMainActivity.class, intent);
      } else {
        Intent intent = new Intent();
        intent.putExtra(Navigator.EXTRA_BLE_DEVICE_ADDRESS, lockMacAddress());
        if (AppConstant.isgFirstAddFingerPrint()) {
          mNavigator.navigateTo(getContext(), KeepOnAddFingerprintActivity.class, intent);
        } else {
          mNavigator.navigateTo(getContext(), BindRouterToLockActivity.class, intent);
        }
      }

    });
  }

  @Override
  public void showDeleteFpSuccess() {

  }

  private boolean isAddFpOnly() {
    return getIntent().getBooleanExtra(Navigator.EXTRA_ADD_FP_ONLY, false);
  }

  private String fpId() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_FP_ID);
  }

}
