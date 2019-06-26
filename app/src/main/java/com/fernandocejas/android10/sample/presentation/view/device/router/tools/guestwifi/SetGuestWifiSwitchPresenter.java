package com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetGuestWifiSwitchRequest;
import com.qtec.router.model.req.SetGuestWifiSwitchRequest;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.SetGuestWifiSwitchResponse;

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
public class SetGuestWifiSwitchPresenter implements Presenter {

  private final UseCase switchUseCase;
  private ISetGuestWifiSwitchView setSwitchView;

  @Inject
  public SetGuestWifiSwitchPresenter(@Named(RouterUseCaseComm.SET_GUEST_WIFI_SWITCH) UseCase switchUseCaseView) {
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

  public void setView(ISetGuestWifiSwitchView setSwitchView) {
    this.setSwitchView = setSwitchView;
  }

  public void setGuestSwitch(String routerID,SetGuestWifiSwitchRequest bean) {
    //build router request
    SetGuestWifiSwitchRequest routerData = new SetGuestWifiSwitchRequest();
    routerData.setEnable(bean.getEnable());
    routerData.setIsHide(bean.getIsHide());
    routerData.setName(bean.getName());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_SET_GUEST_SWITCH, RouterUrlPath.METHOD_GET);

    setSwitchView.showLoading();
    switchUseCase.execute(multiEncryptInfo,
        new AppSubscriber<SetGuestWifiSwitchResponse>(setSwitchView) {

          @Override
          protected void doNext(SetGuestWifiSwitchResponse response) {
            if (response != null) {
              setSwitchView.setGuestWifiSwitch(response);
            }
          }
        });
  }

}
