package com.fernandocejas.android10.sample.domain.interactor.cloud;


import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.CheckLiteVersionResponse;

import javax.inject.Inject;

import rx.Observable;


/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-8
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CheckLiteVersion extends UseCase {

  private final CloudRepository cloudRepository;

  @Inject
  public CheckLiteVersion(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }

  @Override
  protected Observable<CheckLiteVersionResponse> buildUseCaseObservable(IRequest param) {
    return cloudRepository.checkLiteVersion(param);
  }
}

