package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.QueryLockedCatRequest;
import com.qtec.mapp.model.rsp.QueryLockedCatResponse;
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
public class QueryLockedCatPresenter implements Presenter {

    private final UseCase lockUseCase;

    private QueryLockedCatView lockView;

    @Inject
    public QueryLockedCatPresenter(@Named(CloudUseCaseComm.QUERY_LOCKED_CAT) UseCase lockUseCase) {
        this.lockUseCase = lockUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        lockUseCase.unsubscribe();
    }

    public void setView(QueryLockedCatView lockView) {
        this.lockView = lockView;
    }

    public void queryLockedCat(String catId) {
        QueryLockedCatRequest request = new QueryLockedCatRequest();

        request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
        request.setDeviceSerialNo(catId);

        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);

        lockView.showLoading();

        lockUseCase.execute(encryptInfo, new AppSubscriber<QueryLockedCatResponse>(lockView) {
            @Override
            public void onError(Throwable e) {
                super.handleLoginInvalid(e);

            }

            @Override
            protected void doNext(QueryLockedCatResponse response) {
                lockView.queryLockedCat(response);
            }
        });
    }
}
