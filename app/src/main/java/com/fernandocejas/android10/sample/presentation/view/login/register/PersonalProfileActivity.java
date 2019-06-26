package com.fernandocejas.android10.sample.presentation.view.login.register;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.baoyz.actionsheet.ActionSheet;
import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityPersonalProfileBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.OssModule;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.component.InputWatcher;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.MyInfoPresenter;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.MyInfoView;
import com.fernandocejas.android10.sample.presentation.view.mine.utils.ProxyTools;
import com.google.common.base.Strings;
import com.hss01248.photoouter.PhotoCallback;
import com.hss01248.photoouter.PhotoUtil;
import com.qtec.mapp.model.rsp.LogoutResponse;

import java.util.List;

import javax.inject.Inject;

/**
 * <pre>
 *      author: wushaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/14
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class PersonalProfileActivity extends BaseActivity implements MyInfoView {

  private static final String TAG = PersonalProfileActivity.class.getName();

  private ActivityPersonalProfileBinding mBinding;

  private PhotoCallback mPhotoCallback;

  @Inject
  MyInfoPresenter mMyInfoPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_personal_profile);

    initBindData();

    initializeInjector();

    initPresenter();

    initView();

    initPhotoData();
  }

  private void initPhotoData() {
    mPhotoCallback = ProxyTools.getShowMethodInfoProxy(new PhotoCallback() {
      @Override
      public void onFail(String msg, Throwable r, int requestCode) {
        System.out.println("MyInfoActivity.onFail");

      }

      @Override
      public void onSuccessSingle(String originalPath, String compressedPath, int requestCode) {
        System.out.println("MyInfoActivity.onSuccessSingle");

        GlideUtil.loadCircleHeadImage(getContext(), compressedPath, mBinding.imgHead);

        mMyInfoPresenter.getStsToken(compressedPath);

      }

      @Override
      public void onSuccessMulti(List<String> originalPaths, List<String> compressedPaths, int requestCode) {}

      @Override
      public void onCancel(int requestCode) {
        System.out.println("MyInfoActivity.onCancel");

      }
    });
  }

  private void initPresenter() {
    mMyInfoPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
        .ossModule(new OssModule())
        .build()
        .inject(this);
  }

  private void initBindData() {
    mBinding.setSp(new SPUtils(PrefConstant.SP_NAME));
  }

  private void initView() {

    GlideUtil.loadCircleHeadImage(getContext(), userHeadImg(), mBinding.imgHead, R.drawable.default_avatar);

    watchEnterNickName();
  }

  private void watchEnterNickName() {
    InputUtil.allowLetterNumberChinese(mBinding.etNickName, 16);
    InputWatcher watcher = new InputWatcher();
    watcher.addEt(mBinding.etNickName);
    watcher.setInputListener(isEmpty -> {
      mBinding.btnSave.setClickable(!isEmpty);
      if (isEmpty) {
        mBinding.btnSave.setBackgroundColor(getResources().getColor(R.color.white_bbdefb));
      } else {
        mBinding.btnSave.setBackgroundResource(R.drawable.btn_blue_2196f3_selector);
      }
    });

    mBinding.etNickName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable editable) {
        String modifiedNick = editable.toString();
        String savedNick = PrefConstant.getUserNickName();
        boolean isModify = !TextUtils.isEmpty(modifiedNick) && !modifiedNick.equals(savedNick);
        int color = isModify ? R.color.black_424242 : R.color.gray_cdcdcd;
//        mTitleBar.setRightEnable(isModify, getResources().getColor(color));

        int index = mBinding.etNickName.getSelectionStart() - 1;
        if (index > 0) {
          if (InputUtil.isEmojiCharacter(editable.charAt(index))) {
            Editable edit = mBinding.etNickName.getText();
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

  public void clearNickName(View view) {
    mBinding.etNickName.setText("");
  }

  public void modifyHeadImage(View view) {
    showActionSheet();
  }

  public void save(View view) {
    saveUserNickNameLocal();
    mMyInfoPresenter.modifyUserInfo(userPhone(), userHeadImg(), userNickName());
  }

  public void skip(View view) {
    mNavigator.navigateNewAndClearTask(getContext(), MainActivity.class);
    finish();
  }

  private void showActionSheet() {
    setTheme(R.style.ActionSheetStyleiOS7);
    ActionSheet.createBuilder(this, getSupportFragmentManager())
        .setCancelButtonTitle("取消")
        .setOtherButtonTitles("从相册中选择", "拍照")
        .setCancelableOnTouchOutside(true)
        .setListener(new ActionSheet.ActionSheetListener() {
          @Override
          public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
          }

          @Override
          public void onOtherButtonClick(ActionSheet actionSheet, int index) {
            switch (index) {
              // 调用自定义的相册
              case 0:
                PhotoUtil.begin()
                    .setNeedCropWhenOne(true)
                    .setNeedCompress(true)
                    .setMaxSelectCount(1)
                    .setCropMuskOval()
                    .setSelectGif()
                    .start(PersonalProfileActivity.this, 33, mPhotoCallback);
                break;
              // 跳转相机
              case 1:
                PhotoUtil.begin()
                    .setFromCamera(true)
                    .setNeedCropWhenOne(true)
                    .setNeedCompress(true)
                    .setMaxSelectCount(1)
                    .setCropMuskOval()
                    .setSelectGif()
                    .start(PersonalProfileActivity.this, 23, mPhotoCallback);
                break;
              default:
                break;
            }
          }
        }).show();
  }

  private void saveUserNickNameLocal() {
    if (!Strings.isNullOrEmpty(userNickName())) {
      mBinding.getSp().put(PrefConstant.SP_USER_NICK_NAME, userNickName());
    }

    if (!Strings.isNullOrEmpty(mMyInfoPresenter.getImageUrl())) {
      mBinding.getSp().put(PrefConstant.SP_USER_HEAD_IMG, mMyInfoPresenter.getImageUrl());
    }
  }

  private String userNickName() {
    return mBinding.etNickName.getText().toString();
  }

  private String userHeadImg() {
    return mBinding.getSp().getString(PrefConstant.SP_USER_HEAD_IMG, "");
  }

  private String userPhone() {
    return mBinding.getSp().getString(PrefConstant.SP_USER_PHONE, "");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    PhotoUtil.onActivityResult(this, requestCode, resultCode, data);
  }

  @Override
  public void openLogin(LogoutResponse response) {
    mNavigator.navigateNewAndClearTask(getContext(), LoginActivity.class);
  }

  @Override
  public void finishView() {
    finish();
  }

  @Override
  public void showModifySuccess() {
    mNavigator.navigateNewAndClearTask(getContext(), MainActivity.class);
    finish();
  }

  @Override
  public void uploadHeadImageSuccess(String imageUrl) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        hideLoading();
      }
    });
  }

  @Override
  public void uploadHeadImageFail() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        hideLoading();
      }
    });
  }

  @Override
  public void onBackPressed() {}

}
