package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.GetNetModeResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RestartRouterUseCase extends MultipleUseCase {
  Observable<RestartRouterResponse> result;
  @Inject
  public RestartRouterUseCase(RouterRepository routerRepository,
                              CloudRepository cloudRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<RestartRouterResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    IRequest routerEncryptInfo = multiEncryptInfo.getRouterEncryptInfo();
    Observable<RestartRouterResponse> routerObservable =
        routerRepository.restartRouter(routerEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    IRequest cloudEncryptInfo = multiEncryptInfo.getCloudEncryptInfo();
    Observable<RestartRouterResponse> cloudObservable =
        cloudRepository.restartRouterTrans(cloudEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    if(multiEncryptInfo.getRouterDirectConnect()){
      result = Observable.concatDelayError(routerObservable, cloudObservable);
    }else {
      result = cloudObservable;
    }

    return  result;
  }
}
