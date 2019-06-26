package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.router.model.rsp.RemoteLockOperationResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 *
 *
 * @author shaojun
 * @name LoginUseCase
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-9
 */
public class RemoteLockOperationUseCase extends UseCase {

  private final CloudRepository cloudRepository;

  @Inject
  public RemoteLockOperationUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }

  @Override
  protected Observable<RemoteLockOperationResponse> buildUseCaseObservable(IRequest request) {
    return cloudRepository.remoteLockOptTrans(request);
  }
}
