package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.DeleteWifiSwitchRequest;
import com.qtec.router.model.req.GetWifiTimeConfigRequest;
import com.qtec.router.model.rsp.DeleteWifiSwitchResponse;
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
public class DeleteWiFiSwitchPresenter implements Presenter {

  private final UseCase switchUseCase;
  private IDeleteWifiSwitchView wifiView;

  @Inject
  public DeleteWiFiSwitchPresenter(@Named(RouterUseCaseComm.DELETE_WIFI_SWITCH) UseCase switchUseCaseView) {
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

  public void setView(IDeleteWifiSwitchView wifiView) {
    this.wifiView = wifiView;
  }

  public void deleteWIfiSwitch(String routerID,DeleteWifiSwitchRequest bean) {
    //build router request
    DeleteWifiSwitchRequest routerData = new DeleteWifiSwitchRequest();
    routerData.setId(bean.getId());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_DELETE_WIFI_SWITCH, RouterUrlPath.METHOD_POST);

    wifiView.showLoading();
    switchUseCase.execute(multiEncryptInfo,
        new AppSubscriber<DeleteWifiSwitchResponse>(wifiView) {

          @Override
          protected void doNext(DeleteWifiSwitchResponse response) {
            if (response != null) {
              wifiView.deleteWifiSwitch(response);
            }
          }
        });
  }

}
