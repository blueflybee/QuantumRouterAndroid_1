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
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockUserModule;
import com.fernandocejas.android10.sample.presentation.view.device.camera.activity.AboutCameraActivity;
import com.fernandocejas.android10.sample.presentation.view.device.camera.activity.CameraMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.camera.activity.CameraSettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.AboutCatEyeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.CatEyeSettingActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.ConnectLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.DoorBellRecordListActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.InputCatEyeNumActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.UnbindCatEyeActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CaptureActivity;
import com.fernandocejas.android10.sample.presentation.view.device.cateye.activity.CatEyeMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.addIntelDev.AddIntelDevVerifyActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.AddFingerActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.FingerManagerActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.fragment.FingerManagerFragment;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.ExceptionWarnMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.LockUseNoteMoreActivity;
import com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock.SetFingerNameActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.LockMainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.RemoteUnlockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.LockSettingFragment;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.LockStatusFragment;
import com.fernandocejas.android10.sample.presentation.view.device.lock.fragment.SecurityManageFragment;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, DeviceModule.class, RouterModule.class, LockUserModule.class})
public interface DeviceComponent extends ActivityComponent {

    void inject(AddIntelDevVerifyActivity addIntelDevVerifyActivity);
    void inject(LockMainActivity lockMainActivity);
    void inject(LockStatusFragment lockStatusFragment);
    void inject(LockSettingFragment lockSettingFragment);
    void inject(FingerManagerFragment fingerManagerFragment);
    void inject(FingerManagerActivity fingerManagerActivity);
    void inject(SecurityManageFragment securityManageFragment);
    void inject(LockUseNoteMoreActivity lockUseNoteMoreActivity);
    void inject(ExceptionWarnMoreActivity exceptionWarnMoreActivity);
    void inject(SetFingerNameActivity setFingerNameActivity);

    void inject(AddFingerActivity addFingerActivity);
    void inject(ConnectLockActivity connectLockActivity);
    void inject(CatEyeSettingActivity catEyeSettingActivity);
    void inject(UnbindCatEyeActivity unbindCatEyeActivity);
    void inject(CaptureActivity captureActivity);
    void inject(InputCatEyeNumActivity inputCatEyeNumActivity);
    void inject(CatEyeMainActivity catEyeMainActivity);
    void inject(CameraSettingActivity cameraSettingActivity);
    void inject(RemoteUnlockActivity remoteUnlockActivity);
    void inject(DoorBellRecordListActivity doorBellRecordListActivity);
    void inject(CameraMainActivity cameraMainActivity);
    void inject(AboutCameraActivity aboutCameraActivity);
    void inject(AboutCatEyeActivity aboutCatEyeActivity);

}
