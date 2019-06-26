package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.IGetQuestionListView;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;

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
public class GetSharedMemListPresenter implements Presenter {

    private final UseCase shareMemListUseCase;

    private IGetSharedMemListView shareListView;

    @Inject
    public GetSharedMemListPresenter(@Named(CloudUseCaseComm.SHARE_MEMBER_LIST) UseCase shareMemListUseCase) {
        this.shareMemListUseCase = shareMemListUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        shareMemListUseCase.unsubscribe();
    }

    public void setView(IGetSharedMemListView shareListView) {
        this.shareListView = shareListView;
    }

    public void getShareMemList(IRequest request) {
        shareListView.showLoading();
        shareMemListUseCase.execute(request, new AppSubscriber<List<GetShareMemListResponse>>(shareListView) {
            @Override
            protected void doNext(List<GetShareMemListResponse> shareListResponse) {
                System.out.println("++++++++++++++++++=" + shareListResponse);
                shareListView.getSharedMemList(shareListResponse);
            }
        });
    }
}
