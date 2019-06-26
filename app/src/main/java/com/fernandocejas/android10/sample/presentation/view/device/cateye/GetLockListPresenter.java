package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.GetLockListRequest;
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
public class GetLockListPresenter implements Presenter {

    private final UseCase lockListUseCase;

    private GetLockListView lockListView;

    @Inject
    public GetLockListPresenter(@Named(CloudUseCaseComm.GET_LOCK_LIST) UseCase lockListUseCase) {
        this.lockListUseCase = lockListUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        lockListUseCase.unsubscribe();
    }

    public void setView(GetLockListView lockListView) {
        this.lockListView = lockListView;
    }

    void getLockList() {
        GetLockListRequest request = new GetLockListRequest();

        request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);

        lockListView.showLoading();

        lockListUseCase.execute(encryptInfo, new AppSubscriber<List<GetLockListResponse>>(lockListView) {
            @Override
            public void onError(Throwable e) {
                super.handleLoginInvalid(e);
            }

            @Override
            protected void doNext(List<GetLockListResponse> response) {
                lockListView.getLockList(response);
            }
        });
    }
}
