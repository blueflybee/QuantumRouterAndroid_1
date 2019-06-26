package com.fernandocejas.android10.sample.presentation.view.device.router.tools.manageDevice;

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
public class IntelDevInfoModifyPresenter implements Presenter {

  private final UseCase intelDevInfoModifyUseCase;
  private IntelDevInfoModifyView intelDevInfoModifyView;

  @Inject
  public IntelDevInfoModifyPresenter(
      @Named(CloudUseCaseComm.INTEL_DEVICE_MODIFY) UseCase intelDevInfoModifyUseCase) {
    this.intelDevInfoModifyUseCase = checkNotNull(intelDevInfoModifyUseCase, "intelDevInfoModifyUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    intelDevInfoModifyUseCase.unsubscribe();
  }

  public void setView(IntelDevInfoModifyView intelDevInfoModifyView) {
    this.intelDevInfoModifyView = intelDevInfoModifyView;
  }

}
