package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.ConnectLockRequest;
import com.qtec.mapp.model.req.QueryCatLockRequest;
import com.qtec.mapp.model.rsp.ConnectLockResponse;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
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
public class QueryCatLockPresenter implements Presenter {

    private final UseCase queryCatLockUseCase;

    private  QueryCatLockView lockView;

    @Inject
    public QueryCatLockPresenter(@Named(CloudUseCaseComm.QUERY_CAT_LOCK) UseCase lockUseCase) {
        this.queryCatLockUseCase = lockUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        queryCatLockUseCase.unsubscribe();
    }

    public void setView(QueryCatLockView lockView) {
        this.lockView = lockView;
    }

    public void queryCatLock(String catId) {
        QueryCatLockRequest request = new QueryCatLockRequest();
        request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
        request.setSlaveSerialNo(catId);

        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);

        /*lockView.showLoading();*/

        queryCatLockUseCase.execute(encryptInfo, new AppSubscriber<QueryCatLockResponse>(lockView) {
            @Override
            public void onError(Throwable e) {
                super.handleLoginInvalid(e);
            }

            @Override
            protected void doNext(QueryCatLockResponse response) {
                lockView.queryCayLock(response);
            }
        });
    }
}
