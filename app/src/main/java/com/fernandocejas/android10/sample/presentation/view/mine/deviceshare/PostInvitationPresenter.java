package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.view.mine.aboutus.IGetAgreementView;
import com.qtec.mapp.model.rsp.GetAgreementResponse;
import com.qtec.mapp.model.rsp.PostInvitationResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class PostInvitationPresenter implements Presenter {

    private final UseCase invitationUseCase;

    private IPostInvitationView invitationView;

    @Inject
    public PostInvitationPresenter(@Named(CloudUseCaseComm.INVITATION) UseCase invitationUseCase) {
        this.invitationUseCase = invitationUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        invitationUseCase.unsubscribe();
    }

    public void setView(IPostInvitationView invitationView) {
        this.invitationView = invitationView;
    }

    public void postInvitation(IRequest request) {
        invitationView.showLoading();
        invitationUseCase.execute(request, new AppSubscriber<PostInvitationResponse>(invitationView) {
            @Override
            protected void doNext(PostInvitationResponse invitationResponse) {
                invitationView.postInvitation(invitationResponse);
            }
        });
    }
}
