package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.req.SetStaticIPRequest;
import com.qtec.router.model.rsp.SetDHCPResponse;
import com.qtec.router.model.rsp.SetStaticIPResponse;

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
public class StaticIPPresenter implements Presenter {

  private final UseCase setStaticIPUseCase;
  private StaticIPView mStaticIPView;

  @Inject
  public StaticIPPresenter(@Named(RouterUseCaseComm.SET_STATIC_IP) UseCase setStaticIPUseCase) {
    this.setStaticIPUseCase = checkNotNull(setStaticIPUseCase, "setStaticIPUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    setStaticIPUseCase.unsubscribe();
  }

  public void setView(StaticIPView staticIPView) {
    this.mStaticIPView = staticIPView;
  }

  public void setStaticIp(String ip, String netMask, String gateWay, String firstDns, String secondDns, int configed) {
    SetStaticIPRequest request = new SetStaticIPRequest();
    request.setConnectiontype("static");
    request.setIpaddr(ip);
    request.setNetmask(netMask);
    request.setGateway(gateWay);
    request.setDns(firstDns + " " + secondDns);
    request.setConfiged(configed);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_SET_STATIC_IP, RouterUrlPath.METHOD_POST);

    mStaticIPView.showLoading();
    setStaticIPUseCase.execute(multiEncryptInfo, new AppSubscriber<SetStaticIPResponse>(mStaticIPView) {

      @Override
      protected void doNext(SetStaticIPResponse response) {
        mStaticIPView.showSetStaticIpSuccess(response);

      }
    });

  }

}
