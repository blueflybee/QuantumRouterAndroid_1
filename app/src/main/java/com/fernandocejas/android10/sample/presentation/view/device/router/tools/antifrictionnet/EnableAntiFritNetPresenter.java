package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.EnableAntiFritNetRequest;
import com.qtec.router.model.req.GetSambaAccountRequest;
import com.qtec.router.model.req.GetWaitingAuthDeviceListRequest;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;

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
public class EnableAntiFritNetPresenter implements Presenter {

  private final UseCase enableNetUseCase;
  private IEnableAntiFritNetView enableNetView;

  @Inject
  public EnableAntiFritNetPresenter(@Named(RouterUseCaseComm.ENABLE_FRIT_NET) UseCase enableNetUseCase) {
    this.enableNetUseCase = checkNotNull(enableNetUseCase, "enableNetUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    enableNetUseCase.unsubscribe();
  }

  public void setView(IEnableAntiFritNetView enableNetView) {
    this.enableNetView = enableNetView;
  }

  public void enableAntiFritNet(String routerID, int enableFlag, String question, String answer) {
    //build router request
    EnableAntiFritNetRequest routerData = new EnableAntiFritNetRequest();
    routerData.setEnable(enableFlag);
    routerData.setQuestion(question);
    routerData.setAnswer(answer);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_ENABLE_ANTI_FRIT_NET, RouterUrlPath.METHOD_POST);

/*    if (multiEncryptInfo.getRouterDirectConnect()) {
      //开启防蹭网如果是直连的话默认就
      PrefConstant.IS_ANTI_ENABLED_BY_DIRECTNET = true;
    }else {
      PrefConstant.IS_ANTI_ENABLED_BY_DIRECTNET = false;
    }*/

    enableNetView.showLoading();
    enableNetUseCase.execute(multiEncryptInfo,
        new AppSubscriber<EnableAntiFritNetResponse>(enableNetView) {

          @Override
          protected void doNext(EnableAntiFritNetResponse response) {
            if (response != null) {
              enableNetView.getAntiFritNetStatus(response);
            }
          }
        });
  }

}
