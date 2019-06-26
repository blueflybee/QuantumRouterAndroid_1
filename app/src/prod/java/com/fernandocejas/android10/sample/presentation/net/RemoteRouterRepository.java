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

import com.fernandocejas.android10.sample.data.net.RouterRestApi;
import com.fernandocejas.android10.sample.data.net.RouterRestApiImpl;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;
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
import com.qtec.router.model.rsp.RouterStatusResponse.Status;
import com.qtec.router.model.rsp.SearchIntelDevNotifyResponse;
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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/08
 *     desc   : {@link RemoteRouterRepository} for retrieving user data from remote repository.
 *     version: 1.0
 * </pre>
 */
@Singleton
public class RemoteRouterRepository implements RouterRepository {

  private final RouterRestApi routerRestApi;


  @Inject
  public RemoteRouterRepository(RouterRestApiImpl routerRestApi) {
    this.routerRestApi = routerRestApi;
  }


  @Override
  public Observable<RouterStatusResponse<List<Status>>> getRouterStatus(IRequest request) {
    return routerRestApi.getRouterStatus(request);
  }

  @Override
  public Observable<SearchRouterResponse> searchRouter(IRequest request) {
    return routerRestApi.searchRouter(request);
  }

  /**
   * 门锁状态
   *
   * @param
   * @return
   */
  @Override
  public Observable<GetLockStatusResponse> getLockStatus(IRequest request) {
    return routerRestApi.getLockStatus(request);
  }

  @Override
  public Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatus(IRequest request) {
    return routerRestApi.getAntiFritNetStatus(request);
  }

  @Override
  public Observable<EnableAntiFritNetResponse> enableAntiFritNet(IRequest request) {
    return routerRestApi.enableAntiFritNet(request);
  }

  @Override
  public Observable<SetAntiNetQuestionResponse> setAntiQuestion(IRequest request) {
    return routerRestApi.setAntiQuestion(request);
  }

  @Override
  public Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthList(IRequest request) {
    return routerRestApi.getWaitingAuthList(request);
  }

  @Override
  public Observable<AllowAuthDeviceResponse> allowAuthDevice(IRequest request) {
    return routerRestApi.allowAuthDevice(request);
  }

  @Override
  public Observable<GetAntiFritSwitchResponse> getAntiFritSwitch(IRequest request) {
    return routerRestApi.getAntiFritSwitch(request);
  }

  @Override
  public Observable<List<ChildCareListResponse>> getChildCareList(IRequest request) {
    return routerRestApi.getChildCareList(request);
  }

  @Override
  public Observable<PostChildCareDetailResponse> postChildCareDetail(IRequest request) {
    return routerRestApi.postChildCareDetail(request);
  }

  @Override
  public Observable<GetSignalRegulationResponse> getSignalMode(IRequest request) {
    return routerRestApi.getSignalMode(request);
  }

  @Override
  public Observable<OpenBandSpeedResponse> openBandSpeedMeasure(IRequest request) {
    return routerRestApi.openBandSpeedMeasure(request);
  }

  @Override
  public Observable<GetGuestWifiSwitchResponse> getGuestSwitch(IRequest request) {
    return routerRestApi.getGuestSwitch(request);
  }

  @Override
  public Observable<SetGuestWifiSwitchResponse> setGuestSwitch(IRequest request) {
    return routerRestApi.setGuestSwitch(request);
  }

  @Override
  public Observable<SetWifiAllSwitchResponse> setWifiAllSwitch(IRequest request) {
    return routerRestApi.setWifiAllSwitch(request);
  }

  @Override
  public Observable<DeleteWifiSwitchResponse> deleteWifiSwitch(IRequest request) {
    return routerRestApi.deleteWifiSwitch(request);
  }

  @Override
  public Observable<SetWifiDataResponse> setWiFiData(IRequest request) {
    return routerRestApi.setWiFiData(request);
  }

  @Override
  public Observable<AddVpnResponse> addVpn(IRequest request) {
    return routerRestApi.addVpn(request);
  }

