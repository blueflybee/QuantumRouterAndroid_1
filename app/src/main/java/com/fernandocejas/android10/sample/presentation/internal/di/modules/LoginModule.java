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
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.LoginUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.RegisterUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ResetPwdGetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ResetPwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

import javax.inject.Named;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class LoginModule {


    public LoginModule() {
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.LOGIN)
    UseCase provideLoginUseCase(CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new LoginUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.GET_ID_CODE)
    UseCase provideGetIdCodeUseCase(CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new GetIdCodeUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.REGISTER)
    UseCase provideRegisterUseCase(CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new RegisterUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.RESET_PWD_GET_ID_CODE)
    UseCase provideResetPwdGetIdCodeUseCase(CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

        return new ResetPwdGetIdCodeUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named(CloudUseCaseComm.RESET_PWD)
    UseCase provideResetPwdUseCase(CloudRepository cloudRepository,
                                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ResetPwdUseCase(cloudRepository, threadExecutor, postExecutionThread);
    }


}