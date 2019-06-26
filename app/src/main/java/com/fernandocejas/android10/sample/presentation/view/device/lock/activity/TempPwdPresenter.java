package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.text.TextUtils;

import com.blankj.utilcode.util.EncryptUtils;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.MyStringUtil;
import com.google.common.base.Preconditions;
import com.qtec.mapp.model.req.AddTempPwdRequest;
import com.qtec.mapp.model.req.GetTempPwdRequest;
import com.qtec.mapp.model.req.QueryTempPwdRequest;
import com.qtec.mapp.model.rsp.AddTempPwdResponse;
import com.qtec.mapp.model.rsp.GetTempPwdResponse;
import com.qtec.mapp.model.rsp.QueryTempPwdResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-9-11
 *     desc   : presenter
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class TempPwdPresenter implements Presenter {

  private final UseCase mAddTempPwd;
  private final UseCase mQueryTempPwd;
  private final UseCase mGetTempPwd;
  private TempPwdView mView;
  private static final String UTF_8 = "UTF-8";

  @Inject
  public TempPwdPresenter(@Named(CloudUseCaseComm.ADD_TEMP_PWD) UseCase pAddTempPwd,
                          @Named(CloudUseCaseComm.QUERY_TEMP_PWD) UseCase pQueryTempPwd,
                          @Named(CloudUseCaseComm.GET_TEMP_PWD) UseCase pGetTempPwd) {
    this.mAddTempPwd = Preconditions.checkNotNull(pAddTempPwd, "pAddTempPwd is null");
    this.mQueryTempPwd = Preconditions.checkNotNull(pQueryTempPwd, "pQueryTempPwd is null");
    this.mGetTempPwd = Preconditions.checkNotNull(pGetTempPwd, "pGetTempPwd is null");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    mAddTempPwd.unsubscribe();
    mQueryTempPwd.unsubscribe();
    mGetTempPwd.unsubscribe();
  }

  public void setView(TempPwdView view) {
    this.mView = view;
  }

  void addTempPwd(String deviceId, String[] tempPasswords, String bleName) {
    AddTempPwdRequest request = new AddTempPwdRequest();
    request.setDeviceSerialNo(deviceId);
    request.setTempPasswords(encryptAES(tempPasswords, bleName));
    QtecEncryptInfo<AddTempPwdRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    mView.showLoading();
    mAddTempPwd.execute(encryptInfo, new AppSubscriber<AddTempPwdResponse>(mView) {
      @Override
      protected void doNext(AddTempPwdResponse response) {
        mView.onAddTempPwdSuccess(response);
      }
    });
  }

  public void queryTempPwd(String deviceId) {
    QueryTempPwdRequest request = new QueryTempPwdRequest();
    request.setDeviceSerialNo(deviceId);
    QtecEncryptInfo<QueryTempPwdRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    mView.showLoading();
    mQueryTempPwd.execute(encryptInfo, new AppSubscriber<QueryTempPwdResponse>(mView) {
      @Override
      protected void doNext(QueryTempPwdResponse response) {
        mView.onQueryTempPwd(response);
      }
    });
  }

  void getTempPwd(String deviceId, String bleName) {
    GetTempPwdRequest request = new GetTempPwdRequest();
    request.setDeviceSerialNo(deviceId);
    QtecEncryptInfo<GetTempPwdRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    mView.showLoading();
    mGetTempPwd.execute(encryptInfo, new AppSubscriber<GetTempPwdResponse>(mView) {
      @Override
      protected void doNext(GetTempPwdResponse response) {
        String pwd = decryptAES(response.getTempPassword(), bleName);
        if (TextUtils.isEmpty(pwd)) {
          mView.onGetTempPwdFail();
        } else {
          mView.onGetTempPwdSuccess(pwd);
        }

      }
    });
  }


  private String encryptAES(String[] tempPasswords, String bleName) {
    try {
      System.out.println("bleName = " + bleName);
//    FA67B6B7F0D54B6203400F2A291C4D72
      String md5 = EncryptUtils.encryptMD5ToString(bleName);
      System.out.println("md5 = " + md5);
      byte[] key = BleHexUtil.hexStringToBytes(md5);
//      printHexString(key);
      StringBuilder sb = new StringBuilder();
      for (String aPwd : tempPasswords) {
        String tempPassword = aPwd + "          ";
        byte[] data = MyStringUtil.strToBytes(tempPassword, UTF_8);
        byte[] bytes = EncryptUtils.encryptAES2Base64(data, key);
        String result = MyStringUtil.bytesToStr(bytes, UTF_8);
        sb.append(result).append(",");
      }
      System.out.println("sb = " + sb);
      return sb.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  private String decryptAES(String pwd, String bleName) {
    try {
      if (TextUtils.isEmpty(pwd)) return "";
      String md5 = EncryptUtils.encryptMD5ToString(bleName);
      byte[] key = BleHexUtil.hexStringToBytes(md5);
      byte[] data = MyStringUtil.strToBytes(pwd, UTF_8);
      byte[] decrypt = EncryptUtils.decryptBase64AES(data, key);
      String aTempPwd = MyStringUtil.bytesToStr(decrypt, UTF_8).trim();
      System.out.println("aTempPwd = " + aTempPwd);
      return aTempPwd;
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  private void printHexString(byte[] b) {
    for (int i = 0; i < b.length; i++) {
      String hex = Integer.toHexString(b[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      System.out.println("hex = " + hex.toUpperCase());
    }
  }
}
