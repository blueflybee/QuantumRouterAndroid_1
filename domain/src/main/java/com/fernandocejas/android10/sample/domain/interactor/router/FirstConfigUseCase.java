package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.router.model.rsp.FirstConfigResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class FirstConfigUseCase extends UseCase {

  private final RouterRepository routerRepository;

  @Inject
  public FirstConfigUseCase(RouterRepository routerRepository, ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.routerRepository = routerRepository;
  }


  @Override
  protected Observable<FirstConfigResponse> buildUseCaseObservable(IRequest request) {
    return routerRepository.firstConfig(request);
  }
}