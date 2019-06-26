package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.FeedBackResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetMsgDetailResponse;
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
public class GetMsgListPresenter implements Presenter {

    private final UseCase shareMsgListUseCase;

    private IGetMsgListView msgListView;

    @Inject
    public GetMsgListPresenter(@Named(CloudUseCaseComm.MSG_SHARE_LIST) UseCase shareMsgListUseCase) {
        this.shareMsgListUseCase = shareMsgListUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        shareMsgListUseCase.unsubscribe();
    }

    public void setView(IGetMsgListView msgListView) {
        this.msgListView = msgListView;
    }

    public void getShareMsgList(IRequest request) {
        msgListView.showLoading();
        shareMsgListUseCase.execute(request, new AppSubscriber<List<GetMsgListResponse<GetMsgListResponse.messageContent>>>(msgListView) {
            @Override
            protected void doNext(List<GetMsgListResponse<GetMsgListResponse.messageContent>> msgListResponse) {
                msgListView.getMsgList(msgListResponse);
            }
        });
    }
}
