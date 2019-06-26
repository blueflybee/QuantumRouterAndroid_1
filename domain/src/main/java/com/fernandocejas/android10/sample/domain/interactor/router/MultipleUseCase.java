package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;

import rx.Subscriber;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/04
 *     desc   :
 *     version: 1.0
 * </pre>
 */
abstract class MultipleUseCase extends UseCase {

  protected final RouterRepository routerRepository;
  protected final CloudRepository cloudRepository;

  public MultipleUseCase(ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread,
                         CloudRepository cloudRepository,
                         RouterRepository routerRepository) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
    this.routerRepository = routerRepository;
  }

  @Override
  public void execute(IRequest param, Subscriber UseCaseSubscriber) {
    super.subscription = buildUseCaseObservable(param).subscribe(UseCaseSubscriber);
  }
}
