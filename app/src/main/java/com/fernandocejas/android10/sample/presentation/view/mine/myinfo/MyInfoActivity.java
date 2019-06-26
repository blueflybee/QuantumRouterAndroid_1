package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

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
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityMyInfoBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.OssModule;
import com.fernandocejas.android10.sample.presentation.utils.DialogUtil;
import com.fernandocejas.android10.sample.presentation.utils.GlideUtil;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
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
public class MyInfoActivity extends BaseActivity implements MyInfoView {

  private static final String TAG = MyInfoActivity.class.getName();

  private ActivityMyInfoBinding mBinding;

  private PhotoCallback mPhotoCallback;

  @Inject
  MyInfoPresenter mMyInfoPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_info);

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
    initTitleBar("个人资料");
//    mTitleBar.setLeftAsBackButton(v -> {
////      setResult(RESULT_OK, new Intent());
//      finish();
//    });
    mTitleBar.setRightAs("保存", v -> {
      saveUserNickNameLocal();
      modifyUserInfo();
    });
    mTitleBar.setRightEnable(false, getResources().getColor(R.color.gray_cdcdcd));

    GlideUtil.loadCircleHeadImage(getContext(), userHeadImg(), mBinding.imgHead);

    watchEnterNickName();
  }

  private void watchEnterNickName() {
    //控制表情输入
    mBinding.etNickName.setFilters(InputUtil.emojiFilters);

    InputUtil.allowLetterNumberChinese(mBinding.etNickName, 16);
    mBinding.etNickName.setText(PrefConstant.getUserNickName());
    moveEditCursorToEnd(mBinding.etNickName);

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
//        editable.clear();
        int indexOf = editable.toString().indexOf(" ");

        if(indexOf != -1){
          editable.replace(indexOf, indexOf + 1, "");
        }

        String savedNick = PrefConstant.getUserNickName();
        boolean isModify = !TextUtils.isEmpty(modifiedNick) && !modifiedNick.equals(savedNick);
        int color = isModify ? R.color.black_424242 : R.color.gray_cdcdcd;
        mTitleBar.setRightEnable(isModify, getResources().getColor(color));
      }
    });
  }

  public void logout(View view) {
    DialogUtil.showOkCancelAlertDialogWithMessage(this, "退出登录", "您确定要退出登录吗？",
        v -> {
          mMyInfoPresenter.logout(userPhone());
        }, null);
  }

  public void clearNickName(View view) {
    mBinding.etNickName.setText("");
  }

  public void modifyPassword(View view) {
    mNavigator.navigateTo(this, ModifyPwdGetIdCodeActivity.class, null);
  }

  public void modifyHeadImage(View view) {
    showActionSheet();
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
                    .start(MyInfoActivity.this, 33, mPhotoCallback);
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
                    .start(MyInfoActivity.this, 23, mPhotoCallback);
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

  private void modifyUserInfo() {

    mMyInfoPresenter.modifyUserInfo(userPhone(), userHeadImg(), userNickName());
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
    ((AndroidApplication) getApplication()).unbindBleService();
    mNavigator.navigateNewAndClearTask(getContext(), LoginActivity.class);
  }

  @Override
  public void finishView() {
    finish();
  }

  @Override
  public void showModifySuccess() {
    mTitleBar.setRightEnable(false, getResources().getColor(R.color.gray_cdcdcd));
    setResult(RESULT_OK, new Intent());
    finish();
  }

  @Override
  public void uploadHeadImageSuccess(String imageUrl) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        hideLoading();
        mTitleBar.setRightEnable(true, getResources().getColor(R.color.black_424242));
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
  public void onBackPressed() {
    setResult(RESULT_OK, new Intent());
    super.onBackPressed();
  }

}
