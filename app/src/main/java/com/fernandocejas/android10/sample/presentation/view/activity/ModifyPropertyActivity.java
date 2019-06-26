package com.fernandocejas.android10.sample.presentation.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModifyPropertyBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerRouterComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.ModifyPropertyPresenter;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.ModifyPropertyView;
import com.qtec.mapp.model.rsp.IntelDevInfoModifyResponse;

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
public class ModifyPropertyActivity extends BaseActivity implements ModifyPropertyView {

  protected ActivityModifyPropertyBinding mBinding;

  @Inject
  protected ModifyPropertyPresenter mModifyPropertyPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_property);

    initializeInjector();

    initPresenter();

    initView();

  }

  protected void initView() {
    initTitleBar("名称");
    mTitleBar.setRightAs("完成", v -> modifyProperty());
    mBinding.etModifyProperty.setText(property());
    changeTitleRightBtnStyle(property());
    mBinding.etModifyProperty.setHint(hint());
    mBinding.etModifyProperty.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        changeTitleRightBtnStyle(s.toString());
      }
    });
  }

  protected void modifyProperty() {}

  public void clearText(View view) {
    mBinding.etModifyProperty.setText("");
  }


  private String property() {
    return getIntent().getStringExtra(Navigator.EXTRA_MODIFY_PROPERTY);
  }
  protected String getModifyProperty() {
    return getText(mBinding.etModifyProperty);
  }

  private String hint() {
    return getIntent().getStringExtra(Navigator.EXTRA_MODIFY_HINT);
  }

  private void initPresenter() {
    mModifyPropertyPresenter.setView(this);
  }

  protected void initializeInjector() {
    DaggerRouterComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .routerModule(new RouterModule())
        .build()
        .inject(this);
  }

  @Override
  public void showModifyRouterNameSuccess() {
    ToastUtils.showShort("网关名称修改成功");
  }

  @Override
  public void showDevNameEmp() {
    ToastUtils.showShort(R.string.dev_modify_toast_dev_name_emp);
  }

  @Override
  public void showModifySuccess(IntelDevInfoModifyResponse response) {
    finish();
  }
}
