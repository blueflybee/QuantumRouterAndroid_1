package com.fernandocejas.android10.sample.presentation.view.login.forgetpwd;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ResetPwdGetIdCodeRequest;
import com.qtec.mapp.model.req.ResetPwdRequest;
import com.qtec.mapp.model.rsp.ResetPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ResetPwdResponse;

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
public class ResetPwdPresenter implements Presenter {

  private final UseCase resetPwdGetIdCodeUseCase;
  private final UseCase resetPwdUseCase;
  private ResetPwdView resetPwdView;

  @Inject
  public ResetPwdPresenter(@Named(CloudUseCaseComm.RESET_PWD_GET_ID_CODE) UseCase resetPwdGetIdCodeUseCase,
                           @Named(CloudUseCaseComm.RESET_PWD) UseCase resetPwdUseCase) {
    this.resetPwdGetIdCodeUseCase = checkNotNull(resetPwdGetIdCodeUseCase, "resetPwdGetIdCodeUseCase cannot be null!");
    this.resetPwdUseCase = checkNotNull(resetPwdUseCase, "resetPwdUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    resetPwdGetIdCodeUseCase.unsubscribe();
    resetPwdUseCase.unsubscribe();
  }

  public void setView(ResetPwdView resetPwdView) {
    this.resetPwdView = resetPwdView;
  }

  public void getIdCode(String username) {
    if (Strings.isNullOrEmpty(username)) {
      resetPwdView.showUserPhoneEmp();
      return;
    }

    ResetPwdGetIdCodeRequest request = new ResetPwdGetIdCodeRequest();
    request.setUserPhone(username);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    resetPwdView.showLoading();
    resetPwdGetIdCodeUseCase.execute(encryptInfo, new AppSubscriber<ResetPwdGetIdCodeResponse>(resetPwdView) {
      @Override
      protected void doNext(ResetPwdGetIdCodeResponse response) {
        resetPwdView.onGetIdCodeSuccess();
      }
    });
  }

  public void resetPwd(String username, String smsCode, String pwd) {
    if (Strings.isNullOrEmpty(username)) {
      resetPwdView.showUserPhoneEmp();
      return;
    }

    if (Strings.isNullOrEmpty(smsCode)) {
      resetPwdView.showIdCodeEmp();
      return;
    }

    if (Strings.isNullOrEmpty(pwd)) {
      resetPwdView.showPasswordEmp();
      return;
    }

    ResetPwdRequest request = new ResetPwdRequest();
    request.setUserPhone(username);
    request.setSmsCode(smsCode);
    request.setUserPassword(pwd);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    resetPwdView.showLoading();

    resetPwdUseCase.execute(encryptInfo, new AppSubscriber<ResetPwdResponse>(resetPwdView) {
      @Override
      protected void doNext(ResetPwdResponse response) {
        PrefConstant.setUserPwd(pwd);
        resetPwdView.showResetPwdSuccess();
      }
    });

  }
}
