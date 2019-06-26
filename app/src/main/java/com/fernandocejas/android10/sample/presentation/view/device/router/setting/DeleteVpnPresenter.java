package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddVpnRequest;
import com.qtec.router.model.req.DeleteVpnRequest;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.DeleteVpnResponse;

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
public class DeleteVpnPresenter implements Presenter {

    private final UseCase vpnUseCase;
    private DeleteVpnView vpnView;

    @Inject
    public DeleteVpnPresenter(@Named(RouterUseCaseComm.DELETE_VPN) UseCase vpnUseCase) {
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

    public void setView(DeleteVpnView vpnView) {
        this.vpnView = vpnView;
    }

    public void deleteVpn(String routerID,String key) {
        //build router request
        DeleteVpnRequest routerData = new DeleteVpnRequest();
        routerData.setIfname(key);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_DELETE_VPN, RouterUrlPath.METHOD_POST);

        vpnView.showLoading();
        vpnUseCase.execute(multiEncryptInfo,
            new AppSubscriber<DeleteVpnResponse>(vpnView) {

                @Override
                protected void doNext(DeleteVpnResponse response) {
                    if (response != null) {
                        vpnView.deleteVpn(response);
                    }
                }
            });
    }

}
