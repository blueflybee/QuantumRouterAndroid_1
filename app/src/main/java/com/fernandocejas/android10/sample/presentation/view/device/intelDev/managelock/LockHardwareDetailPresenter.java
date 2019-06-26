package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;

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
public class LockHardwareDetailPresenter implements Presenter {

  private final UseCase lockDetailUseCase;
  private IGetLockDetailView lockHardwareDetailView;

  @Inject
  public LockHardwareDetailPresenter(
      @Named(CloudUseCaseComm.INTEL_DEVICE_DETAIL) UseCase lockDetailUseCase) {
    this.lockDetailUseCase = checkNotNull(lockDetailUseCase, "lockDetailUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    lockDetailUseCase.unsubscribe();
  }

  public void setView(IGetLockDetailView lockHardwareDetailView) {
    this.lockHardwareDetailView = lockHardwareDetailView;
  }

}
