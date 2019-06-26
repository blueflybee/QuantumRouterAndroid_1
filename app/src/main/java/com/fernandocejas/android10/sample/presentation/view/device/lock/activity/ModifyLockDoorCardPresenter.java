package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.DeleteLockDoorCardRequest;
import com.qtec.mapp.model.req.ModifyLockDoorCardNameRequest;
import com.qtec.mapp.model.rsp.DeleteLockDoorCardResponse;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.ModifyLockDoorCardNameResponse;
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
public class ModifyLockDoorCardPresenter implements Presenter {

  private final UseCase modifyLockDoorCardNameUseCase;
  private final UseCase deleteLockDoorCardUseCase;
  private ModifyLockDoorCardView mModifyLockDoorCardView;

  @Inject
  public ModifyLockDoorCardPresenter(@Named(CloudUseCaseComm.MODIFY_LOCK_DOOR_CARD_NAME) UseCase modifyLockDoorCardNameUseCase,
                                     @Named(CloudUseCaseComm.DELETE_LOCK_DOOR_CARD) UseCase deleteLockDoorCardUseCase) {
    this.modifyLockDoorCardNameUseCase = checkNotNull(modifyLockDoorCardNameUseCase, "modifyLockDoorCardNameUseCase cannot be null!");
    this.deleteLockDoorCardUseCase = checkNotNull(deleteLockDoorCardUseCase, "deleteLockDoorCardUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyLockDoorCardNameUseCase.unsubscribe();
    deleteLockDoorCardUseCase.unsubscribe();
  }

  public void setView(ModifyLockDoorCardView modifyLockDoorCardView) {
    this.mModifyLockDoorCardView = modifyLockDoorCardView;
  }

  void modifyLockDoorCard(GetDoorCardsResponse doorCardsResponse) {
    ModifyLockDoorCardNameRequest request = new ModifyLockDoorCardNameRequest();
    request.setDoorcardName(doorCardsResponse.getDoorcardName());
    request.setDoorcardSerialNo(doorCardsResponse.getDoorcardSerialNo());
    request.setDeviceSerialNo(doorCardsResponse.getDeviceSerialNo());

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyLockDoorCardView.showLoading();
    modifyLockDoorCardNameUseCase.execute(encryptInfo, new AppSubscriber<ModifyLockDoorCardNameResponse>(mModifyLockDoorCardView) {

      @Override
      protected void doNext(ModifyLockDoorCardNameResponse response) {
        mModifyLockDoorCardView.showModifyDoorCardNameSuccess();
      }
    });
  }

  void deleteLockDoorCard(String deviceSerialNo, String doorCardId) {
    DeleteLockDoorCardRequest request = new DeleteLockDoorCardRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setDoorcardSerialNo(doorCardId);

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyLockDoorCardView.showLoading();
    deleteLockDoorCardUseCase.execute(encryptInfo, new AppSubscriber<DeleteLockDoorCardResponse>(mModifyLockDoorCardView) {

      @Override
      protected void doNext(DeleteLockDoorCardResponse response) {
        mModifyLockDoorCardView.showDeleteDoorCardSuccess();
      }
    });
  }


}
