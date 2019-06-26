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
import com.qtec.router.model.req.GetAntiFritNetStatusRequest;
import com.qtec.router.model.req.GetAntiFritNetStatusRequest;
import com.qtec.router.model.req.GetLockStatusRequest;
import com.qtec.router.model.req.GetWaitingAuthDeviceListRequest;
import com.qtec.router.model.rsp.GetAntiFritNetStatusResponse;
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
public class AntiFritNetStatusPresenter implements Presenter {

    private final UseCase antiFritUseCase;
    private AntiFritNetStatusView antiFritView;

    @Inject
    public AntiFritNetStatusPresenter(@Named(RouterUseCaseComm.ANTI_FRIT_STATUS) UseCase antiFritUseCase) {
        this.antiFritUseCase = checkNotNull(antiFritUseCase, "antiFritUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        antiFritUseCase.unsubscribe();
    }

    public void setView(AntiFritNetStatusView antiFritView) {
        this.antiFritView = antiFritView;
    }

    public void queryAntiFritNetStatus(String routerID) {
        //build router request
        GetAntiFritNetStatusRequest routerData = new GetAntiFritNetStatusRequest();

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_ANTI_FRIT_NET, RouterUrlPath.METHOD_GET);

        antiFritView.showLoading();
        antiFritUseCase.execute(multiEncryptInfo,
            new AppSubscriber<GetAntiFritNetStatusResponse>(antiFritView) {

                @Override
                protected void doNext(GetAntiFritNetStatusResponse response) {
                    if (response != null) {
                        antiFritView.getAntiFritnetStatus(response);
                    }
                }
            });
    }

}
