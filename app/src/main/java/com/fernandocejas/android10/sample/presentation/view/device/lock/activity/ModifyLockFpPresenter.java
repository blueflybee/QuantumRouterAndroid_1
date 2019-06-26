package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.DeleteLockFpRequest;
import com.qtec.mapp.model.req.ModifyLockFpNameRequest;
import com.qtec.mapp.model.rsp.DeleteLockFpResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.ModifyLockFpNameResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

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
public class ModifyLockFpPresenter implements Presenter {

  private final UseCase modifyLockFpNameUseCase;
  private final UseCase deleteLockFpUseCase;
  private ModifyLockFpView mModifyLockFpView;

  @Inject
  public ModifyLockFpPresenter(@Named(CloudUseCaseComm.MODIFY_LOCK_FP_NAME) UseCase modifyLockFpNameUseCase,
                               @Named(CloudUseCaseComm.DELETE_LOCK_FP) UseCase deleteLockFpUseCase) {
    this.modifyLockFpNameUseCase = checkNotNull(modifyLockFpNameUseCase, "modifyLockFpNameUseCase cannot be null!");
    this.deleteLockFpUseCase = checkNotNull(deleteLockFpUseCase, "deleteLockFpUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyLockFpNameUseCase.unsubscribe();
    deleteLockFpUseCase.unsubscribe();
  }

  public void setView(ModifyLockFpView modifyLockFpView) {
    this.mModifyLockFpView = modifyLockFpView;
  }

  void modifyLockFp(GetFingerprintsResponse fpResponse) {
    ModifyLockFpNameRequest request = new ModifyLockFpNameRequest();
    request.setFingerprintName(fpResponse.getFingerprintName());
    request.setFingerprintSerialNo(fpResponse.getFingerprintSerialNo());
    request.setDeviceSerialNo(fpResponse.getDeviceSerialNo());

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyLockFpView.showLoading();
    modifyLockFpNameUseCase.execute(encryptInfo, new AppSubscriber<ModifyLockFpNameResponse>(mModifyLockFpView) {

      @Override
      protected void doNext(ModifyLockFpNameResponse response) {
        mModifyLockFpView.showModifyFpNameSuccess();
      }
    });
  }

  void deleteLockFp(String deviceSerialNo, String fingerprintSerialNo) {
    DeleteLockFpRequest request = new DeleteLockFpRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setFingerprintSerialNo(fingerprintSerialNo);

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyLockFpView.showLoading();
    deleteLockFpUseCase.execute(encryptInfo, new AppSubscriber<DeleteLockFpResponse>(mModifyLockFpView) {

      @Override
      protected void doNext(DeleteLockFpResponse response) {
        mModifyLockFpView.showDeleteFpSuccess();
      }
    });
  }


}
