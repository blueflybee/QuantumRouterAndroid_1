package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.DeleteVpnRequest;
import com.qtec.router.model.req.ModifyVpnRequest;
import com.qtec.router.model.rsp.DeleteVpnResponse;
import com.qtec.router.model.rsp.ModifyVpnResponse;

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
public class ModifyVpnPresenter implements Presenter {

    private final UseCase vpnUseCase;
    private ModifyVpnView vpnView;

    @Inject
    public ModifyVpnPresenter(@Named(RouterUseCaseComm.MODIFY_VPN) UseCase vpnUseCase) {
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

    public void setView(ModifyVpnView vpnView) {
        this.vpnView = vpnView;
    }

    public void modifyVpn(String routerID,ModifyVpnRequest bean) {
        //build router request
        ModifyVpnRequest routerData = new ModifyVpnRequest();
        routerData.setIfname(bean.getIfname());
        routerData.setDescription(bean.getDescription());
        routerData.setMode(bean.getMode());
        routerData.setServer_ip(bean.getServer_ip());
        routerData.setPassword(bean.getPassword());
        routerData.setUsername(bean.getUsername());

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_MODIFY_VPN, RouterUrlPath.METHOD_POST);

        vpnView.showLoading();
        vpnUseCase.execute(multiEncryptInfo,
            new AppSubscriber<ModifyVpnResponse>(vpnView) {

                @Override
                protected void doNext(ModifyVpnResponse response) {
                    if (response != null) {
                        vpnView.modifyVpn(response);
                    }
                }
            });
    }

}
