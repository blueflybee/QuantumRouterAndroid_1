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
import com.qtec.router.model.req.GetAntiFritSwitchRequest;
import com.qtec.router.model.req.GetAntiFritSwitchRequest;
import com.qtec.router.model.req.GetWaitingAuthDeviceListRequest;
import com.qtec.router.model.rsp.GetAntiFritSwitchResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

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
public class AntiFritSwitchPresenter implements Presenter {

    private final UseCase antiFritSwitchUseCase;
    private AntiFritSwitchView antiFritSwitchView;

    @Inject
    public AntiFritSwitchPresenter(@Named(RouterUseCaseComm.ANTI_FRIT_SWITCH) UseCase antiFritSwitchUseCase) {
        this.antiFritSwitchUseCase = checkNotNull(antiFritSwitchUseCase, "antiFritSwitchUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        antiFritSwitchUseCase.unsubscribe();
    }

    public void setView(AntiFritSwitchView antiFritSwitchView) {
        this.antiFritSwitchView = antiFritSwitchView;
    }

    public void postAntiFritSwitch(String routerID,int routerAccess,int lanAccess) {

        //build router request
        GetAntiFritSwitchRequest routerData = new GetAntiFritSwitchRequest();
        routerData.setLan_dev_access(lanAccess);
        routerData.setRouter_access(routerAccess);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_ANTI_FRIT_SWITCH, RouterUrlPath.METHOD_POST);

        antiFritSwitchView.showLoading();
        antiFritSwitchUseCase.execute(multiEncryptInfo,
            new AppSubscriber<GetAntiFritSwitchResponse>(antiFritSwitchView) {

                @Override
                protected void doNext(GetAntiFritSwitchResponse response) {
                    if (response != null) {
                        antiFritSwitchView.postAntiFritnetSwitch(response);
                    }
                }
            });
    }

}
