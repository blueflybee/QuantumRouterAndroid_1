package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetSambaAccountRequest;
import com.qtec.router.model.req.QueryDiskStateRequest;
import com.qtec.router.model.rsp.GetSambaAccountResponse;
import com.qtec.router.model.rsp.QueryDiskStateResponse;

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
public class QueryDiskStatePresenter implements Presenter {

    private final UseCase diskUseCase;
    private IQueryDiskStateView diskView;

    @Inject
    public QueryDiskStatePresenter(@Named(RouterUseCaseComm.QUERY_DISK_STATE) UseCase diskUseCase) {
        this.diskUseCase = checkNotNull(diskUseCase, "diskUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        diskUseCase.unsubscribe();
    }

    public void setView(IQueryDiskStateView diskView) {
        this.diskView = diskView;
    }

    public void queryDiskState() {
        //build router request
        QueryDiskStateRequest routerData = new QueryDiskStateRequest();

        QtecEncryptInfo<QueryDiskStateRequest> encryptInfo = new QtecEncryptInfo<>();
        encryptInfo.setData(routerData);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            GlobleConstant.getgDeviceId(), routerData, RouterUrlPath.PATH_QUERY_DISK_STATE, RouterUrlPath.METHOD_GET);

        /*diskView.showLoading();*/
        diskUseCase.execute(multiEncryptInfo,
            new AppSubscriber<QueryDiskStateResponse>(diskView) {

                @Override
                protected void doNext(QueryDiskStateResponse response) {
                    if (response != null) {
                        diskView.queryDiskState(response);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    diskView.queryDiskStateFailed();
                }
            });
    }

}
