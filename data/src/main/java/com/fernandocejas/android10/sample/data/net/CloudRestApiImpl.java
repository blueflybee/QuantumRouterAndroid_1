/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.data.net;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fernandocejas.android10.sample.data.exception.CloudNoSuchLockException;
import com.fernandocejas.android10.sample.data.exception.LoginInvalidException;
import com.fernandocejas.android10.sample.data.exception.NetworkConnectionException;
import com.fernandocejas.android10.sample.data.exception.PasswordErrMoreTimesException;
import com.fernandocejas.android10.sample.data.exception.PasswordErrThreeTimesException;
import com.fernandocejas.android10.sample.data.utils.CloudConverter;
import com.fernandocejas.android10.sample.data.utils.IConverter;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.cateye.model.response.DeleteDoorBellRecordResponse;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailResponse;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadResponse;
import com.qtec.mapp.model.rsp.AddLockDoorCardResponse;
import com.qtec.mapp.model.rsp.AddLockFpResponse;
import com.qtec.mapp.model.rsp.AddLockPwdResponse;
import com.qtec.mapp.model.rsp.AddTempPwdResponse;
import com.qtec.mapp.model.rsp.AdjustLockVolumeResponse;
import com.qtec.mapp.model.rsp.CheckAppVersionResponse;
import com.qtec.mapp.model.rsp.CheckLiteVersionResponse;
import com.qtec.mapp.model.rsp.CheckLockVersionResponse;
import com.qtec.mapp.model.rsp.CloudUnbindRouterLockResponse;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.ConnectLockResponse;
import com.qtec.mapp.model.rsp.CreateRouterGroupResponse;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.mapp.model.rsp.DeleteLockDoorCardResponse;
import com.qtec.mapp.model.rsp.DeleteLockFpResponse;
import com.qtec.mapp.model.rsp.DeleteLockPwdResponse;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.DeleteMsgResponse;
import com.qtec.mapp.model.rsp.FeedBackResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;
import com.qtec.mapp.model.rsp.GetDeviceCountResponse;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.GetExtraNetPortResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.GetLiteUpdateResponse;
import com.qtec.mapp.model.rsp.GetLockListResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetLockVolumeResponse;
import com.qtec.mapp.model.rsp.GetMemRemarkNameResponse;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;
import com.qtec.mapp.model.rsp.GetMsgListResponse;
import com.qtec.mapp.model.rsp.GetMsgOtherListResponse;
import com.qtec.mapp.model.rsp.GetPassagewayModeResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;
import com.qtec.mapp.model.rsp.GetRouterGroupsResponse;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;
import com.qtec.mapp.model.rsp.GetStsTokenResponse;
import com.qtec.mapp.model.rsp.GetStsTokenResponse.*;
import com.qtec.mapp.model.rsp.GetTempPwdResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;
import com.qtec.mapp.model.rsp.GetUnlockModeResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;
import com.qtec.mapp.model.rsp.GetUsersOfLockResponse;
import com.qtec.mapp.model.rsp.IntelDevInfoModifyResponse;
import com.qtec.mapp.model.rsp.IntelDeviceDetailResponse;
import com.qtec.mapp.model.rsp.IntelDeviceListResponse;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;
import com.qtec.mapp.model.rsp.ModifyDevNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockDoorCardNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockFpNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockPwdNameResponse;
import com.qtec.mapp.model.rsp.ModifyPassagewayModeResponse;
import com.qtec.mapp.model.rsp.ModifyRouterDescResponse;
import com.qtec.mapp.model.rsp.ModifyRouterGroupResponse;
import com.qtec.mapp.model.rsp.ModifyUnlockModeResponse;
import com.qtec.mapp.model.rsp.PostInvitationResponse;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.QueryLockedCatResponse;
import com.qtec.mapp.model.rsp.QueryTempPwdResponse;
import com.qtec.mapp.model.rsp.SetMsgReadResponse;
import com.qtec.mapp.model.rsp.TransmitResponse;
import com.qtec.mapp.model.rsp.UnbindCatLockResponse;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;
import com.qtec.mapp.model.rsp.UnbindLockOfAdminResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.mapp.model.rsp.UpdateLiteResponse;
import com.qtec.mapp.model.rsp.UpdateLockVersionResponse;
import com.qtec.mapp.model.rsp.UploadDevicePwdResponse;
import com.qtec.mapp.model.rsp.UploadLogcatResponse;
import com.qtec.mapp.model.rsp.UploadMsgDeviceIDResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.mapp.model.rsp.GetAgreementResponse;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;
import com.qtec.mapp.model.rsp.GetMyAdviceResponse;
import com.qtec.mapp.model.rsp.GetQuestionDetailResponse;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;
import com.qtec.mapp.model.rsp.GetUpdateFeedBackResponse;
import com.qtec.mapp.model.rsp.GetfeedBackResponse;
import com.qtec.mapp.model.rsp.LoginResponse;
import com.qtec.mapp.model.rsp.LogoutResponse;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ModifyPwdResponse;
import com.qtec.mapp.model.rsp.ModifyUserInfoResponse;
import com.qtec.mapp.model.rsp.RegisterResponse;
import com.qtec.mapp.model.rsp.ResetPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ResetPwdResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;

/**
 * {@link CloudRestApi} implementation for retrieving data from the network.
 */
@Singleton
public class CloudRestApiImpl implements CloudRestApi {

  //  /** *输入密码连续错误三次以上 */ PASSWORD_ERROR_THREE_TIMES("输入密码连续错误三次以上,是否重置密码",10080),
//      /** *短时间内登录次数过多 */ PASSWORD_ERROR_MORE_TIMES("短时间内登录次数过多,请稍后重试",10081)

  public static final int PASSWORD_ERROR_THREE_TIMES = 10080;
  public static final int PASSWORD_ERROR_MORE_TIMES = 10081;
  public static final int NO_SUCH_LOCK = 10;
  public static final String ACTION_ROUTER_KEY_INVALID = "android.intent.action.KeyInvalidReceiver";
  public static final int LOGIN_INVALID = 10086;

