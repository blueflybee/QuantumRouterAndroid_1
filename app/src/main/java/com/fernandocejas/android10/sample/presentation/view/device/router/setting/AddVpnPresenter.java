package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation.IPostSignalInfoView;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddVpnRequest;
import com.qtec.router.model.req.PostChildCareDetailRequest;
import com.qtec.router.model.req.SetSingalRegulationModeRequest;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;
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
public class AddVpnPresenter implements Presenter {

    private final UseCase vpnUseCase;
    private AddVpnView vpnView;

    @Inject
    public AddVpnPresenter(@Named(RouterUseCaseComm.ADD_VPN) UseCase vpnUseCase) {
        this.vpnUseCase = checkNotNull(vpnUseCase, "vpnUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        vpnUseCase.unsubscribe();
    }

    public void setView(AddVpnView vpnView) {
        this.vpnView = vpnView;
    }

    public void addVpn(String routerID,AddVpnRequest bean) {
        //build router request
        AddVpnRequest routerData = new AddVpnRequest();
        routerData.setDescription(bean.getDescription());
        routerData.setMode(bean.getMode());
        routerData.setPassword(bean.getPassword());
        routerData.setUsername(bean.getUsername());
        routerData.setServer_ip(bean.getServer_ip());

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_ADD_VPN, RouterUrlPath.METHOD_POST);

        vpnView.showLoading();
        vpnUseCase.execute(multiEncryptInfo,
            new AppSubscriber<AddVpnResponse>(vpnView) {

                @Override
                protected void doNext(AddVpnResponse response) {
                    if (response != null) {
                        vpnView.addVpn(response);
                    }
                }
            });
    }

}
