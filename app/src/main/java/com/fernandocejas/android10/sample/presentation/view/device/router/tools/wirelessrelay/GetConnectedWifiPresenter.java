package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetConnectedWifiRequest;
import com.qtec.router.model.rsp.GetConnectedWifiResponse;

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
public class GetConnectedWifiPresenter implements Presenter {

  private final UseCase wirelessUseCase;
  private GetConnectedWifiView wirelessView;

  @Inject
  public GetConnectedWifiPresenter(@Named(RouterUseCaseComm.GET_CONNECTED_WIFI) UseCase wirelessUseCase) {
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

  public void setView(GetConnectedWifiView wirelessView) {
    this.wirelessView = wirelessView;
  }

  public void getConnectedWifi(String routerID) {
    //build router request
    GetConnectedWifiRequest routerData = new GetConnectedWifiRequest();

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_CONNECTED_WIFI, RouterUrlPath.METHOD_GET);

    //  wirelessView.showLoading();
    wirelessUseCase.execute(multiEncryptInfo,
        new AppSubscriber<GetConnectedWifiResponse>(wirelessView) {

          @Override
          protected void doNext(GetConnectedWifiResponse response) {
            if (response != null) {
              wirelessView.getConnectedWifi(response);
            }
          }

          @Override
          public void onError(Throwable e) {
            System.out.println("无线中继 GetConnectedWifiPresenter.onError");
            //屏蔽错误信息
           /* super.onError(e);
            System.out.println("弹框信息一致存在");*/
          }
        });
  }

}
