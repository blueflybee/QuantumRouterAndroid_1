package com.fernandocejas.android10.sample.presentation.view.login.forgetpwd;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ResetPwdGetIdCodeRequest;
import com.qtec.mapp.model.rsp.ResetPwdGetIdCodeResponse;

import javax.inject.Inject;
import javax.inject.Named;

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
public class ResetPwdGetIdCodePresenter implements Presenter {

  private final UseCase resetPwdGetIdCodeUseCase;
  private ResetPwdGetIdCodeView resetPwdGetIdCodeView;

  @Inject
  public ResetPwdGetIdCodePresenter(@Named(CloudUseCaseComm.RESET_PWD_GET_ID_CODE) UseCase resetPwdGetIdCodeUseCase) {
    this.resetPwdGetIdCodeUseCase = resetPwdGetIdCodeUseCase;
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
  }

  public void setView(ResetPwdGetIdCodeView getIdCodeView) {
    this.resetPwdGetIdCodeView = getIdCodeView;
  }

  public void getIdCode(String username) {
    if (Strings.isNullOrEmpty(username)) {
      resetPwdGetIdCodeView.showUserPhoneEmp();
      return;
    }
    ResetPwdGetIdCodeRequest request = new ResetPwdGetIdCodeRequest();
    request.setUserPhone(username);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    resetPwdGetIdCodeView.showLoading();

    resetPwdGetIdCodeUseCase.execute(encryptInfo, new AppSubscriber<ResetPwdGetIdCodeResponse>(resetPwdGetIdCodeView) {
      @Override
      protected void doNext(ResetPwdGetIdCodeResponse response) {
        resetPwdGetIdCodeView.showGetIdCodeSuccess();
        resetPwdGetIdCodeView.openResetPwd(response);
      }
    });
  }

}
