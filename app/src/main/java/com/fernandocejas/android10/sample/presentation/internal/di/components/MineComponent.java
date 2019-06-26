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
package com.fernandocejas.android10.sample.presentation.internal.di.components;

import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.ActivityModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MineModule;
import com.fernandocejas.android10.sample.presentation.internal.di.modules.OssModule;
import com.fernandocejas.android10.sample.presentation.view.login.register.PersonalProfileActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.aboutus.AboutUsActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.AdviceFeedBackActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.AllQuestionActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.FeedBackAdviceActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.MyAdviceDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback.MyFeedBackActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.MemDetailActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.RouterListActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.deviceshare.ShareMemberListActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.ModifyPwdActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.ModifyPwdGetIdCodeActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.MyInfoActivity;
import com.fernandocejas.android10.sample.presentation.view.mine.myinfo.VersionInfoActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MineModule.class, OssModule.class})
public interface MineComponent extends ActivityComponent {

  void inject(MyInfoActivity myInfoActivity);

  void inject(ModifyPwdGetIdCodeActivity modifyPwdGetIdCodeActivity);

  void inject(ModifyPwdActivity modifyPwdActivity);

  void inject(AboutUsActivity aboutUsActivity);

  void inject(AdviceFeedBackActivity adviceFeedBackActivity);

  void inject(MyFeedBackActivity myFeedBackActivity);

  void inject(FeedBackAdviceActivity feedBackAdviceActivity);

  void inject(MyAdviceDetailActivity myAdviceDetailActivity);

  void inject(RouterListActivity mRouterListActivity);

  void inject(ShareMemberListActivity mShareMemberListActivity);

  void inject(MemDetailActivity memDetailActivity);

  void inject(VersionInfoActivity versionInfoActivity);

  void inject(AllQuestionActivity allQuestionActivity);
  void inject(PersonalProfileActivity personalProfileActivity);

}
