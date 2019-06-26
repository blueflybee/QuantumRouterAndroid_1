package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddWifiDataRequest;
import com.qtec.router.model.req.DeleteWifiSwitchRequest;
import com.qtec.router.model.rsp.DeleteWifiSwitchResponse;
import com.qtec.router.model.rsp.SetWifiDataResponse;

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
public class SetWifiDataPresenter implements Presenter {

  private final UseCase switchUseCase;
  private ISetWifiDataView wifiView;

  @Inject
  public SetWifiDataPresenter(@Named(RouterUseCaseComm.SET_WIFI_DATA) UseCase switchUseCaseView) {
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

  public void setView(ISetWifiDataView wifiView) {
    this.wifiView = wifiView;
  }

  public void setWiFiData(String routerID,Object routerData) {
    //build router request
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_SET_WIFI_DATA, RouterUrlPath.METHOD_POST);

    wifiView.showLoading();
    switchUseCase.execute(multiEncryptInfo,
        new AppSubscriber<SetWifiDataResponse>(wifiView) {

          @Override
          protected void doNext(SetWifiDataResponse response) {
            if (response != null) {
              wifiView.setWifiData(response);
            }
          }
        });
  }

}
