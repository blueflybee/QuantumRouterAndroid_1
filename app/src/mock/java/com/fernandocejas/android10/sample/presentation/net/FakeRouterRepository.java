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
import android.os.Build;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.fernandocejas.android10.sample.data.exception.KeyInvalidException;
import com.fernandocejas.android10.sample.data.net.RouterRestApi;
import com.fernandocejas.android10.sample.data.net.RouterRestApiImpl;
import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.data.utils.IConverter;
import com.fernandocejas.android10.sample.data.utils.RouterConverter;
import com.fernandocejas.android10.sample.domain.constant.ModelConstant;
import com.fernandocejas.android10.sample.domain.mapper.JsonMapper;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.utils.BleHexUtil;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.data.LZKeyInfo;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecInfo;
import com.qtec.model.core.QtecResult;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.req.SetPPPOERequest;
import com.qtec.router.model.req.SetStaticIPRequest;
import com.qtec.router.model.rsp.AddFingerResponse;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.AllowAuthDeviceResponse;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.BlockAuthedResponse;
import com.qtec.router.model.rsp.CheckAdminPwdResponse;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.ChildCareListResponse;
import com.qtec.router.model.rsp.ConfigWifiResponse;
import com.qtec.router.model.rsp.DeleteFingerResponse;
import com.qtec.router.model.rsp.DeleteVpnResponse;
import com.qtec.router.model.rsp.DeleteWifiSwitchResponse;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.FirstConfigResponse;
import com.qtec.router.model.rsp.FirstGetKeyResponse;
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
import com.qtec.router.model.rsp.RemoveBlackMemResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.SearchIntelDevNotifyResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse.IntelDev;
import com.qtec.router.model.rsp.SearchRouterResponse;
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
import rx.exceptions.CompositeException;
import sun.misc.BASE64Encoder;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/08
 *     desc   : {@link FakeRouterRepository} for retrieving user data from fake remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class FakeRouterRepository implements RouterRepository {

  private final JsonMapper mJsonMapper = new JsonMapper();
  private final IConverter mRouterConverter = new RouterConverter();
  private Context mContext;

  @Inject
  public FakeRouterRepository(Context context) {
    mContext = context;
  }

  @Override
  public Observable<RouterStatusResponse<List<RouterStatusResponse.Status>>> getRouterStatus(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_ROUTER_STATUS, 1);

      RouterStatusResponse<List<RouterStatusResponse.Status>> routerStatusResponse = new RouterStatusResponse<>();
      routerStatusResponse.setRouterrx(1509);
      routerStatusResponse.setRoutertx(1203);
      routerStatusResponse.setHfssid("hsssid");
      routerStatusResponse.setLfssid("lfssid");
      routerStatusResponse.setOwnip("ownip");
      List<RouterStatusResponse.Status> statuses = new ArrayList<>();

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
        RouterStatusResponse.Status status = new RouterStatusResponse.Status();
        status.setStaname("status" + i);
        status.setStastatus(0);
        status.setMacaddr("mac"+i);
        status.setDevicetype(2);
        status.setIpaddr("ip"+i);
        status.setAccesstype("accessType"+i);
        status.setTx(1);
        status.setRx(2);
        statuses.add(status);
      }
      routerStatusResponse.setStalist(statuses);
      routerStatusResponse.setDevnum(statuses.size());

      QtecResult<RouterStatusResponse<List<RouterStatusResponse.Status>>> result = new QtecResult<>();
      result.setData(routerStatusResponse);

      Type type = new TypeToken<
          QtecResult<
              RouterStatusResponse<
                  List<RouterStatusResponse.Status>>>>() {
      }.getType();

      QtecResult<RouterStatusResponse<List<RouterStatusResponse.Status>>> qtecResult = addDecryption(result, type, 1, subscriber);
