package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CloudUnbindRouterLock;
import com.fernandocejas.android10.sample.domain.interactor.router.QueryBindRouterToLockUseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.CloudUnbindRouterLockRequest;
import com.qtec.mapp.model.rsp.CloudUnbindRouterLockResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.QueryBindRouterToLockRequest;
import com.qtec.router.model.req.UnbindRouterToLockRequest;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;
import com.qtec.router.model.rsp.UnbindRouterToLockResponse;

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
public class LockBindRouterInfoPresenter implements Presenter {

  private final UseCase unbindRouterToLockUseCase;
  private final UseCase queryUnbindRouterToLockUseCase;
  private final CloudUnbindRouterLock cloudUnbindRouterLock;
  private LockBindRouterInfoView mLockBindRouterInfoView;

  @Inject
  public LockBindRouterInfoPresenter(
      @Named(RouterUseCaseComm.UNBIND_ROUTER_TO_LOCK) UseCase unbindRouterToLockUseCase,
      @Named(RouterUseCaseComm.QUERY_BIND_ROUTER_TO_LOCK) UseCase queryUnbindRouterToLockUseCase,
      @Named(RouterUseCaseComm.CLOUD_UNBIND_ROUTER_LOCK) CloudUnbindRouterLock cloudUnbindRouterLock) {
    this.unbindRouterToLockUseCase = checkNotNull(unbindRouterToLockUseCase, "unbindRouterToLockUseCase cannot be null!");
    this.queryUnbindRouterToLockUseCase = checkNotNull(queryUnbindRouterToLockUseCase, "queryUnbindRouterToLockUseCase cannot be null!");
    this.cloudUnbindRouterLock = checkNotNull(cloudUnbindRouterLock, "cloudUnbindRouterLock cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    unbindRouterToLockUseCase.unsubscribe();
    queryUnbindRouterToLockUseCase.unsubscribe();
  }

  public void setView(LockBindRouterInfoView lockBindRouterInfoView) {
    this.mLockBindRouterInfoView = lockBindRouterInfoView;
  }

  public void unbindRouter(String routerId, String lockId) {
    mLockBindRouterInfoView.showLoading();
    UnbindRouterToLockRequest request = new UnbindRouterToLockRequest();
    request.setDevid(lockId);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, request, RouterUrlPath.PATH_UNBIND_ROUTER_TO_LOCK, RouterUrlPath.METHOD_POST);
    unbindRouterToLockUseCase.execute(multiEncryptInfo, new AppSubscriber<UnbindRouterToLockResponse>(mLockBindRouterInfoView) {

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        mLockBindRouterInfoView.showUnbindFail(null);
      }

      @Override
      protected void doNext(UnbindRouterToLockResponse response) {
        mLockBindRouterInfoView.showNotifyUnbindSuccess(response);
      }
    });

  }

  public void queryUnbindRouter(String routerId, String lockId) {
    mLockBindRouterInfoView.showLoading();
    QueryBindRouterToLockRequest request = new QueryBindRouterToLockRequest();
    request.setDevid(lockId);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, request, RouterUrlPath.PATH_QUERY_BIND_ROUTER_TO_LOCK, RouterUrlPath.METHOD_POST);
    QueryBindRouterToLockUseCase queryBindRouterToLockUseCase = (QueryBindRouterToLockUseCase) this.queryUnbindRouterToLockUseCase;
    queryBindRouterToLockUseCase.execute(multiEncryptInfo, new AppSubscriber<QueryBindRouterToLockResponse>(mLockBindRouterInfoView) {
      @Override
      protected void doNext(QueryBindRouterToLockResponse response) {
        if (response.getContained() == 0) {
          mLockBindRouterInfoView.showUnbindSuccess(response);
        } else {
          mLockBindRouterInfoView.showUnbindFail(response);
        }
      }
    });
  }

  void unbindRouterCloud(String lockId) {
    mLockBindRouterInfoView.showLoading();
    CloudUnbindRouterLockRequest request = new CloudUnbindRouterLockRequest();
    request.setDeviceSerialNo(lockId);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);
    cloudUnbindRouterLock.execute(encryptInfo, new AppSubscriber<CloudUnbindRouterLockResponse>(mLockBindRouterInfoView) {
      @Override
      protected void doNext(CloudUnbindRouterLockResponse response) {
        mLockBindRouterInfoView.showCloudUnbindSuccess(response);
      }
    });

  }
}
