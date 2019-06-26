package com.fernandocejas.android10.sample.presentation.view.login.register;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.GetIdCodeRequest;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;

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
public class GetIdCodePresenter implements Presenter {

  private final UseCase getIdCodeUseCase;
  private GetIdCodeView getIdCodeView;

  @Inject
  public GetIdCodePresenter(@Named(CloudUseCaseComm.GET_ID_CODE) UseCase getIdCodeUseCase) {
    this.getIdCodeUseCase = getIdCodeUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getIdCodeUseCase.unsubscribe();
  }

  public void setView(GetIdCodeView getIdCodeView) {
    this.getIdCodeView = getIdCodeView;
  }

  public void getIdCode(String username) {
    if (Strings.isNullOrEmpty(username)) {
      getIdCodeView.showUserPhoneEmp();
      return;
    }

    GetIdCodeRequest request = new GetIdCodeRequest();
    request.setUserPhone(username);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    getIdCodeView.showLoading();
    getIdCodeUseCase.execute(encryptInfo, new AppSubscriber<GetIdCodeResponse>(getIdCodeView) {
      @Override
      protected void doNext(GetIdCodeResponse response) {
        getIdCodeView.showGetIdCodeSuccess();
        getIdCodeView.openRegister(response);
      }
    });
  }

}
