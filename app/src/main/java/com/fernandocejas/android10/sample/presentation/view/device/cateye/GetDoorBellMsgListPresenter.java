package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.cateye.model.response.GetDoorBellRecordListRequest;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.List;

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
public class GetDoorBellMsgListPresenter implements Presenter {
    private final UseCase msgUseCase;
    private GetDoorBellRecordListView msgView;

    @Inject
    public GetDoorBellMsgListPresenter(@Named(CloudUseCaseComm.GET_DOOR_BEER_MSG_LIST) UseCase msgCountUseCase)
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

    public void setView(GetDoorBellRecordListView msgView) {
        this.msgView = msgView;
    }

    public void getDoorBeerMsgList(String recordUniqueKey,String pageSize) {
        QtecEncryptInfo<GetDoorBellRecordListRequest> encryptInfo = new QtecEncryptInfo<>();
        GetDoorBellRecordListRequest data = new GetDoorBellRecordListRequest();
        data.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
        data.setDeviceSerialNo(GlobleConstant.getgCatEyeId());
        data.setPageSize(pageSize);
        data.setRecordUniqueKey(recordUniqueKey);
        data.setIsAlarm("");
        data.setMessageCode("");
        encryptInfo.setData(data);

        msgView.showLoading();
        msgUseCase.execute(encryptInfo, new AppSubscriber<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>>(msgView) {
            @Override
            protected void doNext(GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>> response) {
                msgView.getDoorBellRecordList(response);
            }
        });

    }
}
