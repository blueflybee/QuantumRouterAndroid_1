package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.PostConnectWirelessRequest;
import com.qtec.router.model.rsp.PostConnectWirelessResponse;

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
public class PostConnectWirelessPresenter implements Presenter {

  private final UseCase wirelessUseCase;
  private PostConnectWirelessView wirelessView;

  @Inject
  public PostConnectWirelessPresenter(@Named(RouterUseCaseComm.POST_CONNECT_WIRELESS) UseCase wirelessUseCase) {
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

  public void setView(PostConnectWirelessView wirelessView) {
    this.wirelessView = wirelessView;
  }

  public void connectWireless(String routerID, PostConnectWirelessRequest bean) {
    //build router request
    PostConnectWirelessRequest routerData = new PostConnectWirelessRequest();
    routerData.setSsid(bean.getSsid());
    routerData.setPassword(bean.getPassword());
    routerData.setMode(bean.getMode());
    routerData.setEncrypt(bean.getEncrypt());
    routerData.setMac(bean.getMac());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_POST_CONNECT_WIRELESS, RouterUrlPath.METHOD_POST);

    wirelessView.showLoading();
    wirelessUseCase.execute(multiEncryptInfo,
        new AppSubscriber<PostConnectWirelessResponse>(wirelessView) {

          @Override
          protected void doNext(PostConnectWirelessResponse response) {
            if (response != null) {
              wirelessView.postConnectWireless(response);
            }
          }
        });
  }

}
