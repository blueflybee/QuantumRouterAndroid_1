package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import android.os.Bundle;
import android.view.View;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
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
public class ModifyRouterNameActivity extends ModifyPropertyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    InputUtil.allowLetterNumberChinese(mBinding.etModifyProperty, 32);

    mTitleBar.setRightAs("完成", new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        modifyProperty();

      }
    });

  }

  @Override
  protected void modifyProperty() {
    super.modifyProperty();
    mModifyPropertyPresenter.modifyDevName(getModifyProperty(), GlobleConstant.getgDeviceId());
  }

  @Override
  public void showModifyRouterNameSuccess() {
    super.showModifyRouterNameSuccess();
    finish();
  }
}
