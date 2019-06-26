package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetLockStatusRequest;
import com.qtec.router.model.rsp.GetLockStatusResponse;

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
public class LockStatusPresenter implements Presenter {

  private final UseCase lockStatusUseCase;
  private LockStatusView lockStatusView;

  @Inject
  public LockStatusPresenter(@Named(RouterUseCaseComm.LOCK_STATUS) UseCase lockStatusUseCase) {
    this.lockStatusUseCase = checkNotNull(lockStatusUseCase, "lockStatusUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    lockStatusUseCase.unsubscribe();
  }

  public void setView(LockStatusView lockStatusView) {
    this.lockStatusView = lockStatusView;
  }

  void getLockStatus(String routerId, String lockId) {

    GetLockStatusRequest request = new GetLockStatusRequest();
    request.setDevid(lockId);

    lockStatusView.showLoading();
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, request, RouterUrlPath.PATH_GET_LOCK_STATUS, RouterUrlPath.METHOD_POST);
    lockStatusUseCase.execute(multiEncryptInfo, new AppSubscriber<GetLockStatusResponse>(lockStatusView) {

      @Override
      protected void doNext(GetLockStatusResponse response) {
        lockStatusView.showLockStatus(response);
      }
    });
  }

}
