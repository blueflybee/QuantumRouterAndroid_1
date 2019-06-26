package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi.IGetGuestSwitchView;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetGuestWifiSwitchRequest;
import com.qtec.router.model.req.GetWifiTimeConfigRequest;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;

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
public class GetWifiTimeConfigPresenter implements Presenter {

  private final UseCase switchUseCase;
  private IGetWifiTimeConfigView wifiView;

  @Inject
  public GetWifiTimeConfigPresenter(@Named(RouterUseCaseComm.GET_WIFI_TIME_CONFIG) UseCase switchUseCaseView) {
    this.switchUseCase= checkNotNull(switchUseCaseView, "switchUseCasecannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    switchUseCase.unsubscribe();
  }

  public void setView(IGetWifiTimeConfigView wifiView) {
    this.wifiView = wifiView;
  }

  public void getWifiTimeConfig(String routerID) {
    //build router request
    GetWifiTimeConfigRequest routerData = new GetWifiTimeConfigRequest();

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_WIFI_TIME_CONFIG, RouterUrlPath.METHOD_GET);

    wifiView.showLoading();
    switchUseCase.execute(multiEncryptInfo,
        new AppSubscriber<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>(wifiView) {

          @Override
          protected void doNext(GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>> response) {
            if (response != null) {
              wifiView.getWifiTimeConfig(response);
            }
          }
        });
  }

}
