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
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeleteLockUserUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetLockUsersUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetUserRoleUseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class LockUserModule {


  public LockUserModule() {
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_USER_ROLE)
  UseCase provideGetUserRoleUseCase(CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetUserRoleUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_LOCK_USERS)
  UseCase provideGetLockUsersUseCase(CloudRepository cloudRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetLockUsersUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DELETE_LOCK_USER)
  UseCase provideDeleteLockUserUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteLockUserUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

}