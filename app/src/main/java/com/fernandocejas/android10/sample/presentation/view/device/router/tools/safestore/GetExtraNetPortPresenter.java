package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.view.mine.IGetDeviceCountView;
import com.qtec.mapp.model.req.GetDeviceCountRequest;
import com.qtec.mapp.model.req.GetExtraNetPortRequest;
import com.qtec.mapp.model.rsp.GetDeviceCountResponse;
import com.qtec.mapp.model.rsp.GetExtraNetPortResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetExtraNetPortPresenter implements Presenter {

  private final UseCase extraNetPortUseCase;

  private IGetExtraNetPortView portView;

  @Inject
  public GetExtraNetPortPresenter(@Named(CloudUseCaseComm.EXTRA_NET_PORT) UseCase portUseCase) {
    this.extraNetPortUseCase = portUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    extraNetPortUseCase.unsubscribe();
  }

  public void setView(IGetExtraNetPortView portView) {
    this.portView = portView;
  }

  public void getExtraNetPort() {
    SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
    String userUniqueKey = spUtils.getString(PrefConstant.SP_USER_UNIQUE_KEY);

    GetExtraNetPortRequest request  =new GetExtraNetPortRequest();
    request.setDeviceSerialNo(GlobleConstant.getgDeviceId());
    request.setUserUniqueKey(userUniqueKey);

    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    portView.showLoading();

    extraNetPortUseCase.execute(encryptInfo, new AppSubscriber<GetExtraNetPortResponse>(portView) {
      @Override
      public void onError(Throwable e) {
        super.handleLoginInvalid(e);
        portView.getExtraNetPortFailed();
      }

      @Override
      protected void doNext(GetExtraNetPortResponse response) {
        portView.getExtraNetPort(response);
      }
    });
  }
}
