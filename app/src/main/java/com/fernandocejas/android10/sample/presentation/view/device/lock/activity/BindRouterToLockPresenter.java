package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.QueryBindRouterToLockUseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.BindRouterToLockRequest;
import com.qtec.router.model.req.QueryBindRouterToLockRequest;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;

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
public class BindRouterToLockPresenter implements Presenter {

  private final UseCase bindRouterToLockUseCase;
  private final UseCase queryBindRouterToLockUseCase;
  private BindRouterToLockView bindRouterToLockView;

  @Inject
  public BindRouterToLockPresenter(
      @Named(RouterUseCaseComm.BIND_ROUTER_TO_LOCK) UseCase bindRouterToLockUseCase,
      @Named(RouterUseCaseComm.QUERY_BIND_ROUTER_TO_LOCK) UseCase queryBindRouterToLockUseCase) {
    this.bindRouterToLockUseCase = checkNotNull(bindRouterToLockUseCase, "bindRouterToLockUseCase cannot be null!");
    this.queryBindRouterToLockUseCase = checkNotNull(queryBindRouterToLockUseCase, "queryBindRouterToLockUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    bindRouterToLockUseCase.unsubscribe();
    queryBindRouterToLockUseCase.unsubscribe();
  }

  public void setView(BindRouterToLockView bindRouterToLockView) {
    this.bindRouterToLockView = bindRouterToLockView;
  }

  void bindRouter(String routerId, String lockId) {
    bindRouterToLockView.showLoading();
    BindRouterToLockRequest request = new BindRouterToLockRequest();
    request.setDevid(lockId);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, request, RouterUrlPath.PATH_BIND_ROUTER_TO_LOCK, RouterUrlPath.METHOD_POST);
    bindRouterToLockUseCase.execute(multiEncryptInfo, new AppSubscriber<BindRouterToLockResponse>(bindRouterToLockView) {
      @Override
      protected void doNext(BindRouterToLockResponse response) {
        bindRouterToLockView.showNotifyBindSuccess(response);
      }
    });

  }

  void queryBindRouter(String routerId, String lockId) {
    bindRouterToLockView.showLoading();
    QueryBindRouterToLockRequest request = new QueryBindRouterToLockRequest();
    request.setDevid(lockId);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, request, RouterUrlPath.PATH_QUERY_BIND_ROUTER_TO_LOCK, RouterUrlPath.METHOD_POST);

    QueryBindRouterToLockUseCase queryBindRouterToLockUseCase = (QueryBindRouterToLockUseCase) this.queryBindRouterToLockUseCase;
    queryBindRouterToLockUseCase.execute(multiEncryptInfo, new AppSubscriber<QueryBindRouterToLockResponse>(bindRouterToLockView) {


      @Override
      protected void doNext(QueryBindRouterToLockResponse response) {
        bindRouterToLockView.onQuery(response);
      }
    });
  }

}
