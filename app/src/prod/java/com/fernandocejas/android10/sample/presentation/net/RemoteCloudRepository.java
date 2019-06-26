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
package com.fernandocejas.android10.sample.presentation.net;

import com.fernandocejas.android10.sample.data.constant.ExceptionConstant;
import com.fernandocejas.android10.sample.data.net.CloudRestApi;
import com.fernandocejas.android10.sample.data.net.CloudRestApiImpl;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
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
import com.qtec.mapp.model.rsp.FeedBackResponse.ReplyContent;
import com.qtec.mapp.model.rsp.GetAgreementResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;
import com.qtec.mapp.model.rsp.GetDeviceCountResponse;
import com.qtec.mapp.model.rsp.GetDoorCardsResponse;
import com.qtec.mapp.model.rsp.GetExtraNetPortResponse;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.GetIdCodeResponse;
import com.qtec.mapp.model.rsp.GetLiteUpdateResponse;
import com.qtec.mapp.model.rsp.GetLockListResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetLockVolumeResponse;
import com.qtec.mapp.model.rsp.GetMemRemarkNameResponse;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;
import com.qtec.mapp.model.rsp.GetMsgListResponse;
import com.qtec.mapp.model.rsp.GetMsgOtherListResponse;
import com.qtec.mapp.model.rsp.GetMyAdviceResponse;
import com.qtec.mapp.model.rsp.GetPassagewayModeResponse;
import com.qtec.mapp.model.rsp.GetPasswordsResponse;
import com.qtec.mapp.model.rsp.GetQuestionDetailResponse;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;
import com.qtec.mapp.model.rsp.GetRouterGroupsResponse;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;
import com.qtec.mapp.model.rsp.GetStsTokenResponse;
import com.qtec.mapp.model.rsp.GetStsTokenResponse.*;
import com.qtec.mapp.model.rsp.GetTempPwdResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;
import com.qtec.mapp.model.rsp.GetUnlockModeResponse;
import com.qtec.mapp.model.rsp.GetUpdateFeedBackResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;
import com.qtec.mapp.model.rsp.GetUsersOfLockResponse;
import com.qtec.mapp.model.rsp.GetfeedBackResponse;
import com.qtec.mapp.model.rsp.IntelDevInfoModifyResponse;
import com.qtec.mapp.model.rsp.IntelDeviceDetailResponse;
import com.qtec.mapp.model.rsp.IntelDeviceListResponse;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;
import com.qtec.mapp.model.rsp.LoginResponse;
import com.qtec.mapp.model.rsp.LogoutResponse;
import com.qtec.mapp.model.rsp.ModifyLockDoorCardNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockFpNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockPwdNameResponse;
import com.qtec.mapp.model.rsp.ModifyPassagewayModeResponse;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ModifyPwdResponse;
import com.qtec.mapp.model.rsp.ModifyRouterDescResponse;
import com.qtec.mapp.model.rsp.ModifyRouterGroupResponse;
import com.qtec.mapp.model.rsp.ModifyDevNameResponse;
import com.qtec.mapp.model.rsp.ModifyUnlockModeResponse;
import com.qtec.mapp.model.rsp.ModifyUserInfoResponse;
import com.qtec.mapp.model.rsp.PostInvitationResponse;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.QueryLockedCatResponse;
import com.qtec.mapp.model.rsp.QueryTempPwdResponse;
import com.qtec.mapp.model.rsp.RegisterResponse;
import com.qtec.mapp.model.rsp.ResetPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ResetPwdResponse;
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
import com.qtec.model.core.QtecResult;
import com.qtec.router.model.rsp.AddFingerResponse;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.AllowAuthDeviceResponse;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.CheckAdminPwdResponse;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.ChildCareListResponse;
import com.qtec.router.model.rsp.ConfigWifiResponse;
import com.qtec.router.model.rsp.DeleteFingerResponse;
import com.qtec.router.model.rsp.DeleteVpnResponse;
import com.qtec.router.model.rsp.DeleteWifiSwitchResponse;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.GetAntiFritNetStatusResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;
import com.qtec.router.model.rsp.GetAntiFritSwitchResponse;
import com.qtec.router.model.rsp.GetBandSpeedResponse;
import com.qtec.router.model.rsp.GetBlackListResponse;
import com.qtec.router.model.rsp.GetConnectedWifiResponse;
import com.qtec.router.model.rsp.GetFirmwareUpdateStatusResponse;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.GetKeyResponse;
import com.qtec.router.model.rsp.GetKeyResponse.KeyBean;
import com.qtec.router.model.rsp.GetLockStatusResponse;
import com.qtec.router.model.rsp.GetNetModeResponse;
import com.qtec.router.model.rsp.GetQosInfoResponse;
import com.qtec.router.model.rsp.GetRouterFirstConfigResponse;
import com.qtec.router.model.rsp.GetRouterInfoResponse;
import com.qtec.router.model.rsp.GetSambaAccountResponse;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;
import com.qtec.router.model.rsp.GetSpecialAttentionResponse;
import com.qtec.router.model.rsp.GetTimerTaskResponse;
import com.qtec.router.model.rsp.GetVpnListResponse;
import com.qtec.router.model.rsp.GetWaitingAuthDeviceListResponse;
import com.qtec.router.model.rsp.GetWifiConfigResponse;
import com.qtec.router.model.rsp.GetWifiDetailResponse;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;
import com.qtec.router.model.rsp.GetWirelessListResponse;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;
import com.qtec.router.model.rsp.ModifyVpnResponse;
import com.qtec.router.model.rsp.OpenBandSpeedResponse;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;
import com.qtec.router.model.rsp.PostConnectWirelessResponse;
import com.qtec.router.model.rsp.PostInspectionResponse;
import com.qtec.router.model.rsp.PostQosInfoResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;
import com.qtec.router.model.rsp.QueryDiskStateResponse;
import com.qtec.router.model.rsp.QueryFingerInfoResponse;
import com.qtec.router.model.rsp.RemoteLockOperationResponse;
import com.qtec.router.model.rsp.RemoveBlackMemResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;
import com.qtec.router.model.rsp.SearchIntelDevNotifyResponse;
import com.qtec.router.model.rsp.SetAdminPwdResponse;
import com.qtec.router.model.rsp.SetAntiNetQuestionResponse;
import com.qtec.router.model.rsp.SetDHCPResponse;
import com.qtec.router.model.rsp.SetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.SetPPPOEResponse;
import com.qtec.router.model.rsp.SetRouterTimerResponse;
import com.qtec.router.model.rsp.SetStaticIPResponse;
import com.qtec.router.model.rsp.SetVpnResponse;
import com.qtec.router.model.rsp.SetWifiAllSwitchResponse;
import com.qtec.router.model.rsp.SetWifiDataResponse;
import com.qtec.router.model.rsp.SetWifiSwitchResponse;
import com.qtec.router.model.rsp.UnbindRouterToLockResponse;
import com.qtec.router.model.rsp.UpdateFirmwareResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/08
 *     desc   : {@link RemoteCloudRepository} for retrieving user data from remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class RemoteCloudRepository implements CloudRepository {

  private final CloudRestApi cloudRestApi;
  private JsonMapper mJsonMapper;

  @Inject
  public RemoteCloudRepository(CloudRestApiImpl cloudRestApi) {
    this.cloudRestApi = cloudRestApi;
    this.mJsonMapper = new JsonMapper();
  }

  @Override
  public Observable<LoginResponse> login(IRequest request) {
    return cloudRestApi.login(request);
  }

  @Override
  public Observable<GetIdCodeResponse> getIdCode(IRequest request) {
    return cloudRestApi.getIdCode(request);
  }

  @Override
  public Observable<GetAgreementResponse> getUserAgreement(IRequest request) {
    return cloudRestApi.getUserAgreement(request);
  }

  @Override
  public Observable<GetMemRemarkNameResponse> getMemRemarkName(IRequest request) {
    return cloudRestApi.getMemRemarkName(request);
  }

  @Override
  public Observable<RegisterResponse> register(IRequest request) {
    return cloudRestApi.register(request);
  }

  @Override
  public Observable<GetAgreementResponse> getUserSerectAgreement(IRequest request) {
    return cloudRestApi.getUserSerectAgreement(request);
  }

  @Override
  public Observable<ResetPwdGetIdCodeResponse> resetPwdGetIdCode(IRequest request) {
    return cloudRestApi.resetPwdGetIdCode(request);
  }

  @Override
  public Observable<ResetPwdResponse> resetPwd(IRequest request) {
    return cloudRestApi.resetPwd(request);
  }

  @Override
  public Observable<List<GetQuestionListResponse>> getQuestionList(IRequest request) {
    return cloudRestApi.getQuestionList(request);
  }

  @Override
  public Observable<GetQuestionDetailResponse> getQuestionDeatil(IRequest request) {
    return cloudRestApi.getQuestionDeatil(request);
  }

  @Override
  public Observable<List<GetMyAdviceResponse>> getMyAdvice(IRequest request) {
    return cloudRestApi.getMyAdvice(request);
  }

  @Override
  public Observable<FeedBackResponse<List<ReplyContent>>> getAdviceDetail(IRequest request) {
    return cloudRestApi.getAdviceDetail(request);
  }

  @Override
  public Observable<GetfeedBackResponse> getFeedBack(IRequest request) {
    return cloudRestApi.getFeedBack(request);
  }

  @Override
  public Observable<GetUpdateFeedBackResponse> getUpdateFeedBack(IRequest request) {
    return cloudRestApi.getUpdateFeedBack(request);
  }


  @Override
  public Observable<ModifyUserInfoResponse> modifyUserInfo(IRequest request) {
    return cloudRestApi.modifyUserInfo(request);
  }

  @Override
  public Observable<LogoutResponse> logout(IRequest request) {
    return cloudRestApi.logout(request);
  }

  @Override
  public Observable<ModifyPwdGetIdCodeResponse> modifyPwdGetIdCode(IRequest request) {
    return cloudRestApi.modifyPwdGetIdCode(request);
  }

  @Override
  public Observable<ModifyPwdResponse> modifyPwd(IRequest request) {
    return cloudRestApi.modifyPwd(request);
  }

  @Override
  public Observable<GetDeviceCountResponse> getDeviceCount(IRequest request) {
    return cloudRestApi.getDeviceCount(request);
  }

  @Override
  public Observable<UploadMsgDeviceIDResponse> uploadMsgId(IRequest request) {
    return cloudRestApi.uploadMsgId(request);
  }

  @Override
  public Observable<List<GetDevTreeResponse<List<DeviceBean>>>> getDevTree(IRequest request) {
    return cloudRestApi.getDevTree(request);
  }

  @Override
  public Observable<List<GetShareMemListResponse>> getSharedMemList(IRequest request) {
    return cloudRestApi.getSharedMemList(request);
  }

  @Override
  public Observable<List<GetLockListResponse>> getLockList(IRequest request) {
    return cloudRestApi.getLockList(request);
  }

  @Override
  public Observable<QueryCatLockResponse> queryCatLock(IRequest request) {
    return cloudRestApi.queryCatLock(request);
  }

  @Override
  public Observable<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> getDoorBeelMsgList(IRequest request) {
    return cloudRestApi.getDoorBeerMsgList(request);
  }

  @Override
  public Observable<GetDoorBellRecordDetailResponse> getDoorBeelMsgDetail(IRequest request) {
    return cloudRestApi.getDoorBeerMsgDetail(request);
  }

  @Override
  public Observable<DeleteDoorBellRecordResponse> deleteDoorBeelMsg(IRequest request) {
    return cloudRestApi.deleteDoorBeerMsg(request);
  }

  @Override
  public Observable<SetAllDoorBellRecordReadResponse> setAllDoorBeelMsgIsRead(IRequest request) {
    return cloudRestApi.setAllDoorBeerMsgIsRead(request);
  }

  @Override
  public Observable<ConnectLockResponse> connectLock(IRequest request) {
    return cloudRestApi.connectLock(request);
  }

  @Override
  public Observable<QueryLockedCatResponse> queryLockedCat(IRequest request) {
    return cloudRestApi.queryLockedCat(request);
  }

  @Override
  public Observable<UnbindCatLockResponse> unbindCatLock(IRequest request) {
    return cloudRestApi.unbindCatLock(request);
  }

  @Override
  public Observable<UploadDevicePwdResponse> uploadDevicePwd(IRequest request) {
    return cloudRestApi.uploadDevicePwd(request);
  }

  @Override
  public Observable<PostInvitationResponse> postInvitation(IRequest request) {
    return cloudRestApi.postInvitation(request);
  }

  @Override
  public Observable<DealInvitationResponse> dealInvitation(IRequest request) {
    return cloudRestApi.dealInvitation(request);
  }

  @Override
  public Observable<List<GetMsgOtherListResponse>> getMsgList(IRequest request) {
    return cloudRestApi.getMsgList(request);
  }

  @Override
  public Observable<List<GetMsgListResponse<GetMsgListResponse.messageContent>>> getMsgShareList(IRequest request) {
    return cloudRestApi.getMsgShareList(request);
  }

  @Override
  public Observable<List<IntelDeviceListResponse>> getIntelDeviceList(IRequest request) {
    return cloudRestApi.getIntelDeviceList(request);
  }

  @Override
  public Observable<GetExtraNetPortResponse> getExtraNetPort(IRequest request) {
    return cloudRestApi.getExtraNetPort(request);
  }

  @Override
  public Observable<IntelDeviceDetailResponse> getIntelDeviceDetail(IRequest request) {
    return cloudRestApi.getIntelDeviceDetail(request);
  }

  @Override
  public Observable<DeleteMsgResponse> deleteMsg(IRequest request) {
    return cloudRestApi.deleteMsg(request);
  }

  @Override
  public Observable<GetMsgCountResponse> getMsgCount(IRequest request) {
    return cloudRestApi.getMsgCount(request);
  }

  @Override
  public Observable<SetMsgReadResponse> setMsgRead(IRequest request) {
    return cloudRestApi.setMsgRead(request);
  }

  @Override
  public Observable<UnbindIntelDevResponse> unbindTrans(IRequest request) {
    return cloudRestApi.addIntelDevVerifyTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<UnbindIntelDevResponse>>() {
          }.getType();
          return (QtecResult<UnbindIntelDevResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<UnbindIntelDevResponse>, Observable<UnbindIntelDevResponse>>() {
          @Override
          public Observable<UnbindIntelDevResponse> call(final QtecResult<UnbindIntelDevResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<CommitAddRouterInfoResponse> commitAddRouterInfo(IRequest request) {
    return cloudRestApi.commitAddRouterInfo(request);
  }

  @Override
  public Observable<IntelDevInfoModifyResponse> modifyIntelDev(IRequest request) {
    return cloudRestApi.modifyIntelDev(request);
  }

  @Override
  public Observable<RouterStatusResponse<List<Status>>> getRouterStatusTrans(IRequest request) {
    return cloudRestApi.getRouterStatusTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RouterStatusResponse<List<Status>>>>() {
          }.getType();
          return (QtecResult<RouterStatusResponse<List<Status>>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<RouterStatusResponse<List<Status>>>, Observable<RouterStatusResponse<List<Status>>>>() {
          @Override
          public Observable<RouterStatusResponse<List<Status>>> call(QtecResult<RouterStatusResponse<List<Status>>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<AddIntelDevVerifyResponse> addIntelDevVerifyTrans(IRequest request) {
    return cloudRestApi.addIntelDevVerifyTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddIntelDevVerifyResponse>>() {
          }.getType();
          return (QtecResult<AddIntelDevVerifyResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<AddIntelDevVerifyResponse>, Observable<AddIntelDevVerifyResponse>>() {
          @Override
          public Observable<AddIntelDevVerifyResponse> call(final QtecResult<AddIntelDevVerifyResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });

  }

  @Override
  public Observable<AddRouterVerifyResponse> addRouterVerifyTrans(IRequest request) {
    return cloudRestApi.addRouterVerifyTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddRouterVerifyResponse>>() {
          }.getType();
          return (QtecResult<AddRouterVerifyResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<AddRouterVerifyResponse>, Observable<AddRouterVerifyResponse>>() {
          @Override
          public Observable<AddRouterVerifyResponse> call(final QtecResult<AddRouterVerifyResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<List<GetUnlockInfoListResponse>> getLockNoteList(IRequest request) {
    return cloudRestApi.getLockNoteList(request);
  }

  @Override
  public Observable<List<GetUnlockInfoListResponse>> getLockExceptionList(IRequest request) {
    return cloudRestApi.getLockExceptionList(request);
  }

  @Override
  public Observable<GetLockStatusResponse> getLockStatusTrans(IRequest request) {
    return cloudRestApi.getLockStatusTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetLockStatusResponse>>() {
          }.getType();
          return (QtecResult<GetLockStatusResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetLockStatusResponse>, Observable<GetLockStatusResponse>>() {
          @Override
          public Observable<GetLockStatusResponse> call(final QtecResult<GetLockStatusResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatusTrans(IRequest request) {
    return cloudRestApi.getAntiFritNetStatusTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetAntiFritNetStatusResponse>>() {
          }.getType();
          return (QtecResult<GetAntiFritNetStatusResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetAntiFritNetStatusResponse>, Observable<GetAntiFritNetStatusResponse>>() {
          @Override
          public Observable<GetAntiFritNetStatusResponse> call(final QtecResult<GetAntiFritNetStatusResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<EnableAntiFritNetResponse> enableAntiFritNetTrans(IRequest request) {
    return cloudRestApi.enableAntiFritNetTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<EnableAntiFritNetResponse>>() {
          }.getType();
          return (QtecResult<EnableAntiFritNetResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<EnableAntiFritNetResponse>, Observable<EnableAntiFritNetResponse>>() {
          @Override
          public Observable<EnableAntiFritNetResponse> call(final QtecResult<EnableAntiFritNetResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetAntiNetQuestionResponse> setAntiQuestionTrans(IRequest request) {
    return cloudRestApi.setAntiQuestionTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetAntiNetQuestionResponse>>() {
          }.getType();
          return (QtecResult<SetAntiNetQuestionResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetAntiNetQuestionResponse>, Observable<SetAntiNetQuestionResponse>>() {
          @Override
          public Observable<SetAntiNetQuestionResponse> call(final QtecResult<SetAntiNetQuestionResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthListTrans(IRequest request) {
    return cloudRestApi.getWaitingAuthListTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<List<GetWaitingAuthDeviceListResponse>>>() {
          }.getType();
          return (QtecResult<List<GetWaitingAuthDeviceListResponse>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<List<GetWaitingAuthDeviceListResponse>>, Observable<List<GetWaitingAuthDeviceListResponse>>>() {
          @Override
          public Observable<List<GetWaitingAuthDeviceListResponse>> call(final QtecResult<List<GetWaitingAuthDeviceListResponse>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<AllowAuthDeviceResponse> allowAuthDeviceTrans(IRequest request) {
    return cloudRestApi.allowAuthDeviceTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AllowAuthDeviceResponse>>() {
          }.getType();
          return (QtecResult<AllowAuthDeviceResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<AllowAuthDeviceResponse>, Observable<AllowAuthDeviceResponse>>() {
          @Override
          public Observable<AllowAuthDeviceResponse> call(final QtecResult<AllowAuthDeviceResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetAntiFritSwitchResponse> getAntiFritSwitchTrans(IRequest request) {
    return cloudRestApi.getAntiFritSwitchTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetAntiFritSwitchResponse>>() {
          }.getType();
          return (QtecResult<GetAntiFritSwitchResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetAntiFritSwitchResponse>, Observable<GetAntiFritSwitchResponse>>() {
          @Override
          public Observable<GetAntiFritSwitchResponse> call(final QtecResult<GetAntiFritSwitchResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<List<ChildCareListResponse>> getChildCareListTrans(IRequest request) {
    return cloudRestApi.getChildCareListTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<List<ChildCareListResponse>>>() {
          }.getType();
          return (QtecResult<List<ChildCareListResponse>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<List<ChildCareListResponse>>, Observable<List<ChildCareListResponse>>>() {
          @Override
          public Observable<List<ChildCareListResponse>> call(final QtecResult<List<ChildCareListResponse>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<PostChildCareDetailResponse> postChildCareDetailTrans(IRequest request) {
    return cloudRestApi.postChildCareDetailTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostChildCareDetailResponse>>() {
          }.getType();
          return (QtecResult<PostChildCareDetailResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<PostChildCareDetailResponse>, Observable<PostChildCareDetailResponse>>() {
          @Override
          public Observable<PostChildCareDetailResponse> call(final QtecResult<PostChildCareDetailResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetSignalRegulationResponse> getSignalModeTrans(IRequest request) {
    return cloudRestApi.getSignalModeTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSignalRegulationResponse>>() {
          }.getType();
          return (QtecResult<GetSignalRegulationResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetSignalRegulationResponse>, Observable<GetSignalRegulationResponse>>() {
          @Override
          public Observable<GetSignalRegulationResponse> call(final QtecResult<GetSignalRegulationResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<OpenBandSpeedResponse> openBandSpeedMeasureTrans(IRequest request) {
    return cloudRestApi.openBandSpeedMeasureTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<OpenBandSpeedResponse>>() {
          }.getType();
          return (QtecResult<OpenBandSpeedResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<OpenBandSpeedResponse>, Observable<OpenBandSpeedResponse>>() {
          @Override
          public Observable<OpenBandSpeedResponse> call(final QtecResult<OpenBandSpeedResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetGuestWifiSwitchResponse> getGuestSwitchTrans(IRequest request) {
    return cloudRestApi.getGuestSwitchTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetGuestWifiSwitchResponse>>() {
          }.getType();
          return (QtecResult<GetGuestWifiSwitchResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetGuestWifiSwitchResponse>, Observable<GetGuestWifiSwitchResponse>>() {
          @Override
          public Observable<GetGuestWifiSwitchResponse> call(final QtecResult<GetGuestWifiSwitchResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetGuestWifiSwitchResponse> setGuestSwitchTrans(IRequest request) {
    return cloudRestApi.setGuestSwitchTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetGuestWifiSwitchResponse>>() {
          }.getType();
          return (QtecResult<SetGuestWifiSwitchResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetGuestWifiSwitchResponse>, Observable<SetGuestWifiSwitchResponse>>() {
          @Override
          public Observable<SetGuestWifiSwitchResponse> call(final QtecResult<SetGuestWifiSwitchResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetWifiAllSwitchResponse> setWifiAllSwitchTrans(IRequest request) {
    return cloudRestApi.setWifiAllSwitchTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetWifiAllSwitchResponse>>() {
          }.getType();
          return (QtecResult<SetWifiAllSwitchResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetWifiAllSwitchResponse>, Observable<SetWifiAllSwitchResponse>>() {
          @Override
          public Observable<SetWifiAllSwitchResponse> call(final QtecResult<SetWifiAllSwitchResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<DeleteWifiSwitchResponse> deleteWifiSwitchTrans(IRequest request) {
    return cloudRestApi.deleteWifiSwitchTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<DeleteWifiSwitchResponse>>() {
          }.getType();
          return (QtecResult<DeleteWifiSwitchResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<DeleteWifiSwitchResponse>, Observable<DeleteWifiSwitchResponse>>() {
          @Override
          public Observable<DeleteWifiSwitchResponse> call(final QtecResult<DeleteWifiSwitchResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetWifiDataResponse> setWiFiDataTrans(IRequest request) {
    return cloudRestApi.setWiFiDataTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetWifiDataResponse>>() {
          }.getType();
          return (QtecResult<SetWifiDataResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetWifiDataResponse>, Observable<SetWifiDataResponse>>() {
          @Override
          public Observable<SetWifiDataResponse> call(final QtecResult<SetWifiDataResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<AddVpnResponse> addVpnTrans(IRequest request) {
    return cloudRestApi.addVpnTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddVpnResponse>>() {
          }.getType();
          return (QtecResult<AddVpnResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<AddVpnResponse>, Observable<AddVpnResponse>>() {
          @Override
          public Observable<AddVpnResponse> call(final QtecResult<AddVpnResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<DeleteVpnResponse> deleteVpnTrans(IRequest request) {
    return cloudRestApi.deleteVpnTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<DeleteVpnResponse>>() {
          }.getType();
          return (QtecResult<DeleteVpnResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<DeleteVpnResponse>, Observable<DeleteVpnResponse>>() {
          @Override
          public Observable<DeleteVpnResponse> call(final QtecResult<DeleteVpnResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<ModifyVpnResponse> modifyVpnTrans(IRequest request) {
    return cloudRestApi.modifyVpnTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<ModifyVpnResponse>>() {
          }.getType();
          return (QtecResult<ModifyVpnResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<ModifyVpnResponse>, Observable<ModifyVpnResponse>>() {
          @Override
          public Observable<ModifyVpnResponse> call(final QtecResult<ModifyVpnResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetVpnResponse> setVpnTrans(IRequest request) {
    return cloudRestApi.setVpnTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetVpnResponse>>() {
          }.getType();
          return (QtecResult<SetVpnResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetVpnResponse>, Observable<SetVpnResponse>>() {
          @Override
          public Observable<SetVpnResponse> call(final QtecResult<SetVpnResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnListTrans(IRequest request) {
    return cloudRestApi.addVpnTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>>() {
          }.getType();
          return (QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>, Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>>() {
          @Override
          public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> call(final QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelessListTrans(IRequest request) {
    return cloudRestApi.addVpnTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>>() {
          }.getType();
          return (QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>, Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>>() {
          @Override
          public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> call(final QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetConnectedWifiResponse> getConnectedWifiTrans(IRequest request) {
    return cloudRestApi.getConnectedWifiTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetConnectedWifiResponse>>() {
          }.getType();
          return (QtecResult<GetConnectedWifiResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetConnectedWifiResponse>, Observable<GetConnectedWifiResponse>>() {
          @Override
          public Observable<GetConnectedWifiResponse> call(final QtecResult<GetConnectedWifiResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetWifiDetailResponse> getWifiDetailTrans(IRequest request) {
    return cloudRestApi.getWifiDetailTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWifiDetailResponse>>() {
          }.getType();
          return (QtecResult<GetWifiDetailResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetWifiDetailResponse>, Observable<GetWifiDetailResponse>>() {
          @Override
          public Observable<GetWifiDetailResponse> call(final QtecResult<GetWifiDetailResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<PostConnectWirelessResponse> connectWirelessTrans(IRequest request) {
    return cloudRestApi.connectWirelessTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostConnectWirelessResponse>>() {
          }.getType();
          return (QtecResult<PostConnectWirelessResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<PostConnectWirelessResponse>, Observable<PostConnectWirelessResponse>>() {
          @Override
          public Observable<PostConnectWirelessResponse> call(final QtecResult<PostConnectWirelessResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetWifiSwitchResponse> setWifiSwitchTrans(IRequest request) {
    return cloudRestApi.setWifiSwitchTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetWifiSwitchResponse>>() {
          }.getType();
          return (QtecResult<SetWifiSwitchResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetWifiSwitchResponse>, Observable<SetWifiSwitchResponse>>() {
          @Override
          public Observable<SetWifiSwitchResponse> call(final QtecResult<SetWifiSwitchResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<PostInspectionResponse> safeInspectionTrans(IRequest request) {
    return cloudRestApi.safeInspectionTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostInspectionResponse>>() {
          }.getType();
          return (QtecResult<PostInspectionResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<PostInspectionResponse>, Observable<PostInspectionResponse>>() {
          @Override
          public Observable<PostInspectionResponse> call(final QtecResult<PostInspectionResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<List<GetBlackListResponse>> getBlackListTrans(IRequest request) {
    return cloudRestApi.getBlackListTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<List<GetBlackListResponse>>>() {
          }.getType();
          return (QtecResult<List<GetBlackListResponse>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<List<GetBlackListResponse>>, Observable<List<GetBlackListResponse>>>() {
          @Override
          public Observable<List<GetBlackListResponse>> call(final QtecResult<List<GetBlackListResponse>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<RemoveBlackMemResponse> removeBlackMemTrans(IRequest request) {
    return cloudRestApi.removeBlackMemTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RemoveBlackMemResponse>>() {
          }.getType();
          return (QtecResult<RemoveBlackMemResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<RemoveBlackMemResponse>, Observable<RemoveBlackMemResponse>>() {
          @Override
          public Observable<RemoveBlackMemResponse> call(final QtecResult<RemoveBlackMemResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetAntiFritQuestionResponse> getAntiFritQuestionTrans(IRequest request) {
    return cloudRestApi.getAntiFritQuestionTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetAntiFritQuestionResponse>>() {
          }.getType();
          return (QtecResult<GetAntiFritQuestionResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetAntiFritQuestionResponse>, Observable<GetAntiFritQuestionResponse>>() {
          @Override
          public Observable<GetAntiFritQuestionResponse> call(final QtecResult<GetAntiFritQuestionResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<PostSpecialAttentionResponse> postSpecialAttentionTrans(IRequest request) {
    return cloudRestApi.postSpecialAttentionTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostSpecialAttentionResponse>>() {
          }.getType();
          return (QtecResult<PostSpecialAttentionResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<PostSpecialAttentionResponse>, Observable<PostSpecialAttentionResponse>>() {
          @Override
          public Observable<PostSpecialAttentionResponse> call(final QtecResult<PostSpecialAttentionResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetSpecialAttentionResponse> getSpecialAttentionTrans(IRequest request) {
    return cloudRestApi.getSpecialAttentionTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSpecialAttentionResponse>>() {
          }.getType();
          return (QtecResult<GetSpecialAttentionResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetSpecialAttentionResponse>, Observable<GetSpecialAttentionResponse>>() {
          @Override
          public Observable<GetSpecialAttentionResponse> call(final QtecResult<GetSpecialAttentionResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetQosInfoResponse> getQosInfoTrans(IRequest request) {
    return cloudRestApi.getQosInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetQosInfoResponse>>() {
          }.getType();
          return (QtecResult<GetQosInfoResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetQosInfoResponse>, Observable<GetQosInfoResponse>>() {
          @Override
          public Observable<GetQosInfoResponse> call(final QtecResult<GetQosInfoResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<PostQosInfoResponse> postQosInfoTrans(IRequest request) {
    return cloudRestApi.postQosInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostQosInfoResponse>>() {
          }.getType();
          return (QtecResult<PostQosInfoResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<PostQosInfoResponse>, Observable<PostQosInfoResponse>>() {
          @Override
          public Observable<PostQosInfoResponse> call(final QtecResult<PostQosInfoResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfigTrans(IRequest request) {
    return cloudRestApi.getWifiTimeConfigTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>>() {
          }.getType();
          return (QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>, Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>>() {
          @Override
          public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> call(final QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<AddFingerResponse> addFingerInfoTrans(IRequest request) {
    return cloudRestApi.addFingerInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddFingerResponse>>() {
          }.getType();
          return (QtecResult<AddFingerResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<AddFingerResponse>, Observable<AddFingerResponse>>() {
          @Override
          public Observable<AddFingerResponse> call(final QtecResult<AddFingerResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<DeleteFingerResponse> deleteFingerInfoTrans(IRequest request) {
    return cloudRestApi.deleteFingerInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<DeleteFingerResponse>>() {
          }.getType();
          return (QtecResult<DeleteFingerResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<DeleteFingerResponse>, Observable<DeleteFingerResponse>>() {
          @Override
          public Observable<DeleteFingerResponse> call(final QtecResult<DeleteFingerResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfoTrans(IRequest request) {
    return cloudRestApi.queryFingerInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>>() {
          }.getType();
          return (QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>, Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>>() {
          @Override
          public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> call(final QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<ModifyFingerNameResponse> modifyFingerNameTrans(IRequest request) {
    return cloudRestApi.modifyFingerNameTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<ModifyFingerNameResponse>>() {
          }.getType();
          return (QtecResult<ModifyFingerNameResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<ModifyFingerNameResponse>, Observable<ModifyFingerNameResponse>>() {
          @Override
          public Observable<ModifyFingerNameResponse> call(final QtecResult<ModifyFingerNameResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetSambaAccountResponse> getSambaAccountTrans(IRequest request) {
    return cloudRestApi.getSambaAccountTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSambaAccountResponse>>() {
          }.getType();
          return (QtecResult<GetSambaAccountResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetSambaAccountResponse>, Observable<GetSambaAccountResponse>>() {
          @Override
          public Observable<GetSambaAccountResponse> call(final QtecResult<GetSambaAccountResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<QueryDiskStateResponse> queryDiskStateTrans(IRequest request) {
    return cloudRestApi.queryDiskStateTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<QueryDiskStateResponse>>() {
          }.getType();
          return (QtecResult<QueryDiskStateResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<QueryDiskStateResponse>, Observable<QueryDiskStateResponse>>() {
          @Override
          public Observable<QueryDiskStateResponse> call(final QtecResult<QueryDiskStateResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<QueryDiskStateResponse> formatDiskTrans(IRequest request) {
    return cloudRestApi.formatDiskTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<QueryDiskStateResponse>>() {
          }.getType();
          return (QtecResult<QueryDiskStateResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<QueryDiskStateResponse>, Observable<QueryDiskStateResponse>>() {
          @Override
          public Observable<QueryDiskStateResponse> call(final QtecResult<QueryDiskStateResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SearchIntelDevNotifyResponse> searchIntelDevNotifyTrans(IRequest request) {
    return cloudRestApi.searchIntelDevNotifyTrans(request)
        .map(new Func1<TransmitResponse<String>, QtecResult<SearchIntelDevNotifyResponse>>() {
          @Override
          public QtecResult<SearchIntelDevNotifyResponse> call(TransmitResponse<String> transmit) {
            Type type = new TypeToken<QtecResult<SearchIntelDevNotifyResponse>>() {
            }.getType();
            return (QtecResult<SearchIntelDevNotifyResponse>) RemoteCloudRepository.this.logRouterResult(transmit, type);
          }
        })
        .flatMap(new Func1<QtecResult<SearchIntelDevNotifyResponse>, Observable<SearchIntelDevNotifyResponse>>() {
          @Override
          public Observable<SearchIntelDevNotifyResponse> call(final QtecResult<SearchIntelDevNotifyResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> getStsToken(IRequest request) {
    return cloudRestApi.getStsToken(request);
  }

  @Override
  public Observable<GetRouterInfoResponse> getRouterInfoTrans(IRequest request) {
    return cloudRestApi.getRouterInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetRouterInfoResponse>>() {
          }.getType();
          return (QtecResult<GetRouterInfoResponse>) RemoteCloudRepository.this.logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetRouterInfoResponse>, Observable<GetRouterInfoResponse>>() {
          @Override
          public Observable<GetRouterInfoResponse> call(final QtecResult<GetRouterInfoResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetRouterInfoCloudResponse> getRouterInfoCloud(IRequest request) {
    return cloudRestApi.getRouterInfoCloud(request);
  }

  @Override
  public Observable<ModifyRouterDescResponse> modifyRouterDesc(IRequest request) {
    return cloudRestApi.modifyRouterDesc(request);
  }

  @Override
  public Observable<ModifyDevNameResponse> modifyDevName(IRequest request) {
    return cloudRestApi.modifyDevName(request);
  }

  @Override
  public Observable<List<GetRouterGroupsResponse>> getRouterGroups(IRequest request) {
    return cloudRestApi.getRouterGroups(request);
  }

  @Override
  public Observable<ModifyRouterGroupResponse> modifyRouterGroup(IRequest request) {
    return cloudRestApi.modifyRouterGroup(request);
  }

  @Override
  public Observable<CreateRouterGroupResponse> createRouterGroup(IRequest request) {
    return cloudRestApi.createRouterGroup(request);
  }

  @Override
  public Observable<SetDHCPResponse> setDHCPTrans(IRequest request) {
    return cloudRestApi.setDHCPTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetDHCPResponse>>() {
          }.getType();
          return (QtecResult<SetDHCPResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetDHCPResponse>, Observable<SetDHCPResponse>>() {
          @Override
          public Observable<SetDHCPResponse> call(final QtecResult<SetDHCPResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetPPPOEResponse> setPPPOETrans(IRequest request) {
    return cloudRestApi.setPPPOETrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetPPPOEResponse>>() {
          }.getType();
          return (QtecResult<SetPPPOEResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetPPPOEResponse>, Observable<SetPPPOEResponse>>() {
          @Override
          public Observable<SetPPPOEResponse> call(final QtecResult<SetPPPOEResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetStaticIPResponse> setStaticIPTrans(IRequest request) {
    return cloudRestApi.setStaticIPTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetStaticIPResponse>>() {
          }.getType();
          return (QtecResult<SetStaticIPResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetStaticIPResponse>, Observable<SetStaticIPResponse>>() {
          @Override
          public Observable<SetStaticIPResponse> call(final QtecResult<SetStaticIPResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetNetModeResponse> getNetModeTrans(IRequest request) {
    return cloudRestApi.getNetModeTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetNetModeResponse>>() {
          }.getType();
          return (QtecResult<GetNetModeResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetNetModeResponse>, Observable<GetNetModeResponse>>() {
          @Override
          public Observable<GetNetModeResponse> call(final QtecResult<GetNetModeResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<RestartRouterResponse> restartRouterTrans(IRequest request) {
    return cloudRestApi.restartRouterTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RestartRouterResponse>>() {
          }.getType();
          return (QtecResult<RestartRouterResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<RestartRouterResponse>, Observable<RestartRouterResponse>>() {
          @Override
          public Observable<RestartRouterResponse> call(final QtecResult<RestartRouterResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<FactoryResetResponse> factoryResetTrans(IRequest request) {
    return cloudRestApi.factoryResetTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<FactoryResetResponse>>() {
          }.getType();
          return (QtecResult<FactoryResetResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<FactoryResetResponse>, Observable<FactoryResetResponse>>() {
          @Override
          public Observable<FactoryResetResponse> call(final QtecResult<FactoryResetResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<UnbindRouterResponse> unbindRouter(IRequest request) {
    return cloudRestApi.unbindRouter(request);
  }

  @Override
  public Observable<UpdateFirmwareResponse> updateFirmwareTrans(IRequest request) {
    return cloudRestApi.updateFirmwareTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<UpdateFirmwareResponse>>() {
          }.getType();
          return (QtecResult<UpdateFirmwareResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<UpdateFirmwareResponse>, Observable<UpdateFirmwareResponse>>() {
          @Override
          public Observable<UpdateFirmwareResponse> call(final QtecResult<UpdateFirmwareResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetWifiConfigResponse> getWifiConfigTrans(IRequest request) {
    return cloudRestApi.getWifiConfigTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWifiConfigResponse>>() {
          }.getType();
          return (QtecResult<GetWifiConfigResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetWifiConfigResponse>, Observable<GetWifiConfigResponse>>() {
          @Override
          public Observable<GetWifiConfigResponse> call(final QtecResult<GetWifiConfigResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<ConfigWifiResponse> configWifiTrans(IRequest request) {
    return cloudRestApi.configWifiTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<ConfigWifiResponse>>() {
          }.getType();
          return (QtecResult<ConfigWifiResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<ConfigWifiResponse>, Observable<ConfigWifiResponse>>() {
          @Override
          public Observable<ConfigWifiResponse> call(final QtecResult<ConfigWifiResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetTimerTaskResponse> getTimerTaskTrans(IRequest request) {
    return cloudRestApi.getTimerTaskTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetTimerTaskResponse>>() {
          }.getType();
          return (QtecResult<GetTimerTaskResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetTimerTaskResponse>, Observable<GetTimerTaskResponse>>() {
          @Override
          public Observable<GetTimerTaskResponse> call(final QtecResult<GetTimerTaskResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetRouterTimerResponse> setRouterTimerTrans(IRequest request) {
    return cloudRestApi.setRouterTimerTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetRouterTimerResponse>>() {
          }.getType();
          return (QtecResult<SetRouterTimerResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetRouterTimerResponse>, Observable<SetRouterTimerResponse>>() {
          @Override
          public Observable<SetRouterTimerResponse> call(final QtecResult<SetRouterTimerResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<CheckAdminPwdResponse> checkAdminPwdTrans(IRequest request) {
    return cloudRestApi.checkAdminPwdTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<CheckAdminPwdResponse>>() {
          }.getType();
          return (QtecResult<CheckAdminPwdResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<CheckAdminPwdResponse>, Observable<CheckAdminPwdResponse>>() {
          @Override
          public Observable<CheckAdminPwdResponse> call(final QtecResult<CheckAdminPwdResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<SetAdminPwdResponse> setAdminPwdTrans(IRequest request) {
    return cloudRestApi.setAdminPwdTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetAdminPwdResponse>>() {
          }.getType();
          return (QtecResult<SetAdminPwdResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<SetAdminPwdResponse>, Observable<SetAdminPwdResponse>>() {
          @Override
          public Observable<SetAdminPwdResponse> call(final QtecResult<SetAdminPwdResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }
/*@Override
  public Observable<GetSambaAccountResponse> getSambaAccountTrans(IRequest request) {
    return cloudRestApi.getSambaAccountTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSambaAccountResponse>>() {
          }.getType();
          return (QtecResult<GetSambaAccountResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetSambaAccountResponse>, Observable<GetSambaAccountResponse>>() {
          @Override
          public Observable<GetSambaAccountResponse> call(final QtecResult<GetSambaAccountResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }*/


  @Override
  public Observable<PostSignalRegulationResponse> setSignalRegulationInfoTrans(IRequest request) {
    return cloudRestApi.setSignalRegulationInfoTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostSignalRegulationResponse>>() {
          }.getType();
          return (QtecResult<PostSignalRegulationResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<PostSignalRegulationResponse>, Observable<PostSignalRegulationResponse>>() {
          @Override
          public Observable<PostSignalRegulationResponse> call(final QtecResult<PostSignalRegulationResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetBandSpeedResponse> getBandSpeedTrans(IRequest request) {
    return cloudRestApi.getBandSpeedTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetBandSpeedResponse>>() {
          }.getType();
          return (QtecResult<GetBandSpeedResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetBandSpeedResponse>, Observable<GetBandSpeedResponse>>() {
          @Override
          public Observable<GetBandSpeedResponse> call(final QtecResult<GetBandSpeedResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }


  @Override
  public Observable<List<GetFingerprintsResponse>> getFingerprints(IRequest request) {
    return cloudRestApi.getFingerprints(request);
  }

  @Override
  public Observable<GetKeyResponse<List<KeyBean>>> getKeyTrans(IRequest request) {
    return cloudRestApi.getKeyTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetKeyResponse<List<KeyBean>>>>() {
          }.getType();
          return (QtecResult<GetKeyResponse<List<KeyBean>>>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetKeyResponse<List<KeyBean>>>, Observable<GetKeyResponse<List<KeyBean>>>>() {
          @Override
          public Observable<GetKeyResponse<List<KeyBean>>> call(final QtecResult<GetKeyResponse<List<KeyBean>>> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<List<GetPasswordsResponse>> getPasswords(IRequest request) {
    return cloudRestApi.getPasswords(request);
  }

  @Override
  public Observable<ModifyLockPwdNameResponse> modifyLockPwdName(IRequest request) {
    return cloudRestApi.modifyLockPwdName(request);
  }

  @Override
  public Observable<DeleteLockPwdResponse> deleteLockPwd(IRequest request) {
    return cloudRestApi.deleteLockPwd(request);
  }

  @Override
  public Observable<ModifyLockFpNameResponse> modifyLockFpName(IRequest request) {
    return cloudRestApi.modifyLockFpName(request);
  }

  @Override
  public Observable<DeleteLockFpResponse> deleteLockFp(IRequest request) {
    return cloudRestApi.deleteLockFp(request);
  }

  @Override
  public Observable<AddLockPwdResponse> addLockPwd(IRequest request) {
    return cloudRestApi.addLockPwd(request);
  }

  @Override
  public Observable<AddLockFpResponse> addLockFp(IRequest request) {
    return cloudRestApi.addLockFp(request);
  }

  @Override
  public Observable<CheckFirmwareResponse> checkFirmwareTrans(IRequest request) {
    return cloudRestApi.checkFirmwareTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<CheckFirmwareResponse>>() {
          }.getType();
          return (QtecResult<CheckFirmwareResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<CheckFirmwareResponse>, Observable<CheckFirmwareResponse>>() {
          @Override
          public Observable<CheckFirmwareResponse> call(final QtecResult<CheckFirmwareResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatusTrans(IRequest request) {
    return cloudRestApi.getFirmwareUpdateStatusTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetFirmwareUpdateStatusResponse>>() {
          }.getType();
          return (QtecResult<GetFirmwareUpdateStatusResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetFirmwareUpdateStatusResponse>, Observable<GetFirmwareUpdateStatusResponse>>() {
          @Override
          public Observable<GetFirmwareUpdateStatusResponse> call(final QtecResult<GetFirmwareUpdateStatusResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<CheckAppVersionResponse> checkAppVersion(IRequest request) {
    return cloudRestApi.checkAppVersion(request);
  }

  @Override
  public Observable<RemoteLockOperationResponse> remoteLockOptTrans(IRequest request) {
    return cloudRestApi.remoteLockOptTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RemoteLockOperationResponse>>() {
          }.getType();
          return (QtecResult<RemoteLockOperationResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<RemoteLockOperationResponse>, Observable<RemoteLockOperationResponse>>() {
          @Override
          public Observable<RemoteLockOperationResponse> call(final QtecResult<RemoteLockOperationResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLockTrans(IRequest request) {
    return cloudRestApi.bindRouterToLockTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
          }.getType();
          return (QtecResult<BindRouterToLockResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<BindRouterToLockResponse>, Observable<BindRouterToLockResponse>>() {
          @Override
          public Observable<BindRouterToLockResponse> call(final QtecResult<BindRouterToLockResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<QueryBindRouterToLockResponse> queryBindRouterToLockTrans(IRequest request) {
    return cloudRestApi.queryBindRouterToLockTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<QueryBindRouterToLockResponse>>() {
          }.getType();
          return (QtecResult<QueryBindRouterToLockResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<QueryBindRouterToLockResponse>, Observable<QueryBindRouterToLockResponse>>() {
          @Override
          public Observable<QueryBindRouterToLockResponse> call(final QtecResult<QueryBindRouterToLockResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<UnbindRouterToLockResponse> unbindRouterToLockTrans(IRequest request) {
    return cloudRestApi.unbindRouterToLockTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<UnbindRouterToLockResponse>>() {
          }.getType();
          return (QtecResult<UnbindRouterToLockResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<UnbindRouterToLockResponse>, Observable<UnbindRouterToLockResponse>>() {
          @Override
          public Observable<UnbindRouterToLockResponse> call(final QtecResult<UnbindRouterToLockResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(ExceptionConstant.convertCodeToMsg(routerQtecResult.getCode(), routerQtecResult.getMsg())));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetUsersOfLockResponse<List<String>>> getUsersOfLock(IRequest request) {
    return cloudRestApi.getUsersOfLock(request);
  }

  @Override
  public Observable<CheckLockVersionResponse> checkLockVersion(IRequest request) {
    return cloudRestApi.checkLockVersion(request);
  }

  @Override
  public Observable<GetUnlockModeResponse> getUnlockMode(IRequest request) {
    return cloudRestApi.getUnlockMode(request);
  }

  @Override
  public Observable<ModifyUnlockModeResponse> modifyUnlockMode(IRequest request) {
    return cloudRestApi.modifyUnlockMode(request);
  }

  @Override
  public Observable<UploadLogcatResponse> uploadLogcat(IRequest request) {
    return cloudRestApi.uploadLogcat(request);
  }

  @Override
  public Observable<List<GetDoorCardsResponse>> getDoorCards(IRequest request) {
    return cloudRestApi.getDoorCards(request);
  }

  @Override
  public Observable<DeleteLockDoorCardResponse> deleteLockDoorCard(IRequest request) {
    return cloudRestApi.deleteLockDoorCard(request);

  }

  @Override
  public Observable<ModifyLockDoorCardNameResponse> modifyLockDoorCardName(IRequest request) {
    return cloudRestApi.modifyLockDoorCardName(request);
  }

  @Override
  public Observable<AddLockDoorCardResponse> addLockDoorCard(IRequest request) {
    return cloudRestApi.addLockDoorCard(request);
  }

  @Override
  public Observable<LockFactoryResetResponse> lockFactoryReset(IRequest request) {
    return cloudRestApi.lockFactoryReset(request);
  }

  @Override
  public Observable<GetUserRoleResponse> getUserRole(IRequest request) {
    return cloudRestApi.getUserRole(request);
  }

  @Override
  public Observable<List<GetLockUsersResponse>> getLockUsers(IRequest request) {
    return cloudRestApi.getLockUsers(request);
  }

  @Override
  public Observable<DeleteLockUserResponse> deleteLockUser(IRequest request) {
    return cloudRestApi.deleteLockUser(request);
  }

  @Override
  public Observable<UnbindLockOfAdminResponse> unbindLockOfAdmin(IRequest request) {
    return cloudRestApi.unbindLockOfAdmin(request);
  }

  @Override
  public Observable<GetRouterFirstConfigResponse> getFirstConfigTrans(IRequest request) {
    return cloudRestApi.getFirstConfigTrans(request)
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetRouterFirstConfigResponse>>() {
          }.getType();
          return (QtecResult<GetRouterFirstConfigResponse>) logRouterResult(transmit, type);
        })
        .flatMap(new Func1<QtecResult<GetRouterFirstConfigResponse>, Observable<GetRouterFirstConfigResponse>>() {
          @Override
          public Observable<GetRouterFirstConfigResponse> call(final QtecResult<GetRouterFirstConfigResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(routerQtecResult.getMsg()));
              } else {
                subscriber.onNext(routerQtecResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<GetPassagewayModeResponse> getPassagewayMode(IRequest request) {
    return cloudRestApi.getPassagewayMode(request);
  }

  @Override
  public Observable<ModifyPassagewayModeResponse> modifyPassagewayMode(IRequest request) {
    return cloudRestApi.modifyPassagewayMode(request);
  }

  @Override
  public Observable<UpdateLockVersionResponse> updateLockVersion(IRequest request) {
    return cloudRestApi.updateLockVersion(request);
  }

  @Override
  public Observable<AddTempPwdResponse> addTempPwd(IRequest request) {
    return cloudRestApi.addTempPwd(request);
  }

  @Override
  public Observable<QueryTempPwdResponse> queryTempPwd(IRequest request) {
    return cloudRestApi.queryTempPwd(request);
  }

  @Override
  public Observable<GetTempPwdResponse> getTempPwd(IRequest request) {
    return cloudRestApi.getTempPwd(request);
  }

  @Override
  public Observable<GetLockVolumeResponse> getLockVolume(IRequest request) {
    return cloudRestApi.getLockVolume(request);
  }

  @Override
  public Observable<AdjustLockVolumeResponse> adjustLockVolume(IRequest request) {
    return cloudRestApi.adjustLockVolume(request);
  }

  @Override
  public Observable<CheckLiteVersionResponse> checkLiteVersion(IRequest request) {
    return cloudRestApi.checkLiteVersion(request);
  }

  @Override
  public Observable<UpdateLiteResponse> updateLite(IRequest request) {
    return cloudRestApi.updateLite(request);
  }

  @Override
  public Observable<GetLiteUpdateResponse> getLiteUpdate(IRequest request) {
    return cloudRestApi.getLiteUpdate(request);
  }

  @Override
  public Observable<CloudUnbindRouterLockResponse> cloudUnbindRouterLock(IRequest request) {
    return cloudRestApi.cloudUnbindRouterLock(request);
  }

  private QtecResult logRouterResult(TransmitResponse<String> transmit, Type type) {
    try {
      String routerEncryptInfo = transmit.getEncryptInfo();
      Logger.t("router-response").json(routerEncryptInfo);
      return (QtecResult) mJsonMapper.fromJson(routerEncryptInfo, type);
    } catch (Exception e) {
      e.printStackTrace();
      QtecResult result = new QtecResult();
      result.setMsg("网关数据格式异常");
      result.setCode(-1000);
      return result;
    }

  }
}