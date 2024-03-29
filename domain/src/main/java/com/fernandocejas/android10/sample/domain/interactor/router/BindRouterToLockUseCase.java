package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class BindRouterToLockUseCase extends MultipleUseCase {
  @Inject
  public BindRouterToLockUseCase(RouterRepository routerRepository,
                             CloudRepository cloudRepository,
                             ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<BindRouterToLockResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    Observable<BindRouterToLockResponse> routerObservable =
        routerRepository.bindRouterToLock(multiEncryptInfo.getRouterEncryptInfo())
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    Observable<BindRouterToLockResponse> cloudObservable =
        cloudRepository.bindRouterToLockTrans(multiEncryptInfo.getCloudEncryptInfo())
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

//    return Observable.concatDelayError(routerObservable, cloudObservable);

        return cloudObservable;
  }
}
