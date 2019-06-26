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
import com.fernandocejas.android10.sample.domain.interactor.cloud.AdviceDetailUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CheckAppVersionUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.FeedBackUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.InvitationUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.LogoutUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.MemRemarkNameUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyPwdGetIdCodeUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyPwdUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.ModifyUserInfoUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.MyAdviceUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.QuestionDetailUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.QuestionListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.SharedMemListUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UpdateFeedBackUseCase;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UserAgreementUseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UserSerectAgreementUseCase;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class MineModule {


  public MineModule() {
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_USER_INFO)
  UseCase provideModifyUserInfoUseCase(CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ModifyUserInfoUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.LOGOUT)
  UseCase provideLogoutUseCase(CloudRepository cloudRepository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new LogoutUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_PWD_GET_ID_CODE)
  UseCase provideModifyPwdGetIdCodeUseCase(CloudRepository cloudRepository,
                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ModifyPwdGetIdCodeUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MODIFY_PWD)
  UseCase provideModifyPwdUseCase(CloudRepository cloudRepository,
                                  ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new ModifyPwdUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.USER_AGREEMENT)
  UseCase provideUserAgreementUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UserAgreementUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.SHARE_MEM_REMARK_NAME)
  UseCase provideMemRemarkNameUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new MemRemarkNameUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.USER_SECRET_AGREEMENT)
  UseCase provideUserSerectAgreementUseCase(CloudRepository cloudRepository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UserSerectAgreementUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.QUESTION_DETAIL)
  UseCase provideQuestionDetailUseCase(CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new QuestionDetailUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.MY_ADVICE)
  UseCase provideMyAdviceUseCase(CloudRepository cloudRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new MyAdviceUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADVICE_DETAIL)
  UseCase provideAdviceDetailUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new AdviceDetailUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADVICE_FEED_BACK)
  UseCase provideFeedBackUseCase(CloudRepository cloudRepository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new FeedBackUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.ADVICE_UPDATE_FEED_BACK)
  UseCase provideUpdateFeedBackUseCase(CloudRepository cloudRepository,
                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new UpdateFeedBackUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.SHARE_MEMBER_LIST)
  UseCase provideSharedMemListUseCase(CloudRepository cloudRepository,
                                      ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new SharedMemListUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.INVITATION)
  UseCase provideInvitationUseCase(CloudRepository cloudRepository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new InvitationUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.QUESTION_LIST)
  UseCase provideQuestionListUseCase(CloudRepository cloudRepository,
                                     ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {

    return new QuestionListUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named(CloudUseCaseComm.CHECK_APP_VERSION)
  UseCase provideCheckAppVersionUseCase(CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
    return new CheckAppVersionUseCase(cloudRepository, threadExecutor, postExecutionThread);
  }
}