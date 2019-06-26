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
import com.qtec.router.model.req.GetVpnListRequest;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.GetVpnListResponse;

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
public class GetVpnListPresenter implements Presenter {

    private final UseCase vpnUseCase;
    private GetVpnListView vpnView;

    @Inject
    public GetVpnListPresenter(@Named(RouterUseCaseComm.GET_VPN_LIST) UseCase vpnUseCase) {
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

    public void setView(GetVpnListView vpnView) {
        this.vpnView = vpnView;
    }

    public void getVpnList(String routerID) {
        //build router request
        GetVpnListRequest routerData = new GetVpnListRequest();
        System.out.println("VPN routerID = " + routerID);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_VPN_LIST, RouterUrlPath.METHOD_GET);

        vpnView.showLoading();
        vpnUseCase.execute(multiEncryptInfo,
            new AppSubscriber<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>(vpnView) {

                @Override
                protected void doNext(GetVpnListResponse<List<GetVpnListResponse.VpnBean>> response) {
                    if (response != null) {
                        vpnView.getVpnList(response);
                    }
                }
            });
    }

}
