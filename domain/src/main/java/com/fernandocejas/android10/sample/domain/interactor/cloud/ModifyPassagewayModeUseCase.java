package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.ModifyPassagewayModeResponse;
import com.qtec.mapp.model.rsp.ModifyUnlockModeResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ModifyPassagewayModeUseCase extends UseCase {

  private final CloudRepository mCloudRepository;

  @Inject
  public ModifyPassagewayModeUseCase(CloudRepository mCloudRepository, ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.mCloudRepository = mCloudRepository;
  }


  @Override
  protected Observable<ModifyPassagewayModeResponse> buildUseCaseObservable(IRequest request) {
    return mCloudRepository.modifyPassagewayMode(request);
  }
}
