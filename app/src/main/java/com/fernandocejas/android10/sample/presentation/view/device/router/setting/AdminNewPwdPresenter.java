package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetAdminPwdRequest;
import com.qtec.router.model.rsp.SetAdminPwdResponse;

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
class AdminNewPwdPresenter implements Presenter {

  private final UseCase setAdminPwdUseCase;
  private AdminNewPwdView mAdminNewPwdView;

  @Inject
  public AdminNewPwdPresenter(@Named(RouterUseCaseComm.SET_ADMIN_PWD) UseCase setAdminPwdUseCase) {
    this.setAdminPwdUseCase = checkNotNull(setAdminPwdUseCase, "setAdminPwdUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    setAdminPwdUseCase.unsubscribe();
  }

  public void setView(AdminNewPwdView adminNewPwdView) {
    this.mAdminNewPwdView = adminNewPwdView;
  }

  public void setAdminPwd(String pwd, String oldPwd) {
    SetAdminPwdRequest request = new SetAdminPwdRequest();
    request.setPassword(pwd);
    request.setOldpassword(oldPwd);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_SET_ADMIN_PWD, RouterUrlPath.METHOD_POST);

    mAdminNewPwdView.showLoading();
    setAdminPwdUseCase.execute(multiEncryptInfo, new AppSubscriber<SetAdminPwdResponse>(mAdminNewPwdView) {

      @Override
      protected void doNext(SetAdminPwdResponse response) {
        mAdminNewPwdView.setAdminPwdSuccess(response);
      }
    });

  }

}
