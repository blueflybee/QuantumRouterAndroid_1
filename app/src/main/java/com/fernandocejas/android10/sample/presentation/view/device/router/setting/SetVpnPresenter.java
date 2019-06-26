package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetVpnListRequest;
import com.qtec.router.model.req.SetVpnRequest;
import com.qtec.router.model.rsp.GetVpnListResponse;
import com.qtec.router.model.rsp.SetVpnResponse;

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
public class SetVpnPresenter implements Presenter {

    private final UseCase vpnUseCase;
    private SetVpnView vpnView;

    @Inject
    public SetVpnPresenter(@Named(RouterUseCaseComm.SET_VPN) UseCase vpnUseCase) {
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

    public void setView(SetVpnView vpnView) {
        this.vpnView = vpnView;
    }

    public void setVpn(String routerID,SetVpnRequest<List<SetVpnRequest.VpnBean>> bean) {
        //build router request
        SetVpnRequest<List<SetVpnRequest.VpnBean>> routerData = new SetVpnRequest<List<SetVpnRequest.VpnBean>>();
        routerData.setEnable(bean.getEnable());
        routerData.setVpn(bean.getVpn());

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_SET_VPN, RouterUrlPath.METHOD_POST);

        vpnView.showLoading();
        vpnUseCase.execute(multiEncryptInfo,
            new AppSubscriber<SetVpnResponse>(vpnView) {

                @Override
                protected void doNext(SetVpnResponse response) {
                    if (response != null) {
                        vpnView.setVpn(response);
                    }
                }
            });
    }

}
