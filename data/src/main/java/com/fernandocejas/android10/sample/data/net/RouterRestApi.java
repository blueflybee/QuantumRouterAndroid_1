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

import com.fernandocejas.android10.sample.domain.params.IRequest;
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

import rx.Observable;

/**
 * CloudRestApi for retrieving data from the router.
 */
public interface RouterRestApi {

 /* String URL_DEBUG = "http://luyou.qtec.cn/cgi-bin/json.cgi";
  String URL_DEBUG = "http://luyou.qtec.cn/cgi-bin/json.cgi";*/
  /*String URL_DEBUG = "http://192.168.1.1/cgi-bin/json.cgi";*/
  String URL_DEBUG = "http://router.qtec.cn/cgi-bin/json.cgi";
  String URL_FOR_SAMBA_IP = "router.qtec.cn";


  Observable<RouterStatusResponse<List<Status>>> getRouterStatus(IRequest request);

  Observable<GetLockStatusResponse> getLockStatus(IRequest request);

  Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatus(IRequest request);

  Observable<EnableAntiFritNetResponse> enableAntiFritNet(IRequest request);

  Observable<SetAntiNetQuestionResponse> setAntiQuestion(IRequest request);

  Observable<AllowAuthDeviceResponse> allowAuthDevice(IRequest request);

  Observable<GetAntiFritSwitchResponse> getAntiFritSwitch(IRequest request);

  Observable<PostChildCareDetailResponse> postChildCareDetail(IRequest request);

  Observable<OpenBandSpeedResponse> openBandSpeedMeasure(IRequest request);

  Observable<GetGuestWifiSwitchResponse> getGuestSwitch(IRequest request);

  Observable<SetGuestWifiSwitchResponse> setGuestSwitch(IRequest request);

  Observable<SetWifiAllSwitchResponse> setWifiAllSwitch(IRequest request);

  Observable<DeleteWifiSwitchResponse> deleteWifiSwitch(IRequest request);

  Observable<SetWifiDataResponse> setWiFiData(IRequest request);

  Observable<AddVpnResponse> addVpn(IRequest request);

  Observable<DeleteVpnResponse> deleteVpn(IRequest request);

  Observable<ModifyVpnResponse> modifyVpn(IRequest request);

  Observable<SetVpnResponse> setVpn(IRequest request);

  Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnList(IRequest request);

  Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelssList(IRequest request);

  Observable<GetConnectedWifiResponse> getConnectedWifi(IRequest request);

  Observable<SetWifiSwitchResponse> setWifiSwitch(IRequest request);

  Observable<PostInspectionResponse> safeInspection(IRequest request);

  Observable<List<GetBlackListResponse>> getBlackList(IRequest request);

  Observable<RemoveBlackMemResponse> removeBlackMem(IRequest request);

  Observable<GetAntiFritQuestionResponse> getAntiFritQuestion(IRequest request);

  Observable<PostConnectWirelessResponse> connectWireless(IRequest request);

  Observable<GetWifiDetailResponse> getWifiDetail(IRequest request);

  Observable<PostSpecialAttentionResponse> postSpecialAttention(IRequest request);

  Observable<GetSpecialAttentionResponse> getSpecialAttention(IRequest request);

  Observable<GetQosInfoResponse> getQosInfo(IRequest request);

  Observable<PostQosInfoResponse> postQosInfo(IRequest request);

  Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfig(IRequest request);

  Observable<GetSignalRegulationResponse> getSignalMode(IRequest request);

  Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthList(IRequest request);

  Observable<List<ChildCareListResponse>> getChildCareList(IRequest request);

  Observable<AddFingerResponse> addFingerInfo(IRequest request);

  Observable<ModifyFingerNameResponse> modifyFingerName(IRequest request);

  Observable<DeleteFingerResponse> deleteFingerInfo(IRequest request);

  Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfo(IRequest request);

  Observable<SearchRouterResponse> searchRouter(IRequest request);

  Observable<AddRouterVerifyResponse> addRouterVerify(IRequest request);

  Observable<AddIntelDevVerifyResponse> addIntelDevVerify(IRequest request);

  Observable<UnbindIntelDevResponse> unbind(IRequest request);

  Observable<FirstGetKeyResponse> firstGetKey(IRequest request);

  Observable<SearchIntelDevNotifyResponse> searchIntelDevNotify(IRequest request);

  Observable<GetRouterInfoResponse> getRouterInfo(IRequest request);

  Observable<SetDHCPResponse> setDHCP(IRequest request);

  Observable<SetPPPOEResponse> setPPPOE(IRequest request);

  Observable<SetStaticIPResponse> setStaticIP(IRequest request);

  Observable<GetNetModeResponse> getNetMode(IRequest request);

  Observable<RestartRouterResponse> restartRouter(IRequest request);

  Observable<FactoryResetResponse> factoryReset(IRequest request);

  Observable<UpdateFirmwareResponse> updateFirmware(IRequest request);

  Observable<GetWifiConfigResponse> getWifiConfig(IRequest request);

  Observable<ConfigWifiResponse> configWifi(IRequest request);

  Observable<GetTimerTaskResponse> getTimerTask(IRequest request);

  Observable<SetRouterTimerResponse> setRouterTimer(IRequest request);

  Observable<CheckAdminPwdResponse> checkAdminPwd(IRequest request);

  Observable<SetAdminPwdResponse> setAdminPwd(IRequest request);

  Observable<GetSambaAccountResponse> getSambaAccount(IRequest request);

  Observable<QueryDiskStateResponse> queryDiskState(IRequest request);

  Observable<QueryDiskStateResponse> formatDisk(IRequest request);

  Observable<PostSignalRegulationResponse> setSignalRegulationInfo(IRequest request);

  Observable<GetBandSpeedResponse> getBandSpeed(IRequest request);

  Observable<GetKeyResponse<List<GetKeyResponse.KeyBean>>> getKey(IRequest request);

  Observable<CheckFirmwareResponse> checkFirmware(IRequest request);

  Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatus(IRequest request);

  Observable<FirstConfigResponse> firstConfig(IRequest request);

  Observable<BindRouterToLockResponse> bindRouterToLock(IRequest request);

 Observable<QueryBindRouterToLockResponse> queryBindRouterToLock(IRequest request);

 Observable<UnbindRouterToLockResponse> unbindRouterToLock(IRequest request);

  Observable<GetRouterFirstConfigResponse> getFirstConfig(IRequest request);
}
