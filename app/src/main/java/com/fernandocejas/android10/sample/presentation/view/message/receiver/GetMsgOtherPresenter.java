package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetMsgListResponse;
import com.qtec.mapp.model.rsp.GetMsgOtherListResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetMsgOtherPresenter implements Presenter {

    private final UseCase msgListUseCase;

    private IGetMsgOtherListView msgOtherListView;

    @Inject
    public GetMsgOtherPresenter(@Named(CloudUseCaseComm.MSG_LIST) UseCase msgListUseCase) {
        this.msgListUseCase = msgListUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        msgListUseCase.unsubscribe();
    }

    public void setView(IGetMsgOtherListView msgOtherListView) {
        this.msgOtherListView = msgOtherListView;
    }

    public void getMsgList(IRequest request) {
        msgOtherListView.showLoading();
        msgListUseCase.execute(request, new AppSubscriber<List<GetMsgOtherListResponse>>(msgOtherListView) {
            @Override
            protected void doNext(List<GetMsgOtherListResponse> msgListResponse) {
                msgOtherListView.getMsgOtherList(msgListResponse);
            }
        });
    }
}
