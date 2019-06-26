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
package com.fernandocejas.android10.sample.presentation.internal.di.modules;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.AdjustLockVolume;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CheckLiteVersion;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CommitAddRouterInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CreateRouterGroupUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeviceCountUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ExtraNetPortUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetLiteUpdate;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetLockVolume;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetRouterGroupsUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetRouterInfoCloudUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.IntelDevInfoModifyUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.IntelDeviceDetailUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.IntelDeviceListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyDevNameUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyRouterDescUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyRouterGroupUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UnbindLockOfAdminUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UnbindRouterUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UpdateLite;
import com.fernandocejas.android10.sample.domain.interactor.router.AddRouterVerifyUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AddVpnUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AllowAuthDeviceUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AntiFritNetStatusUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AntiFritSwitchUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.BindRouterToLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.BlackListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.CheckAdminPwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.CheckFirmwareUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.ChildCareDetailUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.ChildCareListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.ConfigWifiUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.ConnectWirelssUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.DeleteVpnUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.DeleteWifiSwitchUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.EnableAntiFritNetUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.FactoryResetUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.FirstConfigUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.FormatDiskUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetAntiFritQuestionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetBandSpeedUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetConnectedWifiUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetFirmwareUpdateStatusUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetNetModeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetQosInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetRouterFirstConfigUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetRouterInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetSambaAccountUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetSignalModeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetSpecialAttentionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetTimerTaskUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetVpnListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetWaitingAuthListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetWifiConfigUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetWifiDetailUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetWifiTimeConfigUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetWirelessListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GuestWifiSwitchUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.ModifyVpnUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.OpenBandSpeedUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.PostQosInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.PostSignalReguaInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.PostSpecialAttentionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.QueryBindRouterToLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.QueryDiskStateUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.RemoveBlackMemUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.RestartRouterUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.RouterStatusUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SafeInspectionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SearchRouterUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetAdminPwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetAntiNetQuestionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetDHCPUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetGuestWifiSwitchUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetPPPOEUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetRouterTimerUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetStaticIPUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetVpnUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetWifiAllSwitchUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetWifiDataUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SetWifiSwitchUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.UnbindIntelDevUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.UpdateFirmwareUseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class RouterModule {


  public RouterModule() {
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ROUTER_STATUS)
  UseCase provideRouterStatusUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new RouterStatusUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

//    @Provides
//    @PerActivity
//    @Named(CloudUseCaseComm.ROUTER_STATUS_CLOUD)
//    UseCase provideRouterStatusCloudUseCase(CloudRepository cloudRepository,
//                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
//
//        return new RouterStatusTransUseCase(cloudRepository, threadExecutor, postExecutionThread);
//    }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SEARCH_ROUTER)
  UseCase provideSearchRouterUseCase(RouterRepository routerRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SearchRouterUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ADD_ROUTER_VERIFY)
  UseCase provideAddRouterVerifyUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new AddRouterVerifyUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.INTEL_DEVICE_LIST)
  UseCase provideIntelDeviceListUseCase(CloudRepository routerRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new IntelDeviceListUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.EXTRA_NET_PORT)
  UseCase provideExtraNetPortUseCase(CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ExtraNetPortUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.INTEL_DEVICE_DETAIL)
  UseCase provideIntelDeviceDetailUseCase(CloudRepository routerRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new IntelDeviceDetailUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UNBIND_INTEL_DEV)
  UseCase provideUnbindIntelDevUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UnbindIntelDevUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.COMMIT_ADD_ROUTER_INFO)
  UseCase provideCommitAddRouterInfoUseCase(CloudRepository routerRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new CommitAddRouterInfoUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.INTEL_DEVICE_MODIFY)
  UseCase provideIntelDevInfoModifyUseCase(CloudRepository routerRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new IntelDevInfoModifyUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_ROUTER_INFO)
  UseCase provideGetRouterInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetRouterInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_ROUTER_INFO_CLOUD)
  UseCase provideGetRouterInfoCloudUseCase(CloudRepository cloudRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetRouterInfoCloudUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_ROUTER_DESC)
  UseCase provideModifyRouterDescUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ModifyRouterDescUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_ROUTER_NAME)
  UseCase provideModifyRouterNameUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ModifyDevNameUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_ROUTER_GROUPS)
  UseCase provideGetRouterGroupsUseCase(CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetRouterGroupsUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_ROUTER_GROUP)
  UseCase provideModifyRouterGroupUseCase(CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ModifyRouterGroupUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.CREATE_ROUTER_GROUP)
  UseCase provideCreateRouterGroupUseCase(CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new CreateRouterGroupUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_DHCP)
  UseCase provideSetDHCPUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SetDHCPUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_PPPOE)
  UseCase provideSetPPPOEUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SetPPPOEUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_STATIC_IP)
  UseCase provideSetStaticIPUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SetStaticIPUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_NET_MODE)
  UseCase provideGetNetModeUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetNetModeUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.RESTART_ROUTER)
  UseCase provideRestartRouterUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new RestartRouterUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.FACTORY_RESET)
  UseCase provideFactoryResetUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new FactoryResetUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UNBIND_ROUTER)
  UseCase provideUnbindRouterUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UnbindRouterUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UNBIND_LOCK_OF_ADMIN)
  UseCase provideUnbindLockOfAdminUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UnbindLockOfAdminUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.UPDATE_FIRMWARE)
  UseCase provideUpdateFirmwareUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UpdateFirmwareUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_WIFI_CONFIG)
  UseCase provideGetWifiConfigUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetWifiConfigUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.CONFIG_WIFI)
  UseCase provideConfigWifiUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ConfigWifiUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_TIMER_TASK)
  UseCase provideGetTimerTaskUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetTimerTaskUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_ROUTER_TIMER)
  UseCase provideSetRouterTimerUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SetRouterTimerUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.CHECK_ADMIN_PWD)
  UseCase provideCheckAdminPwdUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new CheckAdminPwdUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_ADMIN_PWD)
  UseCase provideSetAdminPwdUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SetAdminPwdUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.CHECK_FIRMWARE)
  UseCase provideCheckFirmwareUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new CheckFirmwareUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_FIRMWARE_UPDATE_STATUS)
  UseCase provideGetFirmwareUpdateStatusUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetFirmwareUpdateStatusUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_SAMBA_ACCOUNT)
  UseCase provideGetSambaAccountUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetSambaAccountUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.QUERY_DISK_STATE)
  UseCase provideQueryDiskStateUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new QueryDiskStateUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.FORMAT_DISK)
  UseCase provideFormatDiskUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new FormatDiskUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_SIGNAL_REGULATION)
  UseCase providePostSignalReguaInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new PostSignalReguaInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_BAND_SPEED)
  UseCase provideGetBandSpeedUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetBandSpeedUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ANTI_FRIT_STATUS)
  UseCase provideAntiFritNetStatusUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AntiFritNetStatusUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ENABLE_FRIT_NET)
  UseCase provideEnableAntiFritNetUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new EnableAntiFritNetUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_ANTIWIFI_QUESTION)
  UseCase provideSetAntiNetQuestionUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SetAntiNetQuestionUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_WAITING_AUTH_LIST)
  UseCase provideGetWaitingAuthListUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetWaitingAuthListUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ALLOW_AUTH_DEVICE)
  UseCase provideAllowAuthDeviceUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AllowAuthDeviceUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ANTI_FRIT_SWITCH)
  UseCase provideAntiFritSwitchUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AntiFritSwitchUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.CHILD_CARE_LIST)
  UseCase provideChildCareListUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ChildCareListUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.POST_CHILD_CARE_DETAIL)
  UseCase provideChildCareDetailUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ChildCareDetailUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_SIGNAL_MODE)
  UseCase provideGetSignalModeUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetSignalModeUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.OPEN_BAND_SPEED)
  UseCase provideOpenBandSpeedUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new OpenBandSpeedUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_GUEST_WIFI_SWITCH)
  UseCase provideGuestWifiSwitchUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GuestWifiSwitchUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_GUEST_WIFI_SWITCH)
  UseCase provideSetGuestWifiSwitchUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SetGuestWifiSwitchUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_WIFI_TIME_CONFIG)
  UseCase provideGetWifiTimeConfigUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetWifiTimeConfigUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_WIFI_ALL_SWITCH)
  UseCase provideSetWifiAllSwitchUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SetWifiAllSwitchUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.DELETE_WIFI_SWITCH)
  UseCase provideDeleteWifiSwitchUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteWifiSwitchUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_WIFI_DATA)
  UseCase provideSetWifiDataUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SetWifiDataUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ADD_VPN)
  UseCase provideAddVpnUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddVpnUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_VPN_LIST)
  UseCase provideGetVpnListUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetVpnListUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.POST_SPECIAL_ATTENTION)
  UseCase providePostSpecialAttentionUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new PostSpecialAttentionUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_SPECIAL_ATTENTION)
  UseCase provideGetSpecialAttentionUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetSpecialAttentionUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_QOS_INFO)
  UseCase provideGetQosInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetQosInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.POST_QOS_INFO)
  UseCase providePostQosInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new PostQosInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.DELETE_VPN)
  UseCase provideDeleteVpnUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteVpnUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.MODIFY_VPN)
  UseCase provideModifyVpnUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyVpnUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_VPN)
  UseCase provideSetVpnUseCasese(RouterRepository routerRepository, CloudRepository cloudRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SetVpnUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_WIRELESS_LIST)
  UseCase provideGetWirelessListUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetWirelessListUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.FIRST_CONFIG)
  UseCase provideFirstConfigUseCase(RouterRepository routerRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new FirstConfigUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_CONNECTED_WIFI)
  UseCase provideGetConnectedWifiUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetConnectedWifiUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_WIFI_DETAIL)
  UseCase provideGetWifiDetailUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetWifiDetailUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.POST_CONNECT_WIRELESS)
  UseCase provideConnectWirelssUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ConnectWirelssUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SET_WIFI_SWITCH)
  UseCase provideSetWifiSwitchUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SetWifiSwitchUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.POST_SAFE_INSPECTION)
  UseCase provideSafeInspectionUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SafeInspectionUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_BLACK_LIST)
  UseCase provideBlackListUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new BlackListUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.REMOVE_BLACK_MEM)
  UseCase provideRemoveBlackMemUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new RemoveBlackMemUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.BIND_ROUTER_TO_LOCK)
  UseCase provideBindRouterToLockUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new BindRouterToLockUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.QUERY_BIND_ROUTER_TO_LOCK)
  UseCase provideQueryBindRouterToLockUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new QueryBindRouterToLockUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_ANTI_QUESTION)
  UseCase provideGetAntiFritQuestionUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetAntiFritQuestionUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_FIRST_CONFIG)
  UseCase provideGetRouterFirstConfigUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetRouterFirstConfigUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_LOCK_VOLUME)
  UseCase provideGetLockVolume(CloudRepository cloudRepository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetLockVolume(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADJUST_LOCK_VOLUME)
  UseCase provideAdjustLockVolume(CloudRepository cloudRepository,
                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AdjustLockVolume(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.CHECK_LITE_VERSION)
  CheckLiteVersion provideCheckLiteVersion(CloudRepository cloudRepository,
      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new CheckLiteVersion(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UPDATE_LITE)
  UpdateLite provideUpdateLite(CloudRepository cloudRepository,
                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new UpdateLite(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_LITE_UPDATE)
  GetLiteUpdate provideGetLiteUpdate(CloudRepository cloudRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetLiteUpdate(cloudRepository, threadExecutor, postExecutionThread);
  }
}