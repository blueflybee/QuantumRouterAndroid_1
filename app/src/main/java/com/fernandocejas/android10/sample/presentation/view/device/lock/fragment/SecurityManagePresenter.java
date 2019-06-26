package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.GetDoorCardsRequest;
import com.qtec.mapp.model.req.GetFingerprintsRequest;
import com.qtec.mapp.model.req.GetPasswordsRequest;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.List;

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
public class SecurityManagePresenter implements Presenter {

  private final UseCase getFingerprintsUseCase;
  private final UseCase getPasswordsUseCase;
  private final UseCase getDoorCardsUseCase;
  private SecurityManageView mSecurityManageView;

  @Inject
  public SecurityManagePresenter(@Named(CloudUseCaseComm.GET_FINGER_PRINTS) UseCase getFingerprintsUseCase,
                                 @Named(CloudUseCaseComm.GET_PASSWORDS) UseCase getPasswordsUseCase,
                                 @Named(CloudUseCaseComm.GET_DOOR_CARDS) UseCase getDoorCardsUseCase) {
    this.getFingerprintsUseCase = checkNotNull(getFingerprintsUseCase, "getFingerprintsUseCase cannot be null!");
    this.getPasswordsUseCase = checkNotNull(getPasswordsUseCase, "getPasswordsUseCase cannot be null!");
    this.getDoorCardsUseCase = checkNotNull(getDoorCardsUseCase, "getDoorCardsUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getFingerprintsUseCase.unsubscribe();
    getPasswordsUseCase.unsubscribe();
    getDoorCardsUseCase.unsubscribe();
  }

  public void setView(SecurityManageView view) {
    this.mSecurityManageView = view;
  }


  public void getFingerprints(String lockId) {
    GetFingerprintsRequest request = new GetFingerprintsRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mSecurityManageView.showLoading();
    getFingerprintsUseCase.execute(encryptInfo, new AppSubscriber<List<GetFingerprintsResponse>>(mSecurityManageView) {

      @Override
      protected void doNext(List<GetFingerprintsResponse> fingerprints) {
        if (fingerprints == null || fingerprints.isEmpty()) {
          mSecurityManageView.showNoFingerprints();
        } else {
          mSecurityManageView.showFingerprints(fingerprints);
        }
      }
    });

  }

  public void getPasswords(String lockId) {
    GetPasswordsRequest request = new GetPasswordsRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mSecurityManageView.showLoading();
    getPasswordsUseCase.execute(encryptInfo, new AppSubscriber<List<GetPasswordsResponse>>(mSecurityManageView) {

      @Override
      protected void doNext(List<GetPasswordsResponse> passwords) {
        if (passwords == null || passwords.isEmpty()) {
          mSecurityManageView.showNoPasswords();
        } else {
          mSecurityManageView.showPasswords(passwords);
        }
      }
    });

  }

  public void getDoorCards(String lockId) {
    GetDoorCardsRequest request = new GetDoorCardsRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mSecurityManageView.showLoading();
    getDoorCardsUseCase.execute(encryptInfo, new AppSubscriber<List<GetDoorCardsResponse>>(mSecurityManageView) {

      @Override
      protected void doNext(List<GetDoorCardsResponse> responses) {
        if (responses == null || responses.isEmpty()) {
          mSecurityManageView.showNoDoorCards();
        } else {
          mSecurityManageView.showDoorCards(responses);
        }
      }
    });

  }
}
