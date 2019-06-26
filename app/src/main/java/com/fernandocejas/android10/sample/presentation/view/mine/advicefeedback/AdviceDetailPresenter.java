package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.FeedBackResponse;

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
public class AdviceDetailPresenter implements Presenter {

    private final UseCase adviceDetailUseCase;

    private IGetAdviceDetailView adviceDetailView;

    @Inject
    public AdviceDetailPresenter(@Named(CloudUseCaseComm.ADVICE_DETAIL) UseCase adviceDetailUseCase) {
        this.adviceDetailUseCase = adviceDetailUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        adviceDetailUseCase.unsubscribe();
    }

    public void setView(IGetAdviceDetailView adviceDetailView) {
        this.adviceDetailView = adviceDetailView;
    }

    public void getAdviceDetail(IRequest request) {
        adviceDetailView.showLoading();
        adviceDetailUseCase.execute(request, new AppSubscriber<FeedBackResponse<List<FeedBackResponse.ReplyContent>>>(adviceDetailView) {
            @Override
            protected void doNext(FeedBackResponse<List<FeedBackResponse.ReplyContent>> adviceDeatilResponse) {
                adviceDetailView.openAdviceDetail(adviceDeatilResponse);
            }
        });
    }
}
