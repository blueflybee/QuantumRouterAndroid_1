package com.fernandocejas.android10.sample.presentation.view.login.register;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blueflybee.mqttdroidlibrary.MQQTUtils;
import com.fernandocejas.android10.sample.data.net.CloudRestApiImpl;
import com.fernandocejas.android10.sample.data.net.CloudUrlPath;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.mapp.model.req.GetIdCodeRequest;
import com.qtec.mapp.model.req.RegisterRequest;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;
import com.qtec.mapp.model.rsp.RegisterResponse;

import static com.google.common.base.Preconditions.*;

import javax.inject.Inject;
import javax.inject.Named;

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
public class RegisterPresenter implements Presenter {

  private final UseCase getIdCodeUseCase;
  private final UseCase registerUseCase;
  private RegisterView registerView;

  @Inject
  public RegisterPresenter(@Named(CloudUseCaseComm.GET_ID_CODE) UseCase getIdCodeUseCase,
                           @Named(CloudUseCaseComm.REGISTER) UseCase registerUseCase) {
    this.getIdCodeUseCase = checkNotNull(getIdCodeUseCase, "getIdCodeUseCase cannot be null!");
    this.registerUseCase = checkNotNull(registerUseCase, "registerUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getIdCodeUseCase.unsubscribe();
    registerUseCase.unsubscribe();
  }

  public void setView(RegisterView getIdCodeView) {
    this.registerView = getIdCodeView;
  }

  public void getIdCode(String username) {
    if (Strings.isNullOrEmpty(username)) {
      registerView.showUserPhoneEmp();
      return;
    }
    GetIdCodeRequest request = new GetIdCodeRequest();
    request.setUserPhone(username);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    registerView.showLoading();
    getIdCodeUseCase.execute(encryptInfo, new AppSubscriber<GetIdCodeResponse>(registerView) {
      @Override
      protected void doNext(GetIdCodeResponse response) {
        registerView.onGetIdCodeSuccess();

      }
    });
  }

  public void register(String username, String smsCode, String pwd) {
    if (Strings.isNullOrEmpty(username)) {
      registerView.showUserPhoneEmp();
      return;
    }

    if (Strings.isNullOrEmpty(smsCode)) {
      registerView.showIdCodeEmp();
      return;
    }

    if (Strings.isNullOrEmpty(pwd)) {
      registerView.showPasswordEmp();
      return;
    }

    RegisterRequest request = new RegisterRequest();
    request.setUserPhone(username);
    request.setSmsCode(smsCode);
    request.setUserPassword(pwd);
    request.setDeviceSerialNumber(PrefConstant.getMsgDeviceID());
    request.setPlatform(AppConstant.PLATFORM_ANDROID);
    request.setDeviceToken(MQQTUtils.getAndroidID(registerView.getContext()));
    request.setPhoneModel(DeviceUtils.getManufacturer());
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    registerView.showLoading();

    registerUseCase.execute(encryptInfo, new AppSubscriber<RegisterResponse>(registerView) {
      @Override
      protected void doNext(RegisterResponse response) {
        SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
        PrefConstant.setUserId(response.getId());
        spUtils.put(PrefConstant.SP_USER_HEAD_IMG, response.getUserHeadImg());
        spUtils.put(PrefConstant.SP_USER_NICK_NAME, response.getUserNickName());
        spUtils.put(PrefConstant.SP_USER_UNIQUE_KEY, response.getUserUniqueKey());
        spUtils.put(PrefConstant.SP_USER_PHONE, response.getUserPhone());
        PrefConstant.setUserPwd(pwd);
        CloudUrlPath.setToken(response.getToken());
        PrefConstant.putAppToken(response.getToken());
        PrefConstant.putCloudUrl(CloudRestApiImpl.getApiPostConnection().getUrl());

        registerView.showRegisterSuccess(response);
      }
    });

  }
}
