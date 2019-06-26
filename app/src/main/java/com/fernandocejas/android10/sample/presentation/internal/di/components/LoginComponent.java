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
import com.fernandocejas.android10.sample.presentation.internal.di.modules.LoginModule;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.login.forgetpwd.ResetPwdGetIdCodeActivity;
import com.fernandocejas.android10.sample.presentation.view.login.login.LoginActivity;
import com.fernandocejas.android10.sample.presentation.view.login.register.RegisterGetIdCodeActivity;
import com.fernandocejas.android10.sample.presentation.view.login.register.RegisterActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LoginModule.class})
public interface LoginComponent extends ActivityComponent {

  void inject(LoginActivity loginActivity);

  void inject(RegisterGetIdCodeActivity getIdCodeActivity);
  void inject(RegisterActivity registerActivity);


  void inject(ResetPwdGetIdCodeActivity resetPwdGetIdCodeActivity);
  void inject(ResetPwdActivity resetPwdActivity);

}
