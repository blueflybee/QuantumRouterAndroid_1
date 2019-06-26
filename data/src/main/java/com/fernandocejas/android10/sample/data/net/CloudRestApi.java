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

import java.util.List;

import rx.Observable;

/**
 * CloudRestApi for retrieving data from the cloud.
 */
public interface CloudRestApi {

  String URL_TEST = "http://192.168.92.59:10086";
  String WU_ZHI_HAI = "http://192.168.90.76:10086";
  String URL_GATEWAY_3_CARETEC = "https://gateway.3caretec.com/";
  String URL_ZHONG_WEI = "http://xwthird.cloudsee.net/";  //中维


  /**
   * @param request
   * @return
   */
  Observable<LoginResponse> login(IRequest request);

  Observable<ResetPwdResponse> resetPwd(IRequest request);

  Observable<ResetPwdGetIdCodeResponse> resetPwdGetIdCode(IRequest request);

  Observable<RegisterResponse> register(IRequest request);

  Observable<GetIdCodeResponse> getIdCode(IRequest request);

  Observable<List<GetQuestionListResponse>> getQuestionList(IRequest request);

  Observable<GetAgreementResponse> getUserAgreement(IRequest request);

  Observable<GetMemRemarkNameResponse> getMemRemarkName(IRequest request);

  Observable<GetAgreementResponse> getUserSerectAgreement(IRequest request);

  Observable<GetQuestionDetailResponse> getQuestionDeatil(IRequest request);

  Observable<List<GetMyAdviceResponse>> getMyAdvice(IRequest request);

  Observable<FeedBackResponse<List<FeedBackResponse.ReplyContent>>> getAdviceDetail(IRequest request);

  Observable<GetfeedBackResponse> getFeedBack(IRequest request);

  Observable<ModifyUserInfoResponse> modifyUserInfo(IRequest request);

  Observable<LogoutResponse> logout(IRequest request);

  Observable<GetUpdateFeedBackResponse> getUpdateFeedBack(IRequest request);

  Observable<ModifyPwdGetIdCodeResponse> modifyPwdGetIdCode(IRequest request);

  Observable<ModifyPwdResponse> modifyPwd(IRequest request);

  Observable<GetDeviceCountResponse> getDeviceCount(IRequest request);

  Observable<UploadMsgDeviceIDResponse> uploadMsgId(IRequest request);

  Observable<List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>>> getDevTree(IRequest request);

  Observable<List<GetShareMemListResponse>> getSharedMemList(IRequest request);

  Observable<List<GetLockListResponse>> getLockList(IRequest request);

  Observable<QueryCatLockResponse> queryCatLock(IRequest request);

  Observable<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> getDoorBeerMsgList(IRequest request);

  Observable<GetDoorBellRecordDetailResponse> getDoorBeerMsgDetail(IRequest request);

  Observable<DeleteDoorBellRecordResponse> deleteDoorBeerMsg(IRequest request);

  Observable<SetAllDoorBellRecordReadResponse> setAllDoorBeerMsgIsRead(IRequest request);

  Observable<ConnectLockResponse> connectLock(IRequest request);

  Observable<QueryLockedCatResponse> queryLockedCat(IRequest request);

  Observable<UnbindCatLockResponse> unbindCatLock(IRequest request);

  Observable<UploadDevicePwdResponse> uploadDevicePwd(IRequest request);

  Observable<DealInvitationResponse> dealInvitation(IRequest request);

  Observable<PostInvitationResponse> postInvitation(IRequest request);

  Observable<List<GetMsgOtherListResponse>> getMsgList(IRequest request);

  Observable<List<GetMsgListResponse<GetMsgListResponse.messageContent>>> getMsgShareList(IRequest request);

  Observable<List<IntelDeviceListResponse>> getIntelDeviceList(IRequest request);

  Observable<GetExtraNetPortResponse> getExtraNetPort(IRequest request);

  Observable<IntelDeviceDetailResponse> getIntelDeviceDetail(IRequest request);

  Observable<DeleteMsgResponse> deleteMsg(IRequest request);

