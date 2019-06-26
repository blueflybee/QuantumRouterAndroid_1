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
import com.fernandocejas.android10.sample.presentation.internal.di.modules.KeyModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LockUserModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MainModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MessageModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.RouterModule;
import com.fernandocejas.android10.sample.presentation.view.device.lock.activity.BindRouterToLockActivity;
import com.fernandocejas.android10.sample.presentation.view.device.lock.adapter.LockUserManageAdapter;
import com.fernandocejas.android10.sample.presentation.view.main.MainActivity;
import com.fernandocejas.android10.sample.presentation.view.device.fragment.DeviceFragment;
import com.fernandocejas.android10.sample.presentation.view.mine.MineFragment;
import com.fernandocejas.android10.sample.presentation.view.safe.SafeFragment;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MainModule.class, RouterModule.class, KeyModule.class, MineModule.class, MessageModule.class, LockUserModule.class})
public interface MainComponent extends ActivityComponent {

  void inject(MineFragment mineFragment);
  void inject(DeviceFragment deviceFragment);
  void inject(SafeFragment safeFragment);
  void inject(MainActivity mainActivity);
  void inject(BindRouterToLockActivity bindRouterToLockActivity);

}
