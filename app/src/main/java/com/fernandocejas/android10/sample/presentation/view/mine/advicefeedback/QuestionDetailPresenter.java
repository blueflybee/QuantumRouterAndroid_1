package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetQuestionDetailResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author xiehao
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class QuestionDetailPresenter implements Presenter {

    private final UseCase questionDetailUseCase;

    private IGetQuestionDetailView questionDetailView;

    @Inject
    public QuestionDetailPresenter(@Named(CloudUseCaseComm.QUESTION_DETAIL) UseCase questionDetailUseCase) {
        this.questionDetailUseCase = questionDetailUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        questionDetailUseCase.unsubscribe();
    }

    public void setView(IGetQuestionDetailView questionDetailView) {
        this.questionDetailView = questionDetailView;
    }

    public void getQuestionDetail(IRequest request) {
        questionDetailView.showLoading();
        questionDetailUseCase.execute(request, new AppSubscriber<GetQuestionDetailResponse>(questionDetailView) {
            @Override
            protected void doNext(GetQuestionDetailResponse questionDeatilResponse) {
                questionDetailView.openQuestionDetail(questionDeatilResponse);
            }
        });
    }
}
