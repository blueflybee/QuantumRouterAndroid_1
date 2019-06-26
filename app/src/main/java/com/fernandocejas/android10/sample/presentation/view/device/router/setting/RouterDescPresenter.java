package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.ModifyRouterDescRequest;
import com.qtec.mapp.model.rsp.ModifyRouterDescResponse;
import com.qtec.model.core.QtecEncryptInfo;

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
class RouterDescPresenter implements Presenter {

  private final UseCase modifyRouterDescUseCase;
  private RouterDescView mRouterDescView;

  @Inject
  public RouterDescPresenter(@Named(CloudUseCaseComm.MODIFY_ROUTER_DESC) UseCase modifyRouterDescUseCase) {
    this.modifyRouterDescUseCase = checkNotNull(modifyRouterDescUseCase, "getRouterInfoUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyRouterDescUseCase.unsubscribe();
  }

  public void setView(RouterDescView routerDescView) {
    this.mRouterDescView = routerDescView;
  }

  void modifyRouterDesc(String description) {

    ModifyRouterDescRequest request = new ModifyRouterDescRequest();
    request.setDescription(description);
    request.setRouterSerialNo(GlobleConstant.getgDeviceId());
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mRouterDescView.showLoading();
    modifyRouterDescUseCase.execute(encryptInfo, new AppSubscriber<ModifyRouterDescResponse>(mRouterDescView) {

      @Override
      protected void doNext(ModifyRouterDescResponse response) {
        mRouterDescView.showModifySuccess();

      }
    });

  }

}
