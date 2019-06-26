package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.AddLockDoorCardRequest;
import com.qtec.mapp.model.rsp.AddLockDoorCardResponse;
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
public class AddLockDoorCardPresenter implements Presenter {

  private final UseCase addLockDoorCardUseCase;
  private AddLockDoorCardView mAddLockDoorCardView;

  @Inject
  public AddLockDoorCardPresenter(@Named(CloudUseCaseComm.ADD_LOCK_DOOR_CARD) UseCase addLockDoorCardUseCase) {
    this.addLockDoorCardUseCase = checkNotNull(addLockDoorCardUseCase, "addLockDoorCardUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    addLockDoorCardUseCase.unsubscribe();
  }

  public void setView(AddLockDoorCardView addLockDoorCardView) {
    this.mAddLockDoorCardView = addLockDoorCardView;
  }

  void addLockDoorCard(String deviceId, String doorCardName, String doorCardId) {
    AddLockDoorCardRequest request = new AddLockDoorCardRequest();
    request.setDeviceSerialNo(deviceId);
    request.setDoorcardName(doorCardName);
    request.setDoorcardSerialNo(doorCardId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

//    mAddLockFpView.showLoading();
    addLockDoorCardUseCase.execute(encryptInfo, new AppSubscriber<AddLockDoorCardResponse>(mAddLockDoorCardView) {


      @Override
      protected void doNext(AddLockDoorCardResponse response) {
        int anInt = 0;
        try {
          anInt = Integer.parseInt(doorCardId);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        String hexDoorCardId = Integer.toHexString(anInt);
        mAddLockDoorCardView.showAddLockDoorCardSuccess("00" + hexDoorCardId);
      }
    });
  }

}
