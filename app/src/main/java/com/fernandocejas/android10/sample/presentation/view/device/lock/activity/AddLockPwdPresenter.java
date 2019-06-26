package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.AddLockPwdRequest;
import com.qtec.mapp.model.rsp.AddLockPwdResponse;
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
public class AddLockPwdPresenter implements Presenter {

  private final UseCase addLockPwdUseCase;
  private AddLockPwdView mAddLockPwdView;

  @Inject
  public AddLockPwdPresenter(@Named(CloudUseCaseComm.ADD_LOCK_PWD) UseCase addLockPwdUseCase) {
    this.addLockPwdUseCase = checkNotNull(addLockPwdUseCase, "addLockPwdUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    addLockPwdUseCase.unsubscribe();
  }

  public void setView(AddLockPwdView addLockPwdView) {
    this.mAddLockPwdView = addLockPwdView;
  }

  void addLockPwd(String deviceId, String pwdName, String pwdId) {
    AddLockPwdRequest request = new AddLockPwdRequest();
    request.setDeviceSerialNo(deviceId);
    request.setPasswordName(pwdName);
    request.setPasswordSerialNo(pwdId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

//    mAddLockPwdView.showLoading();
    addLockPwdUseCase.execute(encryptInfo, new AppSubscriber<AddLockPwdResponse>(mAddLockPwdView) {


      @Override
      protected void doNext(AddLockPwdResponse response) {
        int anInt = 0;
        try {
          anInt = Integer.parseInt(pwdId);
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
        String hexPwdId = Integer.toHexString(anInt);
        int i = hexPwdId.length();
        if (i == 1) {
          hexPwdId = "000" + hexPwdId;
        } else if (i == 2) {
          hexPwdId = "00" + hexPwdId;
        }
        mAddLockPwdView.showAddLockPwdSuccess(hexPwdId);
      }
    });
  }

}
