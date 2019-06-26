/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.android10.sample.presentation.internal.di.components;

import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.activity.ModifyPropertyActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.SearchLanCatActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.AboutHardwareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.keystore.KeyInvalidActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.CheckLiteVersionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.LiteGatewaySettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.LiteUpdateActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.AddRouterVerifyActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.addrouter.SearchRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.FirstConfigDHCPFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.FirstConfigPPPOEFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.FirstConfigStaticFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig.FirstConfigWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.AddVPNActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.AdminNewPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.AdminPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.DHCPActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.NetModeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.PPPOEActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RestartRouterActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterDesActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterGroupActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterInfoActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.RouterSettingFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.StaticIpActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.UpdateFirmwareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.UpdatingFirmwareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.VPNActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.setting.WifiSettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.status.BlackListActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.status.RouterMemDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.status.RouterStatusFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.AntiFritNetMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.AuthedDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.AuthedFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.SetNetModeFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.SetQuestionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.WaitAuthDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet.WaitingAuthFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure.BandSpeedMeasureActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare.ChildCareActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare.ChildCareDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection.SafeInspectionDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment.RouterToolsFragment;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi.GuestWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband.IntelligentBandActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.manageDevice.ModifyLockNameActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.FormatingDiskActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore.RemoteMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation.SignalRegulationActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention.AddAttentionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention.SpecialAttentionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch.AddTimeIntervalActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch.WifiTimeSwitchActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.WirelessRelayActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.WirelessRelayDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.WirelessRelayOtherActivity;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay.WirelessRelayPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.safe.SafeTestActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RouterModule.class})
public interface RouterComponent extends ActivityComponent {

  void inject(AddRouterActivity addRouterActivity);
  void inject(SearchRouterActivity searchRouterActivity);
  void inject(AddRouterVerifyActivity addRouterVerifyActivity);

  void inject(RouterStatusFragment routerStatusFragment);
  void inject(RouterSettingFragment routerSettingFragment);
  void inject(RouterToolsFragment routerToolsFragment);

  void inject(ModifyLockNameActivity modifyLockNameActivity);

  void inject(AboutHardwareActivity aboutHardwareActivity);
  void inject(RouterInfoActivity routerInfoActivity);

  void inject(ModifyPropertyActivity modifyPropertyActivity);
  void inject(RouterDesActivity routerDesActivity);
  void inject(RouterGroupActivity routerGroupActivity);
  void inject(DHCPActivity dhcpActivity);
  void inject(PPPOEActivity pppoeActivity);
  void inject(StaticIpActivity staticIpActivity);
  void inject(NetModeActivity netModeActivity);
  void inject(UpdateFirmwareActivity updateFirmwareActivity);
  void inject(UpdatingFirmwareActivity updatingFirmwareActivity);
  void inject(WifiSettingActivity wifiSettingActivity);
  void inject(RestartRouterActivity restartRouterActivity);
  void inject(AdminPwdActivity adminPwdActivity);
  void inject(AdminNewPwdActivity adminNewPwdActivity);

  void inject(WirelessRelayActivity wirelessRelayActivity);
  void inject(WirelessRelayDetailActivity wirelessRelayActivity);
  void inject(WirelessRelayPwdActivity wirelessRelayPwdActivity);
  void inject(WirelessRelayOtherActivity wirelessRelayOtherActivity);

  void inject(RemoteMainActivity remoteMainActivity);
  void inject(SignalRegulationActivity signalRegulationActivity);
  void inject(BandSpeedMeasureActivity bandSpeedMeasureActivity);
  void inject(SetQuestionActivity setQuestionActivity);
  void inject(WaitingAuthFragment waitingAuthFragment);
  void inject(AuthedFragment authedFragment);
  void inject(WaitAuthDetailActivity waitAuthDetailActivity);
  void inject(AuthedDetailActivity authedDetailActivity);

  void inject(FirstConfigPPPOEFragment firstConfigPPPOEFragment);
  void inject(FirstConfigDHCPFragment modifyLockNameActivity);
  void inject(FirstConfigStaticFragment firstConfigStaticFragment);
  void inject(FirstConfigWifiActivity firstConfigWifiActivity);

  void inject(SetNetModeFragment setNetModeFragment);
  void inject(AntiFritNetMainActivity antiFritNetMainActivity);
  void inject(ChildCareActivity childCareActivity);
  void inject(ChildCareDetailActivity childCareDetailActivity);
  void inject(GuestWifiActivity guestWifiActivity);
  void inject(WifiTimeSwitchActivity wifiTimeSwitchActivity);
  void inject(AddTimeIntervalActivity addTimeIntervalActivity);

  void inject(AddVPNActivity addVPNActivity);
  void inject(VPNActivity vpnActivity);
  void inject(AddAttentionActivity addAttentionActivity);
  void inject(SpecialAttentionActivity specialAttentionActivity);
  void inject(IntelligentBandActivity intelligentBandActivity);
  void inject(SafeInspectionDetailActivity safeInspectionDetailActivity);
  void inject(BlackListActivity blackListActivity);
  void inject(RouterMemDetailActivity routerMemDetailActivity);

  void inject(SafeTestActivity safeTestActivity);

  void inject(SearchLanCatActivity searchLanCatActivity);
  void inject(FormatingDiskActivity formatingDiskActivity);

  void inject(KeyInvalidActivity keyInvalidActivity);

  //lite gateway
  void inject(LiteGatewaySettingActivity liteGatewaySettingActivity);

  void inject(CheckLiteVersionActivity checkLiteVersionActivity);

  void inject(LiteUpdateActivity liteUpdateActivity);

}
