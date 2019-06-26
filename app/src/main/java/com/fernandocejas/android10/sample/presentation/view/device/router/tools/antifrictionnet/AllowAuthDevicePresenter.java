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
import com.qtec.router.model.req.AllowAuthDeviceRequest;
import com.qtec.router.model.req.GetWaitingAuthDeviceListRequest;
import com.qtec.router.model.rsp.AllowAuthDeviceResponse;
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
public class AllowAuthDevicePresenter implements Presenter {

    private final UseCase allowAuthDeviceUseCase;
    private IAllowAuthDeviceView allowAuthDeviceView;

    @Inject
    public AllowAuthDevicePresenter(@Named(RouterUseCaseComm.ALLOW_AUTH_DEVICE) UseCase allowAuthDeviceUseCase) {
        this.allowAuthDeviceUseCase = checkNotNull(allowAuthDeviceUseCase, "allowAuthDeviceUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        allowAuthDeviceUseCase.unsubscribe();
    }

    public void setView(IAllowAuthDeviceView allowAuthDeviceView) {
        this.allowAuthDeviceView = allowAuthDeviceView;
    }

    public void allowAuthDevive(String routerID,String mac,String name,int auth,int block) {
        AllowAuthDeviceRequest routerData = new AllowAuthDeviceRequest();
        routerData.setDev_mac(mac);
        routerData.setDev_name(name);
        routerData.setAuth(auth);
        routerData.setBlock(block);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_ALLOW_AUTH_DEVICE, RouterUrlPath.METHOD_GET);

        allowAuthDeviceView.showLoading();
        allowAuthDeviceUseCase.execute(multiEncryptInfo,
            new AppSubscriber<AllowAuthDeviceResponse>(allowAuthDeviceView) {

                @Override
                protected void doNext(AllowAuthDeviceResponse response) {
                    if (response != null) {
                        allowAuthDeviceView.allowAuthDevice(response);
                    }
                }
            });
    }

}
