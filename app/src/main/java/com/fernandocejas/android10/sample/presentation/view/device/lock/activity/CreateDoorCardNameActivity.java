package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityCreateDoorCardNameBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerLockComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.utils.LockManager;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
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
public class CreateDoorCardNameActivity extends BaseActivity implements ModifyLockDoorCardView{
  private ActivityCreateDoorCardNameBinding mBinding;

  @Inject
  ModifyLockDoorCardPresenter mModifyLockDoorCardPresenter;

  private BleLock mBleLock;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_door_card_name);

    initializeInjector();

    initPresenter();

    initData();

    initView();
  }

  private void initData() {
    mBleLock = LockManager.getLock(getContext(), lockMacAddress());
  }

  private void initView() {
    initTitleBar("添加门卡");
    mTitleBar.setRightAs("完成", v -> {
      GetDoorCardsResponse response = new GetDoorCardsResponse();
      response.setDeviceSerialNo(mBleLock.getId());
      response.setDoorcardSerialNo(doorCardId());
      response.setDoorcardName(getText(mBinding.etDoorCardName));
      mModifyLockDoorCardPresenter.modifyLockDoorCard(response);
    });

    InputWatcher watcher = new InputWatcher();
    InputWatcher.WatchCondition condition = new InputWatcher.WatchCondition();
    condition.setRange(new InputWatcher.InputRange(1, 16));
    watcher.addEt(mBinding.etDoorCardName, condition);
    watcher.setInputListener(new InputWatcher.InputListener() {
      @Override
      public void onInput(boolean empty) {
        changeTitleRightBtnStyle(empty);
      }
    });

    InputUtil.allowLetterNumberChinese(mBinding.etDoorCardName, 16);

  }

  private void initPresenter() {
    mModifyLockDoorCardPresenter.setView(this);
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
  public void showModifyDoorCardNameSuccess() {
    DialogUtil.showSuccessDialog(getContext(), "设置成功", () -> {
      Intent intent = new Intent();
      intent.putExtra(Navigator.EXTRA_LOCK_PAGE, 1);
      mNavigator.navigateExistAndClearTop(getContext(), LockMainActivity.class, intent);
    });
  }

  @Override
  public void showDeleteDoorCardSuccess() {

  }


  private String doorCardId() {
    return getIntent().getStringExtra(Navigator.EXTRA_LOCK_DOOR_CARD_ID);
  }

}
