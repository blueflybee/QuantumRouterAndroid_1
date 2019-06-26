package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetWaitingAuthDeviceListRequest;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetAuthedDeviceListPresenter implements Presenter {

    private final UseCase waitAuthListUseCase;

    private IGetAuthDeviceListView waitingAuthView;

    @Inject
    public GetAuthedDeviceListPresenter(@Named(RouterUseCaseComm.GET_WAITING_AUTH_LIST) UseCase waitAuthListUseCase) {
        this.waitAuthListUseCase = checkNotNull(waitAuthListUseCase, "waitAuthListUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        waitAuthListUseCase.unsubscribe();
    }

    public void setView(IGetAuthDeviceListView waitingAuthView) {
        this.waitingAuthView = waitingAuthView;
    }

    public void getAuthedDeviceList(String routerID) {
        GetWaitingAuthDeviceListRequest routerData = new GetWaitingAuthDeviceListRequest();
        routerData.setMode(1);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_WAITING_LIST, RouterUrlPath.METHOD_GET);

        waitingAuthView.showLoading();
        waitAuthListUseCase.execute(multiEncryptInfo,
            new AppSubscriber<List<GetWaitingAuthDeviceListResponse>>(waitingAuthView) {

                @Override
                protected void doNext(List<GetWaitingAuthDeviceListResponse> response) {
                    if (response != null) {
                        waitingAuthView.getWaitingAuthDeviceList(response);
                    }
                }
            });
    }

}
