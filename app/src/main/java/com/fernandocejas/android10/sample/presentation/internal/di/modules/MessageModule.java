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
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeleteMsgUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DealInvitationUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetMsgListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetMsgShareListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.MsgCountUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.MsgReadUseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class MessageModule {


    public MessageModule() {
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.DEAL_INVITATION)
    UseCase provideDealInvitationUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new DealInvitationUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.MSG_LIST)
    UseCase provideGetMsgListUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new GetMsgListUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.MSG_SHARE_LIST)
    UseCase provideGetMsgShareListUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new GetMsgShareListUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.DELETE_MSG)
    UseCase provideDeleteMsgUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new DeleteMsgUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.GET_MSG_COUNT)
    UseCase provideMsgCountUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new MsgCountUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.SET_MSG_READ)
    UseCase provideMsgReadUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new MsgReadUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

}