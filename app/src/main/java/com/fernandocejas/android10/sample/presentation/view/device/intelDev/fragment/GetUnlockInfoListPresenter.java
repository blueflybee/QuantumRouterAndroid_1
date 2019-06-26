package com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.view.mine.aboutus.IGetAgreementView;
import com.qtec.mapp.model.rsp.GetAgreementResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetUnlockInfoListPresenter implements Presenter {

  private final UseCase lockNoteListUseCase;
  private final UseCase exceptionWarningUseCase;

  private IGetUnlockInfoListView unlocInfoView;

  @Inject
  public GetUnlockInfoListPresenter(@Named(CloudUseCaseComm.LOCK_NOTE_LIST) UseCase lockNoteListUseCase,
                                    @Named(CloudUseCaseComm.LOCK_EXCEPTION_WARNING_LIST) UseCase exceptionWarningUseCase) {
    this.lockNoteListUseCase = lockNoteListUseCase;
    this.exceptionWarningUseCase = exceptionWarningUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    lockNoteListUseCase.unsubscribe();
    exceptionWarningUseCase.unsubscribe();
  }

  public void setView(IGetUnlockInfoListView unlocInfoView) {
    this.unlocInfoView = unlocInfoView;
  }

  public void getLockUseNoteList(IRequest request) {
    unlocInfoView.showLoading();
    lockNoteListUseCase.execute(request, new AppSubscriber<List<GetUnlockInfoListResponse>>(unlocInfoView) {
      @Override
      protected void doNext(List<GetUnlockInfoListResponse> unlockInfoResponse) {
        unlocInfoView.openLockUseNoteList(unlockInfoResponse);
      }
    });
  }

  public void getExceptionWarningList(IRequest request) {
    unlocInfoView.showLoading();
    exceptionWarningUseCase.execute(request, new AppSubscriber<List<GetUnlockInfoListResponse>>(unlocInfoView) {
      @Override
      protected void doNext(List<GetUnlockInfoListResponse> unlockInfoResponse) {
        unlocInfoView.openExceptionWarningList(unlockInfoResponse);
      }
    });
  }
}
