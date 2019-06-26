package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetNetModeRequest;
import com.qtec.router.model.rsp.GetNetModeResponse;

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
class NetModePresenter implements Presenter {

  private final UseCase getNetModeUseCase;
  private NetModeView mNetModeView;

  @Inject
  public NetModePresenter(@Named(RouterUseCaseComm.GET_NET_MODE) UseCase getNetModeUseCase) {
    this.getNetModeUseCase = checkNotNull(getNetModeUseCase, "getNetModeUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getNetModeUseCase.unsubscribe();
  }

  public void setView(NetModeView netModeView) {
    this.mNetModeView = netModeView;
  }

  public void getNetMode() {

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new GetNetModeRequest(), RouterUrlPath.PATH_GET_NET_MODE, RouterUrlPath.METHOD_GET);

    mNetModeView.showLoading();
    getNetModeUseCase.execute(multiEncryptInfo, new AppSubscriber<GetNetModeResponse>(mNetModeView) {

      @Override
      protected void doNext(GetNetModeResponse response) {
        mNetModeView.showNetMode(response);

      }
    });

  }

}
