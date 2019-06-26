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
import com.qtec.router.model.req.PostChildCareDetailRequest;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;

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
public class GuestWifiSwitchPresenter implements Presenter {

  private final UseCase switchUseCase;
  private IGetGuestSwitchView guestSwitchView;

  @Inject
  public GuestWifiSwitchPresenter(@Named(RouterUseCaseComm.GET_GUEST_WIFI_SWITCH) UseCase switchUseCaseView) {
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

  public void setView(IGetGuestSwitchView guestSwitchView) {
    this.guestSwitchView = guestSwitchView;
  }

  public void getGuestSwitch(String routerID) {
    //build router request
    GetGuestWifiSwitchRequest routerData = new GetGuestWifiSwitchRequest();

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_GUEST_SWITCH, RouterUrlPath.METHOD_GET);

    guestSwitchView.showLoading();
    switchUseCase.execute(multiEncryptInfo,
        new AppSubscriber<GetGuestWifiSwitchResponse>(guestSwitchView) {

          @Override
          protected void doNext(GetGuestWifiSwitchResponse response) {
            if (response != null) {
              guestSwitchView.getGuestWifiSwitch(response);
            }
          }
        });
  }

}
