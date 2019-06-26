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
import com.fernandocejas.android10.sample.domain.interactor.SetAllDoorBeelMsgIsReadUseCase;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ConnectLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeleteDoorBeelMsgUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetDoorBeelMsgDetailUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetDoorBeelMsgListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetDoorCardsUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetFingerprintsUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetLockListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetPasswordsUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.LockExceptionWarnListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.LockNoteListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.QueryCatLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.QueryLockedCatUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UnbindCatLockUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UploadDevicePwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AddFingerInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.AddIntelDevVerifyUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.DeleteFingerInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.LockStatusUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.ModifyFingerNameUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.QueryFingerInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.RemoteLockOperationUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.SearchIntelDevNotifyUseCase;
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
public class DeviceModule {


  public DeviceModule() {
  }



  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.SEARCH_INTEL_DEV_NOTIFY)
  UseCase provideSearchIntelDevNotifyUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new SearchIntelDevNotifyUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ADD_INTEL_DEV_VERIFY)
  UseCase provideAddIntelDevVerifyUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                          ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddIntelDevVerifyUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.LOCK_STATUS)
  UseCase provideLockStatusUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new LockStatusUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.LOCK_NOTE_LIST)
  UseCase provideLockNoteListUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new LockNoteListUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.LOCK_EXCEPTION_WARNING_LIST)
  UseCase provideLockExceptionWarnListUseCase(CloudRepository cloudRepository,
                                              ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new LockExceptionWarnListUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.ADD_FINGER_INFO)
  UseCase provideAddFingerInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new AddFingerInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.MODIFY_FINGER_NAME)
  UseCase provideModifyFingerNameUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new ModifyFingerNameUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.DELETE_FINGER_INFO)
  UseCase provideDeleteFingerInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                         ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new DeleteFingerInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  //中转
  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.REMOTE_LOCK_OPERATION)
  UseCase provideRemoteUnlockUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new RemoteLockOperationUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.QUERY_FINGER_INFO)
  UseCase provideQueryFingerInfoUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new QueryFingerInfoUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_FINGER_PRINTS)
  UseCase provideGetFingerprintsUseCase(CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetFingerprintsUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_PASSWORDS)
  UseCase provideGetPasswordsUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetPasswordsUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_DOOR_CARDS)
  UseCase provideGetDoorCardsUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new GetDoorCardsUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_LOCK_LIST)
  UseCase provideGetLockListUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetLockListUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.CONNECT_LOCK)
  UseCase provideConnectLockUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ConnectLockUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.QUERY_LOCKED_CAT)
  UseCase provideQueryLockedCatUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new QueryLockedCatUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.QUERY_CAT_LOCK)
  UseCase provideQueryCatLockUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new QueryCatLockUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_DOOR_BEER_MSG_LIST)
  UseCase provideGetDoorBeelMsgListUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetDoorBeelMsgListUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.GET_DOOR_BEER_MSG_DETAIL)
  UseCase provideGetDoorBeelMsgDetailUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetDoorBeelMsgDetailUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DELETE_DOOR_BEER_MSG)
  UseCase provideDeleteDoorBeelMsgUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new DeleteDoorBeelMsgUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.SET_ALL_DOOR_BEER_MSG_READ)
  UseCase provideSetAllDoorBeelMsgIsReadUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SetAllDoorBeelMsgIsReadUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UNBIND_CAT_LOCK)
  UseCase provideUnbindCatLockUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UnbindCatLockUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }


  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UPLOAD_DEVICE_PWD)
  UseCase provideUploadDevicePwdUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UploadDevicePwdUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }


}