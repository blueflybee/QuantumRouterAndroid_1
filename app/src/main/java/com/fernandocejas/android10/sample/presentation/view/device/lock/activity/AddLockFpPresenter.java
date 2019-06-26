package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.AddLockFpRequest;
import com.qtec.mapp.model.rsp.AddLockFpResponse;
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
public class AddLockFpPresenter implements Presenter {

  private final UseCase addLockFpUseCase;
  private AddLockFpView mAddLockFpView;

  @Inject
  public AddLockFpPresenter(@Named(CloudUseCaseComm.ADD_LOCK_FP) UseCase addLockFpUseCase) {
    this.addLockFpUseCase = checkNotNull(addLockFpUseCase, "addLockFpUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    addLockFpUseCase.unsubscribe();
  }

  public void setView(AddLockFpView addLockFpView) {
    this.mAddLockFpView = addLockFpView;
  }

  void addLockFp(String deviceId, String fpName, String fpId) {
    AddLockFpRequest request = new AddLockFpRequest();
    request.setDeviceSerialNo(deviceId);
    request.setFingerprintName(fpName);
    request.setFingerprintSerialNo(fpId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

//    mAddLockFpView.showLoading();
    addLockFpUseCase.execute(encryptInfo, new AppSubscriber<AddLockFpResponse>(mAddLockFpView) {


      @Override
      protected void doNext(AddLockFpResponse response) {
        int anInt = 0;
        try {
          anInt = Integer.parseInt(fpId);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        String hexFpId = Integer.toHexString(anInt);
        mAddLockFpView.showAddLockFpSuccess("00" + hexFpId);
      }
    });
  }

}
