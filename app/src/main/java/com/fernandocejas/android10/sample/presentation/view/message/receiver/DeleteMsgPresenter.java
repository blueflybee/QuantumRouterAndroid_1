package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.mapp.model.rsp.DeleteMsgResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class DeleteMsgPresenter implements Presenter {

    private final UseCase deleteMsgUseCase;

    private IDeleteMsgView deleteMsgView;

    @Inject
    public DeleteMsgPresenter(@Named(CloudUseCaseComm.DELETE_MSG) UseCase deleteMsgUseCase) {
        this.deleteMsgUseCase = deleteMsgUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        deleteMsgUseCase.unsubscribe();
    }

    public void setView(IDeleteMsgView deleteMsgView) {
        this.deleteMsgView = deleteMsgView;
    }

    public void deleteMsg(IRequest request) {
        deleteMsgView.showLoading();
        deleteMsgUseCase.execute(request, new AppSubscriber<DeleteMsgResponse>(deleteMsgView) {
            @Override
            protected void doNext(DeleteMsgResponse dealInvitationResponse) {
                deleteMsgView.deleteMsg(dealInvitationResponse);
            }
        });
    }
}
