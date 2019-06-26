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
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.qtec.mapp.model.req.GetRouterInfoCloudRequest;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetRouterInfoRequest;
import com.qtec.router.model.rsp.GetRouterInfoResponse;

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
public class RouterInfoPresenter implements Presenter {

  private final UseCase getRouterInfoUseCase;
  private final UseCase getRouterInfoCloudUseCase;
  private RouterInfoView mRouterInfoView;

  @Inject
  public RouterInfoPresenter(@Named(RouterUseCaseComm.GET_ROUTER_INFO) UseCase getRouterInfoUseCase,
                             @Named(CloudUseCaseComm.GET_ROUTER_INFO_CLOUD) UseCase getRouterInfoCloudUseCase) {
    this.getRouterInfoUseCase = checkNotNull(getRouterInfoUseCase, "getRouterInfoUseCase cannot be null!");
    this.getRouterInfoCloudUseCase = checkNotNull(getRouterInfoCloudUseCase, "getRouterInfoCloudUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getRouterInfoUseCase.unsubscribe();
    getRouterInfoCloudUseCase.unsubscribe();
  }

  public void setView(RouterInfoView routerInfoView) {
    this.mRouterInfoView = routerInfoView;
  }

  void getRouterInfo() {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new GetRouterInfoRequest(), RouterUrlPath.PATH_GET_ROUTER_INFO, RouterUrlPath.METHOD_GET);

    mRouterInfoView.showLoading();
    getRouterInfoUseCase.execute(multiEncryptInfo, new AppSubscriber<GetRouterInfoResponse>(mRouterInfoView) {

      @Override
      protected void doNext(GetRouterInfoResponse response) {
        Router.BaseInfo baseInfo = new Router.BaseInfo();
        baseInfo.setTowPFourG(response.getLfwifissid());
        baseInfo.setFiveG(response.getHfwifissid());
        baseInfo.setLanIp(response.getLanipaddress());
        baseInfo.setWanIp(response.getWanipaddress());
        baseInfo.setCpuModel(response.getCputype());
        baseInfo.setCpuBrand(response.getCpubrand());
        baseInfo.setCpuArch(response.getCpufactory());
        baseInfo.setWanMac(response.getWanmac());

        mRouterInfoView.showRouterInfo(baseInfo);
      }
    });

  }

  public void getRouterInfoCloud(String routerId) {
    GetRouterInfoCloudRequest request = new GetRouterInfoCloudRequest();
    request.setRouterSerialNo(routerId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);

    mRouterInfoView.showLoading();
    getRouterInfoCloudUseCase.execute(encryptInfo, new AppSubscriber<GetRouterInfoCloudResponse>(mRouterInfoView) {
      @Override
      protected void doNext(GetRouterInfoCloudResponse response) {
        mRouterInfoView.showRouterInfoCloud(response);
      }
    });
  }
}
