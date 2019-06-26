package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import android.os.Handler;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetRouterFirstConfigRequest;
import com.qtec.router.model.rsp.GetRouterFirstConfigResponse;

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
public class GetRouterFirstConfigPresenter implements Presenter {

  private final UseCase getRouterFirstConfigUseCase;
  private GetRouterFirstConfigView mGetRouterFirstConfigView;

  private Handler mHandler = new Handler();

  @Inject
  public GetRouterFirstConfigPresenter(@Named(RouterUseCaseComm.GET_FIRST_CONFIG) UseCase getRouterFirstConfigUseCase) {
    this.getRouterFirstConfigUseCase = checkNotNull(getRouterFirstConfigUseCase, "getRouterFirstConfigUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getRouterFirstConfigUseCase.unsubscribe();
  }

  public void setView(GetRouterFirstConfigView getRouterFirstConfigView) {
    this.mGetRouterFirstConfigView = getRouterFirstConfigView;
  }

  public void getFirstConfig() {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new GetRouterFirstConfigRequest(), RouterUrlPath.PATH_GET_ROUTER_FIRST_CONFIG, RouterUrlPath.METHOD_GET);

//    mGetRouterFirstConfigView.showLoading();
    getRouterFirstConfigUseCase.execute(multiEncryptInfo, new AppSubscriber<GetRouterFirstConfigResponse>(mGetRouterFirstConfigView) {

      @Override
      public void onError(Throwable e) {
        //首次配置信息获取失败不需要用户感知
      }

      @Override
      protected void doNext(GetRouterFirstConfigResponse response) {
        if (response.getConfigured() != 0) return;
        mHandler.postDelayed(() -> mGetRouterFirstConfigView.startFirstConfig(response), 500);
      }
    });

  }

}
