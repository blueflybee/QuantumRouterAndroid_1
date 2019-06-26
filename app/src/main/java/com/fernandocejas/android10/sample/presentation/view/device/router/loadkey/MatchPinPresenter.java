package com.fernandocejas.android10.sample.presentation.view.device.router.loadkey;

import android.os.Build;

import com.blueflybee.sm2sm3sm4.library.SM4Utils;
import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.utils.AppStringUtil;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo;
import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo.KeyBean;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.router.model.req.FirstGetKeyRequest;
import com.qtec.router.model.rsp.FirstGetKeyResponse;

import java.lang.reflect.Type;

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
public class MatchPinPresenter implements Presenter {


  private final UseCase mFirstGetKeyUseCase;
  private MatchPinView mMatchPinView;

  private static FirstGetKeyResponse sResponse;

  @Inject
  public MatchPinPresenter(
      @Named(RouterUseCaseComm.FIRST_GET_KEY) UseCase firstGetKeyUseCase) {
    this.mFirstGetKeyUseCase = checkNotNull(firstGetKeyUseCase, "firstGetKeyUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    mFirstGetKeyUseCase.unsubscribe();
  }

  public void setView(MatchPinView matchPinView) {
    this.mMatchPinView = matchPinView;
  }

  void firstGetKey() {
    FirstGetKeyRequest request = new FirstGetKeyRequest();
    request.setKeynumber(200);
    request.setDevicename(Build.MODEL);
    request.setKeytype(1);
    request.setRequestid(10);
    request.setSerialnumber(GlobleConstant.getgDeviceId());
//    request.setSerialnumber("KDJFINDKNFGG");
    QtecEncryptInfo<FirstGetKeyRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);

    mMatchPinView.showLoading();
    mFirstGetKeyUseCase.execute(encryptInfo, new AppSubscriber<FirstGetKeyResponse>(mMatchPinView) {

      @Override
      protected void doNext(FirstGetKeyResponse response) {
        sResponse = response;
        mMatchPinView.showFirstGetKeySuccess();
      }
    });

  }

  void decryptKey(String pin) {
    if (Strings.isNullOrEmpty(pin)) {
      mMatchPinView.showPinEmp();
      return;
    }
    try {
      SM4Utils sm4 = new SM4Utils();
      sm4.setSecretKey(pin);
      sm4.setHexString(true);

      String decryptData = sm4.decryptData_ECB(sResponse.getEncryptinfo(), false);
      if (Strings.isNullOrEmpty(decryptData)) {
        mMatchPinView.showPinVerifyFail();
        return;
      }
      int length = decryptData.length();
//      System.out.println("length = " + length);
//      System.out.println("decryptData++++++++++++++++++++ = " + decryptData.substring(0, 500));
//      System.out.println("decryptData++++++++++++++++++++ = " + decryptData.substring(length - 500));
      decryptData = AppStringUtil.cutTail(decryptData);
      System.out.println("decryptData++++++++++++++++++++ = " + decryptData);
      Type type = new TypeToken<QtecResult<LZKeyInfo<KeyBean>>>() {
      }.getType();
      QtecResult<LZKeyInfo<KeyBean>> qtecResult = new Gson().fromJson(decryptData, type);
      if (isDataIncomplete(qtecResult)) mMatchPinView.showPinVerifyFail();
      mMatchPinView.showPinVerifySuccess();
      mMatchPinView.goLoadKey(qtecResult.getData());
    } catch (Exception e) {
      e.printStackTrace();
      mMatchPinView.showPinVerifyFail();
    }
  }

  private boolean isDataIncomplete(QtecResult<LZKeyInfo<KeyBean>> qtecResult) {
    return qtecResult.getCode() != 0
        || qtecResult.getData() == null
        || qtecResult.getData().getKeylist() == null
        || qtecResult.getData().getKeylist().isEmpty();
  }
}
