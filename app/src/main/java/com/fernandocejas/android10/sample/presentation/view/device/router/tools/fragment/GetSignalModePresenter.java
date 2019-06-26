package com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment;

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
import com.qtec.router.model.req.GetQosInfoRequest;
import com.qtec.router.model.req.GetSignalRegulationRequest;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.rsp.GetQosInfoResponse;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

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

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_SIGNAL_MODE, RouterUrlPath.METHOD_GET);

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
