package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.UnbindRouterRequest;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.FactoryResetRequest;
import com.qtec.router.model.req.RestartRouterRequest;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;

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
public class RouterSettingPresenter implements Presenter {

  private final UseCase restartRouterUseCase;
  private final UseCase factoryResetUseCase;
  private final UseCase unbindRouterUseCase;
  private RouterSettingView mRouterSettingView;

  @Inject
  public RouterSettingPresenter(@Named(RouterUseCaseComm.RESTART_ROUTER) UseCase restartRouterUseCase,
                                @Named(RouterUseCaseComm.FACTORY_RESET) UseCase factoryResetUseCase,
                                @Named(CloudUseCaseComm.UNBIND_ROUTER) UseCase unbindRouterUseCase) {
    this.restartRouterUseCase = checkNotNull(restartRouterUseCase, "restartRouterUseCase cannot be null!");
    this.factoryResetUseCase = checkNotNull(factoryResetUseCase, "factoryResetUseCase cannot be null!");
    this.unbindRouterUseCase = checkNotNull(unbindRouterUseCase, "unbindRouterUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    restartRouterUseCase.unsubscribe();
    factoryResetUseCase.unsubscribe();
    unbindRouterUseCase.unsubscribe();
  }

  public void setView(RouterSettingView netModeView) {
    this.mRouterSettingView = netModeView;
  }

  void restartRouter() {

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new RestartRouterRequest(), RouterUrlPath.PATH_RESTART_ROUTER, RouterUrlPath.METHOD_POST);

    mRouterSettingView.showLoading();
    restartRouterUseCase.execute(multiEncryptInfo, new AppSubscriber<RestartRouterResponse>(mRouterSettingView) {

      @Override
      protected void doNext(RestartRouterResponse response) {
        mRouterSettingView.restartRouterSuccess(response);
      }
    });

  }

  void factoryReset() {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new FactoryResetRequest(), RouterUrlPath.PATH_FACTORY_RESET, RouterUrlPath.METHOD_POST);

    mRouterSettingView.showLoading();
    factoryResetUseCase.execute(multiEncryptInfo, new AppSubscriber<FactoryResetResponse>(mRouterSettingView) {

      @Override
      protected void doNext(FactoryResetResponse response) {
        mRouterSettingView.factoryResetSuccess(response);
      }
    });

  }

  void unbindRouter() {
    UnbindRouterRequest request = new UnbindRouterRequest();
    String deviceId = GlobleConstant.getgDeviceId();
    request.setDeviceSerialNo(deviceId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mRouterSettingView.showLoading();
    unbindRouterUseCase.execute(encryptInfo, new AppSubscriber<UnbindRouterResponse>(mRouterSettingView) {

      @Override
      protected void doNext(UnbindRouterResponse response) {
        DeviceCacheManager.delete(mRouterSettingView.getContext(), deviceId);
        mRouterSettingView.unbindRouterSuccess(response, deviceId);
      }
    });

  }

  public void unbindRouter(String deviceId) {
    UnbindRouterRequest request = new UnbindRouterRequest();
    request.setDeviceSerialNo(deviceId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mRouterSettingView.showLoading();
    unbindRouterUseCase.execute(encryptInfo, new AppSubscriber<UnbindRouterResponse>(mRouterSettingView) {

      @Override
      protected void doNext(UnbindRouterResponse response) {
        DeviceCacheManager.delete(mRouterSettingView.getContext(), deviceId);
        mRouterSettingView.unbindRouterSuccess(response, deviceId);
      }
    });

  }


}
