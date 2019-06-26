package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.ModifyPropertyView;
import com.qtec.mapp.model.req.DeleteLockPwdRequest;
import com.qtec.mapp.model.req.ModifyDevNameRequest;
import com.qtec.mapp.model.req.ModifyLockPwdNameRequest;
import com.qtec.mapp.model.rsp.DeleteLockPwdResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;
import com.qtec.mapp.model.rsp.ModifyDevNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockPwdNameResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

import static android.R.attr.name;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class ModifyLockPwdPresenter implements Presenter {

  private final UseCase modifyLockPwdNameUseCase;
  private final UseCase deleteLockPwdUseCase;
  private ModifyLockPwdView mModifyLockPwdView;

  @Inject
  public ModifyLockPwdPresenter(@Named(CloudUseCaseComm.MODIFY_LOCK_PWD_NAME) UseCase modifyLockPwdNameUseCase,
                                @Named(CloudUseCaseComm.DELETE_LOCK_PWD) UseCase deleteLockPwdUseCase) {
    this.modifyLockPwdNameUseCase = checkNotNull(modifyLockPwdNameUseCase, "modifyLockPwdNameUseCase cannot be null!");
    this.deleteLockPwdUseCase = checkNotNull(deleteLockPwdUseCase, "deleteLockPwdUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyLockPwdNameUseCase.unsubscribe();
    deleteLockPwdUseCase.unsubscribe();
  }

  public void setView(ModifyLockPwdView modifyLockPwdView) {
    this.mModifyLockPwdView = modifyLockPwdView;
  }

  void modifyLockPwd(GetPasswordsResponse pwdResponse) {
    ModifyLockPwdNameRequest request = new ModifyLockPwdNameRequest();
    request.setDeviceSerialNo(pwdResponse.getDeviceSerialNo());
    request.setPasswordName(pwdResponse.getPasswordName());
    request.setPasswordSerialNo(pwdResponse.getPasswordSerialNo());

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyLockPwdView.showLoading();
    modifyLockPwdNameUseCase.execute(encryptInfo, new AppSubscriber<ModifyLockPwdNameResponse>(mModifyLockPwdView) {

      @Override
      protected void doNext(ModifyLockPwdNameResponse response) {
        mModifyLockPwdView.showModifyPwdNameSuccess();
      }
    });
  }

  void deleteLockPwd(String deviceSerialNo, String passwordSerialNo) {
    DeleteLockPwdRequest request = new DeleteLockPwdRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setPasswordSerialNo(passwordSerialNo);

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyLockPwdView.showLoading();
    deleteLockPwdUseCase.execute(encryptInfo, new AppSubscriber<DeleteLockPwdResponse>(mModifyLockPwdView) {

      @Override
      protected void doNext(DeleteLockPwdResponse response) {
        mModifyLockPwdView.showDeletePwdSuccess();
      }
    });
  }


}
