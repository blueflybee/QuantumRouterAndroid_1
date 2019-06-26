package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailRequest;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailResponse;
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
public class GetDoorBellMsgDetailPresenter implements Presenter {
    private final UseCase msgUseCase;
    private GetDoorBellRecordDetailView msgView;

    @Inject
    public GetDoorBellMsgDetailPresenter(@Named(CloudUseCaseComm.GET_DOOR_BEER_MSG_DETAIL) UseCase msgCountUseCase)
    {
        this.msgUseCase = checkNotNull(msgCountUseCase, "msgUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        msgUseCase.unsubscribe();
    }

    public void setView(GetDoorBellRecordDetailView msgView) {
        this.msgView = msgView;
    }

    public void getDoorBeerMsgDetail(String messageUniqueKey) {
        QtecEncryptInfo<GetDoorBellRecordDetailRequest> encryptInfo = new QtecEncryptInfo<>();

        GetDoorBellRecordDetailRequest data = new GetDoorBellRecordDetailRequest();
        data.setMessageUniqueKey(messageUniqueKey);
        encryptInfo.setData(data);

        msgView.showLoading();
        msgUseCase.execute(encryptInfo, new AppSubscriber<GetDoorBellRecordDetailResponse>(msgView) {
            @Override
            protected void doNext(GetDoorBellRecordDetailResponse response) {
                msgView.getDoorBeelRecordDetail(response);
            }
        });

    }
}
