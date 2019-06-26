package com.fernandocejas.android10.sample.presentation.view.device.router.tools.manageDevice;

import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.data.BleLock;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.utils.InputUtil;
import com.fernandocejas.android10.sample.presentation.view.activity.ModifyPropertyActivity;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.IntelDevInfoModifyResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyLockNameActivity extends ModifyPropertyActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    InputUtil.allowLetterNumberChinese(mBinding.etModifyProperty, 32);
  }

  @Override
  protected void modifyProperty() {
    super.modifyProperty();
    mModifyPropertyPresenter.modifyDevName(getModifyProperty(), getLock().getDeviceSerialNo());
  }

  @Override
  public void showDevNameEmp() {
    ToastUtils.showShort(R.string.dev_modify_toast_dev_name_emp);
  }

  @Override
  public void showModifySuccess(IntelDevInfoModifyResponse response) {
    finish();
  }

  @Override
  public void showModifyRouterNameSuccess() {
    ToastUtils.showShort("门锁名称修改成功");
    Intent intent = new Intent();
    intent.putExtra(Navigator.EXTRA_NEW_LOCK_NAME, getModifyProperty());
    setResult(RESULT_OK, intent);
    finish();
  }

  private GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> getLock() {
    return (GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>) getIntent().getSerializableExtra(Navigator.EXTRA_BLE_LOCK);
  }

}