  Observable<GetMsgCountResponse> getMsgCount(IRequest request);

  Observable<SetMsgReadResponse> setMsgRead(IRequest request);

  Observable<UnbindIntelDevResponse> unbind(IRequest request);

  Observable<CommitAddRouterInfoResponse> commitAddRouterInfo(IRequest request);

  Observable<IntelDevInfoModifyResponse> modifyIntelDev(IRequest request);


  Observable<TransmitResponse<String>> getRouterStatusTrans(IRequest request);

  Observable<TransmitResponse<String>> unbindTrans(IRequest request);

  Observable<TransmitResponse<String>> addIntelDevVerifyTrans(IRequest request);

  Observable<TransmitResponse<String>> addRouterVerifyTrans(IRequest request);

  Observable<List<GetUnlockInfoListResponse>> getLockNoteList(IRequest request);

  Observable<List<GetUnlockInfoListResponse>> getLockExceptionList(IRequest request);

  Observable<TransmitResponse<String>> remoteUnlockTrans(IRequest request);

  Observable<TransmitResponse<String>> searchIntelDevNotifyTrans(IRequest request);

  Observable<TransmitResponse<String>> modifyFingerNameTrans(IRequest request);

  Observable<TransmitResponse<String>> queryFingerInfoTrans(IRequest request);

  Observable<TransmitResponse<String>> deleteFingerInfoTrans(IRequest request);

  Observable<TransmitResponse<String>> addFingerInfoTrans(IRequest request);

  Observable<TransmitResponse<String>> getLockStatusTrans(IRequest request);

  Observable<TransmitResponse<String>> getAntiFritNetStatusTrans(IRequest request);

  Observable<TransmitResponse<String>> enableAntiFritNetTrans(IRequest request);

  Observable<TransmitResponse<String>> setAntiQuestionTrans(IRequest request);

  Observable<TransmitResponse<String>> allowAuthDeviceTrans(IRequest request);

  Observable<TransmitResponse<String>> getAntiFritSwitchTrans(IRequest request);

  Observable<TransmitResponse<String>> getChildCareListTrans(IRequest request);

  Observable<TransmitResponse<String>> postChildCareDetailTrans(IRequest request);

  Observable<TransmitResponse<String>> getSignalModeTrans(IRequest request);

  Observable<TransmitResponse<String>> openBandSpeedMeasureTrans(IRequest request);

  Observable<TransmitResponse<String>> getGuestSwitchTrans(IRequest request);

  Observable<TransmitResponse<String>> setGuestSwitchTrans(IRequest request);

  Observable<TransmitResponse<String>> setWifiAllSwitchTrans(IRequest request);

  Observable<TransmitResponse<String>> deleteWifiSwitchTrans(IRequest request);

  Observable<TransmitResponse<String>> setWiFiDataTrans(IRequest request);

  Observable<TransmitResponse<String>> addVpnTrans(IRequest request);

  Observable<TransmitResponse<String>> getConnectedWifiTrans(IRequest request);

  Observable<TransmitResponse<String>> getWifiDetailTrans(IRequest request);

  Observable<TransmitResponse<String>> connectWirelessTrans(IRequest request);

  Observable<TransmitResponse<String>> setWifiSwitchTrans(IRequest request);

  Observable<TransmitResponse<String>> safeInspectionTrans(IRequest request);

  Observable<TransmitResponse<String>> getBlackListTrans(IRequest request);

  Observable<TransmitResponse<String>> removeBlackMemTrans(IRequest request);

  Observable<TransmitResponse<String>> getAntiFritQuestionTrans(IRequest request);

  Observable<TransmitResponse<String>> deleteVpnTrans(IRequest request);

  Observable<TransmitResponse<String>> modifyVpnTrans(IRequest request);

  Observable<TransmitResponse<String>> setVpnTrans(IRequest request);

  Observable<TransmitResponse<String>> postSpecialAttentionTrans(IRequest request);

  Observable<TransmitResponse<String>> getSpecialAttentionTrans(IRequest request);

  Observable<TransmitResponse<String>> getQosInfoTrans(IRequest request);

