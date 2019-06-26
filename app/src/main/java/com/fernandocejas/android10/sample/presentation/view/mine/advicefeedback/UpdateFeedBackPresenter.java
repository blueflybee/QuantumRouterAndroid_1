package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetUpdateFeedBackResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class UpdateFeedBackPresenter implements Presenter {

    private final UseCase updateFeedbackUseCase;

    private IUpdateFeedBackView updateFeedBackView;

    @Inject
    public UpdateFeedBackPresenter(@Named(CloudUseCaseComm.ADVICE_UPDATE_FEED_BACK) UseCase updateFeedbackUseCase) {
        this.updateFeedbackUseCase = updateFeedbackUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        updateFeedbackUseCase.unsubscribe();
    }

    public void setView(IUpdateFeedBackView updateFeedBackView) {
        this.updateFeedBackView = updateFeedBackView;
    }

    public void updateFeedBack(IRequest request) {
        updateFeedBackView.showLoading();
        updateFeedbackUseCase.execute(request, new AppSubscriber<GetUpdateFeedBackResponse>(updateFeedBackView) {
            @Override
            protected void doNext(GetUpdateFeedBackResponse updateResponse) {
                updateFeedBackView.updateFeedback(updateResponse);
            }
        });
    }
}
