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
import com.fernandocejas.android10.sample.domain.interactor.cloud.DeviceCountUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.QuestionListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetDevTreeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UploadLogcatUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UploadMsgIdUseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class MainModule {


  public MainModule() {
  }


  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DEVICE_COUNT)
  UseCase provideDeviceCountUseCase(CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new DeviceCountUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.DEV_TREE)
  UseCase provideGetDevTreeUseCase(CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetDevTreeUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UPLOAD_MSG_ID)
  UseCase provideUploadMsgIdUseCase(CloudRepository cloudRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UploadMsgIdUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.UPLOAD_LOGCAT)
  UseCase provideUploadLogcatUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UploadLogcatUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }


}