  Observable<TransmitResponse<String>> postQosInfoTrans(IRequest request);

  Observable<TransmitResponse<String>> getWifiTimeConfigTrans(IRequest request);

  Observable<TransmitResponse<String>> getWaitingAuthListTrans(IRequest request);

  Observable<GetStsTokenResponse<CredentialsBean, AssumedRoleUserBean>> getStsToken(IRequest request);

  Observable<TransmitResponse<String>> getRouterInfoTrans(IRequest request);

  Observable<GetRouterInfoCloudResponse> getRouterInfoCloud(IRequest request);

  Observable<ModifyRouterDescResponse> modifyRouterDesc(IRequest request);

  Observable<ModifyDevNameResponse> modifyDevName(IRequest request);

  Observable<List<GetRouterGroupsResponse>> getRouterGroups(IRequest request);

  Observable<ModifyRouterGroupResponse> modifyRouterGroup(IRequest request);

  Observable<CreateRouterGroupResponse> createRouterGroup(IRequest request);

  Observable<TransmitResponse<String>> setDHCPTrans(IRequest request);

  Observable<TransmitResponse<String>> setPPPOETrans(IRequest request);

  Observable<TransmitResponse<String>> setStaticIPTrans(IRequest request);

  Observable<TransmitResponse<String>> getNetModeTrans(IRequest request);

  Observable<TransmitResponse<String>> restartRouterTrans(IRequest request);

  Observable<TransmitResponse<String>> factoryResetTrans(IRequest request);

  Observable<UnbindRouterResponse> unbindRouter(IRequest request);

  Observable<TransmitResponse<String>> updateFirmwareTrans(IRequest request);

  Observable<TransmitResponse<String>> getWifiConfigTrans(IRequest request);

  Observable<TransmitResponse<String>> configWifiTrans(IRequest request);

  Observable<TransmitResponse<String>> getSambaAccountTrans(IRequest request);

  Observable<TransmitResponse<String>> queryDiskStateTrans(IRequest request);

  Observable<TransmitResponse<String>> formatDiskTrans(IRequest request);

  Observable<TransmitResponse<String>> setSignalRegulationInfoTrans(IRequest request);

  Observable<TransmitResponse<String>> getBandSpeedTrans(IRequest request);

  Observable<TransmitResponse<String>> getFirmwareUpdateStatusTrans(IRequest request);

  Observable<TransmitResponse<String>> getTimerTaskTrans(IRequest request);

  Observable<TransmitResponse<String>> setRouterTimerTrans(IRequest request);

  Observable<TransmitResponse<String>> checkAdminPwdTrans(IRequest request);

  Observable<TransmitResponse<String>> setAdminPwdTrans(IRequest request);

  Observable<List<GetFingerprintsResponse>> getFingerprints(IRequest request);

  Observable<List<GetPasswordsResponse>> getPasswords(IRequest request);

  Observable<ModifyLockPwdNameResponse> modifyLockPwdName(IRequest request);

  Observable<DeleteLockPwdResponse> deleteLockPwd(IRequest request);

  Observable<ModifyLockFpNameResponse> modifyLockFpName(IRequest request);

  Observable<DeleteLockFpResponse> deleteLockFp(IRequest request);

  Observable<AddLockPwdResponse> addLockPwd(IRequest request);

  Observable<AddLockFpResponse> addLockFp(IRequest request);

  Observable<TransmitResponse<String>> getKeyTrans(IRequest request);

  Observable<TransmitResponse<String>> checkFirmwareTrans(IRequest request);

  Observable<CheckAppVersionResponse> checkAppVersion(IRequest request);

  Observable<TransmitResponse<String>> remoteLockOptTrans(IRequest request);

  Observable<TransmitResponse<String>> bindRouterToLockTrans(IRequest request);

  Observable<TransmitResponse<String>> queryBindRouterToLockTrans(IRequest request);

  Observable<TransmitResponse<String>> unbindRouterToLockTrans(IRequest request);

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

  Observable<TransmitResponse<String>> getFirstConfigTrans(IRequest request);

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