  private static IPostConnection apiPostConnection;
  private final Context context;

  private final IConverter mCloudConverter = new CloudConverter();

  /**
   * Constructor of the class
   *
   * @param context {@link Context}.
   */
  @Inject
  public CloudRestApiImpl(Context context) {

    if (context == null) {
      throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
    }
    this.context = context;
  }

  public static IPostConnection getApiPostConnection() {
    return apiPostConnection;
  }

  public static void setApiPostConnection(IPostConnection apiPostConnection) {
    CloudRestApiImpl.apiPostConnection = apiPostConnection;
  }

  @Override
  public Observable<LoginResponse> login(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<LoginResponse>() {

      @Override
      public void call(Subscriber<? super LoginResponse> subscriber) {

        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_LOGIN);
        Type type = new TypeToken<QtecResult<LoginResponse>>() {
        }.getType();
        LoginResponse response = (LoginResponse) requestService(encryptInfo, type, subscriber, 0, false);
//                apiPostConnection.setSessionId(response.getSessionId());
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ResetPwdResponse> resetPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ResetPwdResponse>() {

      @Override
      public void call(Subscriber<? super ResetPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_RESET_PWD_FORGET);
        Type type = new TypeToken<QtecResult<ResetPwdResponse>>() {
        }.getType();
        ResetPwdResponse response = (ResetPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ResetPwdGetIdCodeResponse> resetPwdGetIdCode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ResetPwdGetIdCodeResponse>() {

      @Override
      public void call(Subscriber<? super ResetPwdGetIdCodeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_RESET_PWD_GET_ID_CODE);
        Type type = new TypeToken<QtecResult<ResetPwdGetIdCodeResponse>>() {
        }.getType();
        ResetPwdGetIdCodeResponse response = (ResetPwdGetIdCodeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<RegisterResponse> register(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<RegisterResponse>() {

      @Override
      public void call(Subscriber<? super RegisterResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_REGISTER);
        Type type = new TypeToken<QtecResult<RegisterResponse>>() {
        }.getType();
        RegisterResponse response = (RegisterResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetIdCodeResponse> getIdCode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetIdCodeResponse>() {

      @Override
      public void call(Subscriber<? super GetIdCodeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_ID_CODE);
        Type type = new TypeToken<QtecResult<GetIdCodeResponse>>() {
        }.getType();
        GetIdCodeResponse response = (GetIdCodeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetQuestionListResponse>> getQuestionList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetQuestionListResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetQuestionListResponse>> subscriber) {

        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_QUESTION_LIST);
        Type type = new TypeToken<QtecResult<List<GetQuestionListResponse>>>() {
        }.getType();
        List<GetQuestionListResponse> response = (List<GetQuestionListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetAgreementResponse> getUserAgreement(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetAgreementResponse>() {
      @Override
      public void call(Subscriber<? super GetAgreementResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_AGREEMENT);
        Type type = new TypeToken<QtecResult<GetAgreementResponse>>() {
        }.getType();
        GetAgreementResponse response = (GetAgreementResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetMemRemarkNameResponse> getMemRemarkName(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetMemRemarkNameResponse>() {
      @Override
      public void call(Subscriber<? super GetMemRemarkNameResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_MEM_REMARK_NAME);
        Type type = new TypeToken<QtecResult<GetMemRemarkNameResponse>>() {
        }.getType();
        GetMemRemarkNameResponse response = (GetMemRemarkNameResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetAgreementResponse> getUserSerectAgreement(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetAgreementResponse>() {
      @Override
      public void call(Subscriber<? super GetAgreementResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_AGREEMENT);
        Type type = new TypeToken<QtecResult<GetAgreementResponse>>() {
        }.getType();
        GetAgreementResponse response = (GetAgreementResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetQuestionDetailResponse> getQuestionDeatil(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetQuestionDetailResponse>() {
      @Override
      public void call(Subscriber<? super GetQuestionDetailResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_QUESTION_DETAIL);
        Type type = new TypeToken<QtecResult<GetQuestionDetailResponse>>() {
        }.getType();
        GetQuestionDetailResponse response = (GetQuestionDetailResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetMyAdviceResponse>> getMyAdvice(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetMyAdviceResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetMyAdviceResponse>> subscriber) {

        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_MY_ADVICE_LIST);
        Type type = new TypeToken<QtecResult<List<GetMyAdviceResponse>>>() {
        }.getType();
        List<GetMyAdviceResponse> response = (List<GetMyAdviceResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<FeedBackResponse<List<FeedBackResponse.ReplyContent>>> getAdviceDetail(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<FeedBackResponse<List<FeedBackResponse.ReplyContent>>>() {
      @Override
      public void call(Subscriber<? super FeedBackResponse<List<FeedBackResponse.ReplyContent>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_ADVICE_DETAIL);
        Type type = new TypeToken<QtecResult<FeedBackResponse<List<FeedBackResponse.ReplyContent>>>>() {
        }.getType();
        FeedBackResponse<List<FeedBackResponse.ReplyContent>> response =
            (FeedBackResponse<List<FeedBackResponse.ReplyContent>>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });

  }

  @Override
  public Observable<GetfeedBackResponse> getFeedBack(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetfeedBackResponse>() {
      @Override
      public void call(Subscriber<? super GetfeedBackResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_POST_FEED_BACK);
        Type type = new TypeToken<QtecResult<GetfeedBackResponse>>() {
        }.getType();
        GetfeedBackResponse response = (GetfeedBackResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyUserInfoResponse> modifyUserInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyUserInfoResponse>() {
      @Override
      public void call(Subscriber<? super ModifyUserInfoResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_USER_INFO);
        Type type = new TypeToken<QtecResult<ModifyUserInfoResponse>>() {
        }.getType();
        ModifyUserInfoResponse response = (ModifyUserInfoResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<LogoutResponse> logout(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<LogoutResponse>() {
      @Override
      public void call(Subscriber<? super LogoutResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_LOGOUT);
        Type type = new TypeToken<QtecResult<LogoutResponse>>() {
        }.getType();
        LogoutResponse response = (LogoutResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetUpdateFeedBackResponse> getUpdateFeedBack(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetUpdateFeedBackResponse>() {
      @Override
      public void call(Subscriber<? super GetUpdateFeedBackResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UPDATE_FEED_BACK);
        Type type = new TypeToken<QtecResult<GetUpdateFeedBackResponse>>() {
        }.getType();
        GetUpdateFeedBackResponse response = (GetUpdateFeedBackResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyPwdGetIdCodeResponse> modifyPwdGetIdCode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyPwdGetIdCodeResponse>() {

      @Override
      public void call(Subscriber<? super ModifyPwdGetIdCodeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_RESET_PWD_GET_ID_CODE);
        Type type = new TypeToken<QtecResult<ModifyPwdGetIdCodeResponse>>() {
        }.getType();
        ModifyPwdGetIdCodeResponse response = (ModifyPwdGetIdCodeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyPwdResponse> modifyPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyPwdResponse>() {

      @Override
      public void call(Subscriber<? super ModifyPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_PWD);
        Type type = new TypeToken<QtecResult<ModifyPwdResponse>>() {
        }.getType();
        ModifyPwdResponse response = (ModifyPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetDeviceCountResponse> getDeviceCount(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetDeviceCountResponse>() {

      @Override
      public void call(Subscriber<? super GetDeviceCountResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_DEVICE_COUNT);
        Type type = new TypeToken<QtecResult<GetDeviceCountResponse>>() {
        }.getType();
        GetDeviceCountResponse response = (GetDeviceCountResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UploadMsgDeviceIDResponse> uploadMsgId(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UploadMsgDeviceIDResponse>() {

      @Override
      public void call(Subscriber<? super UploadMsgDeviceIDResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UPLOAD_MSG_ID);
        Type type = new TypeToken<QtecResult<UploadMsgDeviceIDResponse>>() {
        }.getType();
        UploadMsgDeviceIDResponse response = (UploadMsgDeviceIDResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetDevTreeResponse<List<DeviceBean>>>> getDevTree(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetDevTreeResponse<List<DeviceBean>>>>() {

      @Override
      public void call(Subscriber<? super List<GetDevTreeResponse<List<DeviceBean>>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_DEV_TREE);
        Type type = new TypeToken<QtecResult<List<GetDevTreeResponse<List<DeviceBean>>>>>() {
        }.getType();
        List<GetDevTreeResponse<List<DeviceBean>>> response =
            (List<GetDevTreeResponse<List<DeviceBean>>>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetShareMemListResponse>> getSharedMemList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetShareMemListResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetShareMemListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_SHARED_MEM_LIST);
        Type type = new TypeToken<QtecResult<List<GetShareMemListResponse>>>() {
        }.getType();
        List<GetShareMemListResponse> response = (List<GetShareMemListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetLockListResponse>> getLockList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetLockListResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetLockListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_LOCK_LIST);
        Type type = new TypeToken<QtecResult<List<GetLockListResponse>>>() {
        }.getType();
        List<GetLockListResponse> response = (List<GetLockListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<QueryCatLockResponse> queryCatLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryCatLockResponse>() {

      @Override
      public void call(Subscriber<? super QueryCatLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CAT_LOCK);
        Type type = new TypeToken<QtecResult<QueryCatLockResponse>>() {
        }.getType();
        QueryCatLockResponse response = (QueryCatLockResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> getDoorBeerMsgList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>>() {

      @Override
      public void call(Subscriber<? super GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_DOOR_BEER_MSG_LIST);
        Type type = new TypeToken<QtecResult<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>>>() {
        }.getType();
        GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>> response = (GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetDoorBellRecordDetailResponse> getDoorBeerMsgDetail(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetDoorBellRecordDetailResponse>() {

      @Override
      public void call(Subscriber<? super GetDoorBellRecordDetailResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_DOOR_BEER_MSG_DETAIL);
        Type type = new TypeToken<QtecResult<GetDoorBellRecordDetailResponse>>() {
        }.getType();
        GetDoorBellRecordDetailResponse response = (GetDoorBellRecordDetailResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DeleteDoorBellRecordResponse> deleteDoorBeerMsg(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteDoorBellRecordResponse>() {

      @Override
      public void call(Subscriber<? super DeleteDoorBellRecordResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_DELETE_DOOR_BEER_MSG);
        Type type = new TypeToken<QtecResult<DeleteDoorBellRecordResponse>>() {
        }.getType();
        DeleteDoorBellRecordResponse response = (DeleteDoorBellRecordResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<SetAllDoorBellRecordReadResponse> setAllDoorBeerMsgIsRead(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetAllDoorBellRecordReadResponse>() {

      @Override
      public void call(Subscriber<? super SetAllDoorBellRecordReadResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_SET_DOOR_BEER_MSG_IS_READ);
        Type type = new TypeToken<QtecResult<SetAllDoorBellRecordReadResponse>>() {
        }.getType();
        SetAllDoorBellRecordReadResponse response = (SetAllDoorBellRecordReadResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ConnectLockResponse> connectLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ConnectLockResponse>() {

      @Override
      public void call(Subscriber<? super ConnectLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CONNECT_LOCK);
        Type type = new TypeToken<QtecResult<ConnectLockResponse>>() {
        }.getType();
        ConnectLockResponse response = (ConnectLockResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<QueryLockedCatResponse> queryLockedCat(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryLockedCatResponse>() {

      @Override
      public void call(Subscriber<? super QueryLockedCatResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_QUERY_LOCKED_CAT);
        Type type = new TypeToken<QtecResult<QueryLockedCatResponse>>() {
        }.getType();
        QueryLockedCatResponse response = (QueryLockedCatResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UnbindCatLockResponse> unbindCatLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UnbindCatLockResponse>() {

      @Override
      public void call(Subscriber<? super UnbindCatLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UNBIND_CAT_LOCK);
        Type type = new TypeToken<QtecResult<UnbindCatLockResponse>>() {
        }.getType();
        UnbindCatLockResponse response = (UnbindCatLockResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UploadDevicePwdResponse> uploadDevicePwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UploadDevicePwdResponse>() {

      @Override
      public void call(Subscriber<? super UploadDevicePwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UPLOAD_DEVICE_PWD);
        Type type = new TypeToken<QtecResult<UploadDevicePwdResponse>>() {
        }.getType();
        UploadDevicePwdResponse response = (UploadDevicePwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DealInvitationResponse> dealInvitation(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DealInvitationResponse>() {

      @Override
      public void call(Subscriber<? super DealInvitationResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.DEAL_INVITATION);
        Type type = new TypeToken<QtecResult<DealInvitationResponse>>() {
        }.getType();
        DealInvitationResponse response = (DealInvitationResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<PostInvitationResponse> postInvitation(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<PostInvitationResponse>() {

      @Override
      public void call(Subscriber<? super PostInvitationResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_INVITATION);
        Type type = new TypeToken<QtecResult<PostInvitationResponse>>() {
        }.getType();
        PostInvitationResponse response = (PostInvitationResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetMsgOtherListResponse>> getMsgList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetMsgOtherListResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetMsgOtherListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MSG_LIST);
        Type type = new TypeToken<QtecResult<List<GetMsgOtherListResponse>>>() {
        }.getType();
        List<GetMsgOtherListResponse> response = (List<GetMsgOtherListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetMsgListResponse<GetMsgListResponse.messageContent>>> getMsgShareList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetMsgListResponse<GetMsgListResponse.messageContent>>>() {

      @Override
      public void call(Subscriber<? super List<GetMsgListResponse<GetMsgListResponse.messageContent>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MSG_LIST);
        Type type = new TypeToken<QtecResult<List<GetMsgListResponse<GetMsgListResponse.messageContent>>>>() {
        }.getType();
        List<GetMsgListResponse<GetMsgListResponse.messageContent>> response =
            (List<GetMsgListResponse<GetMsgListResponse.messageContent>>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<IntelDeviceListResponse>> getIntelDeviceList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<IntelDeviceListResponse>>() {

      @Override
      public void call(Subscriber<? super List<IntelDeviceListResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_INTEL_DEVICE_LIST);
        Type type = new TypeToken<QtecResult<List<IntelDeviceListResponse>>>() {
        }.getType();
        List<IntelDeviceListResponse> response = (List<IntelDeviceListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetExtraNetPortResponse> getExtraNetPort(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetExtraNetPortResponse>() {

      @Override
      public void call(Subscriber<? super GetExtraNetPortResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_EXTRACT_NET_PORT);
        Type type = new TypeToken<QtecResult<GetExtraNetPortResponse>>() {
        }.getType();
        GetExtraNetPortResponse response = (GetExtraNetPortResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<IntelDeviceDetailResponse> getIntelDeviceDetail(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<IntelDeviceDetailResponse>() {

      @Override
      public void call(Subscriber<? super IntelDeviceDetailResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_INTEL_DEVICE_DETAIL);
        Type type = new TypeToken<QtecResult<IntelDeviceDetailResponse>>() {
        }.getType();
        IntelDeviceDetailResponse response = (IntelDeviceDetailResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DeleteMsgResponse> deleteMsg(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteMsgResponse>() {

      @Override
      public void call(Subscriber<? super DeleteMsgResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.DELETE_MSG);
        Type type = new TypeToken<QtecResult<DeleteMsgResponse>>() {
        }.getType();
        DeleteMsgResponse response = (DeleteMsgResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetMsgCountResponse> getMsgCount(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetMsgCountResponse>() {

      @Override
      public void call(Subscriber<? super GetMsgCountResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.GET_MSG_COUNT);
        Type type = new TypeToken<QtecResult<GetMsgCountResponse>>() {
        }.getType();
        GetMsgCountResponse response = (GetMsgCountResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<SetMsgReadResponse> setMsgRead(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<SetMsgReadResponse>() {

      @Override
      public void call(Subscriber<? super SetMsgReadResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.SET_MSG_READ);
        Type type = new TypeToken<QtecResult<SetMsgReadResponse>>() {
        }.getType();
        SetMsgReadResponse response = (SetMsgReadResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UnbindIntelDevResponse> unbind(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UnbindIntelDevResponse>() {

      @Override
      public void call(Subscriber<? super UnbindIntelDevResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UNBIND_INTEL_DEV);
        Type type = new TypeToken<QtecResult<UnbindIntelDevResponse>>() {
        }.getType();
        UnbindIntelDevResponse response = (UnbindIntelDevResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<CommitAddRouterInfoResponse> commitAddRouterInfo(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<CommitAddRouterInfoResponse>() {

      @Override
      public void call(Subscriber<? super CommitAddRouterInfoResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_COMMIT_ADD_ROUTER_INFO);
        Type type = new TypeToken<QtecResult<CommitAddRouterInfoResponse>>() {
        }.getType();
        CommitAddRouterInfoResponse response = (CommitAddRouterInfoResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<IntelDevInfoModifyResponse> modifyIntelDev(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<IntelDevInfoModifyResponse>() {

      @Override
      public void call(Subscriber<? super IntelDevInfoModifyResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_DEV);
        Type type = new TypeToken<QtecResult<IntelDevInfoModifyResponse>>() {
        }.getType();
        IntelDevInfoModifyResponse response = (IntelDevInfoModifyResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<TransmitResponse<String>> getRouterStatusTrans(final IRequest request) {
    return createTransmitObservable(request, 1);
  }


  @Override
  public Observable<TransmitResponse<String>> unbindTrans(final IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> addIntelDevVerifyTrans(final IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> addRouterVerifyTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<List<GetUnlockInfoListResponse>> getLockNoteList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetUnlockInfoListResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetUnlockInfoListResponse>> subscriber) {

        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UNLOCK_INFO_LIST);
        Type type = new TypeToken<QtecResult<List<GetUnlockInfoListResponse>>>() {
        }.getType();
        List<GetUnlockInfoListResponse> response = (List<GetUnlockInfoListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetUnlockInfoListResponse>> getLockExceptionList(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetUnlockInfoListResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetUnlockInfoListResponse>> subscriber) {

        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UNLOCK_INFO_LIST);
        Type type = new TypeToken<QtecResult<List<GetUnlockInfoListResponse>>>() {
        }.getType();
        List<GetUnlockInfoListResponse> response = (List<GetUnlockInfoListResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<TransmitResponse<String>> remoteUnlockTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> searchIntelDevNotifyTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> modifyFingerNameTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> queryFingerInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> deleteFingerInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> addFingerInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getLockStatusTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getAntiFritNetStatusTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> enableAntiFritNetTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setAntiQuestionTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> allowAuthDeviceTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getAntiFritSwitchTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getChildCareListTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> postChildCareDetailTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getSignalModeTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> openBandSpeedMeasureTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getGuestSwitchTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setGuestSwitchTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setWifiAllSwitchTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> deleteWifiSwitchTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setWiFiDataTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> addVpnTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getConnectedWifiTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getWifiDetailTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> connectWirelessTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setWifiSwitchTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> safeInspectionTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getBlackListTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> removeBlackMemTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getAntiFritQuestionTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> deleteVpnTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> modifyVpnTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setVpnTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> postSpecialAttentionTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getSpecialAttentionTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getQosInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> postQosInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getWifiTimeConfigTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getWaitingAuthListTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> getStsToken(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>>() {

      @Override
      public void call(Subscriber<? super GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_STS_TOKEN);
        Type type = new TypeToken<QtecResult<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>>>() {
        }.getType();
        GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean> response =
            (GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<TransmitResponse<String>> getRouterInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<GetRouterInfoCloudResponse> getRouterInfoCloud(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetRouterInfoCloudResponse>() {

      @Override
      public void call(Subscriber<? super GetRouterInfoCloudResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_ROUTER_INFO_CLOUD);
        Type type = new TypeToken<QtecResult<GetRouterInfoCloudResponse>>() {
        }.getType();
        GetRouterInfoCloudResponse response = (GetRouterInfoCloudResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyRouterDescResponse> modifyRouterDesc(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyRouterDescResponse>() {

      @Override
      public void call(Subscriber<? super ModifyRouterDescResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_ROUTER_DESC);
        Type type = new TypeToken<QtecResult<ModifyRouterDescResponse>>() {
        }.getType();
        ModifyRouterDescResponse response = (ModifyRouterDescResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyDevNameResponse> modifyDevName(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyDevNameResponse>() {

      @Override
      public void call(Subscriber<? super ModifyDevNameResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_DEV_NAME);
        Type type = new TypeToken<QtecResult<ModifyDevNameResponse>>() {
        }.getType();
        ModifyDevNameResponse response = (ModifyDevNameResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetRouterGroupsResponse>> getRouterGroups(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetRouterGroupsResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetRouterGroupsResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_ROUTER_GROUPS);
        Type type = new TypeToken<QtecResult<List<GetRouterGroupsResponse>>>() {
        }.getType();
        List<GetRouterGroupsResponse> response = (List<GetRouterGroupsResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyRouterGroupResponse> modifyRouterGroup(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyRouterGroupResponse>() {

      @Override
      public void call(Subscriber<? super ModifyRouterGroupResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_ROUTER_GROUP);
        Type type = new TypeToken<QtecResult<ModifyRouterGroupResponse>>() {
        }.getType();
        ModifyRouterGroupResponse response = (ModifyRouterGroupResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<CreateRouterGroupResponse> createRouterGroup(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<CreateRouterGroupResponse>() {

      @Override
      public void call(Subscriber<? super CreateRouterGroupResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CREATE_ROUTER_GROUP);
        Type type = new TypeToken<QtecResult<CreateRouterGroupResponse>>() {
        }.getType();
        CreateRouterGroupResponse response = (CreateRouterGroupResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<TransmitResponse<String>> setDHCPTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setPPPOETrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setStaticIPTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getNetModeTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> restartRouterTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> factoryResetTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<UnbindRouterResponse> unbindRouter(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UnbindRouterResponse>() {

      @Override
      public void call(Subscriber<? super UnbindRouterResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UNBIND_ROUTER);
        Type type = new TypeToken<QtecResult<UnbindRouterResponse>>() {
        }.getType();
        UnbindRouterResponse response = (UnbindRouterResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<TransmitResponse<String>> updateFirmwareTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getWifiConfigTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> configWifiTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getSambaAccountTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> queryDiskStateTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> formatDiskTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setSignalRegulationInfoTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getBandSpeedTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getFirmwareUpdateStatusTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getTimerTaskTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setRouterTimerTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> checkAdminPwdTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> setAdminPwdTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> getKeyTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<TransmitResponse<String>> checkFirmwareTrans(IRequest request) {
    return createTransmitObservable(request, 1);
  }

  @Override
  public Observable<CheckAppVersionResponse> checkAppVersion(final IRequest request) {

    return Observable.create(new Observable.OnSubscribe<CheckAppVersionResponse>() {

      @Override
      public void call(Subscriber<? super CheckAppVersionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CHECK_APP_VERSION);
        Type type = new TypeToken<QtecResult<CheckAppVersionResponse>>() {
        }.getType();
        CheckAppVersionResponse response = (CheckAppVersionResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetFingerprintsResponse>> getFingerprints(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetFingerprintsResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetFingerprintsResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_FINGERPRINTS);
        Type type = new TypeToken<QtecResult<List<GetFingerprintsResponse>>>() {
        }.getType();
        List<GetFingerprintsResponse> response = (List<GetFingerprintsResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetPasswordsResponse>> getPasswords(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetPasswordsResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetPasswordsResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_PASSWORDS);
        Type type = new TypeToken<QtecResult<List<GetPasswordsResponse>>>() {
        }.getType();
        List<GetPasswordsResponse> response = (List<GetPasswordsResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyLockPwdNameResponse> modifyLockPwdName(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyLockPwdNameResponse>() {

      @Override
      public void call(Subscriber<? super ModifyLockPwdNameResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_LOCK_PWD_NAME);
        Type type = new TypeToken<QtecResult<ModifyLockPwdNameResponse>>() {
        }.getType();
        ModifyLockPwdNameResponse response = (ModifyLockPwdNameResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DeleteLockPwdResponse> deleteLockPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteLockPwdResponse>() {

      @Override
      public void call(Subscriber<? super DeleteLockPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_DELETE_LOCK_PWD);
        Type type = new TypeToken<QtecResult<DeleteLockPwdResponse>>() {
        }.getType();
        DeleteLockPwdResponse response = (DeleteLockPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyLockFpNameResponse> modifyLockFpName(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyLockFpNameResponse>() {

      @Override
      public void call(Subscriber<? super ModifyLockFpNameResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_LOCK_FP_NAME);
        Type type = new TypeToken<QtecResult<ModifyLockFpNameResponse>>() {
        }.getType();
        ModifyLockFpNameResponse response = (ModifyLockFpNameResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DeleteLockFpResponse> deleteLockFp(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteLockFpResponse>() {

      @Override
      public void call(Subscriber<? super DeleteLockFpResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_DELETE_LOCK_FP);
        Type type = new TypeToken<QtecResult<DeleteLockFpResponse>>() {
        }.getType();
        DeleteLockFpResponse response = (DeleteLockFpResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<AddLockPwdResponse> addLockPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddLockPwdResponse>() {

      @Override
      public void call(Subscriber<? super AddLockPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_ADD_LOCK_PWD);
        Type type = new TypeToken<QtecResult<AddLockPwdResponse>>() {
        }.getType();
        AddLockPwdResponse response = (AddLockPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<AddLockFpResponse> addLockFp(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddLockFpResponse>() {

      @Override
      public void call(Subscriber<? super AddLockFpResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_ADD_LOCK_FP);
        Type type = new TypeToken<QtecResult<AddLockFpResponse>>() {
        }.getType();
        AddLockFpResponse response = (AddLockFpResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetUsersOfLockResponse<List<String>>> getUsersOfLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetUsersOfLockResponse<List<String>>>() {

      @Override
      public void call(Subscriber<? super GetUsersOfLockResponse<List<String>>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.GET_USERS_OF_LOCK);
        Type type = new TypeToken<QtecResult<GetUsersOfLockResponse<List<String>>>>() {
        }.getType();
        GetUsersOfLockResponse<List<String>> response = (GetUsersOfLockResponse<List<String>>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<CheckLockVersionResponse> checkLockVersion(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<CheckLockVersionResponse>() {

      @Override
      public void call(Subscriber<? super CheckLockVersionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CHECK_LOCK_VERSION);
        Type type = new TypeToken<QtecResult<CheckLockVersionResponse>>() {
        }.getType();
        CheckLockVersionResponse response = (CheckLockVersionResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetUnlockModeResponse> getUnlockMode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetUnlockModeResponse>() {

      @Override
      public void call(Subscriber<? super GetUnlockModeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_UNLOCK_MODE);
        Type type = new TypeToken<QtecResult<GetUnlockModeResponse>>() {
        }.getType();
        GetUnlockModeResponse response = (GetUnlockModeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyUnlockModeResponse> modifyUnlockMode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyUnlockModeResponse>() {

      @Override
      public void call(Subscriber<? super ModifyUnlockModeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
//        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_UNLOCK_MODE);
        Type type = new TypeToken<QtecResult<ModifyUnlockModeResponse>>() {
        }.getType();
        ModifyUnlockModeResponse response = (ModifyUnlockModeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UploadLogcatResponse> uploadLogcat(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UploadLogcatResponse>() {

      @Override
      public void call(Subscriber<? super UploadLogcatResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UPLOAD_LOGCAT);
        Type type = new TypeToken<QtecResult<UploadLogcatResponse>>() {
        }.getType();
        UploadLogcatResponse response = (UploadLogcatResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetDoorCardsResponse>> getDoorCards(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetDoorCardsResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetDoorCardsResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_DOOR_CARDS);
        Type type = new TypeToken<QtecResult<List<GetDoorCardsResponse>>>() {
        }.getType();
        List<GetDoorCardsResponse> response = (List<GetDoorCardsResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DeleteLockDoorCardResponse> deleteLockDoorCard(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteLockDoorCardResponse>() {

      @Override
      public void call(Subscriber<? super DeleteLockDoorCardResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_DELETE_LOCK_DOOR_CARD);
        Type type = new TypeToken<QtecResult<DeleteLockDoorCardResponse>>() {
        }.getType();
        DeleteLockDoorCardResponse response = (DeleteLockDoorCardResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyLockDoorCardNameResponse> modifyLockDoorCardName(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyLockDoorCardNameResponse>() {

      @Override
      public void call(Subscriber<? super ModifyLockDoorCardNameResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_LOCK_DOOR_CARD_NAME);
        Type type = new TypeToken<QtecResult<ModifyLockDoorCardNameResponse>>() {
        }.getType();
        ModifyLockDoorCardNameResponse response = (ModifyLockDoorCardNameResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<AddLockDoorCardResponse> addLockDoorCard(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddLockDoorCardResponse>() {

      @Override
      public void call(Subscriber<? super AddLockDoorCardResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_ADD_LOCK_DOOR_CARD);
        Type type = new TypeToken<QtecResult<AddLockDoorCardResponse>>() {
        }.getType();
        AddLockDoorCardResponse response = (AddLockDoorCardResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<LockFactoryResetResponse> lockFactoryReset(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<LockFactoryResetResponse>() {

      @Override
      public void call(Subscriber<? super LockFactoryResetResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_LOCK_FACTORY_RESET);
        Type type = new TypeToken<QtecResult<LockFactoryResetResponse>>() {
        }.getType();
        LockFactoryResetResponse response = (LockFactoryResetResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetUserRoleResponse> getUserRole(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetUserRoleResponse>() {

      @Override
      public void call(Subscriber<? super GetUserRoleResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_USER_ROLE);
        Type type = new TypeToken<QtecResult<GetUserRoleResponse>>() {
        }.getType();
        GetUserRoleResponse response = (GetUserRoleResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<List<GetLockUsersResponse>> getLockUsers(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<List<GetLockUsersResponse>>() {

      @Override
      public void call(Subscriber<? super List<GetLockUsersResponse>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_LOCK_USERS);
        Type type = new TypeToken<QtecResult<List<GetLockUsersResponse>>>() {
        }.getType();
        List<GetLockUsersResponse> response = (List<GetLockUsersResponse>) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<DeleteLockUserResponse> deleteLockUser(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<DeleteLockUserResponse>() {

      @Override
      public void call(Subscriber<? super DeleteLockUserResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_DELETE_LOCK_USER);
        Type type = new TypeToken<QtecResult<DeleteLockUserResponse>>() {
        }.getType();
        DeleteLockUserResponse response = (DeleteLockUserResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UnbindLockOfAdminResponse> unbindLockOfAdmin(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UnbindLockOfAdminResponse>() {

      @Override
      public void call(Subscriber<? super UnbindLockOfAdminResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UNBIND_LOCK_OF_ADMIN);
        Type type = new TypeToken<QtecResult<UnbindLockOfAdminResponse>>() {
        }.getType();
        UnbindLockOfAdminResponse response = (UnbindLockOfAdminResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<TransmitResponse<String>> remoteLockOptTrans(IRequest request) {
    return createTransmitObservable(request, 0);
  }

  @Override
  public Observable<TransmitResponse<String>> bindRouterToLockTrans(IRequest request) {
    return createTransmitObservable(request, 0);
  }

  @Override
  public Observable<TransmitResponse<String>> queryBindRouterToLockTrans(IRequest request) {
    return createTransmitObservable(request, 0);
  }

  @Override
  public Observable<TransmitResponse<String>> unbindRouterToLockTrans(IRequest request) {
    return createTransmitObservable(request, 0);
  }

  @Override
  public Observable<TransmitResponse<String>> getFirstConfigTrans(IRequest request) {
    return createTransmitObservable(request, 0);
  }

  @Override
  public Observable<GetPassagewayModeResponse> getPassagewayMode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetPassagewayModeResponse>() {

      @Override
      public void call(Subscriber<? super GetPassagewayModeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_PASSAGEWAY_MODE);
        Type type = new TypeToken<QtecResult<GetPassagewayModeResponse>>() {
        }.getType();
        GetPassagewayModeResponse response = (GetPassagewayModeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<ModifyPassagewayModeResponse> modifyPassagewayMode(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<ModifyPassagewayModeResponse>() {

      @Override
      public void call(Subscriber<? super ModifyPassagewayModeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_MODIFY_PASSAGEWAY);
        Type type = new TypeToken<QtecResult<ModifyPassagewayModeResponse>>() {
        }.getType();
        ModifyPassagewayModeResponse response = (ModifyPassagewayModeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UpdateLockVersionResponse> updateLockVersion(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UpdateLockVersionResponse>() {

      @Override
      public void call(Subscriber<? super UpdateLockVersionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UPDATE_LOCK_VERSION);
        Type type = new TypeToken<QtecResult<UpdateLockVersionResponse>>() {
        }.getType();
        UpdateLockVersionResponse response = (UpdateLockVersionResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<AddTempPwdResponse> addTempPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AddTempPwdResponse>() {
      @Override
      public void call(Subscriber<? super AddTempPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_ADD_TEMP_PWD);
        Type type = new TypeToken<QtecResult<AddTempPwdResponse>>() {
        }.getType();
        AddTempPwdResponse response = (AddTempPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<QueryTempPwdResponse> queryTempPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<QueryTempPwdResponse>() {
      @Override
      public void call(Subscriber<? super QueryTempPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_QUERY_TEMP_PWD);
        Type type = new TypeToken<QtecResult<QueryTempPwdResponse>>() {
        }.getType();
        QueryTempPwdResponse response = (QueryTempPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetTempPwdResponse> getTempPwd(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetTempPwdResponse>() {
      @Override
      public void call(Subscriber<? super GetTempPwdResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_TEMP_PWD);
        Type type = new TypeToken<QtecResult<GetTempPwdResponse>>() {
        }.getType();
        GetTempPwdResponse response = (GetTempPwdResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<GetLockVolumeResponse> getLockVolume(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetLockVolumeResponse>() {
      @Override
      public void call(Subscriber<? super GetLockVolumeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_LOCK_VOLUME);
        Type type = new TypeToken<QtecResult<GetLockVolumeResponse>>() {
        }.getType();
        GetLockVolumeResponse response = (GetLockVolumeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<AdjustLockVolumeResponse> adjustLockVolume(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<AdjustLockVolumeResponse>() {
      @Override
      public void call(Subscriber<? super AdjustLockVolumeResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_ADJUST_LOCK_VOLUME);
        Type type = new TypeToken<QtecResult<AdjustLockVolumeResponse>>() {
        }.getType();
        AdjustLockVolumeResponse response = (AdjustLockVolumeResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<CheckLiteVersionResponse> checkLiteVersion(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<CheckLiteVersionResponse>() {
      @Override
      public void call(Subscriber<? super CheckLiteVersionResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CHECK_LITE_VERSION);
        Type type = new TypeToken<QtecResult<CheckLiteVersionResponse>>() {
        }.getType();
        CheckLiteVersionResponse response = (CheckLiteVersionResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<UpdateLiteResponse> updateLite(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<UpdateLiteResponse>() {
      @Override
      public void call(Subscriber<? super UpdateLiteResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_UPDATE_LITE);
        Type type = new TypeToken<QtecResult<UpdateLiteResponse>>() {
        }.getType();
        UpdateLiteResponse response = (UpdateLiteResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }
  
  @Override
  public Observable<GetLiteUpdateResponse> getLiteUpdate(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<GetLiteUpdateResponse>() {
      @Override
      public void call(Subscriber<? super GetLiteUpdateResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_GET);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_GET_LITE_UPDATE);
        Type type = new TypeToken<QtecResult<GetLiteUpdateResponse>>() {
        }.getType();
        GetLiteUpdateResponse response = (GetLiteUpdateResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<CloudUnbindRouterLockResponse> cloudUnbindRouterLock(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<CloudUnbindRouterLockResponse>() {
      @Override
      public void call(Subscriber<? super CloudUnbindRouterLockResponse> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_CLOUD_UNBIND_ROUTER_LOCK);
        Type type = new TypeToken<QtecResult<CloudUnbindRouterLockResponse>>() {
        }.getType();
        CloudUnbindRouterLockResponse response = (CloudUnbindRouterLockResponse) requestService(encryptInfo, type, subscriber, 0, false);
        subscriber.onNext(response);
        subscriber.onCompleted();
      }
    });
  }


  private Object requestService(IRequest request, Type type, Subscriber<?> subscriber, int encryption, boolean isTrans) {

    ((QtecEncryptInfo) request).setToken(CloudUrlPath.getToken());

    Object data = null;

    if (apiPostConnection.getClass() != MockApiPostConnection.class) {
      checkNetworkConnection(subscriber);
    }

    JsonMapper jsonMapper = new JsonMapper();

    String requestMsg = null;

    try {
      requestMsg = jsonMapper.toJson(request);
    } catch (JsonIOException e) {
      e.printStackTrace();
      subscriber.onError(new JsonIOException("请求数据不完整"));
    }

    if (!CloudUrlPath.PATH_UPLOAD_LOGCAT.equals(((QtecEncryptInfo) request).getRequestUrl())) {
      Logger.t("cloud-none-request").json(requestMsg);
    }

    if (isTrans) {
      requestMsg = mCloudConverter.convertTo(requestMsg, encryption);
      Logger.t("cloud-sm4-request").json(requestMsg);
    }

    String result = null;
    try {
      result = apiPostConnection.requestSyncCall(requestMsg, ((QtecEncryptInfo) request).getRequestUrl(), ((QtecEncryptInfo) request));
    } catch (IOException e) {
      e.printStackTrace();
      subscriber.onError(e);
    }

    if (isTrans) {
      Logger.t("cloud-sm4-response").json(result);
      result = mCloudConverter.convertFrom(result, null);
      if (IConverter.EXP_KEY_INVALID.equals(result)) {
        Intent intent = new Intent();
        intent.setAction(ACTION_ROUTER_KEY_INVALID);
        context.sendBroadcast(intent);
      }
    }

    if (!CloudUrlPath.PATH_UPLOAD_LOGCAT.equals(((QtecEncryptInfo) request).getRequestUrl())) {
      Logger.t("cloud-none-response").json(result);
    }

    preHandleLoginInvalid(subscriber, result);

    QtecResult qtecResult = null;
    try {
      qtecResult = (QtecResult) jsonMapper.fromJson(result, type);
    } catch (JsonSyntaxException e) {
      e.printStackTrace();
      Logger.t("response_exp_json").json(result);
      subscriber.onError(new IOException("返回数据不完整"));
    }

    if (qtecResult == null) {
      subscriber.onError(new IOException("数据异常"));
    }

    if (qtecResult.getCode() == -1) {
      String msg = "服务异常";
      subscriber.onError(new IOException(msg));
    }

    if (qtecResult.getCode() == LOGIN_INVALID) {
      subscriber.onError(new LoginInvalidException(qtecResult.getMsg()));
    }

    if (qtecResult.getCode() == PASSWORD_ERROR_THREE_TIMES) {
      subscriber.onError(new PasswordErrThreeTimesException(qtecResult.getMsg(), PASSWORD_ERROR_THREE_TIMES));
    }

    if (qtecResult.getCode() == PASSWORD_ERROR_MORE_TIMES) {
      subscriber.onError(new PasswordErrMoreTimesException(qtecResult.getMsg(), PASSWORD_ERROR_MORE_TIMES));
    }

    if (qtecResult.getCode() == NO_SUCH_LOCK) {
      subscriber.onError(new CloudNoSuchLockException(qtecResult.getMsg(), NO_SUCH_LOCK));
    }

    if (qtecResult.getCode() != 0) {
      String msg = TextUtils.isEmpty(qtecResult.getMsg()) ? "业务异常" : qtecResult.getMsg();
      subscriber.onError(new IOException(msg));
    }

    data = qtecResult.getData();
    if (data == null) {
      subscriber.onError(new IOException("data数据不完整"));
    }

    return data;
  }

  private void preHandleLoginInvalid(Subscriber<?> subscriber, String result) {
    try {
      JSONObject jsonObject = new JSONObject(result);
      int code = jsonObject.getInt("code");
      String msg = jsonObject.getString("msg");
      if (code == LOGIN_INVALID) {
        subscriber.onError(new LoginInvalidException(msg));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void checkNetworkConnection(Subscriber<?> subscriber) {
    if (!isThereInternetConnection()) {
      subscriber.onError(new NetworkConnectionException());
    }
  }

  /**
   * Checks if the device has any active internet connection.
   *
   * @return true device with internet connection, otherwise false.
   */
  private boolean isThereInternetConnection() {
    boolean isConnected;

    ConnectivityManager connectivityManager =
        (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

    return isConnected;
  }

  @NonNull
  private Observable<TransmitResponse<String>> createTransmitObservable(final IRequest request, final int encryption) {
    return Observable.create(new Observable.OnSubscribe<TransmitResponse<String>>() {

      @Override
      public void call(Subscriber<? super TransmitResponse<String>> subscriber) {
        QtecEncryptInfo encryptInfo = (QtecEncryptInfo) request;
        encryptInfo.setMethod(CloudUrlPath.METHOD_POST);
        encryptInfo.setRequestUrl(CloudUrlPath.PATH_TRANSMIT);
        Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
        }.getType();
        TransmitResponse<String> response =
            (TransmitResponse<String>) requestService(encryptInfo, type, subscriber, encryption, true);
        if (response != null) {
          subscriber.onNext(response);
        }

//        subscriber.onCompleted();
      }
    });
  }
}
