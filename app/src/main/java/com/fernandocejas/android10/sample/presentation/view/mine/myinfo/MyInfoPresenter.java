package com.fernandocejas.android10.sample.presentation.view.mine.myinfo;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.fernandocejas.android10.sample.data.exception.LoginInvalidException;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.OssUtil;
import com.fernandocejas.android10.sample.presentation.utils.QtecStringUtil;
import com.fernandocejas.android10.sample.presentation.view.message.mqtt.MQTTManager;
import com.qtec.mapp.model.req.LogoutRequest;
import com.qtec.mapp.model.req.ModifyUserInfoRequest;
import com.qtec.mapp.model.rsp.GetStsTokenResponse;
import com.qtec.mapp.model.rsp.GetStsTokenResponse.*;
import com.qtec.mapp.model.rsp.LogoutResponse;
import com.qtec.mapp.model.rsp.ModifyUserInfoResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

import static com.fernandocejas.android10.sample.presentation.utils.OssUtil.STS_SERVER;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class MyInfoPresenter implements Presenter {

  private final UseCase modifyUserInfoUseCase;
  private final UseCase logoutUseCase;
  private final UseCase getStsTokenUseCase;
  private MyInfoView myInfoView;
  private String mImageUrl;

  @Inject
  public MyInfoPresenter(@Named(CloudUseCaseComm.MODIFY_USER_INFO) UseCase modifyUserInfoUseCase,
                         @Named(CloudUseCaseComm.LOGOUT) UseCase logoutUseCase,
                         @Named(CloudUseCaseComm.GET_STS_TOKEN) UseCase getStsTokenUseCase
  ) {
    this.modifyUserInfoUseCase = checkNotNull(modifyUserInfoUseCase, "modifyUserInfoUseCase cannot be null!");
    this.logoutUseCase = checkNotNull(logoutUseCase, "logoutUseCase cannot be null!");
    this.getStsTokenUseCase = checkNotNull(getStsTokenUseCase, "getStsTokenUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    modifyUserInfoUseCase.unsubscribe();
    logoutUseCase.unsubscribe();
    getStsTokenUseCase.unsubscribe();
  }

  public void setView(MyInfoView myInfoView) {
    this.myInfoView = myInfoView;
  }

  public void modifyUserInfo(String username, String headImage, String userNickName) {
//        String userPhone = encryptInfo.getData().getUserPhone();
//        if (Strings.isNullOrEmpty(userPhone)) {
//            myInfoView.showUserPhoneEmp();
//            return;
//        }
//
//        String userHeadImg = encryptInfo.getData().getUserHeadImg();
//        if (Strings.isNullOrEmpty(userHeadImg)) {
//            myInfoView.showUserHeadImgEmp();
//            return;
//        }
//
//        String userPassword = encryptInfo.getData().getUserNickName();
//        if (Strings.isNullOrEmpty(userPassword)) {
//            myInfoView.showUserNickNameEmp();
//            return;
//        }
    ModifyUserInfoRequest request = new ModifyUserInfoRequest();
    request.setUserPhone(username);
    request.setUserHeadImg(headImage);
    request.setUserNickName(userNickName);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    myInfoView.showLoading();

    modifyUserInfoUseCase.execute(encryptInfo, new AppSubscriber<ModifyUserInfoResponse>(myInfoView) {

      @Override
      protected void doNext(ModifyUserInfoResponse response) {
        PrefConstant.putUserNickName(userNickName);
        myInfoView.showModifySuccess();
      }
    });

  }

  public void logout(String username) {
    LogoutRequest request = new LogoutRequest();
    request.setUserPhone(username);
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);

    myInfoView.showLoading();

    logoutUseCase.execute(encryptInfo, new AppSubscriber<LogoutResponse>(myInfoView) {
      @Override
      public void onStart() {
        super.onStart();
        MQTTManager.instance().stop();
        PrefConstant.putAppToken("");
//        PrefConstant.putCloudUrl("");
      }

      @Override
      public void onError(Throwable e) {
        super.onError(e);
        myInfoView.openLogin(null);
      }

      @Override
      protected void doNext(LogoutResponse response) {
        myInfoView.openLogin(response);
      }
    });

  }

  public void getStsToken(String compressPath) {

    myInfoView.showLoading();
    getStsTokenUseCase.execute(new QtecEncryptInfo(), new AppSubscriber<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>>(myInfoView) {
      @Override
      protected void doNext(GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean> response) {

        String objectKey = PrefConstant.getUserUniqueKey("0") + "/" + QtecStringUtil.getUUID() + ".jpg";
        myInfoView.showLoading();
        OssUtil.upload(myInfoView.getContext(), response.getCredentials(), objectKey, compressPath,
            new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {


              @Override
              public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                Log.d("ObjectKey", request.getObjectKey());

                mImageUrl = STS_SERVER + request.getObjectKey();

                myInfoView.uploadHeadImageSuccess(mImageUrl);
              }

              @Override
              public void onFailure(PutObjectRequest putObjectRequest, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                  // 本地异常如网络异常等
                  clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                  // 服务异常
                  Log.e("ErrorCode", serviceException.getErrorCode());
                  Log.e("RequestId", serviceException.getRequestId());
                  Log.e("HostId", serviceException.getHostId());
                  Log.e("RawMessage", serviceException.getRawMessage());
                }

                myInfoView.uploadHeadImageFail();
              }
            },
            new OSSProgressCallback<PutObjectRequest>() {
              @Override
              public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);

              }
            });
      }
    });

  }

  public String getImageUrl() {
    return mImageUrl;
  }
}
