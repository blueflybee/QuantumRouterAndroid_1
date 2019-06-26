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
package com.fernandocejas.android10.sample.domain.repository;

import com.fernandocejas.android10.sample.domain.params.IRequest;
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
import com.qtec.mapp.model.rsp.GetMsgListResponse.messageContent;
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
import com.qtec.mapp.model.rsp.UnbindCatLockResponse;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;
import com.qtec.mapp.model.rsp.UnbindLockOfAdminResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.mapp.model.rsp.UpdateLiteResponse;
import com.qtec.mapp.model.rsp.UpdateLockVersionResponse;
import com.qtec.mapp.model.rsp.UploadDevicePwdResponse;
import com.qtec.mapp.model.rsp.UploadLogcatResponse;
import com.qtec.mapp.model.rsp.UploadMsgDeviceIDResponse;
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

import java.util.List;

import rx.Observable;

/**
 * App与量子云端的服务接口
 * Interface that represents a CloudRepository for getting related data.
 */
public interface CloudRepository {

  Observable<LoginResponse> login(IRequest request);

  Observable<GetIdCodeResponse> getIdCode(IRequest request);

  Observable<GetAgreementResponse> getUserAgreement(IRequest request);

  Observable<GetMemRemarkNameResponse> getMemRemarkName(IRequest request);

  Observable<RegisterResponse> register(IRequest request);

  Observable<GetAgreementResponse> getUserSerectAgreement(IRequest request);

  Observable<ResetPwdGetIdCodeResponse> resetPwdGetIdCode(IRequest request);

  Observable<ResetPwdResponse> resetPwd(IRequest request);

  Observable<List<GetQuestionListResponse>> getQuestionList(IRequest request);

  Observable<GetQuestionDetailResponse> getQuestionDeatil(IRequest request);

  Observable<List<GetMyAdviceResponse>> getMyAdvice(IRequest request);

  Observable<FeedBackResponse<List<FeedBackResponse.ReplyContent>>> getAdviceDetail(IRequest request);

  Observable<GetfeedBackResponse> getFeedBack(IRequest request);

  Observable<GetUpdateFeedBackResponse> getUpdateFeedBack(IRequest request);

  Observable<ModifyUserInfoResponse> modifyUserInfo(IRequest request);

  Observable<LogoutResponse> logout(IRequest request);

  Observable<ModifyPwdGetIdCodeResponse> modifyPwdGetIdCode(IRequest request);

  Observable<ModifyPwdResponse> modifyPwd(IRequest request);

  Observable<GetDeviceCountResponse> getDeviceCount(IRequest request);

  Observable<UploadMsgDeviceIDResponse> uploadMsgId(IRequest request);

  Observable<List<GetDevTreeResponse<List<DeviceBean>>>> getDevTree(IRequest request);

  Observable<List<GetShareMemListResponse>> getSharedMemList(IRequest request);

  Observable<List<GetLockListResponse>> getLockList(IRequest request);

  Observable<QueryCatLockResponse> queryCatLock(IRequest request);

  Observable<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> getDoorBeelMsgList(IRequest request);

  Observable<GetDoorBellRecordDetailResponse> getDoorBeelMsgDetail(IRequest request);

  Observable<DeleteDoorBellRecordResponse> deleteDoorBeelMsg(IRequest request);

  Observable<SetAllDoorBellRecordReadResponse> setAllDoorBeelMsgIsRead(IRequest request);

  Observable<ConnectLockResponse> connectLock(IRequest request);

  Observable<QueryLockedCatResponse> queryLockedCat(IRequest request);

  Observable<UnbindCatLockResponse> unbindCatLock(IRequest request);

  Observable<UploadDevicePwdResponse> uploadDevicePwd(IRequest request);

  Observable<PostInvitationResponse> postInvitation(IRequest request);

  Observable<DealInvitationResponse> dealInvitation(IRequest request);

  Observable<List<GetMsgOtherListResponse>> getMsgList(IRequest request);

  Observable<List<GetMsgListResponse<messageContent>>> getMsgShareList(IRequest request);

  Observable<List<IntelDeviceListResponse>> getIntelDeviceList(IRequest request);

  Observable<GetExtraNetPortResponse> getExtraNetPort(IRequest request);

  Observable<IntelDeviceDetailResponse> getIntelDeviceDetail(IRequest request);

  Observable<DeleteMsgResponse> deleteMsg(IRequest request);

  Observable<GetMsgCountResponse> getMsgCount(IRequest request);

  Observable<SetMsgReadResponse> setMsgRead(IRequest request);

  Observable<UnbindIntelDevResponse> unbindTrans(IRequest request);

  Observable<CommitAddRouterInfoResponse> commitAddRouterInfo(IRequest request);

  Observable<IntelDevInfoModifyResponse> modifyIntelDev(IRequest request);

  Observable<RouterStatusResponse<List<Status>>> getRouterStatusTrans(IRequest request);

  Observable<AddIntelDevVerifyResponse> addIntelDevVerifyTrans(IRequest request);

