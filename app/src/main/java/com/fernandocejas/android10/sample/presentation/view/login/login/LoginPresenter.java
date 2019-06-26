package com.fernandocejas.android10.sample.presentation.view.login.login;

import android.util.Log;

import com.alibaba.sdk.android.push.CommonCallback;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blueflybee.mqttdroidlibrary.MQQTUtils;
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
import com.fernandocejas.android10.sample.presentation.utils.MD5Util;
import com.google.common.base.Strings;
import com.qtec.mapp.model.req.LoginRequest;
import com.qtec.mapp.model.rsp.LoginResponse;
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
public class LoginPresenter implements Presenter {

  private final UseCase loginUseCase;
  private LoginView loginView;

  @Inject
  public LoginPresenter(@Named(CloudUseCaseComm.LOGIN) UseCase loginUseCase) {
    this.loginUseCase = checkNotNull(loginUseCase, "loginUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    loginUseCase.unsubscribe();
  }

  public void setView(LoginView loginView) {
    this.loginView = loginView;
  }

  public void login(String username, String password) {
    if (Strings.isNullOrEmpty(username)) {
      loginView.showUsernameEmp();
      return;
    }
    if (Strings.isNullOrEmpty(password)) {
      loginView.showPasswordEmp();
      return;
    }
    LoginRequest request = new LoginRequest();
    request.setUserPhone(username);
    request.setUserPassword(password);
    request.setDeviceSerialNumber(PrefConstant.getMsgDeviceID());
    request.setPlatform(AppConstant.PLATFORM_ANDROID);
    request.setDeviceToken(MQQTUtils.getAndroidID(loginView.getContext()));
    request.setPhoneModel(DeviceUtils.getManufacturer());

    QtecEncryptInfo<LoginRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);

    loginView.showLoading();
    loginUseCase.execute(encryptInfo, new AppSubscriber<LoginResponse>(loginView) {

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        if (e instanceof PasswordErrThreeTimesException) {
          loginView.showPasswordErrorThreeTimes(e);
        } else if (e instanceof PasswordErrMoreTimesException) {
          loginView.showPasswordErrorMoreTimes(e);
        }
      }

      @Override
      protected void doNext(LoginResponse response) {

        SPUtils spUtils = new SPUtils(PrefConstant.SP_NAME);
        PrefConstant.setUserId(response.getId());
        spUtils.put(PrefConstant.SP_USER_HEAD_IMG, response.getUserHeadImg());
        spUtils.put(PrefConstant.SP_USER_NICK_NAME, response.getUserNickName());
        spUtils.put(PrefConstant.SP_USER_UNIQUE_KEY, response.getUserUniqueKey());
        spUtils.put(PrefConstant.SP_USER_PHONE, response.getUserPhone());
        spUtils.put(PrefConstant.SP_USER_PWD, MD5Util.encryption(password));

        //登录成功之后再绑定账户(消息中心)
        System.out.println("AndroidApplication.getPushService() = " + AndroidApplication.getPushService());
        System.out.println("PrefConstant.getUserPhone() = " + PrefConstant.getUserPhone());
        AndroidApplication.getPushService().bindAccount(PrefConstant.getUserPhone(), new CommonCallback() {
          @Override
          public void onSuccess(String s) {
            Log.i("message bingAccount", "pushService bind account success!");
          }

          @Override
          public void onFailed(String s, String s1) {
            Log.i("message bingAccount", "pushService bind account failed!");
          }
        });

        CloudUrlPath.setToken(response.getToken());
        PrefConstant.putAppToken(response.getToken());
        PrefConstant.putCloudUrl(CloudRestApiImpl.getApiPostConnection().getUrl());
        loginView.openMain(response);
      }
    });
  }


}
