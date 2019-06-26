package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetWirelessListRequest;
import com.qtec.router.model.rsp.GetWirelessListResponse;

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
public class GetWirelessListPresenter implements Presenter {

    private final UseCase wirelessUseCase;
    private GetWirelessListView wirelessView;

    @Inject
    public GetWirelessListPresenter(@Named(RouterUseCaseComm.GET_WIRELESS_LIST) UseCase wirelessUseCase) {
        this.wirelessUseCase = checkNotNull(wirelessUseCase, "wirelessUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        wirelessUseCase.unsubscribe();
    }

    public void setView(GetWirelessListView wirelessView) {
        this.wirelessView = wirelessView;
    }

    public void getWirelessList(String routerID) {
        //build router request
       GetWirelessListRequest routerData = new GetWirelessListRequest();

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_WIRELESS_LIST, RouterUrlPath.METHOD_GET);

        wirelessView.showLoading();
        wirelessUseCase.execute(multiEncryptInfo,
            new AppSubscriber<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>(wirelessView) {

                @Override
                protected void doNext(GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>> response) {
                    if (response != null) {
                        wirelessView.getWirelessList(response);
                    }
                }

              @Override
              public void onError(Throwable e) {
                System.out.println("e.getClass().getSimpleName() = " + e.getClass().getSimpleName());
                /*super.onError(e);*/
                System.out.println("无线中继 GetWirelessListPresenter.onError");
              }
            });
    }

    public void hideLoadingDialog(){
      if(wirelessView != null){
        wirelessView.hideLoading();
      }
    }

}