  Observable<AddRouterVerifyResponse> addRouterVerifyTrans(IRequest request);

  Observable<List<GetUnlockInfoListResponse>> getLockNoteList(IRequest request);

  Observable<List<GetUnlockInfoListResponse>> getLockExceptionList(IRequest request);

  Observable<RemoteLockOperationResponse> remoteLockOptTrans(IRequest request);

  Observable<GetLockStatusResponse> getLockStatusTrans(IRequest request);

  Observable<GetAntiFritNetStatusResponse> getAntiFritNetStatusTrans(IRequest request);

  Observable<EnableAntiFritNetResponse> enableAntiFritNetTrans(IRequest request);

  Observable<SetAntiNetQuestionResponse> setAntiQuestionTrans(IRequest request);

  Observable<List<GetWaitingAuthDeviceListResponse>> getWaitingAuthListTrans(IRequest request);

  Observable<AllowAuthDeviceResponse> allowAuthDeviceTrans(IRequest request);

  Observable<GetAntiFritSwitchResponse> getAntiFritSwitchTrans(IRequest request);

  Observable<List<ChildCareListResponse>> getChildCareListTrans(IRequest request);

  Observable<PostChildCareDetailResponse> postChildCareDetailTrans(IRequest request);

  Observable<GetSignalRegulationResponse> getSignalModeTrans(IRequest request);

  Observable<OpenBandSpeedResponse> openBandSpeedMeasureTrans(IRequest request);

  Observable<GetGuestWifiSwitchResponse> getGuestSwitchTrans(IRequest request);

  Observable<SetGuestWifiSwitchResponse> setGuestSwitchTrans(IRequest request);

  Observable<SetWifiAllSwitchResponse> setWifiAllSwitchTrans(IRequest request);

  Observable<DeleteWifiSwitchResponse> deleteWifiSwitchTrans(IRequest request);

  Observable<SetWifiDataResponse> setWiFiDataTrans(IRequest request);

  Observable<AddVpnResponse> addVpnTrans(IRequest request);

  Observable<DeleteVpnResponse> deleteVpnTrans(IRequest request);

  Observable<ModifyVpnResponse> modifyVpnTrans(IRequest request);

  Observable<SetVpnResponse> setVpnTrans(IRequest request);

  Observable<GetVpnListResponse<List<GetVpnListResponse.VpnBean>>> getVpnListTrans(IRequest request);

  Observable<GetWirelessListResponse<List<GetWirelessListResponse.WirelessBean>>> getWirelessListTrans(IRequest request);

  Observable<GetConnectedWifiResponse> getConnectedWifiTrans(IRequest request);

  Observable<GetWifiDetailResponse> getWifiDetailTrans(IRequest request);

  Observable<PostConnectWirelessResponse> connectWirelessTrans(IRequest request);

  Observable<SetWifiSwitchResponse> setWifiSwitchTrans(IRequest request);

  Observable<PostInspectionResponse> safeInspectionTrans(IRequest request);

  Observable<List<GetBlackListResponse>> getBlackListTrans(IRequest request);

  Observable<RemoveBlackMemResponse> removeBlackMemTrans(IRequest request);

  Observable<GetAntiFritQuestionResponse> getAntiFritQuestionTrans(IRequest request);

  Observable<PostSpecialAttentionResponse> postSpecialAttentionTrans(IRequest request);

  Observable<GetSpecialAttentionResponse> getSpecialAttentionTrans(IRequest request);

  Observable<GetQosInfoResponse> getQosInfoTrans(IRequest request);

  Observable<PostQosInfoResponse> postQosInfoTrans(IRequest request);

  Observable<GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>>> getWifiTimeConfigTrans(IRequest request);

  Observable<AddFingerResponse> addFingerInfoTrans(IRequest request);

  Observable<DeleteFingerResponse> deleteFingerInfoTrans(IRequest request);

  Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> queryFingerInfoTrans(IRequest request);

  Observable<ModifyFingerNameResponse> modifyFingerNameTrans(IRequest request);

  Observable<GetSambaAccountResponse> getSambaAccountTrans(IRequest request);

  Observable<QueryDiskStateResponse> queryDiskStateTrans(IRequest request);

  Observable<QueryDiskStateResponse> formatDiskTrans(IRequest request);

  Observable<PostSignalRegulationResponse> setSignalRegulationInfoTrans(IRequest request);

  Observable<GetBandSpeedResponse> getBandSpeedTrans(IRequest request);

  Observable<SearchIntelDevNotifyResponse> searchIntelDevNotifyTrans(IRequest request);

  Observable<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> getStsToken(IRequest request);

  Observable<GetRouterInfoResponse> getRouterInfoTrans(IRequest request);

  Observable<GetRouterInfoCloudResponse> getRouterInfoCloud(IRequest request);

  Observable<ModifyRouterDescResponse> modifyRouterDesc(IRequest request);

  Observable<ModifyDevNameResponse> modifyDevName(IRequest request);

  Observable<List<GetRouterGroupsResponse>> getRouterGroups(IRequest request);

