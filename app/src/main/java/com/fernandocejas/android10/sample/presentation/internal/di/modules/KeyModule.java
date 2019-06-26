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

import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.FirstGetKeyUseCase;
import com.fernandocejas.android10.sample.domain.interactor.router.GetKeyUseCase;
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
public class KeyModule {


  public KeyModule() {
  }


  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.FIRST_GET_KEY)
  UseCase provideFirstGetKeyUseCase(RouterRepository routerRepository,
                                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new FirstGetKeyUseCase(routerRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(RouterUseCaseComm.GET_KEY)
  UseCase provideGetKeyUseCase(RouterRepository routerRepository, CloudRepository cloudRepository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new GetKeyUseCase(routerRepository, cloudRepository, threadExecutor, postExecutionThread);
  }

}