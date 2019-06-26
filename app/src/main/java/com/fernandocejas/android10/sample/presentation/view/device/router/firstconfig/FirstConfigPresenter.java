package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.router.model.req.FirstConfigRequest;
import com.qtec.router.model.rsp.FirstConfigResponse;

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
public class FirstConfigPresenter implements Presenter {

  private final UseCase firstConfigUseCase;
  private FirstConfigView mFirstConfigView;

  @Inject
  public FirstConfigPresenter(@Named(RouterUseCaseComm.FIRST_CONFIG) UseCase firstConfigUseCase) {
    this.firstConfigUseCase = checkNotNull(firstConfigUseCase, "firstConfigUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    firstConfigUseCase.unsubscribe();
  }

  public void setView(FirstConfigView firstConfigView) {
    this.mFirstConfigView = firstConfigView;
  }

  void firstConfig(String ssid, String key, String password) {
    FirstConfigRequest request = new FirstConfigRequest();
    request.setSsid(ssid);
    request.setKey(key);
    request.setPassword(password);

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mFirstConfigView.showLoading();
    firstConfigUseCase.execute(encryptInfo, new AppSubscriber<FirstConfigResponse>(mFirstConfigView) {

      @Override
      protected void doNext(FirstConfigResponse response) {
        mFirstConfigView.showFirstConfigSuccess(response);
      }
    });

  }

}
