package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.ModifyDevNameRequest;
import com.qtec.mapp.model.rsp.ModifyDevNameResponse;
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
public class ModifyPropertyPresenter implements Presenter {

  private final UseCase modifyDevNameUseCase;
  private ModifyPropertyView mModifyPropertyView;

  @Inject
  public ModifyPropertyPresenter(@Named(CloudUseCaseComm.MODIFY_ROUTER_NAME) UseCase modifyDevNameUseCase) {
    this.modifyDevNameUseCase = checkNotNull(modifyDevNameUseCase, "modifyDevNameUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyDevNameUseCase.unsubscribe();
  }

  public void setView(ModifyPropertyView modifyPropertyView) {
    this.mModifyPropertyView = modifyPropertyView;
  }

  public void modifyDevName(String name, String deviceId) {
    ModifyDevNameRequest request = new ModifyDevNameRequest();
    request.setDeviceNickName(name);
    request.setDeviceSerialNo(deviceId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mModifyPropertyView.showLoading();
    modifyDevNameUseCase.execute(encryptInfo, new AppSubscriber<ModifyDevNameResponse>(mModifyPropertyView) {

      @Override
      protected void doNext(ModifyDevNameResponse response) {
        mModifyPropertyView.showModifyRouterNameSuccess();

      }
    });
  }


}
