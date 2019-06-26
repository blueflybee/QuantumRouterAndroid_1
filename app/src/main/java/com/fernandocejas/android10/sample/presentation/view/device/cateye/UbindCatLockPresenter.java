package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.QueryCatLockRequest;
import com.qtec.mapp.model.req.UnbindCatLockRequest;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.UnbindCatLockResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author xiehao
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class UbindCatLockPresenter implements Presenter {

    private final UseCase unbindCatLockUseCase;

    private UnbindCatLockView lockView;

    @Inject
    public UbindCatLockPresenter(@Named(CloudUseCaseComm.UNBIND_CAT_LOCK) UseCase lockUseCase) {
        this.unbindCatLockUseCase = lockUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        unbindCatLockUseCase.unsubscribe();
    }

    public void setView(UnbindCatLockView lockView) {
        this.lockView = lockView;
    }

    void unbindCatLock(String lockID,String catID) {
        UnbindCatLockRequest request = new UnbindCatLockRequest();
        request.setMasterSerialNo(lockID);
        request.setSlaveSerialNo(catID);

        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);

        lockView.showLoading();

        unbindCatLockUseCase.execute(encryptInfo, new AppSubscriber<UnbindCatLockResponse>(lockView) {
            @Override
            public void onError(Throwable e) {
                super.handleLoginInvalid(e);

            }

            @Override
            protected void doNext(UnbindCatLockResponse response) {
                lockView.unbindCayLock(response);
            }
        });
    }
}
