package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ModifyPwdGetIdCodeRequest;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;

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
public class ModifyPwdGetIdCodePresenter implements Presenter {

  private final UseCase modifyPwdGetIdCodeUseCase;
  private ModifyPwdGetIdCodeView modifyPwdGetIdCodeView;

  @Inject
  public ModifyPwdGetIdCodePresenter(@Named(CloudUseCaseComm.MODIFY_PWD_GET_ID_CODE) UseCase modifyPwdGetIdCodeUseCase) {
    this.modifyPwdGetIdCodeUseCase = modifyPwdGetIdCodeUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyPwdGetIdCodeUseCase.unsubscribe();
  }

  public void setView(ModifyPwdGetIdCodeView getIdCodeView) {
    this.modifyPwdGetIdCodeView = getIdCodeView;
  }

  public void getIdCode(String username) {
    if (Strings.isNullOrEmpty(username)) {
      modifyPwdGetIdCodeView.showUserPhoneEmp();
      return;
    }

    ModifyPwdGetIdCodeRequest request = new ModifyPwdGetIdCodeRequest();
    request.setUserPhone(username);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    modifyPwdGetIdCodeView.showLoading();

    modifyPwdGetIdCodeUseCase.execute(encryptInfo, new AppSubscriber<ModifyPwdGetIdCodeResponse>(modifyPwdGetIdCodeView) {
      @Override
      protected void doNext(ModifyPwdGetIdCodeResponse response) {
        modifyPwdGetIdCodeView.showGetIdCodeSuccess();
        modifyPwdGetIdCodeView.openModifyPwd(response);
      }
    });
  }

}
