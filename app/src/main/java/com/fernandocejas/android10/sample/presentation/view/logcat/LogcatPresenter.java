package com.fernandocejas.android10.sample.presentation.view.logcat;

import android.util.Log;

import com.alibaba.sdk.android.push.CommonCallback;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.exception.PasswordErrMoreTimesException;
import com.fernandocejas.android10.sample.data.exception.PasswordErrThreeTimesException;
import com.fernandocejas.android10.sample.data.net.CloudRestApiImpl;
import com.fernandocejas.android10.sample.data.net.CloudUrlPath;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.AndroidApplication;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.utils.MD5Util;
import com.fernandocejas.android10.sample.presentation.utils.VersionUtil;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginView;
import com.google.common.base.Strings;
import com.qtec.mapp.model.req.LoginRequest;
import com.qtec.mapp.model.req.UploadLogcatRequest;
import com.qtec.mapp.model.rsp.LoginResponse;
import com.qtec.mapp.model.rsp.UploadLogcatResponse;
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
public class LogcatPresenter implements Presenter {

  private final UseCase uploadLogcatUseCase;
  private LogcatView logcatView;

  @Inject
  public LogcatPresenter(@Named(CloudUseCaseComm.UPLOAD_LOGCAT) UseCase loginUseCase) {
    this.uploadLogcatUseCase = checkNotNull(loginUseCase, "uploadLogcatUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    uploadLogcatUseCase.unsubscribe();
  }

  public void setView(LogcatView logcatView) {
    this.logcatView = logcatView;
  }

  public void uploadLogcat() {
    UploadLogcatRequest request = new UploadLogcatRequest();
    request.setPhone(PrefConstant.getUserPhone());
    request.setBrand(DeviceUtils.getManufacturer());
    request.setModel(DeviceUtils.getModel());
    request.setAppversion(VersionUtil.getVersionName(logcatView.getContext()));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    uploadLogcatUseCase.execute(encryptInfo, new AppSubscriber<UploadLogcatResponse>(logcatView) {

      @Override
      public void onError(Throwable e) {
        if (uploadLogcatUseCase != null) {
          uploadLogcatUseCase.unsubscribe();
          uploadLogcat();
        }
      }

      @Override
      public void onNext(UploadLogcatResponse uploadLogcatResponse) {
        doNext(uploadLogcatResponse);
      }

      @Override
      protected void doNext(UploadLogcatResponse response) {
      }
    });
  }


}
