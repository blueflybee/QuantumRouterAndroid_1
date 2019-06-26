package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.ModifyPropertyActivity;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyLiteGwNameActivity extends ModifyPropertyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    InputUtil.allowLetterNumberChinese(mBinding.etModifyProperty, 32);
  }

  @Override
  protected void modifyProperty() {
    super.modifyProperty();
    mModifyPropertyPresenter.modifyDevName(getModifyProperty(), gwId());
  }

  @Override
  public void showModifyRouterNameSuccess() {
    ToastUtils.showShort("LITE网关名称修改成功");
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_FOR_RESULT_DATA, getModifyProperty());
    setResult(RESULT_OK, intent);
    finish();
  }

  private String gwId() {
    return getIntent().getStringExtra(Navigator.EXTRA_ROUTER_ID);
  }
}
