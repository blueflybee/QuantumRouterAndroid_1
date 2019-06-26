package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetTimerTaskRequest;
import com.qtec.router.model.req.GetWifiConfigRequest;
import com.qtec.router.model.req.SetRouterTimerRequest;
import com.qtec.router.model.rsp.GetTimerTaskResponse;
import com.qtec.router.model.rsp.GetWifiConfigResponse;
import com.qtec.router.model.rsp.SetRouterTimerResponse;

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
class RestartRouterPresenter implements Presenter {

  private final UseCase getTimerTaskUseCase;
  private final UseCase setRouterTimerUseCase;
  private RestartRouterView mRestartRouterView;

  @Inject
  public RestartRouterPresenter(@Named(RouterUseCaseComm.GET_TIMER_TASK) UseCase getTimerTaskUseCase,
                                @Named(RouterUseCaseComm.SET_ROUTER_TIMER) UseCase setRouterTimerUseCase) {
    this.getTimerTaskUseCase = checkNotNull(getTimerTaskUseCase, "getTimerTaskUseCase cannot be null!");
    this.setRouterTimerUseCase = checkNotNull(setRouterTimerUseCase, "setRouterTimerUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getTimerTaskUseCase.unsubscribe();
    setRouterTimerUseCase.unsubscribe();
  }

  public void setView(RestartRouterView restartRouterView) {
    this.mRestartRouterView = restartRouterView;
  }


  void getTimerTask() {
    GetTimerTaskRequest request = new GetTimerTaskRequest();
    request.setTasktype(2);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_GET_TIMER_TASK, RouterUrlPath.METHOD_GET);

    mRestartRouterView.showLoading();
    getTimerTaskUseCase.execute(multiEncryptInfo, new AppSubscriber<GetTimerTaskResponse>(mRestartRouterView) {

      @Override
      protected void doNext(GetTimerTaskResponse response) {
        mRestartRouterView.getTimerTaskSuccess(response);
      }
    });

  }

  void setRouterTimer(SetRouterTimerRequest request) {

    request.setTasktype(2);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_SET_ROUTER_TIMER, RouterUrlPath.METHOD_POST);

    mRestartRouterView.showLoading();
    setRouterTimerUseCase.execute(multiEncryptInfo, new AppSubscriber<SetRouterTimerResponse>(mRestartRouterView) {

      @Override
      protected void doNext(SetRouterTimerResponse response) {
        mRestartRouterView.setRouterTimerSuccess(response);
      }
    });

  }


}
