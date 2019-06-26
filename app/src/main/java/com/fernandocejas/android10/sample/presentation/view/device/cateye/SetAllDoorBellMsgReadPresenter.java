package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadRequest;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadResponse;
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
public class SetAllDoorBellMsgReadPresenter implements Presenter {
    private final UseCase msgUseCase;
    private SetAllDoorBellRecordIsReadView msgView;

    @Inject
    public SetAllDoorBellMsgReadPresenter(@Named(CloudUseCaseComm.SET_ALL_DOOR_BEER_MSG_READ) UseCase msgCountUseCase)
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

    public void setView(SetAllDoorBellRecordIsReadView msgView) {
        this.msgView = msgView;
    }

    public void setAllDoorBellMsgIsRead(String userUniqueKey,String deviceSerialNo) {
        QtecEncryptInfo<SetAllDoorBellRecordReadRequest> encryptInfo = new QtecEncryptInfo<>();

        SetAllDoorBellRecordReadRequest data = new SetAllDoorBellRecordReadRequest();
        data.setDeviceSerialNo(deviceSerialNo);
        data.setUserUniqueKey(userUniqueKey);
        encryptInfo.setData(data);

        msgView.showLoading();
        msgUseCase.execute(encryptInfo, new AppSubscriber<SetAllDoorBellRecordReadResponse>(msgView) {
            @Override
            protected void doNext(SetAllDoorBellRecordReadResponse response) {
                msgView.setAllDoorBeelRecordRead(response);
            }
        });

    }
}
