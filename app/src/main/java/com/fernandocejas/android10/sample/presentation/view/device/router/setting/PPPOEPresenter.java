package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.req.SetPPPOERequest;
import com.qtec.router.model.rsp.SetDHCPResponse;
import com.qtec.router.model.rsp.SetPPPOEResponse;

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
public class PPPOEPresenter implements Presenter {

  private final UseCase setPPPOEUseCase;
  private PPPOEView mPPPOEView;

  @Inject
  public PPPOEPresenter(@Named(RouterUseCaseComm.SET_PPPOE) UseCase setPPPOEUseCase) {
    this.setPPPOEUseCase = checkNotNull(setPPPOEUseCase, "setPPPOEUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    setPPPOEUseCase.unsubscribe();
  }

  public void setView(PPPOEView pppoeView) {
    this.mPPPOEView = pppoeView;
  }

  public void setPPPOE(String username, String pwd, int configed) {
    SetPPPOERequest request = new SetPPPOERequest();
    request.setConnectiontype("pppoe");
    request.setUsername(username);
    request.setPassword(pwd);
    request.setConfiged(configed);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_SET_PPPOE, RouterUrlPath.METHOD_POST);

    mPPPOEView.showLoading();
    setPPPOEUseCase.execute(multiEncryptInfo, new AppSubscriber<SetPPPOEResponse>(mPPPOEView) {

      @Override
      protected void doNext(SetPPPOEResponse response) {
        mPPPOEView.showSetPPPOESuccess(response);

      }
    });

  }

}
