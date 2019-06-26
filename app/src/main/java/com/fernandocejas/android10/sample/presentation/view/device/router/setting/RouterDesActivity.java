package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityRouterDesBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

import javax.inject.Inject;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   : 修改属性公共activity
 *     version: 1.0
 * </pre>
 */
public class RouterDesActivity extends BaseActivity implements RouterDescView{

  protected ActivityRouterDesBinding mBinding;

  @Inject
  protected RouterDescPresenter mRouterDescPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_router_des);

    initializeInjector();

    initPresenter();

    initView();

  }

  private void initView() {
    initTitleBar("网关描述");
    mTitleBar.setRightAs("完成", v -> commitDescription());

    mBinding.etDescription.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        mBinding.tvInputCount.setText(String.valueOf(s.length()) + "/100");
        String input = s.toString();
        //changeTitleRightBtnStyle(input);
      /*  if(input.trim().length() > 0){
          //长度大于0 可点击
          mTitleBar.setRightEnable(true, getResources().getColor(R.color.black_424242));
        }else {
          mTitleBar.setRightEnable(false, getResources().getColor(R.color.gray_cdcdcd));
        }*/
      }
    });
    mBinding.etDescription.setText(description());
  }

  private void commitDescription() {
    if(TextUtils.isEmpty(getDescription())){
      mRouterDescPresenter.modifyRouterDesc("");
    }else {
      mRouterDescPresenter.modifyRouterDesc(getDescription());
    }

  }

  @NonNull
  private String getDescription() {
    return mBinding.etDescription.getText().toString();
  }

  private String description() {
    return getIntent().getStringExtra(Navigator.EXTRA_ROUTER_DESCRIPTION);
  }

  private void initPresenter() {
    mRouterDescPresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerRouterComponent.builder().applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void showModifySuccess() {
    ToastUtils.showShort("描述信息修改成功");
    finish();
  }
}
