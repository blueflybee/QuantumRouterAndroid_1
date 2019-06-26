package com.fernandocejas.android10.sample.presentation.view.device.intelDev.addIntelDev;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddIntelDevVerifyRequest;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class AddIntelDevVerifyPresenter implements Presenter {

  private final UseCase addIntelDevVerifyUseCase;
  private AddIntelDevVerifyView addIntelDevVerifyView;

  @Inject
  public AddIntelDevVerifyPresenter(@Named(RouterUseCaseComm.ADD_INTEL_DEV_VERIFY) UseCase addIntelDevVerifyUseCase) {
    this.addIntelDevVerifyUseCase = checkNotNull(addIntelDevVerifyUseCase, "addIntelDevVerifyUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    addIntelDevVerifyUseCase.unsubscribe();
  }

  public void setView(AddIntelDevVerifyView addRouterVerifyView) {
    this.addIntelDevVerifyView = addRouterVerifyView;
  }

  public void addIntelDevVerify(QtecMultiEncryptInfo request, String pwd) {
    if (Strings.isNullOrEmpty(pwd)) {
      addIntelDevVerifyView.showAdminPwdEmp();
      return;
    }
    addIntelDevVerifyView.showLoading();
    addIntelDevVerifyUseCase.execute(request, new AppSubscriber<AddIntelDevVerifyResponse>(addIntelDevVerifyView) {
      @Override
      protected void doNext(AddIntelDevVerifyResponse response) {
        if (response != null) {
          addIntelDevVerifyView.onAddSuccess(response);
        }
      }
    });

  }

}
