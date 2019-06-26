package com.fernandocejas.android10.sample.presentation.view.mine.aboutus;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.databinding.ActivityAboutUsBinding;
import com.fernandocejas.android10.sample.presentation.internal.di.components.DaggerMineComponent;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.view.activity.BaseActivity;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/08
 *      desc:
 *      version: 1.0
 * </pre>
 */

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
  private ActivityAboutUsBinding mAboutUsBinding;

 /*   @Inject
    GetAgreementPresenter mGetAgreementPresenter;*/
  // IGetAgreementView

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mAboutUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
    initTitleBar("关于我们");

    initializeInjector();
    initPresenter();
    initData();
  }

  private void initializeInjector() {
    DaggerMineComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .mineModule(new MineModule())
        .build()
        .inject(this);
  }

  private void initPresenter() {
        /*mGetAgreementPresenter.setView(this);*/
  }

  private void initData() {
    mAboutUsBinding.tvUserAgreement.setOnClickListener(this);
    mAboutUsBinding.tvSecretAgreement.setOnClickListener(this);

    mAboutUsBinding.tvContent.setText(getResources().getString(R.string.app_name) + "\n" + "为您营造安全舒适的家" + "\n" + "版本号:" + AppUtils.getAppVersionName());
    mAboutUsBinding.imgLogo.setBackground(getResources().getDrawable(R.drawable.bb_logo));
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_userAgreement:
        startActivity(new Intent(this, GetAgreementActivity.class));
        break;

      case R.id.tv_secretAgreement:
        startActivity(new Intent(this, GetSecretAgreementActivity.class));
        break;

      default:
        break;
    }
  }

  @Override
  public void onError(String message) {

    if (!TextUtils.isEmpty(message)) {
      Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

  }

  @Override
  public Context getContext() {
    return this;
  }

  /**
   * 用户协议
   *
   * @param
   * @return
   */
  /*  @Override
    public void openUserAgreement(GetAgreementResponse agreementResponse) {
        //有内容返回再进去
        Intent intent = new Intent();
        intent.putExtra(mNavigator.EXTR_AGREEMENT_CONTENT, agreementResponse.getStatuteContent());
        mNavigator.navigateTo(this, GetAgreementActivity.class, intent);
    }*/

  /**
   * 隐私协议
   *
   * @param
   * @return
   */
   /* @Override
    public void openUserSerectAgreement(GetAgreementResponse agreementResponse) {
        //有内容返回再进去
        Intent intent = new Intent();
        intent.putExtra(mNavigator.EXTR_SERECT_AGREEMENT_CONTENT, agreementResponse.getStatuteContent());
        mNavigator.navigateTo(this, GetSecretAgreementActivity.class, intent);
    }*/

  /*  @Override
    protected void onDestroy() {
        super.onDestroy();
        mGetAgreementPresenter.destroy();
    }*/
}
