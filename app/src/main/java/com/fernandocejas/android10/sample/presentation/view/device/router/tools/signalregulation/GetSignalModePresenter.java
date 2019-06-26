package com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetSignalRegulationRequest;
import com.qtec.router.model.req.GetSignalRegulationRequest;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;

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
public class GetSignalModePresenter implements Presenter {

    private final UseCase signalModeUseCase;
    private IGetSignalModeView getSignalModeView;

    @Inject
    public GetSignalModePresenter(@Named(RouterUseCaseComm.GET_SIGNAL_MODE) UseCase signalModeUseCase) {
        this.signalModeUseCase = checkNotNull(signalModeUseCase, "signalModeUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        signalModeUseCase.unsubscribe();
    }

    public void setView(IGetSignalModeView getSignalModeView) {
        this.getSignalModeView = getSignalModeView;
    }

    public void getSignalMode(String routerID) {
        //build router request
        GetSignalRegulationRequest routerData = new GetSignalRegulationRequest();

        QtecEncryptInfo<GetSignalRegulationRequest> routerRequest = new QtecEncryptInfo<>();
        routerRequest.setRequestUrl(RouterUrlPath.PATH_GET_SIGNAL_MODE);
        routerRequest.setMethod(RouterUrlPath.METHOD_GET);
        routerRequest.setData(routerData);

        //build transmit
        TransmitRequest<QtecEncryptInfo<GetSignalRegulationRequest>> transmit = new TransmitRequest<>();
        transmit.setRouterSerialNo(routerID);
        transmit.setEncryptInfo(routerRequest);

        //build cloud EncryptInfo
        QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
        cloudEncryptInfo.setData(transmit);

        //build router EncryptInfo
        QtecEncryptInfo<GetSignalRegulationRequest> routerEncryptInfo = new QtecEncryptInfo<>();
        routerEncryptInfo.setData(new GetSignalRegulationRequest());

        QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
        multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
        multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

        getSignalModeView.showLoading();
        signalModeUseCase.execute(multiEncryptInfo,
                new AppSubscriber<GetSignalRegulationResponse>(getSignalModeView) {

                    @Override
                    protected void doNext(GetSignalRegulationResponse response) {
                        if (response != null) {
                            getSignalModeView.getSignalMode(response);
                        }
                    }
                });
    }

}
