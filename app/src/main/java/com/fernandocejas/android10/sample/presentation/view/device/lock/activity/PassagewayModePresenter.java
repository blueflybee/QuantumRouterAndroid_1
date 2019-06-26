package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.GetPassagewayModeRequest;
import com.qtec.mapp.model.req.ModifyPassagewayModeRequest;
import com.qtec.mapp.model.req.ModifyUnlockModeRequest;
import com.qtec.mapp.model.rsp.GetPassagewayModeResponse;
import com.qtec.mapp.model.rsp.GetUnlockModeResponse;
import com.qtec.mapp.model.rsp.ModifyPassagewayModeResponse;
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
class PassagewayModePresenter implements Presenter {

  private final UseCase getPassagewayModeUseCase;
  private final UseCase modifyPassagewayModeUseCase;
  private PassagewayModeView mPassagewayModeView;

  @Inject
  public PassagewayModePresenter(@Named(CloudUseCaseComm.GET_PASSAGEWAY_MODE) UseCase getPassagewayModeUseCase,
                                 @Named(CloudUseCaseComm.MODIFY_PASSAGEWAY_MODE) UseCase modifyPassagewayModeUseCase) {
    this.getPassagewayModeUseCase = checkNotNull(getPassagewayModeUseCase, "getPassagewayModeUseCase cannot be null!");
    this.modifyPassagewayModeUseCase = checkNotNull(modifyPassagewayModeUseCase, "modifyPassagewayModeUseCase cannot be null!");
  }

  @Override
  public void resume() {}

  @Override
  public void pause() {}

  @Override
  public void destroy() {
    getPassagewayModeUseCase.unsubscribe();
    modifyPassagewayModeUseCase.unsubscribe();
  }

  public void setView(PassagewayModeView view) {
    this.mPassagewayModeView = view;
  }


  void getPassagewayMode(String deviceSerialNo) {

    GetPassagewayModeRequest request = new GetPassagewayModeRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mPassagewayModeView.showLoading();
    getPassagewayModeUseCase.execute(encryptInfo, new AppSubscriber<GetPassagewayModeResponse>(mPassagewayModeView) {

      @Override
      protected void doNext(GetPassagewayModeResponse response) {
        mPassagewayModeView.showPassageway(response);
      }
    });

  }


  void modifyPassagewayMode(String deviceSerialNo, boolean passageway) {
    ModifyPassagewayModeRequest request = new ModifyPassagewayModeRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setChannelConfig(passageway ? GetPassagewayModeResponse.CHANNEL_OPEN : GetPassagewayModeResponse.CHANNEL_CLOSED);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mPassagewayModeView.showLoading();
    modifyPassagewayModeUseCase.execute(encryptInfo, new AppSubscriber<ModifyPassagewayModeResponse>(mPassagewayModeView) {

      @Override
      protected void doNext(ModifyPassagewayModeResponse response) {
        mPassagewayModeView.showModifyPassagewaySuccess();
      }
    });
  }
}
