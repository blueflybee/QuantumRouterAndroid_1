package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityModifyPwdGetIdCodeBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.utils.MyStringUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;

import javax.inject.Inject;

/**
 * <pre>
 *      author: shaojun
 *      e-mail: wusj@qtec.cn
 *      time: 2017/06/14
 *      desc:
 *      version: 1.1
 * </pre>
 */
public class ModifyPwdGetIdCodeActivity extends BaseActivity implements ModifyPwdGetIdCodeView {
  private ActivityModifyPwdGetIdCodeBinding mBinding;

  @Inject
  ModifyPwdGetIdCodePresenter modifyPwdGetIdCodePresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_modify_pwd_get_id_code);

    initializeInjector();

    initPresenter();

    initView();
  }

  private void initView() {
    initTitleBar("修改密码");
    mBinding.tvModifyPwdNumber.setText(MyStringUtil.addStar(userPhone()));
  }

  public void confirmModify(View view) {
    modifyPwdGetIdCodePresenter.getIdCode(userPhone());
  }

  private String userPhone() {
    return new SPUtils(PrefConstant.SP_NAME).getString(PrefConstant.SP_USER_PHONE);
  }

  private void initPresenter() {
    modifyPwdGetIdCodePresenter.setView(this);
  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
        .build()
        .inject(this);
  }

  @Override
  public void showUserPhoneEmp() {
    Toast.makeText(this, R.string.reset_pwd_toast_phone_emp, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showGetIdCodeSuccess() {
    Toast.makeText(getContext(), R.string.register_toast_get_id_code_success, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void openModifyPwd(ModifyPwdGetIdCodeResponse response) {
    mNavigator.navigateTo(getContext(), ModifyPwdActivity.class, null);
  }
}
