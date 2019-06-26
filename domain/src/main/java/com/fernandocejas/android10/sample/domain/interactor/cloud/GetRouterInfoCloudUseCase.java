package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;
import com.qtec.mapp.model.rsp.LoginResponse;

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
public class GetRouterInfoCloudUseCase extends UseCase {

  private final CloudRepository cloudRepository;

  @Inject public GetRouterInfoCloudUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }

  @Override protected Observable<GetRouterInfoCloudResponse> buildUseCaseObservable(IRequest param) {
    return cloudRepository.getRouterInfoCloud(param);
  }
}
