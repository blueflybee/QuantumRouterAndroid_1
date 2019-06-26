package com.fernandocejas.android10.sample.presentation.view.mine;

import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.GetDeviceCountRequest;
import com.qtec.mapp.model.rsp.GetDeviceCountResponse;
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
public class GetDeviceCountPresenter implements Presenter {

  private final UseCase deviceCountUseCase;

  private IGetDeviceCountView deviceCountView;

  @Inject
  public GetDeviceCountPresenter(@Named(CloudUseCaseComm.DEVICE_COUNT) UseCase deviceCountUseCase) {
    this.deviceCountUseCase = deviceCountUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    deviceCountUseCase.unsubscribe();
  }

  public void setView(IGetDeviceCountView deviceCountView) {
    this.deviceCountView = deviceCountView;
  }

  public void getDeviceCount() {
    SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
    String userUniqueKey = spUtils.getString(PrefConstant.SP_USER_UNIQUE_KEY);
    GetDeviceCountRequest request = new GetDeviceCountRequest();
    request.setUserUniqueKey(userUniqueKey);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
//    deviceCountView.showLoading();
    deviceCountUseCase.execute(encryptInfo, new AppSubscriber<GetDeviceCountResponse>(deviceCountView) {
      @Override
      public void onError(Throwable e) {
        super.handleLoginInvalid(e);
      }

      @Override
      protected void doNext(GetDeviceCountResponse deviceCountResponse) {
        deviceCountView.getDeviceCount(deviceCountResponse);
      }
    });
  }
}
