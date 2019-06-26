package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetWifiTimeConfigRequest;
import com.qtec.router.model.req.SetWifiAllSwitchRequest;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;
import com.qtec.router.model.rsp.SetWifiAllSwitchResponse;

import java.util.ArrayList;
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
public class SetWifiAllSwitchPresenter implements Presenter {

  private final UseCase switchUseCase;
  private ISetWifiAllSwitchView wifiView;

  @Inject
  public SetWifiAllSwitchPresenter(@Named(RouterUseCaseComm.SET_WIFI_ALL_SWITCH) UseCase switchUseCaseView) {
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

  public void setView(ISetWifiAllSwitchView wifiView) {
    this.wifiView = wifiView;
  }

  public void setWifiAllSwitch(String routerID,SetWifiAllSwitchRequest<List<SetWifiAllSwitchRequest.WifiSwitchConfig>> bean) {
    //build router request
    SetWifiAllSwitchRequest<List<SetWifiAllSwitchRequest.WifiSwitchConfig>> routerData = new SetWifiAllSwitchRequest<List<SetWifiAllSwitchRequest.WifiSwitchConfig>>();
    routerData.setEnable(bean.getEnable());
    routerData.setRules(bean.getRules());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_SET_WIFI_ALL_SWITCH, RouterUrlPath.METHOD_POST);

    wifiView.showLoading();
    switchUseCase.execute(multiEncryptInfo,
        new AppSubscriber<SetWifiAllSwitchResponse>(wifiView) {

          @Override
          protected void doNext(SetWifiAllSwitchResponse response) {
            if (response != null) {
              wifiView.setWifiAllSwitch(response);
            }
          }
        });
  }

}
