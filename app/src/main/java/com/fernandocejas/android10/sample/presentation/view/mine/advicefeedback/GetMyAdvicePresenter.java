package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetMyAdviceResponse;

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
public class GetMyAdvicePresenter implements Presenter {

    private final UseCase myAdviceUseCase;

    private IMyAdviceView myAdviceView;

    @Inject
    public GetMyAdvicePresenter(@Named(CloudUseCaseComm.MY_ADVICE) UseCase myAdviceUseCase) {
        this.myAdviceUseCase = myAdviceUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        myAdviceUseCase.unsubscribe();
    }

    public void setView(IMyAdviceView myAdviceView) {
        this.myAdviceView = myAdviceView;
    }

    public void getMyAdviceList(IRequest request) {
        myAdviceView.showLoading();
        myAdviceUseCase.execute(request, new AppSubscriber<List<GetMyAdviceResponse>>(myAdviceView) {
            @Override
            protected void doNext(List<GetMyAdviceResponse> myAdviceListResponse) {
                myAdviceView.openMyAdvice(myAdviceListResponse);
            }
        });
    }
}
