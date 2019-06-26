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
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.AddLockDoorCardUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.AddLockFpUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.AddLockPwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.AddTempPwd;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CheckLockVersionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CloudUnbindRouterLock;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CommitAddRouterInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeleteLockDoorCardUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeleteLockFpUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeleteLockPwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetPassagewayModeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetRouterGroupsUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetTempPwd;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetUnlockModeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetUsersOfLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.LockFactoryResetUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyLockDoorCardNameUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyLockFpNameUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyLockPwdNameUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyPassagewayModeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyUnlockModeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.QueryTempPwd;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UpdateLockVersionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.QueryBindRouterToLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.UnbindRouterToLockUseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class LockModule {


  public LockModule() {
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_LOCK_PWD_NAME)
  UseCase provideModifyLockPwdNameUseCase(CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyLockPwdNameUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DELETE_LOCK_PWD)
  UseCase provideDeleteLockPwdUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteLockPwdUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_LOCK_FP_NAME)
  UseCase provideModifyLockFpNameUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyLockFpNameUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DELETE_LOCK_FP)
  UseCase provideDeleteLockFpUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteLockFpUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADD_LOCK_PWD)
  UseCase provideAddLockPwdUseCase(CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddLockPwdUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADD_LOCK_FP)
  UseCase provideAddLockFpUseCase(CloudRepository cloudRepository,
                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddLockFpUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.UNBIND_ROUTER_TO_LOCK)
  UseCase provideBindRouterToLockUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UnbindRouterToLockUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.CLOUD_UNBIND_ROUTER_LOCK)
  CloudUnbindRouterLock provideCloudUnbindRouterLock(RouterRepository routerRepository, CloudRepository cloudRepository,
                                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new CloudUnbindRouterLock(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_USERS_OF_LOCK)
  UseCase provideGetUsersOfLockUseCase(CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetUsersOfLockUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.CHECK_LOCK_VERSION)
  UseCase provideCheckLockVersionUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new CheckLockVersionUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UPDATE_LOCK_VERSION)
  UseCase provideUpdateLockVersionUseCase(CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new UpdateLockVersionUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_UNLOCK_MODE)
  UseCase provideGetUnlockModeUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetUnlockModeUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_PASSAGEWAY_MODE)
  UseCase provideGetPassagewayModeUseCase(CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetPassagewayModeUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_UNLOCK_MODE)
  UseCase provideModifyUnlockModeUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyUnlockModeUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_PASSAGEWAY_MODE)
  UseCase provideModifyPassagewayUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyPassagewayModeUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DELETE_LOCK_DOOR_CARD)
  UseCase provideDeleteLockDoorCardUseCase(CloudRepository cloudRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteLockDoorCardUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_LOCK_DOOR_CARD_NAME)
  UseCase provideModifyLockDoorCardNameUseCase(CloudRepository cloudRepository,
                                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyLockDoorCardNameUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADD_LOCK_DOOR_CARD)
  UseCase provideAddLockDoorCardUseCase(CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddLockDoorCardUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.LOCK_FACTORY_RESET)
  UseCase provideLockFactoryResetUseCase(CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new LockFactoryResetUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADD_TEMP_PWD)
  UseCase provideAddTempPwd(CloudRepository cloudRepository,
                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddTempPwd(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.QUERY_TEMP_PWD)
  UseCase provideQueryTempPwd(CloudRepository cloudRepository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new QueryTempPwd(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_TEMP_PWD)
  UseCase provideGetTempPwd(CloudRepository cloudRepository,
                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetTempPwd(cloudRepository, threadExecutor, postExecutionThread);
  }

}