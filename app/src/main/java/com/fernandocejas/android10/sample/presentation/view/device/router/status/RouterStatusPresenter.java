package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.rsp.RouterStatusResponse;

import java.util.List;

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
public class RouterStatusPresenter implements Presenter {

  private final UseCase routerStatusUseCase;
  private RouterStatusView routerStatusView;

  @Inject
  public RouterStatusPresenter(@Named(RouterUseCaseComm.ROUTER_STATUS) UseCase routerStatusUseCase) {
    this.routerStatusUseCase = checkNotNull(routerStatusUseCase, "routerStatusUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    routerStatusUseCase.unsubscribe();
  }

  public void setView(RouterStatusView routerStatusView) {
    this.routerStatusView = routerStatusView;
  }

  public void getRouterStatus(String routerId) {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, new RouterStatusRequest(), RouterUrlPath.PATH_GET_ROUTER_STATUS, RouterUrlPath.METHOD_POST);
    //routerStatusView.showLoading();
    routerStatusUseCase.execute(multiEncryptInfo,
        new AppSubscriber<RouterStatusResponse<List<RouterStatusResponse.Status>>>(routerStatusView) {
          @Override
          protected void doNext(RouterStatusResponse<List<RouterStatusResponse.Status>> response) {
            routerStatusView.showRouterStatus(response);
          }
        });
  }

}
