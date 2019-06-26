package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.ModifyPwdGetIdCodeRequest;
import com.qtec.mapp.model.req.ModifyPwdRequest;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ModifyPwdResponse;

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
public class ModifyPwdPresenter implements Presenter {

  private final UseCase modifyPwdGetIdCodeUseCase;
  private final UseCase modifyPwdUseCase;
  private ModifyPwdView modifyPwdView;

  @Inject
  public ModifyPwdPresenter(@Named(CloudUseCaseComm.MODIFY_PWD_GET_ID_CODE) UseCase modifyPwdGetIdCodeUseCase,
                            @Named(CloudUseCaseComm.MODIFY_PWD) UseCase modifyPwdUseCase) {
    this.modifyPwdGetIdCodeUseCase = checkNotNull(modifyPwdGetIdCodeUseCase, "modifyPwdGetIdCodeUseCase cannot be null!");
    this.modifyPwdUseCase = checkNotNull(modifyPwdUseCase, "modifyPwdUseCase cannot be null!");
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
    modifyPwdUseCase.unsubscribe();
  }

  public void setView(ModifyPwdView modifyPwdView) {
    this.modifyPwdView = modifyPwdView;
  }

  public void getIdCode(QtecEncryptInfo<ModifyPwdGetIdCodeRequest> encryptInfo) {
    String userPhone = encryptInfo.getData().getUserPhone();
    if (Strings.isNullOrEmpty(userPhone)) {
      modifyPwdView.showUserPhoneEmp();
      return;
    }
    modifyPwdView.showLoading();

    modifyPwdGetIdCodeUseCase.execute(encryptInfo, new AppSubscriber<ModifyPwdGetIdCodeResponse>(modifyPwdView) {
      @Override
      protected void doNext(ModifyPwdGetIdCodeResponse response) {
        modifyPwdView.onGetIdCodeSuccess();
      }
    });
  }

  public void modifyPwd(String username, String smsCode, String pwd) {
    if (Strings.isNullOrEmpty(username)) {
      modifyPwdView.showUserPhoneEmp();
      return;
    }

    if (Strings.isNullOrEmpty(smsCode)) {
      modifyPwdView.showIdCodeEmp();
      return;
    }

    if (Strings.isNullOrEmpty(pwd)) {
      modifyPwdView.showPasswordEmp();
      return;
    }

    ModifyPwdRequest request = new ModifyPwdRequest();
    request.setUserPhone(username);
    request.setSmsCode(smsCode);
    request.setUserPassword(pwd);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    modifyPwdView.showLoading();


    modifyPwdUseCase.execute(encryptInfo, new AppSubscriber<ModifyPwdResponse>(modifyPwdView) {
      @Override
      protected void doNext(ModifyPwdResponse response) {
        PrefConstant.setUserPwd(pwd);
        modifyPwdView.showModifyPwdSuccess();
      }
    });

  }
}
