package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.CheckAdminPwdRequest;
import com.qtec.router.model.rsp.CheckAdminPwdResponse;

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
class AdminPwdPresenter implements Presenter {

  private final UseCase checkAdminPwdUseCase;
  private AdminPwdView mAdminPwdView;

  @Inject
  public AdminPwdPresenter(@Named(RouterUseCaseComm.CHECK_ADMIN_PWD) UseCase checkAdminPwdUseCase) {
    this.checkAdminPwdUseCase = checkNotNull(checkAdminPwdUseCase, "checkAdminPwdUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    checkAdminPwdUseCase.unsubscribe();
  }

  public void setView(AdminPwdView adminPwdView) {
    this.mAdminPwdView = adminPwdView;
  }

  public void checkAdminPwd(String pwd) {
    CheckAdminPwdRequest request = new CheckAdminPwdRequest();
    request.setPassword(pwd);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_CHECK_ADMIN_PWD, RouterUrlPath.METHOD_POST);

    mAdminPwdView.showLoading();
    checkAdminPwdUseCase.execute(multiEncryptInfo, new AppSubscriber<CheckAdminPwdResponse>(mAdminPwdView) {

      @Override
      protected void doNext(CheckAdminPwdResponse response) {
        mAdminPwdView.checkAdminPwdSuccess(response);

      }
    });

  }

}
