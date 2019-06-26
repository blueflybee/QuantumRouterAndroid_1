package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.UploadDevicePwdRequest;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.UploadDevicePwdResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class UploadDevicePwdPresenter implements Presenter {

  private final UseCase uploadPwdUseCase;
  private UploadDevicePwdView uploadPwdView;

  @Inject
  public UploadDevicePwdPresenter(
      @Named(CloudUseCaseComm.UPLOAD_DEVICE_PWD) UseCase uploadPwdUseCase) {
    this.uploadPwdUseCase = checkNotNull(uploadPwdUseCase, "uploadPwdUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    uploadPwdUseCase.unsubscribe();
  }

  public void setView(UploadDevicePwdView uploadPwdView) {
    this.uploadPwdView = uploadPwdView;
  }

  public void uploadDevicePwd(String deviceSerialNo,String password) {
    UploadDevicePwdRequest request = new UploadDevicePwdRequest();

    request.setDeviceSerialNo(deviceSerialNo);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    request.setDevicePass(password);

    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    uploadPwdView.showLoading();

    uploadPwdUseCase.execute(encryptInfo, new AppSubscriber<UploadDevicePwdResponse>(uploadPwdView) {
      @Override
      public void onError(Throwable e) {
        super.handleLoginInvalid(e);
      }

      @Override
      protected void doNext(UploadDevicePwdResponse response) {
        uploadPwdView.uploadDevicePwdSuccessed();
      }
    });
  }
}
