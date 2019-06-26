package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetWifiDetailRequest;
import com.qtec.router.model.rsp.GetWifiDetailResponse;

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
public class GetWifiDetailPresenter implements Presenter {

  private final UseCase wirelessUseCase;
  private GetWifiDetailView wirelessView;

  @Inject
  public GetWifiDetailPresenter(@Named(RouterUseCaseComm.GET_WIFI_DETAIL) UseCase wirelessUseCase) {
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

  public void setView(GetWifiDetailView wirelessView) {
    this.wirelessView = wirelessView;
  }

  public void getWifiDetail(String routerID) {
    //build router request
    GetWifiDetailRequest routerData = new GetWifiDetailRequest();

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_WIFI_DETAIL, RouterUrlPath.METHOD_GET);

    wirelessView.showLoading();
    wirelessUseCase.execute(multiEncryptInfo,
        new AppSubscriber<GetWifiDetailResponse>(wirelessView) {

          @Override
          protected void doNext(GetWifiDetailResponse response) {
            if (response != null) {
              wirelessView.getWifiDetail(response);
            }
          }
        });
  }

}