  @Override
  public Observable<DeleteVpnResponse> deleteVpn(IRequest request) {
    return routerRestApi.deleteVpn(request);
  }

  @Override
  public Observable<ModifyVpnResponse> modifyVpn(IRequest request) {
    return routerRestApi.modifyVpn(request);
  }

  @Override
  public Observable<SetVpnResponse> setVpn(IRequest request) {
    return routerRestApi.setVpn(request);
  }

  @Override
  public Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnList(IRequest request) {
    return routerRestApi.getVpnList(request);
  }

  @Override
  public Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelssList(IRequest request) {
    return routerRestApi.getWirelssList(request);
  }

  @Override
  public Observable<GetConnectedWifiResponse> getConnectedWifi(IRequest request) {
    return routerRestApi.getConnectedWifi(request);
  }

  @Override
  public Observable<GetWifiDetailResponse> getWifiDetail(IRequest request) {
    return routerRestApi.getWifiDetail(request);
  }

  @Override
  public Observable<PostConnectWirelessResponse> connectWireless(IRequest request) {
    return routerRestApi.connectWireless(request);
  }

  @Override
  public Observable<SetWifiSwitchResponse> setWifiSwitch(IRequest request) {
    return routerRestApi.setWifiSwitch(request);
  }

  @Override
  public Observable<PostInspectionResponse> safeInspection(IRequest request) {
    return routerRestApi.safeInspection(request);
  }

  @Override
  public Observable<List<GetBlackListResponse>> getBlackList(IRequest request) {
    return routerRestApi.getBlackList(request);
  }

  @Override
  public Observable<RemoveBlackMemResponse> removeBlackMem(IRequest request) {
    return routerRestApi.removeBlackMem(request);
  }

  @Override
  public Observable<GetAntiFritQuestionResponse> getAntiFritQuestion(IRequest request) {
    return routerRestApi.getAntiFritQuestion(request);
  }

  @Override
  public Observable<PostSpecialAttentionResponse> postSpecialAttention(IRequest request) {
    return routerRestApi.postSpecialAttention(request);
  }

  @Override
  public Observable<GetSpecialAttentionResponse> getSpecialAttention(IRequest request) {
    return routerRestApi.getSpecialAttention(request);
  }

  @Override
  public Observable<GetQosInfoResponse> getQosInfo(IRequest request) {
    return routerRestApi.getQosInfo(request);
  }

  @Override
  public Observable<PostQosInfoResponse> postQosInfo(IRequest request) {
    return routerRestApi.postQosInfo(request);
  }

  @Override
  public Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfig(IRequest request) {
    return routerRestApi.getWifiTimeConfig(request);
  }

  @Override
  public Observable<AddRouterVerifyResponse> addRouterVerify(IRequest request) {
    return routerRestApi.addRouterVerify(request);
  }

  @Override
  public Observable<AddIntelDevVerifyResponse> addIntelDevVerify(IRequest request) {
    return routerRestApi.addIntelDevVerify(request);
  }

  @Override
  public Observable<UnbindIntelDevResponse> unbind(IRequest request) {
    return routerRestApi.unbind(request);
  }

  @Override
  public Observable<AddFingerResponse> addFingerInfo(IRequest request) {
    return routerRestApi.addFingerInfo(request);
  }

  @Override
  public Observable<ModifyFingerNameResponse> modifyFingerName(IRequest request) {
    return routerRestApi.modifyFingerName(request);
  }

  @Override
  public Observable<DeleteFingerResponse> deleteFingerInfo(IRequest request) {
    return routerRestApi.deleteFingerInfo(request);
  }

  @Override
  public Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfo(IRequest request) {
    return routerRestApi.queryFingerInfo(request);
  }

  @Override
  public Observable<FirstGetKeyResponse> firstGetKey(IRequest request) {
    return routerRestApi.firstGetKey(request);
  }

  @Override
  public Observable<SearchIntelDevNotifyResponse> searchIntelDevNotify(IRequest request) {
    return routerRestApi.searchIntelDevNotify(request);
  }

  @Override
  public Observable<GetRouterInfoResponse> getRouterInfo(IRequest request) {
    return routerRestApi.getRouterInfo(request);
  }

