package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.GetFingerprintsResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   : 添加网关成功后提交网关信息至云端
 *     version: 1.0
 * </pre>
 */
public class GetFingerprintsUseCase extends UseCase {

  private final CloudRepository cloudRepository;

  @Inject
  public GetFingerprintsUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.cloudRepository = cloudRepository;
  }


  @Override
  protected Observable<List<GetFingerprintsResponse>> buildUseCaseObservable(IRequest request) {
    return cloudRepository.getFingerprints(request);
  }
}
