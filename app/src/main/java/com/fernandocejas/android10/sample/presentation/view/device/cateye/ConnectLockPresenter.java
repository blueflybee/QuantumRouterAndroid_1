package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.ConnectLockRequest;
import com.qtec.mapp.model.req.GetLockListRequest;
import com.qtec.mapp.model.rsp.ConnectLockResponse;
import com.qtec.mapp.model.rsp.GetLockListResponse;
import com.qtec.model.core.QtecEncryptInfo;

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
public class ConnectLockPresenter implements Presenter {

    private final UseCase connectLockUseCase;

    private ConnectLockView lockView;

    @Inject
    public ConnectLockPresenter(@Named(CloudUseCaseComm.CONNECT_LOCK) UseCase lockUseCase) {
        this.connectLockUseCase = lockUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        connectLockUseCase.unsubscribe();
    }

    public void setView(ConnectLockView lockView) {
        this.lockView = lockView;
    }

    void connectLock(String lockId,String catId) {
        ConnectLockRequest request = new ConnectLockRequest();
        request.setMasterSerialNo(lockId);
        request.setSlaveSerialNo(catId);

        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);

        lockView.showLoading();

        connectLockUseCase.execute(encryptInfo, new AppSubscriber<ConnectLockResponse>(lockView) {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                lockView.connectLockFailed();
            }

            @Override
            protected void doNext(ConnectLockResponse response) {
                lockView.connectLockSuccess(response);
            }
        });
    }
}