  @Override
  public Observable<SetDHCPResponse> setDHCP(IRequest request) {
    return routerRestApi.setDHCP(request);
  }

  @Override
  public Observable<SetPPPOEResponse> setPPPOE(IRequest request) {
    return routerRestApi.setPPPOE(request);
  }

  @Override
  public Observable<SetStaticIPResponse> setStaticIP(IRequest request) {
    return routerRestApi.setStaticIP(request);
  }

  @Override
  public Observable<GetNetModeResponse> getNetMode(IRequest request) {
    return routerRestApi.getNetMode(request);
  }

  @Override
  public Observable<RestartRouterResponse> restartRouter(IRequest request) {
    return routerRestApi.restartRouter(request);
  }

  @Override
  public Observable<FactoryResetResponse> factoryReset(IRequest request) {
    return routerRestApi.factoryReset(request);
  }

  @Override
  public Observable<UpdateFirmwareResponse> updateFirmware(IRequest request) {
    return routerRestApi.updateFirmware(request);
  }

  @Override
  public Observable<GetWifiConfigResponse> getWifiConfig(IRequest request) {
    return routerRestApi.getWifiConfig(request);
  }

  @Override
  public Observable<ConfigWifiResponse> configWifi(IRequest request) {
    return routerRestApi.configWifi(request);
  }

  @Override
  public Observable<GetTimerTaskResponse> getTimerTask(IRequest request) {
    return routerRestApi.getTimerTask(request);
  }

  @Override
  public Observable<SetRouterTimerResponse> setRouterTimer(IRequest request) {
    return routerRestApi.setRouterTimer(request);
  }

  @Override
  public Observable<CheckAdminPwdResponse> checkAdminPwd(IRequest request) {
    return routerRestApi.checkAdminPwd(request);
  }

  @Override
  public Observable<SetAdminPwdResponse> setAdminPwd(IRequest request) {
    return routerRestApi.setAdminPwd(request);
  }

  @Override
  public Observable<GetSambaAccountResponse> getSambaAccount(IRequest request) {
    return routerRestApi.getSambaAccount(request);
  }

  @Override
  public Observable<QueryDiskStateResponse> queryDiskState(IRequest request) {
    return routerRestApi.queryDiskState(request);
  }

  @Override
  public Observable<QueryDiskStateResponse> formatDisk(IRequest request) {
    return routerRestApi.formatDisk(request);
  }

  @Override
  public Observable<PostSignalRegulationResponse> setSignalRegulationInfo(IRequest request) {
    return routerRestApi.setSignalRegulationInfo(request);
  }

  @Override
  public Observable<GetBandSpeedResponse> getBandSpeed(IRequest request) {
    return routerRestApi.getBandSpeed(request);
  }

  @Override
  public Observable<GetKeyResponse<List<GetKeyResponse.KeyBean>>> getKey(IRequest request) {
    return routerRestApi.getKey(request);
  }

  @Override
  public Observable<CheckFirmwareResponse> checkFirmware(IRequest request) {
    return routerRestApi.checkFirmware(request);
  }

  @Override
  public Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatus(IRequest request) {
    return routerRestApi.getFirmwareUpdateStatus(request);
  }

  @Override
  public Observable<FirstConfigResponse> firstConfig(IRequest request) {
    return routerRestApi.firstConfig(request);
  }

  @Override
  public Observable<BindRouterToLockResponse> bindRouterToLock(IRequest request) {
    return routerRestApi.bindRouterToLock(request);
  }

  @Override
  public Observable<QueryBindRouterToLockResponse> queryBindRouterToLock(IRequest request) {
    return routerRestApi.queryBindRouterToLock(request);
  }

  @Override
  public Observable<UnbindRouterToLockResponse> unbindRouterToLock(IRequest request) {
    return routerRestApi.unbindRouterToLock(request);
  }

  @Override
  public Observable<GetRouterFirstConfigResponse> getFirstConfig(IRequest request) {
    return routerRestApi.getFirstConfig(request);
  }

}