package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.mapp.model.rsp.GetMsgDetailResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class DealInvitationPresenter implements Presenter {

    private final UseCase dealInvitationUseCase;

    private IDealInvitationView dealInvitationView;

    @Inject
    public DealInvitationPresenter(@Named(CloudUseCaseComm.DEAL_INVITATION) UseCase dealInvitationUseCase) {
        this.dealInvitationUseCase = dealInvitationUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        dealInvitationUseCase.unsubscribe();
    }

    public void setView(IDealInvitationView dealInvitationView) {
        this.dealInvitationView = dealInvitationView;
    }

    public void dealInvitation(IRequest request) {
        dealInvitationView.showLoading();
        dealInvitationUseCase.execute(request, new AppSubscriber<DealInvitationResponse>(dealInvitationView) {
            @Override
            protected void doNext(DealInvitationResponse dealInvitationResponse) {
                dealInvitationView.dealInvitation(dealInvitationResponse);
            }
        });
    }
}
