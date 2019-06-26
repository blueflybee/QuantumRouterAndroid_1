package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author xiehao
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetQuestionListPresenter implements Presenter {

    private final UseCase questionListUseCase;

    private IGetQuestionListView questionListView;

    @Inject
    public GetQuestionListPresenter(@Named(CloudUseCaseComm.QUESTION_LIST) UseCase questionListUseCase) {
        this.questionListUseCase = questionListUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        questionListUseCase.unsubscribe();
    }

    public void setView(IGetQuestionListView questionListView) {
        this.questionListView = questionListView;
    }

    public void getQuestionList(IRequest request) {
        questionListView.showLoading();
        questionListUseCase.execute(request, new AppSubscriber<List<GetQuestionListResponse>>(questionListView) {
            @Override
            protected void doNext(List<GetQuestionListResponse> questionListResponse) {
                questionListView.openQuestionList(questionListResponse);
            }
        });
    }
}
