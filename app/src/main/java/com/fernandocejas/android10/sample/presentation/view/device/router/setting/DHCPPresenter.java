package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.rsp.SetDHCPResponse;

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
public class DHCPPresenter implements Presenter {

  private final UseCase setDHCPUseCase;
  private DHCPView mDHCPView;

  @Inject
  public DHCPPresenter(@Named(RouterUseCaseComm.SET_DHCP) UseCase setDHCPUseCase) {
    this.setDHCPUseCase = checkNotNull(setDHCPUseCase, "setDHCPUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    setDHCPUseCase.unsubscribe();
  }

  public void setView(DHCPView dhcpView) {
    this.mDHCPView = dhcpView;
  }

  public void setDHCP(int configed) {
    SetDHCPRequest request = new SetDHCPRequest();
    request.setConnectiontype("dhcp");
    request.setConfiged(configed);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_SET_DHCP, RouterUrlPath.METHOD_POST);

    mDHCPView.showLoading();
    setDHCPUseCase.execute(multiEncryptInfo, new AppSubscriber<SetDHCPResponse>(mDHCPView) {

      @Override
      protected void doNext(SetDHCPResponse response) {
        mDHCPView.showSetDHCPSuccess(response);

      }
    });

  }

}