  Observable<ModifyRouterGroupResponse> modifyRouterGroup(IRequest request);

  Observable<CreateRouterGroupResponse> createRouterGroup(IRequest request);

  Observable<SetDHCPResponse> setDHCPTrans(IRequest request);

  Observable<SetPPPOEResponse> setPPPOETrans(IRequest request);

  Observable<SetStaticIPResponse> setStaticIPTrans(IRequest request);

  Observable<GetNetModeResponse> getNetModeTrans(IRequest request);

  Observable<RestartRouterResponse> restartRouterTrans(IRequest request);

  Observable<FactoryResetResponse> factoryResetTrans(IRequest request);

  Observable<UnbindRouterResponse> unbindRouter(IRequest request);

  Observable<UpdateFirmwareResponse> updateFirmwareTrans(IRequest request);

  Observable<GetWifiConfigResponse> getWifiConfigTrans(IRequest request);

  Observable<ConfigWifiResponse> configWifiTrans(IRequest request);

  Observable<GetTimerTaskResponse> getTimerTaskTrans(IRequest request);

  Observable<SetRouterTimerResponse> setRouterTimerTrans(IRequest request);

  Observable<CheckAdminPwdResponse> checkAdminPwdTrans(IRequest request);

  Observable<SetAdminPwdResponse> setAdminPwdTrans(IRequest request);

  Observable<List<GetFingerprintsResponse>> getFingerprints(IRequest request);

  Observable<GetKeyResponse<List<GetKeyResponse.KeyBean>>> getKeyTrans(IRequest request);

  Observable<List<GetPasswordsResponse>> getPasswords(IRequest request);

  Observable<ModifyLockPwdNameResponse> modifyLockPwdName(IRequest request);

  Observable<DeleteLockPwdResponse> deleteLockPwd(IRequest request);

  Observable<ModifyLockFpNameResponse> modifyLockFpName(IRequest request);

  Observable<DeleteLockFpResponse> deleteLockFp(IRequest request);

  Observable<AddLockPwdResponse> addLockPwd(IRequest request);

  Observable<AddLockFpResponse> addLockFp(IRequest request);

  Observable<CheckFirmwareResponse> checkFirmwareTrans(IRequest request);

  Observable<GetFirmwareUpdateStatusResponse> getFirmwareUpdateStatusTrans(IRequest request);

  Observable<CheckAppVersionResponse> checkAppVersion(IRequest request);


  Observable<BindRouterToLockResponse> bindRouterToLockTrans(IRequest request);

  Observable<QueryBindRouterToLockResponse> queryBindRouterToLockTrans(IRequest request);

  Observable<UnbindRouterToLockResponse> unbindRouterToLockTrans(IRequest request);

  Observable<GetUsersOfLockResponse<List<String>>> getUsersOfLock(IRequest request);

  Observable<CheckLockVersionResponse> checkLockVersion(IRequest request);

  Observable<GetUnlockModeResponse> getUnlockMode(IRequest request);

  Observable<ModifyUnlockModeResponse> modifyUnlockMode(IRequest request);

  Observable<UploadLogcatResponse> uploadLogcat(IRequest request);

  Observable<List<GetDoorCardsResponse>> getDoorCards(IRequest request);

  Observable<DeleteLockDoorCardResponse> deleteLockDoorCard(IRequest request);

  Observable<ModifyLockDoorCardNameResponse> modifyLockDoorCardName(IRequest request);

  Observable<AddLockDoorCardResponse> addLockDoorCard(IRequest request);

  Observable<LockFactoryResetResponse> lockFactoryReset(IRequest request);

  Observable<GetUserRoleResponse> getUserRole(IRequest request);

  Observable<List<GetLockUsersResponse>> getLockUsers(IRequest request);

  Observable<DeleteLockUserResponse> deleteLockUser(IRequest request);

  Observable<UnbindLockOfAdminResponse> unbindLockOfAdmin(IRequest request);

  Observable<GetRouterFirstConfigResponse> getFirstConfigTrans(IRequest request);

  Observable<GetPassagewayModeResponse> getPassagewayMode(IRequest request);

  Observable<ModifyPassagewayModeResponse> modifyPassagewayMode(IRequest request);

  Observable<UpdateLockVersionResponse> updateLockVersion(IRequest request);

  Observable<AddTempPwdResponse> addTempPwd(IRequest request);

  Observable<QueryTempPwdResponse> queryTempPwd(IRequest request);

  Observable<GetTempPwdResponse> getTempPwd(IRequest request);

  Observable<GetLockVolumeResponse> getLockVolume(IRequest request);

  Observable<AdjustLockVolumeResponse> adjustLockVolume(IRequest request);

  Observable<CheckLiteVersionResponse> checkLiteVersion(IRequest request);

  Observable<UpdateLiteResponse> updateLite(IRequest request);

  Observable<GetLiteUpdateResponse> getLiteUpdate(IRequest request);

  Observable<CloudUnbindRouterLockResponse> cloudUnbindRouterLock(IRequest request);
}
