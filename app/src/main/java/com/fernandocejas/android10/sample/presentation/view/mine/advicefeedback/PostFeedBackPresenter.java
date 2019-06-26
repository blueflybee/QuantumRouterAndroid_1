package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetfeedBackResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class PostFeedBackPresenter implements Presenter {

    private final UseCase feedbackUseCase;

    private IPostFeedBackView feedBackView;

    @Inject
    public PostFeedBackPresenter(@Named(CloudUseCaseComm.ADVICE_FEED_BACK) UseCase feedbackUseCase) {
        this.feedbackUseCase = feedbackUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        feedbackUseCase.unsubscribe();
    }

    public void setView(IPostFeedBackView feedBackView) {
        this.feedBackView = feedBackView;
    }

    public void getFeedBack(IRequest request) {
        feedBackView.showLoading();
        feedbackUseCase.execute(request, new AppSubscriber<GetfeedBackResponse>(feedBackView) {
            @Override
            protected void doNext(GetfeedBackResponse feedbackResponse) {
                feedBackView.openFeedback(feedbackResponse);
            }
        });
    }
}
