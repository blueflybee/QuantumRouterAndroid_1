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
import com.fernandocejas.android10.sample.presentation.internal.di.modules.DeviceModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockUserModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.LiteGatewayMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity.PairWifiActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockDoorCardActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockFpActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.AddLockPwdAgainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.CreateDoorCardNameActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.CreateFingerprintNameActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockBindRouterInfoActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockFactoryResetActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockInjectKeyActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockCheckVersionActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.PassagewayModeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserManageActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockUserManageDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ModifyLockDoorCardActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ModifyLockFpActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.ModifyLockPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.SetLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.TempPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.UnlockModeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.UnlockModeActivityForV15;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LockModule.class, RouterModule.class, DeviceModule.class, LockUserModule.class})
public interface LockComponent extends ActivityComponent {

  void inject(AddLockActivity addLockActivity);
  void inject(LockInjectKeyActivity lockInjectKeyActivity);
  void inject(ModifyLockPwdActivity modifyLockPwdActivity);
  void inject(ModifyLockFpActivity modifyLockFpActivity);
  void inject(AddLockPwdAgainActivity addLockPwdAgainActivity);
  void inject(AddLockFpActivity addLockFpActivity);
  void inject(CreateFingerprintNameActivity createFingerprintNameActivity);
  void inject(LockBindRouterInfoActivity lockBindRouterInfoActivity);
  void inject(SetLockActivity setLockActivity);
  void inject(LockCheckVersionActivity lockCheckVersionActivity);
  void inject(UnlockModeActivity unlockModeActivity);
  void inject(UnlockModeActivityForV15 unlockModeActivityForV15);
  void inject(AddLockDoorCardActivity addLockDoorCardActivity);
  void inject(CreateDoorCardNameActivity createDoorCardNameActivity);
  void inject(ModifyLockDoorCardActivity modifyLockDoorCardActivity);
  void inject(LockUserManageActivity lockUserManageActivity);
  void inject(LockUserManageDetailActivity lockUserManageActivity);
  void inject(LockFactoryResetActivity lockFactoryResetActivity);
  void inject(PassagewayModeActivity passagewayModeActivity);
  void inject(PairWifiActivity pairWifiActivity);
  void inject(LiteGatewayMainActivity liteGatewayMainActivity);

  void inject(TempPwdActivity tempPwdActivity);
}
