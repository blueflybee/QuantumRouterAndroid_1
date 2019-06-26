package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.GetUnlockModeRequest;
import com.qtec.mapp.model.req.ModifyUnlockModeRequest;
import com.qtec.mapp.model.rsp.GetUnlockModeResponse;
import com.qtec.mapp.model.rsp.ModifyUnlockModeResponse;
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
class UnlockModePresenter implements Presenter {

  private final UseCase getUnlockModeUseCase;
  private final UseCase modifyUnlockModeUseCase;
  private UnlockModeView mUnlockModeView;

  @Inject
  public UnlockModePresenter(@Named(CloudUseCaseComm.GET_UNLOCK_MODE) UseCase getUnlockModeUseCase,
                             @Named(CloudUseCaseComm.MODIFY_UNLOCK_MODE) UseCase modifyUnlockModeUseCase) {
    this.getUnlockModeUseCase = checkNotNull(getUnlockModeUseCase, "getUnlockModeUseCase cannot be null!");
    this.modifyUnlockModeUseCase = checkNotNull(modifyUnlockModeUseCase, "modifyUnlockModeUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getUnlockModeUseCase.unsubscribe();
    modifyUnlockModeUseCase.unsubscribe();
  }

  public void setView(UnlockModeView unlockModeView) {
    this.mUnlockModeView = unlockModeView;
  }


  void getUnlockMode(String deviceSerialNo) {

    GetUnlockModeRequest request = new GetUnlockModeRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mUnlockModeView.showLoading();
    getUnlockModeUseCase.execute(encryptInfo, new AppSubscriber<GetUnlockModeResponse>(mUnlockModeView) {

      @Override
      protected void doNext(GetUnlockModeResponse response) {
        mUnlockModeView.showUnlockMode(response);
      }
    });

  }


  void modifyUnlockMode(String deviceSerialNo, String fpOpenConfig, String pwdOpenConfig, String cardOpenConfig, String reqUrl) {
    ModifyUnlockModeRequest request = new ModifyUnlockModeRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setOpenConfig(fpOpenConfig);
    request.setPasswordOpenConfig(pwdOpenConfig);
    request.setDoorcardOpenConfig(cardOpenConfig);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);
    encryptInfo.setRequestUrl(reqUrl);

    mUnlockModeView.showLoading();
    modifyUnlockModeUseCase.execute(encryptInfo, new AppSubscriber<ModifyUnlockModeResponse>(mUnlockModeView) {

      @Override
      protected void doNext(ModifyUnlockModeResponse response) {

        mUnlockModeView.showModifyUnlockModeSuccess();
      }
    });
  }
}
