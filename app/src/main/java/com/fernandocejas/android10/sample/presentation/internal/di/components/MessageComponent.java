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
import com.fernandocejas.android10.sample.presentation.internal.di.modules.MessageModule;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.AcceptInvitationActivity;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.DealWarningInfoActivity;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.MessageCenterActivity;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.OtherMessageListActivity;
import com.fernandocejas.android10.sample.presentation.view.message.receiver.ShareMessageListActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MessageModule.class})
public interface MessageComponent extends ActivityComponent {

    void inject(OtherMessageListActivity otherMessageListActivity);
    void inject(ShareMessageListActivity shareMessageListActivity);
    void inject(AcceptInvitationActivity acceptInvitationActivity);
    void inject(DealWarningInfoActivity dealWarningInfoActivity);
    void inject(MessageCenterActivity messageCenterActivity);

}
