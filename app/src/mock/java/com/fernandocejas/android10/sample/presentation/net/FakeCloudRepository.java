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

import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ConvertUtils;
import com.fernandocejas.android10.sample.data.mapper.BleMapper;
import com.fernandocejas.android10.sample.data.net.CloudUrlPath;
import com.fernandocejas.android10.sample.data.utils.CloudConverter;
import com.fernandocejas.android10.sample.data.utils.IConverter;
import com.fernandocejas.android10.sample.domain.constant.ModelConstant;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.presentation.utils.Base64Util;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.cateye.model.response.DeleteDoorBellRecordResponse;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailResponse;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadResponse;
import com.qtec.mapp.model.req.TransmitRequest;
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
import com.qtec.mapp.model.rsp.GetStsTokenResponse.AssumedRoleUserBean;
import com.qtec.mapp.model.rsp.GetStsTokenResponse.CredentialsBean;
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
import com.qtec.mapp.model.rsp.ModifyDevNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockDoorCardNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockFpNameResponse;
import com.qtec.mapp.model.rsp.ModifyLockPwdNameResponse;
import com.qtec.mapp.model.rsp.ModifyPassagewayModeResponse;
import com.qtec.mapp.model.rsp.ModifyPwdGetIdCodeResponse;
import com.qtec.mapp.model.rsp.ModifyPwdResponse;
import com.qtec.mapp.model.rsp.ModifyRouterDescResponse;
import com.qtec.mapp.model.rsp.ModifyRouterGroupResponse;
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
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.router.model.req.RemoteLockOperationRequest;
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
import com.qtec.router.model.rsp.SearchIntelDevResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse.IntelDev;
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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import sun.misc.BASE64Encoder;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/08
 *     desc   : {@link FakeCloudRepository} for retrieving user data from fake remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class FakeCloudRepository implements CloudRepository {

  private final JsonMapper mJsonMapper = new JsonMapper();
  private final IConverter mCloudConverter = new CloudConverter();
  private Context mContext;

  @Inject
  public FakeCloudRepository(Context context) {
    mContext = context;
  }

  @Override
  public Observable<LoginResponse> login(IRequest request) {

    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_LOGIN, CloudUrlPath.METHOD_POST, 0, false);

      LoginResponse response = new LoginResponse();
      response.setToken("asd9f8as9df8asd7gadsfua90sd8f09asd");
      response.setUserHeadImg("img");
      response.setUserUniqueKey("unikey");
      response.setUserNickName("jklolin");
      response.setUserPhone("13866666666");
      response.setId(Integer.MAX_VALUE);
      QtecResult<LoginResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<LoginResponse>>() {
      }.getType();

      QtecResult<LoginResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  @Override
  public Observable<GetIdCodeResponse> getIdCode(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_ID_CODE, CloudUrlPath.METHOD_POST, 0, false);

      GetIdCodeResponse response = new GetIdCodeResponse();

      QtecResult<GetIdCodeResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetIdCodeResponse>>() {
      }.getType();

      QtecResult<GetIdCodeResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  /**
   * 请求用户协议
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetAgreementResponse> getUserAgreement(IRequest request) {

    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_AGREEMENT, CloudUrlPath.METHOD_GET, 0, false);

      GetAgreementResponse response = new GetAgreementResponse();
   /* response.setStatuteContent("用户协议 2017/6/12 用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12");*/
      QtecResult<GetAgreementResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetAgreementResponse>>() {
      }.getType();

      QtecResult<GetAgreementResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetMemRemarkNameResponse> getMemRemarkName(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_MEM_REMARK_NAME, CloudUrlPath.METHOD_POST, 1, false);

      GetMemRemarkNameResponse response = new GetMemRemarkNameResponse();

   /* response.setStatuteContent("用户协议 2017/6/12 用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12" +
        "用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12用户协议 2017/6/12");*/
      QtecResult<GetMemRemarkNameResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetMemRemarkNameResponse>>() {
      }.getType();

      QtecResult<GetMemRemarkNameResponse> qtecResult = addDecryption(result, type, 1, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  @Override
  public Observable<RegisterResponse> register(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_REGISTER, CloudUrlPath.METHOD_POST, 0, false);

      RegisterResponse response = new RegisterResponse();
      response.setToken("asd9f8as9df8asd7gadsfua90sd8f09asd");
      response.setUserHeadImg("img");
      response.setUserUniqueKey("unikey");
      response.setUserNickName("jklolin");
      response.setUserPhone("13866666666");
      response.setId(Integer.MAX_VALUE);
      QtecResult<RegisterResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<RegisterResponse>>() {
      }.getType();

      QtecResult<RegisterResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  /**
   * 请求用户隐私协议
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetAgreementResponse> getUserSerectAgreement(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_AGREEMENT, CloudUrlPath.METHOD_GET, 0, false);

      GetAgreementResponse response = new GetAgreementResponse();
      response.setStatuteContent("2017/6/12 隐私协议 2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议" +
          "2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议" +
          "2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议" +
          "2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议" +
          "2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议2017/6/12 隐私协议" +
          "");
      QtecResult<GetAgreementResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetAgreementResponse>>() {
      }.getType();

      QtecResult<GetAgreementResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ResetPwdGetIdCodeResponse> resetPwdGetIdCode(final IRequest request) {

    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_RESET_PWD_GET_ID_CODE, CloudUrlPath.METHOD_POST, 0, false);

      ResetPwdGetIdCodeResponse response = new ResetPwdGetIdCodeResponse();

      QtecResult<ResetPwdGetIdCodeResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ResetPwdGetIdCodeResponse>>() {
      }.getType();

      QtecResult<ResetPwdGetIdCodeResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  @Override
  public Observable<ResetPwdResponse> resetPwd(final IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_RESET_PWD_FORGET, CloudUrlPath.METHOD_POST, 0, false);

      ResetPwdResponse response = new ResetPwdResponse();

      QtecResult<ResetPwdResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ResetPwdResponse>>() {
      }.getType();

      QtecResult<ResetPwdResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 请求 问题列表
   */
  @Override
  public Observable<List<GetQuestionListResponse>> getQuestionList(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_QUESTION_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetQuestionListResponse> questionListResponses = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      GetQuestionListResponse response = new GetQuestionListResponse();
      response.setFaqUniqueKey("" + (i + 1));
      response.setTitle((i + 1) + "、网关有问题");
      questionListResponses.add(response);
    }
      QtecResult<List<GetQuestionListResponse>> result = new QtecResult<>();
      result.setData(questionListResponses);
      Type type = new TypeToken<QtecResult<List<GetQuestionListResponse>>>() {
      }.getType();

      QtecResult<List<GetQuestionListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  /**
   * 问题详情
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetQuestionDetailResponse> getQuestionDeatil(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_QUESTION_DETAIL, CloudUrlPath.METHOD_GET, 0, false);

      GetQuestionDetailResponse response = new GetQuestionDetailResponse();
      response.setTitle("关于你的网关问题的解答");
      response.setContent("网关就是有问题，你知道吗？网关就是有问题，你知道吗？网关就是有问题，你知道吗？网关就是有问题，你知道吗？" +
          "网关就是有问题，你知道吗？网关就是有问题，你知道吗？网关就是有问题，你知道吗？网关就是有问题，你知道吗？知道");
      QtecResult<GetQuestionDetailResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetQuestionDetailResponse>>() {
      }.getType();

      QtecResult<GetQuestionDetailResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 我的反馈
   *
   * @param
   * @return
   */
  @Override
  public Observable<List<GetMyAdviceResponse>> getMyAdvice(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_MY_ADVICE_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetMyAdviceResponse> myAdviceResponses = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        GetMyAdviceResponse response = new GetMyAdviceResponse();
        response.setCreateTime("createTime" + i);
        response.setFeedbackUniqueKey("uniqueKey" + i);
        response.setFeedbackContent("content" + i);
        myAdviceResponses.add(response);
      }

      QtecResult<List<GetMyAdviceResponse>> result = new QtecResult<>();
      result.setData(myAdviceResponses);
      Type type = new TypeToken<QtecResult<List<GetMyAdviceResponse>>>() {
      }.getType();

      QtecResult<List<GetMyAdviceResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  /**
   * 我的反馈 详情
   *
   * @param
   * @return
   */
  @Override
  public Observable<FeedBackResponse<List<FeedBackResponse.ReplyContent>>> getAdviceDetail(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_ADVICE_DETAIL, CloudUrlPath.METHOD_GET, 0, false);
      FeedBackResponse<List<FeedBackResponse.ReplyContent>> details = new FeedBackResponse<List<FeedBackResponse.ReplyContent>>();
      details.setFeedbackContent(Base64Util.encodeBase64("网关有问题？"));

      FeedBackResponse.ReplyContent detailResponses = new FeedBackResponse.ReplyContent();
      detailResponses.setContent(Base64Util.encodeBase64("回复1"));
      detailResponses.setType("0");
      detailResponses.setTime(Base64Util.encodeBase64("2016-05-01"));

      FeedBackResponse.ReplyContent detailResponses1 = new FeedBackResponse.ReplyContent();
      detailResponses1.setContent(Base64Util.encodeBase64("回复2"));
      detailResponses1.setType("1");
      detailResponses1.setTime(Base64Util.encodeBase64("2017-05-02"));

      FeedBackResponse.ReplyContent detailResponses2 = new FeedBackResponse.ReplyContent();
      detailResponses2.setContent(Base64Util.encodeBase64("回复1"));
      detailResponses2.setType("0");
      detailResponses2.setTime(Base64Util.encodeBase64("2017-06-01"));

      FeedBackResponse.ReplyContent detailResponses3 = new FeedBackResponse.ReplyContent();
      detailResponses3.setContent(Base64Util.encodeBase64("回复2"));
      detailResponses3.setType("1");
      detailResponses3.setTime(Base64Util.encodeBase64("2017-07-02"));

      List<FeedBackResponse.ReplyContent> detailList = new ArrayList<FeedBackResponse.ReplyContent>();
      detailList.add(detailResponses);
      detailList.add(detailResponses1);
      detailList.add(detailResponses2);
      detailList.add(detailResponses3);

      details.setReplyContent(detailList);

      QtecResult<FeedBackResponse<List<FeedBackResponse.ReplyContent>>> result = new QtecResult<>();
      result.setData(details);
      Type type = new TypeToken<QtecResult<FeedBackResponse<List<FeedBackResponse.ReplyContent>>>>() {
      }.getType();

      QtecResult<FeedBackResponse<List<FeedBackResponse.ReplyContent>>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  /**
   * 新增反馈
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetfeedBackResponse> getFeedBack(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_POST_FEED_BACK, CloudUrlPath.METHOD_POST, 0, false);
      GetfeedBackResponse response = new GetfeedBackResponse();
      response.setFeedbackUniqueKey("123");
      String tmp = "已经收到反馈，请在我的反馈中查看";
      response.setReplyContent(Base64Util.encodeBase64(tmp));
      QtecResult<GetfeedBackResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetfeedBackResponse>>() {
      }.getType();

      QtecResult<GetfeedBackResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 更新反饋
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetUpdateFeedBackResponse> getUpdateFeedBack(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_UPDATE_FEED_BACK, CloudUrlPath.METHOD_POST, 0, false);
      GetUpdateFeedBackResponse response = new GetUpdateFeedBackResponse();
      response.setReplyContent(Base64Util.encodeBase64("反饋內容 11"));
      QtecResult<GetUpdateFeedBackResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetUpdateFeedBackResponse>>() {
      }.getType();

      QtecResult<GetUpdateFeedBackResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyUserInfoResponse> modifyUserInfo(final IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_USER_INFO, CloudUrlPath.METHOD_POST, 0, false);

      ModifyUserInfoResponse response = new ModifyUserInfoResponse();

      QtecResult<ModifyUserInfoResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyUserInfoResponse>>() {
      }.getType();

      QtecResult<ModifyUserInfoResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<LogoutResponse> logout(final IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_LOGOUT, CloudUrlPath.METHOD_POST, 0, false);

      LogoutResponse response = new LogoutResponse();

      QtecResult<LogoutResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<LogoutResponse>>() {
      }.getType();

      QtecResult<LogoutResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyPwdGetIdCodeResponse> modifyPwdGetIdCode(final IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_RESET_PWD_GET_ID_CODE, CloudUrlPath.METHOD_POST, 0, false);

      ModifyPwdGetIdCodeResponse response = new ModifyPwdGetIdCodeResponse();

      QtecResult<ModifyPwdGetIdCodeResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyPwdGetIdCodeResponse>>() {
      }.getType();

      QtecResult<ModifyPwdGetIdCodeResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyPwdResponse> modifyPwd(final IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_PWD, CloudUrlPath.METHOD_POST, 0, false);

      ModifyPwdResponse response = new ModifyPwdResponse();

      QtecResult<ModifyPwdResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyPwdResponse>>() {
      }.getType();

      QtecResult<ModifyPwdResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 获得设备数量
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetDeviceCountResponse> getDeviceCount(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_DEVICE_COUNT, CloudUrlPath.METHOD_GET, 0, false);
      GetDeviceCountResponse response = new GetDeviceCountResponse();
      response.setRouterNum("5");
      QtecResult<GetDeviceCountResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetDeviceCountResponse>>() {
      }.getType();

      QtecResult<GetDeviceCountResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UploadMsgDeviceIDResponse> uploadMsgId(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_UPLOAD_MSG_ID, CloudUrlPath.METHOD_POST, 0, false);
      UploadMsgDeviceIDResponse response = new UploadMsgDeviceIDResponse();
      QtecResult<UploadMsgDeviceIDResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<UploadMsgDeviceIDResponse>>() {
      }.getType();

      QtecResult<UploadMsgDeviceIDResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 获得网关列表
   *
   * @param
   * @return
   */
  @Override
  public Observable<List<GetDevTreeResponse<List<DeviceBean>>>> getDevTree(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_DEV_TREE, CloudUrlPath.METHOD_GET, 0, false);

      List<DeviceBean> deviceBeanList = new ArrayList<>();

      List<GetDevTreeResponse<List<DeviceBean>>> deviceList = new ArrayList<>();
      for (int i = 0; i < 10; i++) {
        GetDevTreeResponse<List<DeviceBean>> device = new GetDevTreeResponse<>();
        device.setDeviceName("九州量子设备" + i + "s");
        device.setDeviceModel("12");
        device.setDeviceVersion("13");
        if (i == 1) {
          device.setDeviceSerialNo("LOC29834");
          device.setRouterSerialNo("qtec0000_0");
        } else {
          device.setDeviceSerialNo(String.valueOf("qtec0000_" + i));
        }

        device.setDeviceShareNum("5");
        device.setDeviceList(deviceBeanList);

        /*device.setDeviceType(i % 2 == 0 ? "0" : "1");*/

        if(i%3 == 0){
          device.setDeviceType("0");
        }else if(i%3 == 1){
          device.setDeviceType("1");
        }else {
          device.setDeviceType("2");
        }

        deviceList.add(device);
      }

      QtecResult<List<GetDevTreeResponse<List<DeviceBean>>>> result = new QtecResult<>();
      result.setData(deviceList);
      Type type = new TypeToken<QtecResult<List<GetDevTreeResponse<List<DeviceBean>>>>>() {
      }.getType();

      QtecResult<List<GetDevTreeResponse<List<DeviceBean>>>> qtecResult = addDecryption(result, type, 0, false, subscriber);

      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 设备共享成员
   *
   * @param
   * @return
   */
  @Override
  public Observable<List<GetShareMemListResponse>> getSharedMemList(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_SHARED_MEM_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetShareMemListResponse> memListResponse = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
        GetShareMemListResponse mem = new GetShareMemListResponse();
        mem.setHistoryUniqueKey("123");
        mem.setSharedUserPhone("18827321287");
        mem.setShareDate("2017/6/23");
        mem.setSharedUserName("qtec");
        mem.setHandleType("1");
        memListResponse.add(mem);
      }
      for (int i = 0; i < 3; i++) {
        GetShareMemListResponse mem = new GetShareMemListResponse();
        mem.setHistoryUniqueKey("123");
        mem.setSharedUserPhone("15558132381");
        mem.setShareDate("2017/7/24");
        mem.setSharedUserName("qtec");
        mem.setHandleType("1");
        memListResponse.add(mem);
      }
      QtecResult<List<GetShareMemListResponse>> result = new QtecResult<>();
      result.setData(memListResponse);
      Type type = new TypeToken<QtecResult<List<GetShareMemListResponse>>>() {
      }.getType();

      QtecResult<List<GetShareMemListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  @Override
  public Observable<List<GetLockListResponse>> getLockList(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_LOCK_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetLockListResponse> responses = new ArrayList<GetLockListResponse>();
      for (int i = 0; i < 3; i++) {
        GetLockListResponse response = new GetLockListResponse();
        response.setDeviceSerialNo("serialId"+i);
        response.setDeviceNickName("lockName"+i);
        responses.add(response);
      }

      QtecResult<List<GetLockListResponse>> result = new QtecResult<>();
      result.setData(responses);
      Type type = new TypeToken<QtecResult<List<GetLockListResponse>>>() {
      }.getType();

      QtecResult<List<GetLockListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<QueryCatLockResponse> queryCatLock(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_CAT_LOCK, CloudUrlPath.METHOD_GET, 0, false);
      QueryCatLockResponse response = new QueryCatLockResponse();
      response.setDeviceNickName("nickName");
      response.setDeviceSerialNo("serialNo");
      response.setDeviceModel("model");

      QtecResult<QueryCatLockResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<QueryCatLockResponse>>() {
      }.getType();

      QtecResult<QueryCatLockResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> getDoorBeelMsgList(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetDoorBellRecordDetailResponse> getDoorBeelMsgDetail(IRequest request) {
    return null;
  }

  @Override
  public Observable<DeleteDoorBellRecordResponse> deleteDoorBeelMsg(IRequest request) {
    return null;
  }

  @Override
  public Observable<SetAllDoorBellRecordReadResponse> setAllDoorBeelMsgIsRead(IRequest request) {
    return null;
  }

  @Override
  public Observable<ConnectLockResponse> connectLock(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_CONNECT_LOCK, CloudUrlPath.METHOD_POST, 0, false);
      List<GetLockListResponse> responses = new ArrayList<GetLockListResponse>();
      ConnectLockResponse response = new ConnectLockResponse();


      QtecResult<ConnectLockResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ConnectLockResponse>>() {
      }.getType();

      QtecResult<ConnectLockResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UnbindCatLockResponse> unbindCatLock(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_UNBIND_CAT_LOCK, CloudUrlPath.METHOD_POST, 0, false);

      UnbindCatLockResponse response = new UnbindCatLockResponse();

      QtecResult<UnbindCatLockResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<UnbindCatLockResponse>>() {
      }.getType();

      QtecResult<UnbindCatLockResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UploadDevicePwdResponse> uploadDevicePwd(IRequest request) {
    return null;
  }

  /**
   * 邀请
   *
   * @param
   * @return
   */
  @Override
  public Observable<PostInvitationResponse> postInvitation(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_INVITATION, CloudUrlPath.METHOD_POST, 0, false);
      PostInvitationResponse response = new PostInvitationResponse();
      QtecResult<PostInvitationResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<PostInvitationResponse>>() {
      }.getType();

      QtecResult<PostInvitationResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 其他消息列表
   *
   * @param
   * @return
   */
  @Override
  public Observable<List<GetMsgOtherListResponse>> getMsgList(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MSG_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetMsgOtherListResponse> response = new ArrayList<>();

      GetMsgOtherListResponse otherMsg1 = new GetMsgOtherListResponse();
      otherMsg1.setMessageTitle("九州量子网关");
      otherMsg1.setCreateDate("2017-10-12 12:12:04");
      otherMsg1.setIsRead("0");
      otherMsg1.setMessageUniqueKey("0");
      otherMsg1.setMessageContent("测试");
      response.add(otherMsg1);

      GetMsgOtherListResponse otherMsg2 = new GetMsgOtherListResponse();
      otherMsg2.setMessageTitle("九州量子网关");
      otherMsg2.setCreateDate("2017-10-11 12:12:04");
      otherMsg2.setIsRead("0");
      otherMsg2.setMessageUniqueKey("1");
      otherMsg2.setMessageContent("测试");
      response.add(otherMsg2);

      for (int i = 0; i < 4; i++) {
        GetMsgOtherListResponse otherMsg = new GetMsgOtherListResponse();
        otherMsg.setMessageTitle("九州量子网关" + i);
        otherMsg.setCreateDate("2017-07-12 12:12:04");
        otherMsg.setIsRead("0");
        otherMsg.setMessageUniqueKey("" + (i + 2));
        otherMsg.setMessageContent("测试");
        response.add(otherMsg);
      }

      GetMsgOtherListResponse otherMsg3 = new GetMsgOtherListResponse();
      otherMsg3.setMessageTitle("九州量子网关");
      otherMsg3.setCreateDate("2016-08-13 12:12:04");
      otherMsg3.setIsRead("0");
      otherMsg3.setMessageUniqueKey("6");
      otherMsg3.setMessageContent("测试");
      response.add(otherMsg3);

      QtecResult<List<GetMsgOtherListResponse>> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<List<GetMsgOtherListResponse>>>() {
      }.getType();

      QtecResult<List<GetMsgOtherListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 分享消息列表
   *
   * @param
   * @return
   */
  public Observable<List<GetMsgListResponse<GetMsgListResponse.messageContent>>> getMsgShareList(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MSG_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetMsgListResponse<GetMsgListResponse.messageContent>> response = new ArrayList<>();
      String[] lock = {"门锁1", "门锁2"};

      for (int i = 0; i < 3; i++) {
        GetMsgListResponse.messageContent deviceInfo = new GetMsgListResponse.messageContent<>();
        deviceInfo.setDeviceName("九州量子网关" + i);
        deviceInfo.setDeviceInfo(lock);
        deviceInfo.setHandleType("" + i);
        deviceInfo.setHistoryUniqueKey("" + i);
        deviceInfo.setUserUniqueKey("" + i);

        GetMsgListResponse<GetMsgListResponse.messageContent> msgList = new GetMsgListResponse<GetMsgListResponse.messageContent>();
        msgList.setMessageTitle("九妹妹邀您共享以下设备" + i);
        msgList.setCreateDate("2017-10-12 12:12:04");
        msgList.setIsRead("0");
        msgList.setMessageUniqueKey("" + i);
        msgList.setMessageContent(deviceInfo);
        response.add(msgList);
      }

      GetMsgListResponse.messageContent deviceInfo = new GetMsgListResponse.messageContent<>();
      deviceInfo.setDeviceName("九州量子网关3");
      deviceInfo.setDeviceInfo(lock);
      deviceInfo.setHandleType("3");
      deviceInfo.setHistoryUniqueKey("3");
      deviceInfo.setUserUniqueKey("3");

      GetMsgListResponse<GetMsgListResponse.messageContent> msgList = new GetMsgListResponse<GetMsgListResponse.messageContent>();
      msgList.setMessageTitle("九妹妹邀您共享以下设备");
      msgList.setCreateDate("2017-10-11 12:12:04");
      msgList.setIsRead("0");
      msgList.setMessageUniqueKey("3");
      msgList.setMessageContent(deviceInfo);
      response.add(msgList);


      GetMsgListResponse.messageContent deviceInfo1 = new GetMsgListResponse.messageContent<>();
      deviceInfo1.setDeviceName("九州量子网关3");
      deviceInfo1.setDeviceInfo(lock);
      deviceInfo1.setHandleType("0");
      deviceInfo1.setHistoryUniqueKey("4");
      deviceInfo1.setUserUniqueKey("4");
      GetMsgListResponse<GetMsgListResponse.messageContent> msgList1 = new GetMsgListResponse<GetMsgListResponse.messageContent>();
      msgList1.setMessageTitle("九妹妹邀您共享以下设备");
      msgList1.setCreateDate("2017-07-12 12:12:04");
      msgList1.setIsRead("0");
      msgList1.setMessageUniqueKey("4");
      msgList1.setMessageContent(deviceInfo1);
      response.add(msgList1);

      QtecResult<List<GetMsgListResponse<GetMsgListResponse.messageContent>>> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<List<GetMsgListResponse<GetMsgListResponse.messageContent>>>>() {
      }.getType();

      QtecResult<List<GetMsgListResponse<GetMsgListResponse.messageContent>>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  @Override
  public Observable<List<IntelDeviceListResponse>> getIntelDeviceList(final IRequest request) {
    return Observable.create(subscriber -> {
//      try {
//        Thread.sleep(3000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_INTEL_DEVICE_LIST, CloudUrlPath.METHOD_GET, 0, false);

      List<IntelDeviceListResponse> responseList = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
        IntelDeviceListResponse deviceListResponse = new IntelDeviceListResponse();
        deviceListResponse.setDeviceSerialNo(String.valueOf(i));
        deviceListResponse.setDeviceName("设备门锁" + i);
        if (i == 3) {
          deviceListResponse.setDeviceStatus("0");
        } else {
          deviceListResponse.setDeviceStatus("1");

        }
        responseList.add(deviceListResponse);
      }

      QtecResult<List<IntelDeviceListResponse>> result = new QtecResult<>();
      result.setData(responseList);

      Type type = new TypeToken<QtecResult<List<IntelDeviceListResponse>>>() {
      }.getType();

      QtecResult<List<IntelDeviceListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetExtraNetPortResponse> getExtraNetPort(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_EXTRACT_NET_PORT, CloudUrlPath.METHOD_POST, 0, false);

      GetExtraNetPortResponse response = new GetExtraNetPortResponse();
      response.setPort(10111);

      QtecResult<GetExtraNetPortResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetExtraNetPortResponse>>() {
      }.getType();

      QtecResult<GetExtraNetPortResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<IntelDeviceDetailResponse> getIntelDeviceDetail(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_INTEL_DEVICE_DETAIL, CloudUrlPath.METHOD_GET, 0, false);


      IntelDeviceDetailResponse detailResponse = new IntelDeviceDetailResponse();
      detailResponse.setDeviceSerialNo("23425141");
      detailResponse.setDeviceName("设备门锁");
      detailResponse.setDeviceStatus("使用中");

      QtecResult<IntelDeviceDetailResponse> result = new QtecResult<>();
      result.setData(detailResponse);

      Type type = new TypeToken<QtecResult<IntelDeviceDetailResponse>>() {
      }.getType();

      QtecResult<IntelDeviceDetailResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UnbindIntelDevResponse> unbindTrans(final IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          FakeCloudRepository.this.addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          UnbindIntelDevResponse unbindIntelDevResponse = new UnbindIntelDevResponse();
          QtecResult<UnbindIntelDevResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(unbindIntelDevResponse);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<UnbindIntelDevResponse>>() {
          }.getType();
          QtecResult<UnbindIntelDevResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<UnbindIntelDevResponse>, Observable<UnbindIntelDevResponse>>() {
          @Override
          public Observable<UnbindIntelDevResponse> call(final QtecResult<UnbindIntelDevResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<CommitAddRouterInfoResponse> commitAddRouterInfo(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_COMMIT_ADD_ROUTER_INFO, CloudUrlPath.METHOD_POST, 0, false);

      CommitAddRouterInfoResponse response = new CommitAddRouterInfoResponse();

      QtecResult<CommitAddRouterInfoResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<CommitAddRouterInfoResponse>>() {
      }.getType();

      QtecResult<CommitAddRouterInfoResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<IntelDevInfoModifyResponse> modifyIntelDev(final IRequest request) {
    return Observable.create(new Observable.OnSubscribe<IntelDevInfoModifyResponse>() {
      @Override
      public void call(Subscriber<? super IntelDevInfoModifyResponse> subscriber) {
        addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_DEV, CloudUrlPath.METHOD_POST, 0, false);


        IntelDevInfoModifyResponse response = new IntelDevInfoModifyResponse();

        QtecResult<IntelDevInfoModifyResponse> result = new QtecResult<>();
        result.setData(response);

        Type type = new TypeToken<QtecResult<IntelDevInfoModifyResponse>>() {
        }.getType();

        QtecResult<IntelDevInfoModifyResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
        subscriber.onNext(qtecResult.getData());
        subscriber.onCompleted();
      }
    });
  }

  @Override
  public Observable<RouterStatusResponse<List<Status>>> getRouterStatusTrans(final IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<RouterStatusResponse<List<Status>>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          RouterStatusResponse<List<Status>> routerStatusResponse = new RouterStatusResponse<>();
          routerStatusResponse.setRouterrx(1509);
          routerStatusResponse.setRoutertx(1203);
          routerStatusResponse.setHfssid("hsssid");
          routerStatusResponse.setLfssid("lfssid");
          routerStatusResponse.setOwnip("ownip");
          List<Status> statuses = new ArrayList<>();

          RouterStatusResponse.Status status0 = new RouterStatusResponse.Status();
          status0.setStaname("status" + 0);
          status0.setStastatus(1114674125);
          status0.setMacaddr("mac"+0);
          status0.setDevicetype(2);
          status0.setIpaddr("ip"+0);
          status0.setAccesstype("accessType"+0);
          status0.setTx(1);
          status0.setRx(2);
          statuses.add(status0);

          RouterStatusResponse.Status status1 = new RouterStatusResponse.Status();
          status1.setStaname("status" + 0);
          status1.setStastatus(114674125);
          status1.setMacaddr("mac"+0);
          status1.setDevicetype(2);
          status1.setIpaddr("ip"+0);
          status1.setAccesstype("accessType"+0);
          status1.setTx(1);
          status1.setRx(2);
          statuses.add(status1);

          for (int i = 0; i < 2; i++) {
            Status status = new Status();
            status.setStaname("status" + i);
            status.setStastatus(0);
            status.setMacaddr("mac" + i);
            status.setDevicetype(2);
            status.setIpaddr("ip" + i);
            status.setAccesstype("accessType" + i);
            status.setTx(1);
            status.setRx(2);
            statuses.add(status);
          }
          routerStatusResponse.setStalist(statuses);
          routerStatusResponse.setDevnum(statuses.size());

          routerEncryptInfo.setData(routerStatusResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//          qtecResult.setCode(100);
//          qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          } else {
            subscriber.onNext(qtecResult.getData());
          }

//          subscriber.onError(new IOException("dfdasdf"));
//          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RouterStatusResponse<List<Status>>>>() {
          }.getType();
          QtecResult<RouterStatusResponse<List<Status>>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<RouterStatusResponse<List<Status>>>, Observable<RouterStatusResponse<List<Status>>>>() {
          @Override
          public Observable<RouterStatusResponse<List<Status>>> call(QtecResult<RouterStatusResponse<List<Status>>> routerResult) {
            return Observable.create(subscriber -> {
//              routerResult.setCode(100);
//              routerResult.setMsg("err");
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              } else {
                subscriber.onNext(routerResult.getData());
              }
            });
          }
        });
  }

  @Override
  public Observable<AddIntelDevVerifyResponse> addIntelDevVerifyTrans(final IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo("{\"code\":0,\"msg\":null,\"data\":{\"encryptInfo\":\"{\\\"data\\\":{},\\\"msg\\\":\\\"ok\\\", \\\"code\\\":0}\"}}");

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        }).map(transmit -> {
          Type type = new TypeToken<QtecResult<AddIntelDevVerifyResponse>>() {
          }.getType();
          QtecResult<AddIntelDevVerifyResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        }).flatMap(new Func1<QtecResult<AddIntelDevVerifyResponse>, Observable<AddIntelDevVerifyResponse>>() {
          @Override
          public Observable<AddIntelDevVerifyResponse> call(QtecResult<AddIntelDevVerifyResponse> routerQtecResult) {
            return Observable.create(subscriber -> {
              if (routerQtecResult.getCode() != 0) {
                subscriber.onError(new IOException(routerQtecResult.getMsg()));
              }
              subscriber.onNext(routerQtecResult.getData());
//              subscriber.onCompleted();
            });
          }
        });

  }

  @Override
  public Observable<AddRouterVerifyResponse> addRouterVerifyTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);

          //封装网关层
          AddRouterVerifyResponse unbindIntelDevResponse = new AddRouterVerifyResponse();
          QtecResult<AddRouterVerifyResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(unbindIntelDevResponse);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddRouterVerifyResponse>>() {
          }.getType();
          QtecResult<AddRouterVerifyResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<AddRouterVerifyResponse>, Observable<AddRouterVerifyResponse>>() {
          @Override
          public Observable<AddRouterVerifyResponse> call(final QtecResult<AddRouterVerifyResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<List<GetUnlockInfoListResponse>> getLockNoteList(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_UNLOCK_INFO_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetUnlockInfoListResponse> mUnlockInfoList = new ArrayList<>();


      GetUnlockInfoListResponse response1 = new GetUnlockInfoListResponse();
      response1.setOccurTime("2017-07-12 12:12:04");
      response1.setRecordUniqueKey("1");
      response1.setMessageCode("1");
      response1.setUserHeadImage("1");
      response1.setUserNickName("九妹妹");
      mUnlockInfoList.add(response1);

      GetUnlockInfoListResponse response2 = new GetUnlockInfoListResponse();
      response2.setOccurTime("2017-07-11 08:12:04");
      response2.setRecordUniqueKey("1");
      response2.setMessageCode("1");
      response2.setUserHeadImage("1");
      response2.setUserNickName("九妹妹");
      mUnlockInfoList.add(response2);

      GetUnlockInfoListResponse response = new GetUnlockInfoListResponse();
      response.setOccurTime("2017-02-03 12:12:04");
      response.setRecordUniqueKey("1");
      response.setMessageCode("1");
      response.setUserHeadImage("1");
      response.setUserNickName("九妹妹");
      mUnlockInfoList.add(response);

      GetUnlockInfoListResponse response3 = new GetUnlockInfoListResponse();
      response3.setOccurTime("2016-07-11 15:12:04");
      response3.setRecordUniqueKey("1");
      response3.setMessageCode("1");
      response3.setUserHeadImage("1");
      response3.setUserNickName("九妹妹");
      mUnlockInfoList.add(response3);

   /*   for (int i = 0; i < 4; i++) {
        GetUnlockInfoListResponse response5 = new GetUnlockInfoListResponse();
        response5.setOccurTime("2016-06-11 15:12:04");
        response5.setOpenType("1");
        response5.setRecordUniqueKey("1");
        response5.setOperateCode("1");
        response5.setUserHeadImage("1");
        response5.setUserNickName("九妹妹");
        mUnlockInfoList.add(response5);
      }*/

      QtecResult<List<GetUnlockInfoListResponse>> result = new QtecResult<>();
      result.setData(mUnlockInfoList);
      Type type = new TypeToken<QtecResult<List<GetUnlockInfoListResponse>>>() {
      }.getType();

      QtecResult<List<GetUnlockInfoListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<List<GetUnlockInfoListResponse>> getLockExceptionList(IRequest request) {

    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_UNLOCK_INFO_LIST, CloudUrlPath.METHOD_GET, 0, false);
      List<GetUnlockInfoListResponse> mUnlockInfoList = new ArrayList<>();
      GetUnlockInfoListResponse response1 = new GetUnlockInfoListResponse();
      response1.setOccurTime("2017-07-12 12:12:04");
      response1.setRecordUniqueKey("1");
      response1.setMessageCode("1");
      response1.setUserHeadImage("1");
      response1.setUserNickName("九妹妹1");
      mUnlockInfoList.add(response1);

      GetUnlockInfoListResponse response2 = new GetUnlockInfoListResponse();
      response2.setOccurTime("2017-07-11 08:12:04");
      response2.setRecordUniqueKey("1");
      response2.setMessageCode("1");
      response2.setUserHeadImage("1");
      response2.setUserNickName("九妹妹1");
      mUnlockInfoList.add(response2);

      GetUnlockInfoListResponse response = new GetUnlockInfoListResponse();
      response.setOccurTime("2017-02-03 12:12:04");
      response.setRecordUniqueKey("1");
      response.setMessageCode("1");
      response.setUserHeadImage("1");
      response.setUserNickName("九妹妹1");
      mUnlockInfoList.add(response);

      GetUnlockInfoListResponse response3 = new GetUnlockInfoListResponse();
      response3.setOccurTime("2016-07-11 15:12:04");
      response3.setRecordUniqueKey("1");
      response3.setMessageCode("1");
      response3.setUserHeadImage("1");
      response3.setUserNickName("九妹妹1");
      mUnlockInfoList.add(response3);

   /*   for (int i = 0; i < 15; i++) {
        GetUnlockInfoListResponse response5 = new GetUnlockInfoListResponse();
        response5.setOccurTime("2016-06-11 15:12:04");
        response5.setOpenType("1");
        response5.setRecordUniqueKey("1");
        response5.setOperateCode("1");
        response5.setUserHeadImage("1");
        response5.setUserNickName("九妹妹1");
        mUnlockInfoList.add(response5);
      }*/
      QtecResult<List<GetUnlockInfoListResponse>> result = new QtecResult<>();
      result.setData(mUnlockInfoList);
      Type type = new TypeToken<QtecResult<List<GetUnlockInfoListResponse>>>() {
      }.getType();

      QtecResult<List<GetUnlockInfoListResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });


  }

  /**
   * 远程操作门锁
   *
   * @param
   * @return
   */
  @Override
  public Observable<RemoteLockOperationResponse> remoteLockOptTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);
          TransmitRequest transmitRequest = (TransmitRequest) ((QtecEncryptInfo) request).getData();
          QtecEncryptInfo encryptInfo = (QtecEncryptInfo) transmitRequest.getEncryptInfo();
          RemoteLockOperationRequest operationRequest = (RemoteLockOperationRequest) encryptInfo.getData();
          String cmdType = operationRequest.getCmdType();
          //封装网关层
          RemoteLockOperationResponse response = new RemoteLockOperationResponse();

          switch (cmdType) {
            case BleMapper.BLE_CMD_UNLOCK_REMOTE:
              response.setEncrypdata("EQACqg3//wAAAAAAAQCYk1tQzT5EAAEdbRoE4vFwBRQsAAAAAAAAAA==");
              break;

            case BleMapper.BLE_CMD_GET_LOCK_STATUS:
//              response.setEncrypdata("EQAGqhH/////AAAAAQAUAAEBAQFEAAEAAAAAAAAAAAAAAAAAAAAAAA==");
              response.setEncrypdata("EQAGqhH/////AAAAAQAUAAAAAABEAAEAAAAAAAAAAAAAAAAAAAAAAA==");
              break;
          }

          QtecResult<RemoteLockOperationResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
          //          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RemoteLockOperationResponse>>() {
          }.getType();
          QtecResult<RemoteLockOperationResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<RemoteLockOperationResponse>, Observable<RemoteLockOperationResponse>>() {
          @Override
          public Observable<RemoteLockOperationResponse> call(final QtecResult<RemoteLockOperationResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
              //              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetLockStatusResponse> getLockStatusTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetLockStatusResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetLockStatusResponse remoteLockStatusResponse = new GetLockStatusResponse();
          routerEncryptInfo.setData(remoteLockStatusResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetLockStatusResponse>>() {
          }.getType();
          QtecResult<GetLockStatusResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetLockStatusResponse>, Observable<GetLockStatusResponse>>() {
          @Override
          public Observable<GetLockStatusResponse> call(QtecResult<GetLockStatusResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatusTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetAntiFritNetStatusResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetAntiFritNetStatusResponse statusResponse = new GetAntiFritNetStatusResponse();
          routerEncryptInfo.setData(statusResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetAntiFritNetStatusResponse>>() {
          }.getType();
          QtecResult<GetAntiFritNetStatusResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetAntiFritNetStatusResponse>, Observable<GetAntiFritNetStatusResponse>>() {
          @Override
          public Observable<GetAntiFritNetStatusResponse> call(QtecResult<GetAntiFritNetStatusResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<EnableAntiFritNetResponse> enableAntiFritNetTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<EnableAntiFritNetResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          EnableAntiFritNetResponse response = new EnableAntiFritNetResponse();
          routerEncryptInfo.setData(response);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<EnableAntiFritNetResponse>>() {
          }.getType();
          QtecResult<EnableAntiFritNetResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<EnableAntiFritNetResponse>, Observable<EnableAntiFritNetResponse>>() {
          @Override
          public Observable<EnableAntiFritNetResponse> call(QtecResult<EnableAntiFritNetResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetAntiNetQuestionResponse> setAntiQuestionTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<SetAntiNetQuestionResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          SetAntiNetQuestionResponse response = new SetAntiNetQuestionResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetAntiNetQuestionResponse>>() {
          }.getType();
          QtecResult<SetAntiNetQuestionResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetAntiNetQuestionResponse>, Observable<SetAntiNetQuestionResponse>>() {
          @Override
          public Observable<SetAntiNetQuestionResponse> call(QtecResult<SetAntiNetQuestionResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthListTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<List<GetWaitingAuthDeviceListResponse>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          List<GetWaitingAuthDeviceListResponse> responses = new ArrayList<GetWaitingAuthDeviceListResponse>();

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<List<GetWaitingAuthDeviceListResponse>>>() {
          }.getType();
          QtecResult<List<GetWaitingAuthDeviceListResponse>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<List<GetWaitingAuthDeviceListResponse>>, Observable<List<GetWaitingAuthDeviceListResponse>>>() {
          @Override
          public Observable<List<GetWaitingAuthDeviceListResponse>> call(QtecResult<List<GetWaitingAuthDeviceListResponse>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<AllowAuthDeviceResponse> allowAuthDeviceTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<AllowAuthDeviceResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          AllowAuthDeviceResponse response = new AllowAuthDeviceResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AllowAuthDeviceResponse>>() {
          }.getType();
          QtecResult<AllowAuthDeviceResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<AllowAuthDeviceResponse>, Observable<AllowAuthDeviceResponse>>() {
          @Override
          public Observable<AllowAuthDeviceResponse> call(QtecResult<AllowAuthDeviceResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetAntiFritSwitchResponse> getAntiFritSwitchTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetAntiFritSwitchResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetAntiFritSwitchResponse response = new GetAntiFritSwitchResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetAntiFritSwitchResponse>>() {
          }.getType();
          QtecResult<GetAntiFritSwitchResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetAntiFritSwitchResponse>, Observable<GetAntiFritSwitchResponse>>() {
          @Override
          public Observable<GetAntiFritSwitchResponse> call(QtecResult<GetAntiFritSwitchResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<List<ChildCareListResponse>> getChildCareListTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<List<ChildCareListResponse>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          List<ChildCareListResponse> response = new ArrayList<ChildCareListResponse>();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<List<ChildCareListResponse>>>() {
          }.getType();
          QtecResult<List<ChildCareListResponse>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<List<ChildCareListResponse>>, Observable<List<ChildCareListResponse>>>() {
          @Override
          public Observable<List<ChildCareListResponse>> call(QtecResult<List<ChildCareListResponse>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<PostChildCareDetailResponse> postChildCareDetailTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<PostChildCareDetailResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          PostChildCareDetailResponse response = new PostChildCareDetailResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostChildCareDetailResponse>>() {
          }.getType();
          QtecResult<PostChildCareDetailResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<PostChildCareDetailResponse>, Observable<PostChildCareDetailResponse>>() {
          @Override
          public Observable<PostChildCareDetailResponse> call(QtecResult<PostChildCareDetailResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetSignalRegulationResponse> getSignalModeTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetSignalRegulationResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetSignalRegulationResponse response = new GetSignalRegulationResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSignalRegulationResponse>>() {
          }.getType();
          QtecResult<GetSignalRegulationResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetSignalRegulationResponse>, Observable<GetSignalRegulationResponse>>() {
          @Override
          public Observable<GetSignalRegulationResponse> call(QtecResult<GetSignalRegulationResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<OpenBandSpeedResponse> openBandSpeedMeasureTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<OpenBandSpeedResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          OpenBandSpeedResponse response = new OpenBandSpeedResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<OpenBandSpeedResponse>>() {
          }.getType();
          QtecResult<OpenBandSpeedResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<OpenBandSpeedResponse>, Observable<OpenBandSpeedResponse>>() {
          @Override
          public Observable<OpenBandSpeedResponse> call(QtecResult<OpenBandSpeedResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetGuestWifiSwitchResponse> getGuestSwitchTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetGuestWifiSwitchResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetGuestWifiSwitchResponse response = new GetGuestWifiSwitchResponse();
          response.setEnable(1);
          response.setIsHide(1);
          response.setUserNum(3);
          response.setName("九州访客");

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetGuestWifiSwitchResponse>>() {
          }.getType();
          QtecResult<GetGuestWifiSwitchResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetGuestWifiSwitchResponse>, Observable<GetGuestWifiSwitchResponse>>() {
          @Override
          public Observable<GetGuestWifiSwitchResponse> call(QtecResult<GetGuestWifiSwitchResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetGuestWifiSwitchResponse> setGuestSwitchTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<SetGuestWifiSwitchResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          SetGuestWifiSwitchResponse response = new SetGuestWifiSwitchResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetGuestWifiSwitchResponse>>() {
          }.getType();
          QtecResult<SetGuestWifiSwitchResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetGuestWifiSwitchResponse>, Observable<SetGuestWifiSwitchResponse>>() {
          @Override
          public Observable<SetGuestWifiSwitchResponse> call(QtecResult<SetGuestWifiSwitchResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetWifiAllSwitchResponse> setWifiAllSwitchTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<SetWifiAllSwitchResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          SetWifiAllSwitchResponse response = new SetWifiAllSwitchResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetWifiAllSwitchResponse>>() {
          }.getType();
          QtecResult<SetWifiAllSwitchResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetWifiAllSwitchResponse>, Observable<SetWifiAllSwitchResponse>>() {
          @Override
          public Observable<SetWifiAllSwitchResponse> call(QtecResult<SetWifiAllSwitchResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<DeleteWifiSwitchResponse> deleteWifiSwitchTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<DeleteWifiSwitchResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          DeleteWifiSwitchResponse response = new DeleteWifiSwitchResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<DeleteWifiSwitchResponse>>() {
          }.getType();
          QtecResult<DeleteWifiSwitchResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<DeleteWifiSwitchResponse>, Observable<DeleteWifiSwitchResponse>>() {
          @Override
          public Observable<DeleteWifiSwitchResponse> call(QtecResult<DeleteWifiSwitchResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetWifiDataResponse> setWiFiDataTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<SetWifiDataResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          SetWifiDataResponse response = new SetWifiDataResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetWifiDataResponse>>() {
          }.getType();
          QtecResult<SetWifiDataResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetWifiDataResponse>, Observable<SetWifiDataResponse>>() {
          @Override
          public Observable<SetWifiDataResponse> call(QtecResult<SetWifiDataResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<AddVpnResponse> addVpnTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<AddVpnResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          AddVpnResponse response = new AddVpnResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddVpnResponse>>() {
          }.getType();
          QtecResult<AddVpnResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<AddVpnResponse>, Observable<AddVpnResponse>>() {
          @Override
          public Observable<AddVpnResponse> call(QtecResult<AddVpnResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<DeleteVpnResponse> deleteVpnTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<DeleteVpnResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          DeleteVpnResponse response = new DeleteVpnResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<DeleteVpnResponse>>() {
          }.getType();
          QtecResult<DeleteVpnResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<DeleteVpnResponse>, Observable<DeleteVpnResponse>>() {
          @Override
          public Observable<DeleteVpnResponse> call(QtecResult<DeleteVpnResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<ModifyVpnResponse> modifyVpnTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<ModifyVpnResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          ModifyVpnResponse response = new ModifyVpnResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<ModifyVpnResponse>>() {
          }.getType();
          QtecResult<ModifyVpnResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<ModifyVpnResponse>, Observable<ModifyVpnResponse>>() {
          @Override
          public Observable<ModifyVpnResponse> call(QtecResult<ModifyVpnResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetVpnResponse> setVpnTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<SetVpnResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          SetVpnResponse response = new SetVpnResponse();

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetVpnResponse>>() {
          }.getType();
          QtecResult<SetVpnResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetVpnResponse>, Observable<SetVpnResponse>>() {
          @Override
          public Observable<SetVpnResponse> call(QtecResult<SetVpnResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnListTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");


          GetVpnListResponse<List<GetVpnListResponse.VpnBean>> responses = new GetVpnListResponse<List<GetVpnListResponse.VpnBean>>();
          responses.setEnable(1);
          List<GetVpnListResponse.VpnBean> response = new ArrayList<>();
          for (int i = 0; i < 5; i++) {
            GetVpnListResponse.VpnBean bean = new GetVpnListResponse.VpnBean();
            bean.setServer_ip("ip" + i);
            bean.setUsername("name" + i);
            bean.setMode("" + i);
            bean.setDescription("description" + i);
            bean.setIfname("key" + i);
            response.add(bean);
          }

          responses.setVpn_list(response);

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>>() {
          }.getType();
          QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>, Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>>>() {
          @Override
          public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> call(QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelessListTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");


          GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>> responses = new GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>();
          List<GetWirelessListResponse.WirelessBean> response = new ArrayList<>();
          for (int i = 0; i < 5; i++) {
            GetWirelessListResponse.WirelessBean bean = new GetWirelessListResponse.WirelessBean();
            bean.setMode(1);
            bean.setPower(2);
            bean.setSsid("ssid" + i);
            bean.setStatus(1);
            response.add(bean);
          }

          responses.setWifi(response);

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>>() {
          }.getType();
          QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>, Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>>() {
          @Override
          public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> call(QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetConnectedWifiResponse> getConnectedWifiTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetConnectedWifiResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          GetConnectedWifiResponse responses = new GetConnectedWifiResponse();
          responses.setAuto_switch(1);
          responses.setStatus(1);
          responses.setSsid("九州量子网络wifi");
          responses.setEnable(1);

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetConnectedWifiResponse>>() {
          }.getType();
          QtecResult<GetConnectedWifiResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetConnectedWifiResponse>, Observable<GetConnectedWifiResponse>>() {
          @Override
          public Observable<GetConnectedWifiResponse> call(QtecResult<GetConnectedWifiResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetWifiDetailResponse> getWifiDetailTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetWifiDetailResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          GetWifiDetailResponse responses = new GetWifiDetailResponse();
          responses.setDns("dns");
          responses.setIpaddr("ip");
          responses.setGateway("gateway");
          responses.setNetmask("netmask");

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWifiDetailResponse>>() {
          }.getType();
          QtecResult<GetWifiDetailResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetWifiDetailResponse>, Observable<GetWifiDetailResponse>>() {
          @Override
          public Observable<GetWifiDetailResponse> call(QtecResult<GetWifiDetailResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<PostConnectWirelessResponse> connectWirelessTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<PostConnectWirelessResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          PostConnectWirelessResponse responses = new PostConnectWirelessResponse();

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostConnectWirelessResponse>>() {
          }.getType();
          QtecResult<PostConnectWirelessResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<PostConnectWirelessResponse>, Observable<PostConnectWirelessResponse>>() {
          @Override
          public Observable<PostConnectWirelessResponse> call(QtecResult<PostConnectWirelessResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetWifiSwitchResponse> setWifiSwitchTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<SetWifiSwitchResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          SetWifiSwitchResponse responses = new SetWifiSwitchResponse();

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetWifiSwitchResponse>>() {
          }.getType();
          QtecResult<SetWifiSwitchResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetWifiSwitchResponse>, Observable<SetWifiSwitchResponse>>() {
          @Override
          public Observable<SetWifiSwitchResponse> call(QtecResult<SetWifiSwitchResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<PostInspectionResponse> safeInspectionTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<PostInspectionResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          PostInspectionResponse responses = new PostInspectionResponse();
          responses.setDdos(1);
          responses.setDmz(0);
          responses.setFtp(1);
          responses.setSamba(0);
          responses.setVirtualservice(1);
          responses.setDmz(1);

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostInspectionResponse>>() {
          }.getType();
          QtecResult<PostInspectionResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<PostInspectionResponse>, Observable<PostInspectionResponse>>() {
          @Override
          public Observable<PostInspectionResponse> call(QtecResult<PostInspectionResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<List<GetBlackListResponse>> getBlackListTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<List<GetBlackListResponse>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          List<GetBlackListResponse> responses = new ArrayList<>();
          for (int i = 0; i < 5; i++) {
            GetBlackListResponse bean = new GetBlackListResponse();
            bean.setEnabled(1);
            bean.setMacaddr("mac" + i);

            responses.add(bean);
          }

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<List<GetBlackListResponse>>>() {
          }.getType();
          QtecResult<List<GetBlackListResponse>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<List<GetBlackListResponse>>, Observable<List<GetBlackListResponse>>>() {
          @Override
          public Observable<List<GetBlackListResponse>> call(QtecResult<List<GetBlackListResponse>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<RemoveBlackMemResponse> removeBlackMemTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<RemoveBlackMemResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          RemoveBlackMemResponse responses = new RemoveBlackMemResponse();

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RemoveBlackMemResponse>>() {
          }.getType();
          QtecResult<RemoveBlackMemResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<RemoveBlackMemResponse>, Observable<RemoveBlackMemResponse>>() {
          @Override
          public Observable<RemoveBlackMemResponse> call(QtecResult<RemoveBlackMemResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetAntiFritQuestionResponse> getAntiFritQuestionTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetAntiFritQuestionResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          GetAntiFritQuestionResponse responses = new GetAntiFritQuestionResponse();
        /*  responses.setAnswer("answer");
          responses.setQuestion("question");*/

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetAntiFritQuestionResponse>>() {
          }.getType();
          QtecResult<GetAntiFritQuestionResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetAntiFritQuestionResponse>, Observable<GetAntiFritQuestionResponse>>() {
          @Override
          public Observable<GetAntiFritQuestionResponse> call(QtecResult<GetAntiFritQuestionResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<PostSpecialAttentionResponse> postSpecialAttentionTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<PostSpecialAttentionResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          PostSpecialAttentionResponse responses = new PostSpecialAttentionResponse();

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostSpecialAttentionResponse>>() {
          }.getType();
          QtecResult<PostSpecialAttentionResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<PostSpecialAttentionResponse>, Observable<PostSpecialAttentionResponse>>() {
          @Override
          public Observable<PostSpecialAttentionResponse> call(QtecResult<PostSpecialAttentionResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetSpecialAttentionResponse> getSpecialAttentionTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetSpecialAttentionResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          GetSpecialAttentionResponse responses = new GetSpecialAttentionResponse();
          responses.setSpecialcare("4c:cc:6a:e3:5a:d5|3 4c:cc:6a:e3:5a:d4|2");

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSpecialAttentionResponse>>() {
          }.getType();
          QtecResult<GetSpecialAttentionResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetSpecialAttentionResponse>, Observable<GetSpecialAttentionResponse>>() {
          @Override
          public Observable<GetSpecialAttentionResponse> call(QtecResult<GetSpecialAttentionResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetQosInfoResponse> getQosInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetQosInfoResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          GetQosInfoResponse responses = new GetQosInfoResponse();
          responses.setDownload(100);
          responses.setUpload(1212);
          responses.setEnabled(1);
          responses.setQosmode(1);

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetQosInfoResponse>>() {
          }.getType();
          QtecResult<GetQosInfoResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetQosInfoResponse>, Observable<GetQosInfoResponse>>() {
          @Override
          public Observable<GetQosInfoResponse> call(QtecResult<GetQosInfoResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<PostQosInfoResponse> postQosInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<PostQosInfoResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");

          PostQosInfoResponse responses = new PostQosInfoResponse();

          routerEncryptInfo.setData(responses);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostQosInfoResponse>>() {
          }.getType();
          QtecResult<PostQosInfoResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<PostQosInfoResponse>, Observable<PostQosInfoResponse>>() {
          @Override
          public Observable<PostQosInfoResponse> call(QtecResult<PostQosInfoResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfigTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>> response = new GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>();
          response.setEnable(1);
          List<GetWifiTimeConfigResponse.WifiTimeConfig> beans = new ArrayList<GetWifiTimeConfigResponse.WifiTimeConfig>();
          for (int i = 0; i < 5; i++) {
            GetWifiTimeConfigResponse.WifiTimeConfig bean = new GetWifiTimeConfigResponse.WifiTimeConfig();
            bean.setName("时段" + i);
            bean.setId(i);
            bean.setRule_enable(1);
            bean.setStart_hour(11);
            bean.setStart_min(30);
            bean.setStop_hour(19);
            bean.setStop_min(12);
            bean.setWeek_day("0 2 4");
            beans.add(bean);
          }
          response.setRules(beans);

          routerEncryptInfo.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>>() {
          }.getType();
          QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>, Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>>() {
          @Override
          public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> call(QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<AddFingerResponse> addFingerInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<AddFingerResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          AddFingerResponse addFingerInfoResponse = new AddFingerResponse();
          routerEncryptInfo.setData(addFingerInfoResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<AddFingerResponse>>() {
          }.getType();
          QtecResult<AddFingerResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<AddFingerResponse>, Observable<AddFingerResponse>>() {
          @Override
          public Observable<AddFingerResponse> call(QtecResult<AddFingerResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<DeleteFingerResponse> deleteFingerInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<DeleteFingerResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          DeleteFingerResponse deleteFingerInfoResponse = new DeleteFingerResponse();
          routerEncryptInfo.setData(deleteFingerInfoResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<DeleteFingerResponse>>() {
          }.getType();
          QtecResult<DeleteFingerResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<DeleteFingerResponse>, Observable<DeleteFingerResponse>>() {
          @Override
          public Observable<DeleteFingerResponse> call(QtecResult<DeleteFingerResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>> queryFingerInfoResponses = new QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>();
          //List<QueryFingerInfoResponse> queryFingerInfoResponses = new ArrayList<QueryFingerInfoResponse>();
          List<QueryFingerInfoResponse.FingerInfo> list = new ArrayList<QueryFingerInfoResponse.FingerInfo>();
          for (int i = 0; i < 3; i++) {
            QueryFingerInfoResponse.FingerInfo response = new QueryFingerInfoResponse.FingerInfo();

            response.setFpid("" + i);
            response.setFpname("手指" + i);
            list.add(response);
          }
          queryFingerInfoResponses.setFingerprintlist(list);

          routerEncryptInfo.setData(queryFingerInfoResponses);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>>() {
          }.getType();
          QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>, Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>>() {
          @Override
          public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> call(QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<ModifyFingerNameResponse> modifyFingerNameTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<ModifyFingerNameResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          ModifyFingerNameResponse modifyFingerNameResponse = new ModifyFingerNameResponse();
          routerEncryptInfo.setData(modifyFingerNameResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<ModifyFingerNameResponse>>() {
          }.getType();
          QtecResult<ModifyFingerNameResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<ModifyFingerNameResponse>, Observable<ModifyFingerNameResponse>>() {
          @Override
          public Observable<ModifyFingerNameResponse> call(QtecResult<ModifyFingerNameResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetSambaAccountResponse> getSambaAccountTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetSambaAccountResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetSambaAccountResponse response = new GetSambaAccountResponse();
          routerEncryptInfo.setData(response);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSambaAccountResponse>>() {
          }.getType();
          QtecResult<GetSambaAccountResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetSambaAccountResponse>, Observable<GetSambaAccountResponse>>() {
          @Override
          public Observable<GetSambaAccountResponse> call(QtecResult<GetSambaAccountResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

 /* @Override
  public Observable<GetSambaAccountResponse> getSambaAccountTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetSambaAccountResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetSambaAccountResponse getSambaAccountResponse = new GetSambaAccountResponse();
          getSambaAccountResponse.setPassword("123456");
          getSambaAccountResponse.setUsername("network");
          routerEncryptInfo.setData(getSambaAccountResponse);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetSambaAccountResponse>>() {
          }.getType();
          QtecResult<GetSambaAccountResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetSambaAccountResponse>, Observable<GetSambaAccountResponse>>() {
          @Override
          public Observable<GetSambaAccountResponse> call(QtecResult<GetSambaAccountResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }*/

  @Override
  public Observable<PostSignalRegulationResponse> setSignalRegulationInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<PostSignalRegulationResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          PostSignalRegulationResponse response = new PostSignalRegulationResponse();
          routerEncryptInfo.setData(response);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<PostSignalRegulationResponse>>() {
          }.getType();
          QtecResult<PostSignalRegulationResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<PostSignalRegulationResponse>, Observable<PostSignalRegulationResponse>>() {
          @Override
          public Observable<PostSignalRegulationResponse> call(QtecResult<PostSignalRegulationResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetBandSpeedResponse> getBandSpeedTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {

          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关数据包
          QtecResult<GetBandSpeedResponse> routerEncryptInfo = new QtecResult<>();
          routerEncryptInfo.setCode(0);
          routerEncryptInfo.setMsg("ok");
          GetBandSpeedResponse response = new GetBandSpeedResponse();
          routerEncryptInfo.setData(response);

          //封装Transmit层
          TransmitResponse<String> transmitResponse = new TransmitResponse<>();
          transmitResponse.setEncryptInfo(mJsonMapper.toJson(routerEncryptInfo));

          //封装cloud层
          QtecResult<TransmitResponse> result = new QtecResult<>();
          result.setData(transmitResponse);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);
//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }

//          subscriber.onError(new IOException());
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetBandSpeedResponse>>() {
          }.getType();
          QtecResult<GetBandSpeedResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetBandSpeedResponse>, Observable<GetBandSpeedResponse>>() {
          @Override
          public Observable<GetBandSpeedResponse> call(QtecResult<GetBandSpeedResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SearchIntelDevNotifyResponse> searchIntelDevNotifyTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          SearchIntelDevNotifyResponse searchIntelDevNotifyResponse = new SearchIntelDevNotifyResponse();
          QtecResult<SearchIntelDevNotifyResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(searchIntelDevNotifyResponse);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SearchIntelDevNotifyResponse>>() {
          }.getType();
          QtecResult<SearchIntelDevNotifyResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SearchIntelDevNotifyResponse>, Observable<SearchIntelDevNotifyResponse>>() {
          @Override
          public Observable<SearchIntelDevNotifyResponse> call(final QtecResult<SearchIntelDevNotifyResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> getStsToken(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_STS_TOKEN, CloudUrlPath.METHOD_GET, 0, false);

      GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean> response = new GetStsTokenResponse<>();
      response.setRequestId("BD18BFBA-88EB-48A2-8603-6CCE35E8A2B8");

      CredentialsBean credentialsBean = new CredentialsBean();
      credentialsBean.setAccessKeyId("STS.KZiMX3JyGbNE3p4aFXiDb6ZDD");
      credentialsBean.setExpiration("2017-07-17T06:25:16Z");
      credentialsBean.setSecurityToken("CAIS9gJ1q6Ft5B2yfSjIqpncBuKHp6Zm1YyuMVaFhUYNZ");
      credentialsBean.setAccessKeySecret("5rsThNtbDKYtFYNbghYPevza9GcHP1CYxCyVyKobTtSp");
      response.setCredentials(credentialsBean);

      AssumedRoleUserBean assumedRoleUserBean = new AssumedRoleUserBean();
      assumedRoleUserBean.setArn("acs:ram::1488296633579781:role/oss-token/oss-token");
      assumedRoleUserBean.setAssumedRoleId("395881770167581823:oss-token");
      response.setAssumedRoleUser(assumedRoleUserBean);


      QtecResult<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>>>() {
      }.getType();

      QtecResult<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetRouterInfoResponse> getRouterInfoTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);

          //封装网关层
          GetRouterInfoResponse response = new GetRouterInfoResponse();
          response.setLfwifissid("2.4G量子WIFI");
          response.setHfwifissid("5G量子WIFI");
          response.setLanipaddress("192.168.1.100");
          response.setWanipaddress("10.10.10.78");
          response.setCputype("MT7628AN_1");
          response.setCpubrand("MediaTek");
          response.setCpufactory("MIPS_1");

          QtecResult<GetRouterInfoResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetRouterInfoResponse>>() {
          }.getType();
          QtecResult<GetRouterInfoResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetRouterInfoResponse>, Observable<GetRouterInfoResponse>>() {
          @Override
          public Observable<GetRouterInfoResponse> call(final QtecResult<GetRouterInfoResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetRouterInfoCloudResponse> getRouterInfoCloud(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_ROUTER_INFO_CLOUD, CloudUrlPath.METHOD_GET, 0, false);

      GetRouterInfoCloudResponse response = new GetRouterInfoCloudResponse();
      response.setRouterNickName("我的量子网关");
      response.setGroupName("家庭");
      response.setDescription("我的量子网关——家用");

      QtecResult<GetRouterInfoCloudResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetRouterInfoCloudResponse>>() {
      }.getType();

      QtecResult<GetRouterInfoCloudResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyRouterDescResponse> modifyRouterDesc(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_ROUTER_DESC, CloudUrlPath.METHOD_POST, 0, false);

      ModifyRouterDescResponse response = new ModifyRouterDescResponse();

      QtecResult<ModifyRouterDescResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyRouterDescResponse>>() {
      }.getType();

      QtecResult<ModifyRouterDescResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyDevNameResponse> modifyDevName(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_DEV_NAME, CloudUrlPath.METHOD_POST, 0, false);

      ModifyDevNameResponse response = new ModifyDevNameResponse();

      QtecResult<ModifyDevNameResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyDevNameResponse>>() {
      }.getType();

      QtecResult<ModifyDevNameResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<List<GetRouterGroupsResponse>> getRouterGroups(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_ROUTER_GROUPS, CloudUrlPath.METHOD_GET, 0, false);

      List<GetRouterGroupsResponse> groupsResponses = new ArrayList<>();
      GetRouterGroupsResponse group = new GetRouterGroupsResponse();
      group.setGroupId("1");
      group.setGroupName("家庭");
      groupsResponses.add(group);

      group = new GetRouterGroupsResponse();
      group.setGroupId("2");
      group.setGroupName("公司");
      groupsResponses.add(group);

      group = new GetRouterGroupsResponse();
      group.setGroupId("3");
      group.setGroupName("公共场合");
      groupsResponses.add(group);


      QtecResult<List<GetRouterGroupsResponse>> result = new QtecResult<>();
      result.setData(groupsResponses);
      Type type = new TypeToken<QtecResult<List<GetRouterGroupsResponse>>>() {
      }.getType();

      QtecResult<List<GetRouterGroupsResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyRouterGroupResponse> modifyRouterGroup(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_ROUTER_GROUP, CloudUrlPath.METHOD_POST, 0, false);

      ModifyRouterGroupResponse response = new ModifyRouterGroupResponse();

      QtecResult<ModifyRouterGroupResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyRouterGroupResponse>>() {
      }.getType();

      QtecResult<ModifyRouterGroupResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<CreateRouterGroupResponse> createRouterGroup(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_CREATE_ROUTER_GROUP, CloudUrlPath.METHOD_POST, 0, false);

      CreateRouterGroupResponse response = new CreateRouterGroupResponse();

      QtecResult<CreateRouterGroupResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<CreateRouterGroupResponse>>() {
      }.getType();

      QtecResult<CreateRouterGroupResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetDHCPResponse> setDHCPTrans(final IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          FakeCloudRepository.this.addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          SetDHCPResponse SetDHCPResponse = new SetDHCPResponse();
          QtecResult<SetDHCPResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(SetDHCPResponse);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetDHCPResponse>>() {
          }.getType();
          QtecResult<SetDHCPResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetDHCPResponse>, Observable<SetDHCPResponse>>() {
          @Override
          public Observable<SetDHCPResponse> call(final QtecResult<SetDHCPResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetPPPOEResponse> setPPPOETrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          FakeCloudRepository.this.addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          SetPPPOEResponse SetPPPOEResponse = new SetPPPOEResponse();
          QtecResult<SetPPPOEResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(SetPPPOEResponse);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetPPPOEResponse>>() {
          }.getType();
          QtecResult<SetPPPOEResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetPPPOEResponse>, Observable<SetPPPOEResponse>>() {
          @Override
          public Observable<SetPPPOEResponse> call(final QtecResult<SetPPPOEResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetStaticIPResponse> setStaticIPTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          FakeCloudRepository.this.addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          SetStaticIPResponse SetStaticIPResponse = new SetStaticIPResponse();
          QtecResult<SetStaticIPResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(SetStaticIPResponse);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetStaticIPResponse>>() {
          }.getType();
          QtecResult<SetStaticIPResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetStaticIPResponse>, Observable<SetStaticIPResponse>>() {
          @Override
          public Observable<SetStaticIPResponse> call(final QtecResult<SetStaticIPResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetNetModeResponse> getNetModeTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          GetNetModeResponse response = new GetNetModeResponse();
          QtecResult<GetNetModeResponse> routerQtecResult = new QtecResult<>();
          response.setConnectiontype(ModelConstant.NET_MODE_STATIC);
          response.setIpaddr("1");
          response.setNetmask("2");
          response.setGateway("3");
          response.setDns("4 5");
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetNetModeResponse>>() {
          }.getType();
          QtecResult<GetNetModeResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetNetModeResponse>, Observable<GetNetModeResponse>>() {
          @Override
          public Observable<GetNetModeResponse> call(final QtecResult<GetNetModeResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<RestartRouterResponse> restartRouterTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          RestartRouterResponse response = new RestartRouterResponse();
          QtecResult<RestartRouterResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<RestartRouterResponse>>() {
          }.getType();
          QtecResult<RestartRouterResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<RestartRouterResponse>, Observable<RestartRouterResponse>>() {
          @Override
          public Observable<RestartRouterResponse> call(final QtecResult<RestartRouterResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<FactoryResetResponse> factoryResetTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          FactoryResetResponse response = new FactoryResetResponse();
          QtecResult<FactoryResetResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<FactoryResetResponse>>() {
          }.getType();
          QtecResult<FactoryResetResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<FactoryResetResponse>, Observable<FactoryResetResponse>>() {
          @Override
          public Observable<FactoryResetResponse> call(final QtecResult<FactoryResetResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<UnbindRouterResponse> unbindRouter(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_UNBIND_ROUTER, CloudUrlPath.METHOD_POST, 0, false);

      UnbindRouterResponse response = new UnbindRouterResponse();

      QtecResult<UnbindRouterResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<UnbindRouterResponse>>() {
      }.getType();

      QtecResult<UnbindRouterResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UpdateFirmwareResponse> updateFirmwareTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          UpdateFirmwareResponse response = new UpdateFirmwareResponse();
          QtecResult<UpdateFirmwareResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<UpdateFirmwareResponse>>() {
          }.getType();
          QtecResult<UpdateFirmwareResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<UpdateFirmwareResponse>, Observable<UpdateFirmwareResponse>>() {
          @Override
          public Observable<UpdateFirmwareResponse> call(final QtecResult<UpdateFirmwareResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetWifiConfigResponse> getWifiConfigTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          GetWifiConfigResponse response = new GetWifiConfigResponse();
          response.setLfssid("2.4G_wifi");
          response.setLfdisabled("1");
          response.setLfhiden("1");

          response.setHfssid("5G_wifi");
          response.setHfdisabled("1");
          response.setHfhiden("0");
          QtecResult<GetWifiConfigResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetWifiConfigResponse>>() {
          }.getType();
          QtecResult<GetWifiConfigResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetWifiConfigResponse>, Observable<GetWifiConfigResponse>>() {
          @Override
          public Observable<GetWifiConfigResponse> call(final QtecResult<GetWifiConfigResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<ConfigWifiResponse> configWifiTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          ConfigWifiResponse response = new ConfigWifiResponse();

          QtecResult<ConfigWifiResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<ConfigWifiResponse>>() {
          }.getType();
          QtecResult<ConfigWifiResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<ConfigWifiResponse>, Observable<ConfigWifiResponse>>() {
          @Override
          public Observable<ConfigWifiResponse> call(final QtecResult<ConfigWifiResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetTimerTaskResponse> getTimerTaskTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          GetTimerTaskResponse response = new GetTimerTaskResponse();
          response.setEnable(1);
          response.setHour(10);
          response.setMinute(30);
          response.setDay("0,1,2,4,6");
          QtecResult<GetTimerTaskResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetTimerTaskResponse>>() {
          }.getType();
          QtecResult<GetTimerTaskResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetTimerTaskResponse>, Observable<GetTimerTaskResponse>>() {
          @Override
          public Observable<GetTimerTaskResponse> call(final QtecResult<GetTimerTaskResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetRouterTimerResponse> setRouterTimerTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          SetRouterTimerResponse response = new SetRouterTimerResponse();
          QtecResult<SetRouterTimerResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetRouterTimerResponse>>() {
          }.getType();
          QtecResult<SetRouterTimerResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetRouterTimerResponse>, Observable<SetRouterTimerResponse>>() {
          @Override
          public Observable<SetRouterTimerResponse> call(final QtecResult<SetRouterTimerResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<CheckAdminPwdResponse> checkAdminPwdTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          CheckAdminPwdResponse response = new CheckAdminPwdResponse();
          QtecResult<CheckAdminPwdResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<CheckAdminPwdResponse>>() {
          }.getType();
          QtecResult<CheckAdminPwdResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<CheckAdminPwdResponse>, Observable<CheckAdminPwdResponse>>() {
          @Override
          public Observable<CheckAdminPwdResponse> call(final QtecResult<CheckAdminPwdResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<SetAdminPwdResponse> setAdminPwdTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          SetAdminPwdResponse response = new SetAdminPwdResponse();
          QtecResult<SetAdminPwdResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<SetAdminPwdResponse>>() {
          }.getType();
          QtecResult<SetAdminPwdResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<SetAdminPwdResponse>, Observable<SetAdminPwdResponse>>() {
          @Override
          public Observable<SetAdminPwdResponse> call(final QtecResult<SetAdminPwdResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<List<GetFingerprintsResponse>> getFingerprints(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_FINGERPRINTS, CloudUrlPath.METHOD_GET, 0, false);

      List<GetFingerprintsResponse> fingerprints = new ArrayList<>();

      for (int i = 0; i < 5; i++) {
        GetFingerprintsResponse fingerprint = new GetFingerprintsResponse();
        fingerprint.setDeviceSerialNo("lock_serial" + i);
        fingerprint.setFingerprintName("指纹" + i);
        fingerprint.setFingerprintSerialNo("fingerprint_" + i);
        fingerprint.setFingerprintUniqueKey("adsfasdfasdfasdf" + i);
        fingerprints.add(fingerprint);
      }

      QtecResult<List<GetFingerprintsResponse>> result = new QtecResult<>();
      result.setData(fingerprints);
      Type type = new TypeToken<QtecResult<List<GetFingerprintsResponse>>>() {
      }.getType();

      QtecResult<List<GetFingerprintsResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetKeyResponse<List<KeyBean>>> getKeyTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          GetKeyResponse<List<KeyBean>> response = new GetKeyResponse<>();

          response.setKeynumber(150);
          response.setDevicename("devicename");
          List<KeyBean> keyBeanList = new ArrayList<>();
          BASE64Encoder base64Encoder = new BASE64Encoder();
          for (int i = 0; i < 150; i++) {
            KeyBean keyBean = new KeyBean();
            keyBean.setKeyid("supply100000001000_" + i);
            String key = null;
            if (i < 10) {
              key = "83435f05a719c14839c649402ddd539" + i;
            } else if (i >= 10 && i < 100) {
              key = "83435f05a719c14839c649402ddd53" + i;
            } else if (i >= 100 && i < 1000) {
              key = "83435f05a719c14839c649402ddd5" + i;
            } else if (i >= 1000 && i < 3000) {
              key = "83435f05a719c14839c649402ddd" + i;
            }
            keyBean.setKey(base64Encoder.encode(ConvertUtils.hexString2Bytes(key)));
            keyBeanList.add(keyBean);
          }

          response.setKeylist(keyBeanList);

          QtecResult<GetKeyResponse<List<KeyBean>>> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

//                        qtecResult.setCode(100);
//                        qtecResult.setMsg("err cloud");
          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetKeyResponse<List<KeyBean>>>>() {
          }.getType();
          QtecResult<GetKeyResponse<List<KeyBean>>> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetKeyResponse<List<KeyBean>>>, Observable<GetKeyResponse<List<KeyBean>>>>() {
          @Override
          public Observable<GetKeyResponse<List<KeyBean>>> call(final QtecResult<GetKeyResponse<List<KeyBean>>> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<List<GetPasswordsResponse>> getPasswords(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_GET_PASSWORDS, CloudUrlPath.METHOD_GET, 0, false);

      List<GetPasswordsResponse> passwords = new ArrayList<>();

      for (int i = 0; i < 5; i++) {
        GetPasswordsResponse password = new GetPasswordsResponse();
        password.setDeviceSerialNo("lock_serial" + i);
        password.setPasswordName("密码_" + i);
        password.setPasswordSerialNo("password_" + i);
        password.setPasswordUniqueKey("adsfasdfasdfadfasdfasdf" + i);
        passwords.add(password);
      }

      QtecResult<List<GetPasswordsResponse>> result = new QtecResult<>();
      result.setData(passwords);
      Type type = new TypeToken<QtecResult<List<GetPasswordsResponse>>>() {
      }.getType();

      QtecResult<List<GetPasswordsResponse>> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  /**
   * 处理邀请
   */
  @Override
  public Observable<DealInvitationResponse> dealInvitation(IRequest request) {

    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.DEAL_INVITATION, CloudUrlPath.METHOD_POST, 0, false);
      DealInvitationResponse response = new DealInvitationResponse();

      QtecResult<DealInvitationResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<DealInvitationResponse>>() {
      }.getType();

      QtecResult<DealInvitationResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  /**
   * 消息删除
   */
  @Override
  public Observable<DeleteMsgResponse> deleteMsg(IRequest request) {

    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.DELETE_MSG, CloudUrlPath.METHOD_POST, 0, false);
      DeleteMsgResponse response = new DeleteMsgResponse();

      QtecResult<DeleteMsgResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<DeleteMsgResponse>>() {
      }.getType();

      QtecResult<DeleteMsgResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetMsgCountResponse> getMsgCount(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.GET_MSG_COUNT, CloudUrlPath.METHOD_GET, 0, false);
      GetMsgCountResponse response = new GetMsgCountResponse();
      response.setTotalMsgTipNum("100");
      response.setUnHandleNum("99");
      response.setUnReadMsg("32");

      QtecResult<GetMsgCountResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetMsgCountResponse>>() {
      }.getType();

      QtecResult<GetMsgCountResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetMsgReadResponse> setMsgRead(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.SET_MSG_READ, CloudUrlPath.METHOD_POST, 0, false);
      SetMsgReadResponse response = new SetMsgReadResponse();

      QtecResult<SetMsgReadResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<SetMsgReadResponse>>() {
      }.getType();

      QtecResult<SetMsgReadResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyLockPwdNameResponse> modifyLockPwdName(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_LOCK_PWD_NAME, CloudUrlPath.METHOD_POST, 0, false);

      ModifyLockPwdNameResponse response = new ModifyLockPwdNameResponse();

      QtecResult<ModifyLockPwdNameResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyLockPwdNameResponse>>() {
      }.getType();

      QtecResult<ModifyLockPwdNameResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<DeleteLockPwdResponse> deleteLockPwd(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_DELETE_LOCK_PWD, CloudUrlPath.METHOD_POST, 0, false);

      DeleteLockPwdResponse response = new DeleteLockPwdResponse();

      QtecResult<DeleteLockPwdResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<DeleteLockPwdResponse>>() {
      }.getType();

      QtecResult<DeleteLockPwdResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyLockFpNameResponse> modifyLockFpName(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_MODIFY_LOCK_FP_NAME, CloudUrlPath.METHOD_POST, 0, false);

      ModifyLockFpNameResponse response = new ModifyLockFpNameResponse();

      QtecResult<ModifyLockFpNameResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ModifyLockFpNameResponse>>() {
      }.getType();

      QtecResult<ModifyLockFpNameResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<DeleteLockFpResponse> deleteLockFp(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_DELETE_LOCK_FP, CloudUrlPath.METHOD_POST, 0, false);

      DeleteLockFpResponse response = new DeleteLockFpResponse();

      QtecResult<DeleteLockFpResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<DeleteLockFpResponse>>() {
      }.getType();

      QtecResult<DeleteLockFpResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<AddLockPwdResponse> addLockPwd(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_ADD_LOCK_PWD, CloudUrlPath.METHOD_POST, 0, false);

      AddLockPwdResponse response = new AddLockPwdResponse();

      QtecResult<AddLockPwdResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<AddLockPwdResponse>>() {
      }.getType();

      QtecResult<AddLockPwdResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<AddLockFpResponse> addLockFp(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_ADD_LOCK_FP, CloudUrlPath.METHOD_POST, 0, false);

      AddLockFpResponse response = new AddLockFpResponse();

      QtecResult<AddLockFpResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<AddLockFpResponse>>() {
      }.getType();

      QtecResult<AddLockFpResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetUsersOfLockResponse<List<String>>> getUsersOfLock(IRequest request) {
    return null;
  }

  @Override
  public Observable<CheckLockVersionResponse> checkLockVersion(IRequest request) {
    return null;
  }

  @Override
  public Observable<CheckFirmwareResponse> checkFirmwareTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          CheckFirmwareResponse response = new CheckFirmwareResponse();
          response.setUpdateversionNo("79.2.2.3024");
          response.setEffectivity(1);
          QtecResult<CheckFirmwareResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
//          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<CheckFirmwareResponse>>() {
          }.getType();
          QtecResult<CheckFirmwareResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<CheckFirmwareResponse>, Observable<CheckFirmwareResponse>>() {
          @Override
          public Observable<CheckFirmwareResponse> call(final QtecResult<CheckFirmwareResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
//              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatusTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 1, true);

          //封装网关层
          GetFirmwareUpdateStatusResponse response = new GetFirmwareUpdateStatusResponse();
          response.setStatus("正在升级");

          QtecResult<GetFirmwareUpdateStatusResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 1, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
          //          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<GetFirmwareUpdateStatusResponse>>() {
          }.getType();
          QtecResult<GetFirmwareUpdateStatusResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<GetFirmwareUpdateStatusResponse>, Observable<GetFirmwareUpdateStatusResponse>>() {
          @Override
          public Observable<GetFirmwareUpdateStatusResponse> call(final QtecResult<GetFirmwareUpdateStatusResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
              //              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<CheckAppVersionResponse> checkAppVersion(IRequest request) {
    return Observable.create(subscriber -> {

      addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_CHECK_APP_VERSION, CloudUrlPath.METHOD_GET, 0, false);

      CheckAppVersionResponse response = new CheckAppVersionResponse();
      response.setVersionNo("V1.2.0");
      response.setVersionNum(1);
      response.setVersionStatement("1.优化部分功能\n2.增加一键体检功能\n3.增加智能门锁功能");
      response.setCreateTime("20170829");
      response.setMinVersion(1);
      response.setDownloadUrl("https://raw.githubusercontent.com/feicien/android-auto-update/develop/extras/android-auto-update-v1.1.apk");
      QtecResult<CheckAppVersionResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<CheckAppVersionResponse>>() {
      }.getType();

      QtecResult<CheckAppVersionResponse> qtecResult = addDecryption(result, type, 0, false, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });

  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLockTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);

          //封装网关层
          BindRouterToLockResponse response = new BindRouterToLockResponse();

          QtecResult<BindRouterToLockResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
          //          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
          }.getType();
          QtecResult<BindRouterToLockResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<BindRouterToLockResponse>, Observable<BindRouterToLockResponse>>() {
          @Override
          public Observable<BindRouterToLockResponse> call(final QtecResult<BindRouterToLockResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
              //              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<QueryBindRouterToLockResponse> queryBindRouterToLockTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);

          //封装网关层
          QueryBindRouterToLockResponse response = new QueryBindRouterToLockResponse();
          response.setContained(1);

          QtecResult<QueryBindRouterToLockResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
          //          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<QueryBindRouterToLockResponse>>() {
          }.getType();
          QtecResult<QueryBindRouterToLockResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<QueryBindRouterToLockResponse>, Observable<QueryBindRouterToLockResponse>>() {
          @Override
          public Observable<QueryBindRouterToLockResponse> call(final QtecResult<QueryBindRouterToLockResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
              //              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<UnbindRouterToLockResponse> unbindRouterToLockTrans(IRequest request) {
    return Observable
        .create((Observable.OnSubscribe<TransmitResponse<String>>) subscriber -> {
          addEncryption((QtecEncryptInfo) request, CloudUrlPath.PATH_TRANSMIT, CloudUrlPath.METHOD_POST, 0, true);

          //封装网关层
          UnbindRouterToLockResponse response = new UnbindRouterToLockResponse();

          QtecResult<UnbindRouterToLockResponse> routerQtecResult = new QtecResult<>();
          routerQtecResult.setCode(0);
          routerQtecResult.setMsg("ok");
          routerQtecResult.setData(response);
          //封装Transmit层
          TransmitResponse<String> transmit = new TransmitResponse<>();
          transmit.setEncryptInfo(mJsonMapper.toJson(routerQtecResult));

          //封装Cloud层
          QtecResult<TransmitResponse<String>> result = new QtecResult<>();
          result.setData(transmit);

          Type type = new TypeToken<QtecResult<TransmitResponse<String>>>() {
          }.getType();

          QtecResult<TransmitResponse<String>> qtecResult = addDecryption(result, type, 0, true, subscriber);

          if (qtecResult.getCode() != 0) {
            subscriber.onError(new IOException(qtecResult.getMsg()));
          }
          subscriber.onNext(qtecResult.getData());
          //          subscriber.onCompleted();
        })
        .map(transmit -> {
          Type type = new TypeToken<QtecResult<UnbindRouterToLockResponse>>() {
          }.getType();
          QtecResult<UnbindRouterToLockResponse> routerResult = logRouterResult(transmit, type);
          return routerResult;
        })
        .flatMap(new Func1<QtecResult<UnbindRouterToLockResponse>, Observable<UnbindRouterToLockResponse>>() {
          @Override
          public Observable<UnbindRouterToLockResponse> call(final QtecResult<UnbindRouterToLockResponse> routerResult) {
            return Observable.create(subscriber -> {
              if (routerResult.getCode() != 0) {
                subscriber.onError(new IOException(routerResult.getMsg()));
              }
              subscriber.onNext(routerResult.getData());
              //              subscriber.onCompleted();
            });
          }
        });
  }

  @Override
  public Observable<QueryLockedCatResponse> queryLockedCat(IRequest request) {
    return null;
  }

  @Override
  public Observable<QueryDiskStateResponse> queryDiskStateTrans(IRequest request) {
    return null;
  }

  @Override
  public Observable<QueryDiskStateResponse> formatDiskTrans(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetUnlockModeResponse> getUnlockMode(IRequest request) {
    return null;
  }

  @Override
  public Observable<ModifyUnlockModeResponse> modifyUnlockMode(IRequest request) {
    return null;
  }

  @Override
  public Observable<UploadLogcatResponse> uploadLogcat(IRequest request) {
    return null;
  }

  @Override
  public Observable<List<GetDoorCardsResponse>> getDoorCards(IRequest request) {
    return null;
  }

  @Override
  public Observable<DeleteLockDoorCardResponse> deleteLockDoorCard(IRequest request) {
    return null;
  }

  @Override
  public Observable<ModifyLockDoorCardNameResponse> modifyLockDoorCardName(IRequest request) {
    return null;
  }

  @Override
  public Observable<AddLockDoorCardResponse> addLockDoorCard(IRequest request) {
    return null;
  }

  @Override
  public Observable<LockFactoryResetResponse> lockFactoryReset(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetUserRoleResponse> getUserRole(IRequest request) {
    return null;
  }

  @Override
  public Observable<List<GetLockUsersResponse>> getLockUsers(IRequest request) {
    return null;
  }

  @Override
  public Observable<DeleteLockUserResponse> deleteLockUser(IRequest request) {
    return null;
  }

  @Override
  public Observable<UnbindLockOfAdminResponse> unbindLockOfAdmin(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetRouterFirstConfigResponse> getFirstConfigTrans(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetPassagewayModeResponse> getPassagewayMode(IRequest request) {
    return null;
  }

  @Override
  public Observable<ModifyPassagewayModeResponse> modifyPassagewayMode(IRequest request) {
    return null;
  }

  @Override
  public Observable<UpdateLockVersionResponse> updateLockVersion(IRequest request) {
    return null;
  }

  @Override
  public Observable<AddTempPwdResponse> addTempPwd(IRequest request) {
    return null;
  }

  @Override
  public Observable<QueryTempPwdResponse> queryTempPwd(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetTempPwdResponse> getTempPwd(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetLockVolumeResponse> getLockVolume(IRequest request) {
    return null;
  }

  @Override
  public Observable<AdjustLockVolumeResponse> adjustLockVolume(IRequest request) {
    return null;
  }

  @Override
  public Observable<CheckLiteVersionResponse> checkLiteVersion(IRequest request) {
    return null;
  }

  @Override
  public Observable<UpdateLiteResponse> updateLite(IRequest request) {
    return null;
  }

  @Override
  public Observable<GetLiteUpdateResponse> getLiteUpdate(IRequest request) {
    return null;
  }

  @Override
  public Observable<CloudUnbindRouterLockResponse> cloudUnbindRouterLock(IRequest request) {
    return null;
  }

  private void addEncryption(QtecEncryptInfo request, String route, String method, int encryption, boolean isTrans) {
    QtecEncryptInfo encryptInfo = request;
    encryptInfo.setMethod(method);
    encryptInfo.setRequestUrl(route);
    String encryptInfoJson = mJsonMapper.toJson(encryptInfo);
    Logger.t("cloud-none-request").json(encryptInfoJson);
    if (isTrans) {
      String encryptReq = mCloudConverter.convertTo(encryptInfoJson, encryption);
      Logger.t("cloud-sm4-request").json(encryptReq);
    }

  }

  private QtecResult addDecryption(QtecResult result, Type type, int encryption, boolean isTrans, Subscriber subscriber) {
    result.setCode(0);
    result.setMsg("ok");
    String resultJson = mJsonMapper.toJson(result);
    if (isTrans) {
      String encryptResult = mCloudConverter.createEncryptResponse(resultJson, encryption);
      Logger.t("cloud-sm4-response").json(encryptResult);

      resultJson = mCloudConverter.convertFromForTestResponse(encryptResult, null);
      if (IConverter.EXP_KEY_INVALID.equals(resultJson)) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.KeyInvalidReceiver");
        mContext.sendBroadcast(intent);
      }
    }

    Logger.t("cloud-none-response").json(resultJson);
    return (QtecResult) mJsonMapper.fromJson(resultJson, type);
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
      result.setCode(-10000);
      return result;
    }

  }
}