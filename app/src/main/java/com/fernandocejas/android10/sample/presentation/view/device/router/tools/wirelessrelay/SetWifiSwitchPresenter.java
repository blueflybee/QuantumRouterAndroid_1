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
import com.qtec.router.model.req.SetWifiSwitchRequest;
import com.qtec.router.model.rsp.GetConnectedWifiResponse;
import com.qtec.router.model.rsp.SetWifiSwitchResponse;

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
public class SetWifiSwitchPresenter implements Presenter {

  private final UseCase wirelessUseCase;
  private SetWifiSwitchView wirelessView;

  @Inject
  public SetWifiSwitchPresenter(@Named(RouterUseCaseComm.SET_WIFI_SWITCH) UseCase wirelessUseCase) {
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

  public void setView(SetWifiSwitchView wirelessView) {
    this.wirelessView = wirelessView;
  }

  public void setWifiSwitch(String routerID, int enable) {
    //build router request
    SetWifiSwitchRequest routerData = new SetWifiSwitchRequest();
    routerData.setEnable(enable);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_SET_WIFI_SWITCH, RouterUrlPath.METHOD_POST);

    wirelessView.showLoading();
    wirelessUseCase.execute(multiEncryptInfo,
        new AppSubscriber<SetWifiSwitchResponse>(wirelessView) {

          @Override
          protected void doNext(SetWifiSwitchResponse response) {
            if (response != null) {
              wirelessView.setWifiSwitch(response);
            }
          }

          @Override
          public void onError(Throwable e) {
            /*super.onError(e);*/
            System.out.println("无线中继 SetWifiSwitchPresenter.onError");
          }
        });
  }

}