//      qtecResult.setCode(100);
//      qtecResult.setMsg("err");
      if (qtecResult.getCode() != 0) {
        subscriber.onError(new IOException(qtecResult.getMsg()));
      }
      subscriber.onNext(qtecResult.getData());
    });
  }

  @Override
  public Observable<SearchRouterResponse> searchRouter(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SEARCH_ROUTER, 0);

      SearchRouterResponse response = new SearchRouterResponse();
      response.setHostname("九州量子网关0s");
      response.setSerialnum("qtec0000_0");
      response.setDevmodel("12");
      response.setVersion("13");
      response.setConfigured(1);

      QtecResult<SearchRouterResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SearchRouterResponse>>() {
      }.getType();

      QtecResult<SearchRouterResponse> qtecResult = addDecryption(result, type, 0, subscriber);

      if (qtecResult.getCode() != 0) {
        subscriber.onError(new IOException(qtecResult.getMsg()));
      }
      subscriber.onNext(qtecResult.getData());

    });
  }

  @Override
  public Observable<GetLockStatusResponse> getLockStatus(IRequest request) {
    // TODO: 2017/7/10 dadd....
    return Observable.just(new GetLockStatusResponse());

  }

  @Override
  public Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatus(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ANTI_FRIT_NET, 1);

      GetAntiFritNetStatusResponse response = new GetAntiFritNetStatusResponse();
      response.setEnable(0);
      response.setLan_dev_access(1);
      response.setRouter_access(1);

      QtecResult<GetAntiFritNetStatusResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetAntiFritNetStatusResponse>>() {
      }.getType();

      QtecResult<GetAntiFritNetStatusResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<EnableAntiFritNetResponse> enableAntiFritNet(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ENABLE_ANTI_FRIT_NET, 1);

      EnableAntiFritNetResponse response = new EnableAntiFritNetResponse();

      QtecResult<EnableAntiFritNetResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<EnableAntiFritNetResponse>>() {
      }.getType();

      QtecResult<EnableAntiFritNetResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetAntiNetQuestionResponse> setAntiQuestion(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_ANTI_QUESTION, 1);

      SetAntiNetQuestionResponse response = new SetAntiNetQuestionResponse();

      QtecResult<SetAntiNetQuestionResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SetAntiNetQuestionResponse>>() {
      }.getType();

      QtecResult<SetAntiNetQuestionResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthList(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_WAITING_LIST, 1);

      List<GetWaitingAuthDeviceListResponse> responses = new ArrayList<GetWaitingAuthDeviceListResponse>();

      for (int i = 0; i < 6; i++) {
        GetWaitingAuthDeviceListResponse response = new GetWaitingAuthDeviceListResponse();
        response.setDev_ip("ip" + i);
        response.setDev_mac("mac" + i);
        response.setDev_name("name" + i);
        response.setDev_type(2);

        responses.add(response);
      }

      QtecResult<List<GetWaitingAuthDeviceListResponse>> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<List<GetWaitingAuthDeviceListResponse>>>() {
      }.getType();

      QtecResult<List<GetWaitingAuthDeviceListResponse>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<AllowAuthDeviceResponse> allowAuthDevice(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ALLOW_AUTH_DEVICE, 1);

      AllowAuthDeviceResponse response = new AllowAuthDeviceResponse();

      QtecResult<AllowAuthDeviceResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<AllowAuthDeviceResponse>>() {
      }.getType();

      QtecResult<AllowAuthDeviceResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetAntiFritSwitchResponse> getAntiFritSwitch(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ANTI_FRIT_SWITCH, 1);

      GetAntiFritSwitchResponse response = new GetAntiFritSwitchResponse();

      QtecResult<GetAntiFritSwitchResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetAntiFritSwitchResponse>>() {
      }.getType();

      QtecResult<GetAntiFritSwitchResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<List<ChildCareListResponse>> getChildCareList(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_CHILD_CARE_LIST, 1);

      List<ChildCareListResponse> responses = new ArrayList<ChildCareListResponse>();
      for (int i = 0; i < 10; i++) {
        ChildCareListResponse response = new ChildCareListResponse();
        response.setMacaddr("" + i);
        response.setEnabled(1);
        response.setStarttime("12:13");
        response.setStoptime("13:01");
        response.setWeekdays("1 3 7");
        responses.add(response);
      }

      QtecResult<List<ChildCareListResponse>> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<List<ChildCareListResponse>>>() {
      }.getType();

      QtecResult<List<ChildCareListResponse>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<PostChildCareDetailResponse> postChildCareDetail(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_CHILD_CARE_LIST, 1);

      PostChildCareDetailResponse responses = new PostChildCareDetailResponse();


      QtecResult<PostChildCareDetailResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<PostChildCareDetailResponse>>() {
      }.getType();

      QtecResult<PostChildCareDetailResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetSignalRegulationResponse> getSignalMode(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_SIGNAL_MODE, 1);

      GetSignalRegulationResponse response = new GetSignalRegulationResponse();
      response.setMode(2);

      QtecResult<GetSignalRegulationResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetSignalRegulationResponse>>() {
      }.getType();

      QtecResult<GetSignalRegulationResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<OpenBandSpeedResponse> openBandSpeedMeasure(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_BAND_SPEED, 1);

      OpenBandSpeedResponse response = new OpenBandSpeedResponse();

      QtecResult<OpenBandSpeedResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<OpenBandSpeedResponse>>() {
      }.getType();

      QtecResult<OpenBandSpeedResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetGuestWifiSwitchResponse> getGuestSwitch(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_GUEST_SWITCH, 1);

      GetGuestWifiSwitchResponse response = new GetGuestWifiSwitchResponse();
      response.setEnable(1);
      response.setIsHide(1);
      response.setUserNum(3);
      response.setName("九州访客");

      QtecResult<GetGuestWifiSwitchResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetGuestWifiSwitchResponse>>() {
      }.getType();

      QtecResult<GetGuestWifiSwitchResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetGuestWifiSwitchResponse> setGuestSwitch(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_GUEST_SWITCH, 1);

      SetGuestWifiSwitchResponse response = new SetGuestWifiSwitchResponse();

      QtecResult<SetGuestWifiSwitchResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SetGuestWifiSwitchResponse>>() {
      }.getType();

      QtecResult<SetGuestWifiSwitchResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetWifiAllSwitchResponse> setWifiAllSwitch(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_WIFI_ALL_SWITCH, 1);

      SetWifiAllSwitchResponse response = new SetWifiAllSwitchResponse();

      QtecResult<SetWifiAllSwitchResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SetWifiAllSwitchResponse>>() {
      }.getType();

      QtecResult<SetWifiAllSwitchResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<DeleteWifiSwitchResponse> deleteWifiSwitch(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_DELETE_WIFI_SWITCH, 1);

      DeleteWifiSwitchResponse response = new DeleteWifiSwitchResponse();

      QtecResult<DeleteWifiSwitchResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<DeleteWifiSwitchResponse>>() {
      }.getType();

      QtecResult<DeleteWifiSwitchResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetWifiDataResponse> setWiFiData(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_WIFI_DATA, 1);

      SetWifiDataResponse response = new SetWifiDataResponse();

      QtecResult<SetWifiDataResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SetWifiDataResponse>>() {
      }.getType();

      QtecResult<SetWifiDataResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<AddVpnResponse> addVpn(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ADD_VPN, 1);

      AddVpnResponse response = new AddVpnResponse();

      QtecResult<AddVpnResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<AddVpnResponse>>() {
      }.getType();

      QtecResult<AddVpnResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<DeleteVpnResponse> deleteVpn(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_DELETE_VPN, 1);

      DeleteVpnResponse response = new DeleteVpnResponse();

      QtecResult<DeleteVpnResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<DeleteVpnResponse>>() {
      }.getType();

      QtecResult<DeleteVpnResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyVpnResponse> modifyVpn(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_MODIFY_VPN, 1);

      ModifyVpnResponse response = new ModifyVpnResponse();

      QtecResult<ModifyVpnResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<ModifyVpnResponse>>() {
      }.getType();

      QtecResult<ModifyVpnResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetVpnResponse> setVpn(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_VPN, 1);

      SetVpnResponse response = new SetVpnResponse();

      QtecResult<SetVpnResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SetVpnResponse>>() {
      }.getType();

      QtecResult<SetVpnResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnList(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_VPN_LIST, 1);

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

      QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<List<GetVpnListResponse>>>() {
      }.getType();

      QtecResult<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelssList(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_VPN_LIST, 1);

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

      QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>>>() {
      }.getType();

      QtecResult<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetConnectedWifiResponse> getConnectedWifi(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_CONNECTED_WIFI, 1);

      GetConnectedWifiResponse responses = new GetConnectedWifiResponse();
      responses.setAuto_switch(1);
      responses.setStatus(1);
      responses.setSsid("九州量子网络wifi");
      responses.setEnable(1);

      QtecResult<GetConnectedWifiResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<GetConnectedWifiResponse>>() {
      }.getType();

      QtecResult<GetConnectedWifiResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetWifiDetailResponse> getWifiDetail(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_WIFI_DETAIL, 1);

      GetWifiDetailResponse responses = new GetWifiDetailResponse();
      responses.setDns("dns");
      responses.setIpaddr("ip");
      responses.setGateway("gateway");
      responses.setNetmask("netmask");

      QtecResult<GetWifiDetailResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<GetWifiDetailResponse>>() {
      }.getType();

      QtecResult<GetWifiDetailResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<PostConnectWirelessResponse> connectWireless(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_POST_CONNECT_WIRELESS, 1);

      PostConnectWirelessResponse responses = new PostConnectWirelessResponse();

      QtecResult<PostConnectWirelessResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<PostConnectWirelessResponse>>() {
      }.getType();

      QtecResult<PostConnectWirelessResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetWifiSwitchResponse> setWifiSwitch(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_WIFI_SWITCH, 1);

      SetWifiSwitchResponse responses = new SetWifiSwitchResponse();

      QtecResult<SetWifiSwitchResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<SetWifiSwitchResponse>>() {
      }.getType();

      QtecResult<SetWifiSwitchResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<PostInspectionResponse> safeInspection(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_POST_SAFE_INSPECTION, 1);

      PostInspectionResponse responses = new PostInspectionResponse();
      responses.setDdos(1);
      responses.setDmz(0);
      responses.setFtp(1);
      responses.setSamba(0);
      responses.setVirtualservice(1);
      responses.setDmz(1);

      QtecResult<PostInspectionResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<PostInspectionResponse>>() {
      }.getType();

      QtecResult<PostInspectionResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<List<GetBlackListResponse>> getBlackList(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_BLACK_LIST, 1);

      List<GetBlackListResponse> responses = new ArrayList<GetBlackListResponse>();
      for (int i = 0; i < 5; i++) {
        GetBlackListResponse bean = new GetBlackListResponse();
        bean.setEnabled(1);
        bean.setMacaddr("mac"+i);

        responses.add(bean);
      }

      QtecResult<List<GetBlackListResponse>> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<List<GetBlackListResponse>>>() {
      }.getType();

      QtecResult<List<GetBlackListResponse>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<RemoveBlackMemResponse> removeBlackMem(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_REMOVE_BLACK_MEM, 1);

      RemoveBlackMemResponse responses = new RemoveBlackMemResponse();

      QtecResult<RemoveBlackMemResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<RemoveBlackMemResponse>>() {
      }.getType();

      QtecResult<RemoveBlackMemResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetAntiFritQuestionResponse> getAntiFritQuestion(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_ANTI_FRIT_QUESTION, 1);

      GetAntiFritQuestionResponse responses = new GetAntiFritQuestionResponse();
      /*responses.setQuestion("question");
      responses.setAnswer("answer");*/

      QtecResult<GetAntiFritQuestionResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<GetAntiFritQuestionResponse>>() {
      }.getType();

      QtecResult<GetAntiFritQuestionResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<PostSpecialAttentionResponse> postSpecialAttention(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_POST_SPECIAL_ATTENTION, 1);

      PostSpecialAttentionResponse responses = new PostSpecialAttentionResponse();

      QtecResult<PostSpecialAttentionResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<PostSpecialAttentionResponse>>() {
      }.getType();

      QtecResult<PostSpecialAttentionResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetSpecialAttentionResponse> getSpecialAttention(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_SPECIAL_ATTENTION, 1);

      GetSpecialAttentionResponse responses = new GetSpecialAttentionResponse();
      responses.setSpecialcare("4c:cc:6a:e3:5a:d5|3 4c:cc:6a:e3:5a:d4|2");

      QtecResult<GetSpecialAttentionResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<GetSpecialAttentionResponse>>() {
      }.getType();

      QtecResult<GetSpecialAttentionResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetQosInfoResponse> getQosInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_QOS_INFO, 1);

      GetQosInfoResponse responses = new GetQosInfoResponse();
      responses.setDownload(100);
      responses.setUpload(1212);
      responses.setEnabled(1);
      responses.setQosmode(1);

      QtecResult<GetQosInfoResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<GetQosInfoResponse>>() {
      }.getType();

      QtecResult<GetQosInfoResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<PostQosInfoResponse> postQosInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_QOS_INFO, 1);

      PostQosInfoResponse responses = new PostQosInfoResponse();

      QtecResult<PostQosInfoResponse> result = new QtecResult<>();
      result.setData(responses);

      Type type = new TypeToken<QtecResult<PostQosInfoResponse>>() {
      }.getType();

      QtecResult<PostQosInfoResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfig(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_WIFI_TIME_CONFIG, 1);

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

      QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>>>() {
      }.getType();

      QtecResult<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<AddRouterVerifyResponse> addRouterVerify(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ADD_ROUTER_VERIFY, 0);

      AddRouterVerifyResponse response = new AddRouterVerifyResponse();

      QtecResult<AddRouterVerifyResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<AddRouterVerifyResponse>>() {
      }.getType();

      QtecResult<AddRouterVerifyResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<AddIntelDevVerifyResponse> addIntelDevVerify(final IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ADD_INTEL_DEV_VERIFY, 1);

      AddIntelDevVerifyResponse response = new AddIntelDevVerifyResponse();

      QtecResult<AddIntelDevVerifyResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<AddIntelDevVerifyResponse>>() {
      }.getType();

      QtecResult<AddIntelDevVerifyResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UnbindIntelDevResponse> unbind(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_UNBIND_INTEL_DEV, 1);

      UnbindIntelDevResponse response = new UnbindIntelDevResponse();
      QtecResult<UnbindIntelDevResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<UnbindIntelDevResponse>>() {
      }.getType();
      QtecResult<UnbindIntelDevResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  /**
   * 添加指纹
   *
   * @param
   * @return
   */
  @Override
  public Observable<AddFingerResponse> addFingerInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_ADD_FINGER, 1);

      AddFingerResponse response = new AddFingerResponse();
      response.setFpid("11111");

      QtecResult<AddFingerResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<AddFingerResponse>>() {
      }.getType();

      QtecResult<AddFingerResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ModifyFingerNameResponse> modifyFingerName(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_MODIFY_FINGER_NAME, 1);

      ModifyFingerNameResponse response = new ModifyFingerNameResponse();

      QtecResult<ModifyFingerNameResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<ModifyFingerNameResponse>>() {
      }.getType();

      QtecResult<ModifyFingerNameResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetSambaAccountResponse> getSambaAccount(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_SAM_PWD, 1);

      GetSambaAccountResponse response = new GetSambaAccountResponse();
      response.setUsername("network");
      response.setPassword("123456");

      QtecResult<GetSambaAccountResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetSambaAccountResponse>>() {
      }.getType();

      QtecResult<GetSambaAccountResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<PostSignalRegulationResponse> setSignalRegulationInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_SIGNAL_REGULATION, 1);

      PostSignalRegulationResponse response = new PostSignalRegulationResponse();

      QtecResult<PostSignalRegulationResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<PostSignalRegulationResponse>>() {
      }.getType();

      QtecResult<PostSignalRegulationResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetBandSpeedResponse> getBandSpeed(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_BAND_SPEED, 1);

      GetBandSpeedResponse response = new GetBandSpeedResponse();
      response.setSpeedtest(1);
      response.setDownspeed("11");
      response.setUpspeed("122");
      QtecResult<GetBandSpeedResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetBandSpeedResponse>>() {
      }.getType();

      QtecResult<GetBandSpeedResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<DeleteFingerResponse> deleteFingerInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_DELETE_FINGER, 1);

      DeleteFingerResponse response = new DeleteFingerResponse();

      QtecResult<DeleteFingerResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<DeleteFingerResponse>>() {
      }.getType();

      QtecResult<DeleteFingerResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_QUERY_FINGER_INFO, 1);

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

      QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> result = new QtecResult<>();
      result.setData(queryFingerInfoResponses);

      Type type = new TypeToken<QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>>() {
      }.getType();

      QtecResult<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<FirstGetKeyResponse> firstGetKey(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_FIRST_GET_KEY, 0);

      LZKeyInfo<LZKeyInfo.KeyBean> response = new LZKeyInfo<>();
      response.setKeytype(1);
      response.setUserid(PrefConstant.getUserPhone());
      response.setDeviceid(DeviceUtils.getAndroidID());
      response.setDevicename(Build.MODEL);
      response.setKeynumber(200);
      response.setRequestid(100);

      List<LZKeyInfo.KeyBean> keyBeanList = new ArrayList<>();
      BASE64Encoder base64Encoder = new BASE64Encoder();
      for (int i = 0; i < 200; i++) {
        LZKeyInfo.KeyBean keyBean = new LZKeyInfo.KeyBean();
        keyBean.setKeyid("id100000001000_" + i);
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

      QtecResult<LZKeyInfo<LZKeyInfo.KeyBean>> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<FirstGetKeyResponse>>() {
      }.getType();
      QtecResult<FirstGetKeyResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SearchIntelDevNotifyResponse> searchIntelDevNotify(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SEARCH_INTEL_DEV_NOTIFY, 1);

      SearchIntelDevNotifyResponse response = new SearchIntelDevNotifyResponse();

      QtecResult<SearchIntelDevNotifyResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<SearchIntelDevNotifyResponse>>() {
      }.getType();

      QtecResult<SearchIntelDevNotifyResponse> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetRouterInfoResponse> getRouterInfo(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_ROUTER_INFO, 0);

      GetRouterInfoResponse response = new GetRouterInfoResponse();

      response.setLfwifissid("2.4G量子WIFI_r");
      response.setHfwifissid("5G量子WIFI_r");
      response.setLanipaddress("192.168.1.100_r");
      response.setWanipaddress("10.10.10.78_r");
      response.setCputype("MT7628AN_1_r");
      response.setCpubrand("MediaTek_r");
      response.setCpufactory("MIPS_1_r");

      QtecResult<GetRouterInfoResponse> result = new QtecResult<>();
      result.setData(response);

      Type type = new TypeToken<QtecResult<GetRouterInfoResponse>>() {
      }.getType();

      QtecResult<GetRouterInfoResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetDHCPResponse> setDHCP(IRequest request) {
    return Observable.create(subscriber -> {
      int configed = ((SetDHCPRequest) ((QtecEncryptInfo) request).getData()).getConfiged();
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_DHCP, configed);

      SetDHCPResponse response = new SetDHCPResponse();
      QtecResult<SetDHCPResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<SetDHCPResponse>>() {
      }.getType();
      QtecResult<SetDHCPResponse> qtecResult = addDecryption(result, type, configed, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetPPPOEResponse> setPPPOE(IRequest request) {
    return Observable.create(subscriber -> {
      int configed = ((SetPPPOERequest) ((QtecEncryptInfo) request).getData()).getConfiged();

      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_PPPOE, configed);

      SetPPPOEResponse response = new SetPPPOEResponse();
      QtecResult<SetPPPOEResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<SetPPPOEResponse>>() {
      }.getType();
      QtecResult<SetPPPOEResponse> qtecResult = addDecryption(result, type, configed, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetStaticIPResponse> setStaticIP(IRequest request) {
    return Observable.create(subscriber -> {
      int configed = ((SetStaticIPRequest) ((QtecEncryptInfo) request).getData()).getConfiged();
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_STATIC_IP, configed);

      SetStaticIPResponse response = new SetStaticIPResponse();
      QtecResult<SetStaticIPResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<SetStaticIPResponse>>() {
      }.getType();
      QtecResult<SetStaticIPResponse> qtecResult = addDecryption(result, type, configed, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetNetModeResponse> getNetMode(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_NET_MODE, 0);

      GetNetModeResponse response = new GetNetModeResponse();
      response.setConnectiontype(ModelConstant.NET_MODE_STATIC);
      response.setIpaddr("192.168.1.1");
      response.setNetmask("2");
      response.setGateway("192.168.1.1");
      response.setDns("192.168.1.1 192.168.1.1");
      QtecResult<GetNetModeResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetNetModeResponse>>() {
      }.getType();
      QtecResult<GetNetModeResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<RestartRouterResponse> restartRouter(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_RESTART_ROUTER, 0);

      RestartRouterResponse response = new RestartRouterResponse();
      QtecResult<RestartRouterResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<RestartRouterResponse>>() {
      }.getType();
      QtecResult<RestartRouterResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<FactoryResetResponse> factoryReset(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_FACTORY_RESET, 0);

      FactoryResetResponse response = new FactoryResetResponse();
      QtecResult<FactoryResetResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<FactoryResetResponse>>() {
      }.getType();
      QtecResult<FactoryResetResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<UpdateFirmwareResponse> updateFirmware(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_UPDATE_FIRMWARE, 0);

      UpdateFirmwareResponse response = new UpdateFirmwareResponse();
      QtecResult<UpdateFirmwareResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<UpdateFirmwareResponse>>() {
      }.getType();
      QtecResult<UpdateFirmwareResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetWifiConfigResponse> getWifiConfig(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_WIFI_CONFIG, 0);

      GetWifiConfigResponse response = new GetWifiConfigResponse();
      response.setLfssid("2.4G_wifi");
      response.setLfdisabled("0");
      response.setLfhiden("0");

      response.setHfssid("5G_wifi");
      response.setHfdisabled("0");
      response.setHfhiden("0");
      QtecResult<GetWifiConfigResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetWifiConfigResponse>>() {
      }.getType();
      QtecResult<GetWifiConfigResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<ConfigWifiResponse> configWifi(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_CONFIG_WIFI, 0);

      ConfigWifiResponse response = new ConfigWifiResponse();

      QtecResult<ConfigWifiResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<ConfigWifiResponse>>() {
      }.getType();
      QtecResult<ConfigWifiResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetTimerTaskResponse> getTimerTask(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_TIMER_TASK, 0);

      GetTimerTaskResponse response = new GetTimerTaskResponse();
      response.setEnable(1);
      response.setHour(10);
      response.setMinute(30);
      response.setDay("0,1,2,4,6");

      QtecResult<GetTimerTaskResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetTimerTaskResponse>>() {
      }.getType();
      QtecResult<GetTimerTaskResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetRouterTimerResponse> setRouterTimer(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_ROUTER_TIMER, 0);

      SetRouterTimerResponse response = new SetRouterTimerResponse();

      QtecResult<SetRouterTimerResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<SetRouterTimerResponse>>() {
      }.getType();
      QtecResult<SetRouterTimerResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<CheckAdminPwdResponse> checkAdminPwd(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_CHECK_ADMIN_PWD, 0);

      CheckAdminPwdResponse response = new CheckAdminPwdResponse();

      QtecResult<CheckAdminPwdResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<CheckAdminPwdResponse>>() {
      }.getType();
      QtecResult<CheckAdminPwdResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<SetAdminPwdResponse> setAdminPwd(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_SET_ADMIN_PWD, 0);

      SetAdminPwdResponse response = new SetAdminPwdResponse();

      QtecResult<SetAdminPwdResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<SetAdminPwdResponse>>() {
      }.getType();
      QtecResult<SetAdminPwdResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetKeyResponse<List<KeyBean>>> getKey(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_KEY, 1);

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
      QtecResult<GetKeyResponse<List<KeyBean>>> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetKeyResponse<List<KeyBean>>>>() {
      }.getType();
      QtecResult<GetKeyResponse<List<KeyBean>>> qtecResult = addDecryption(result, type, 1, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onError(new IOException());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<CheckFirmwareResponse> checkFirmware(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_CHECK_FIRMWARE, 0);

      CheckFirmwareResponse response = new CheckFirmwareResponse();
      response.setUpdateversionNo("79.2.2.3026");
      response.setLocalversionNo("79.2.2.3024");
      response.setEffectivity(1);

      QtecResult<CheckFirmwareResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<CheckFirmwareResponse>>() {
      }.getType();
      QtecResult<CheckFirmwareResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
//      subscriber.onCompleted();
    });
  }

  @Override
  public Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatus(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_GET_FIRMWARE_UPDATE_STATUS, 0);

      GetFirmwareUpdateStatusResponse response = new GetFirmwareUpdateStatusResponse();
      response.setStatus(AppConstant.IMG_UPLOADING_FAILED);

      QtecResult<GetFirmwareUpdateStatusResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<GetFirmwareUpdateStatusResponse>>() {
      }.getType();
      QtecResult<GetFirmwareUpdateStatusResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
    });
  }

  @Override
  public Observable<FirstConfigResponse> firstConfig(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_FIRST_CONFIG, 0);

      FirstConfigResponse response = new FirstConfigResponse();

      QtecResult<FirstConfigResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<FirstConfigResponse>>() {
      }.getType();
      QtecResult<FirstConfigResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
    });
  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLock(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_BIND_ROUTER_TO_LOCK, 0);

      BindRouterToLockResponse response = new BindRouterToLockResponse();

      QtecResult<BindRouterToLockResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<BindRouterToLockResponse>>() {
      }.getType();
      QtecResult<BindRouterToLockResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
    });
  }

  @Override
  public Observable<QueryBindRouterToLockResponse> queryBindRouterToLock(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_QUERY_BIND_ROUTER_TO_LOCK, 0);

      QueryBindRouterToLockResponse response = new QueryBindRouterToLockResponse();
      response.setContained(1);

      QtecResult<QueryBindRouterToLockResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<QueryBindRouterToLockResponse>>() {
      }.getType();
      QtecResult<QueryBindRouterToLockResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
    });
  }

  @Override
  public Observable<UnbindRouterToLockResponse> unbindRouterToLock(IRequest request) {
    return Observable.create(subscriber -> {
      addEncryption((QtecEncryptInfo) request, RouterUrlPath.PATH_UNBIND_ROUTER_TO_LOCK, 0);

      UnbindRouterToLockResponse response = new UnbindRouterToLockResponse();

      QtecResult<UnbindRouterToLockResponse> result = new QtecResult<>();
      result.setData(response);
      Type type = new TypeToken<QtecResult<UnbindRouterToLockResponse>>() {
      }.getType();
      QtecResult<UnbindRouterToLockResponse> qtecResult = addDecryption(result, type, 0, subscriber);
      subscriber.onNext(qtecResult.getData());
    });
  }

  @Override
  public Observable<GetRouterFirstConfigResponse> getFirstConfig(IRequest request) {
    return null;
  }

  @Override
  public Observable<QueryDiskStateResponse> queryDiskState(IRequest request) {
    return null;
  }

  @Override
  public Observable<QueryDiskStateResponse> formatDisk(IRequest request) {
    return null;
  }

  private String mPath;

  private void addEncryption(QtecEncryptInfo request, String route, int encryption) {
    QtecEncryptInfo encryptInfo = request;
    encryptInfo.setRequestUrl(route);
    String encryptInfoJson = mJsonMapper.toJson(encryptInfo);
    Logger.t("router-none-request").json(encryptInfoJson);
    String encryptReq = mRouterConverter.convertTo(encryptInfoJson, encryption);
    Logger.t("router-sm4-request").json(encryptReq);
    mPath = route;
  }

  private QtecResult addDecryption(QtecResult result, Type type, int encryption, Subscriber subscriber) {
    result.setCode(0);
    result.setMsg("ok");
    String resultJson = mJsonMapper.toJson(result);

    String encryptResult = null;
    if (RouterUrlPath.PATH_FIRST_GET_KEY.equals(mPath)) {
      encryptResult = mRouterConverter.convertToForFake(resultJson, encryption);
    } else {
      encryptResult = mRouterConverter.createEncryptResponse(resultJson, encryption);
    }
    Logger.t("router-sm4-response").json(encryptResult);
//    System.out.println(encryptResult);

    String decryptResult = mRouterConverter.convertFromForTestResponse(encryptResult, mPath);
    if (IConverter.EXP_ACCESS_DEVICE_NOT_CONNECTED.equals(decryptResult)) {
      subscriber.onError(new IOException("未链接当前网关的wifi，尝试转发"));
    }

    if (IConverter.EXP_KEY_INVALID.equals(decryptResult)) {
      Intent intent = new Intent();
      intent.setAction("android.intent.action.KeyInvalidReceiver");
      mContext.sendBroadcast(intent);
    }

    Logger.t("router-none-response").json(decryptResult);
    return (QtecResult) mJsonMapper.fromJson(decryptResult, type);
  }


}
