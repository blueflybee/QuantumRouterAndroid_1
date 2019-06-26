package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.GetLiteUpdateResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-8
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class GetLiteUpdate extends UseCase {

  private final CloudRepository cloudRepository;

  @Inject
  public GetLiteUpdate(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }

  @Override
  protected Observable<GetLiteUpdateResponse> buildUseCaseObservable(final IRequest param) {
    return Observable.interval(4, TimeUnit.SECONDS)
        .flatMap(new Func1<Long, Observable<GetLiteUpdateResponse>>() {
          @Override
          public Observable<GetLiteUpdateResponse> call(Long aLong) {
            return cloudRepository.getLiteUpdate(param);
          }
        });
  }
}

