package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.GetMsgCountRequest;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class GetMsgCountPresenter implements Presenter {
    private final UseCase msgCountUseCase;
    private IGetMsgCountView msgCountView;

    @Inject
    public GetMsgCountPresenter(@Named(CloudUseCaseComm.GET_MSG_COUNT) UseCase msgCountUseCase)
    {
        this.msgCountUseCase = checkNotNull(msgCountUseCase, "msgCountUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        msgCountUseCase.unsubscribe();
    }

    public void setView(IGetMsgCountView msgCountView) {
        this.msgCountView = msgCountView;
    }

    public void getMsgCount() {
        QtecEncryptInfo<GetMsgCountRequest> encryptInfo = new QtecEncryptInfo<>();
        GetMsgCountRequest data = new GetMsgCountRequest();
        data.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
        encryptInfo.setData(data);

        msgCountView.showLoading();
        msgCountUseCase.execute(encryptInfo, new AppSubscriber<GetMsgCountResponse>(msgCountView) {
            @Override
            protected void doNext(GetMsgCountResponse response) {
                msgCountView.getMsgCount(response);
            }
        });

    }
}
