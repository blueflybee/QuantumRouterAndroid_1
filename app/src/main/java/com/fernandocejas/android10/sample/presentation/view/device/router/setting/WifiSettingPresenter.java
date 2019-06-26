package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.ConfigWifiRequest;
import com.qtec.router.model.req.GetWifiConfigRequest;
import com.qtec.router.model.req.UpdateFirmwareRequest;
import com.qtec.router.model.rsp.ConfigWifiResponse;
import com.qtec.router.model.rsp.GetWifiConfigResponse;
import com.qtec.router.model.rsp.UpdateFirmwareResponse;

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
class WifiSettingPresenter implements Presenter {

  private final UseCase getWifiConfigUseCase;
  private final UseCase configWifiUseCase;
  private WifiSettingView mWifiSettingView;

  @Inject
  public WifiSettingPresenter(@Named(RouterUseCaseComm.GET_WIFI_CONFIG) UseCase getWifiConfigUseCase,
                              @Named(RouterUseCaseComm.CONFIG_WIFI) UseCase configWifiUseCase) {
    this.getWifiConfigUseCase = checkNotNull(getWifiConfigUseCase, "getWifiConfigUseCase cannot be null!");
    this.configWifiUseCase = checkNotNull(configWifiUseCase, "configWifiUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getWifiConfigUseCase.unsubscribe();
    configWifiUseCase.unsubscribe();
  }

  public void setView(WifiSettingView wifiSettingView) {
    this.mWifiSettingView = wifiSettingView;
  }


  void getWifiConfig() {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new GetWifiConfigRequest(), RouterUrlPath.PATH_GET_WIFI_CONFIG, RouterUrlPath.METHOD_GET);

    mWifiSettingView.showLoading();
    getWifiConfigUseCase.execute(multiEncryptInfo, new AppSubscriber<GetWifiConfigResponse>(mWifiSettingView) {

      @Override
      protected void doNext(GetWifiConfigResponse response) {
        mWifiSettingView.getWifiConfigSuccess(response);
      }
    });

  }

  void configWifi(ConfigWifiRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_CONFIG_WIFI, RouterUrlPath.METHOD_POST);

    mWifiSettingView.showLoading();
    configWifiUseCase.execute(multiEncryptInfo, new AppSubscriber<ConfigWifiResponse>(mWifiSettingView) {

      @Override
      protected void doNext(ConfigWifiResponse response) {
        mWifiSettingView.configWifiSuccess(response);
      }
    });

  }

}
