package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.req.SetPPPOERequest;
import com.qtec.router.model.rsp.SetDHCPResponse;
import com.qtec.router.model.rsp.SetPPPOEResponse;

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
public class SetPPPOEUseCase extends MultipleUseCase {
  Observable<SetPPPOEResponse> result;
  @Inject
  public SetPPPOEUseCase(RouterRepository routerRepository,
                         CloudRepository cloudRepository,
                         ThreadExecutor threadExecutor,
                         PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<SetPPPOEResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    IRequest routerEncryptInfo = multiEncryptInfo.getRouterEncryptInfo();
    Observable<SetPPPOEResponse> routerObservable =
        routerRepository.setPPPOE(routerEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    IRequest cloudEncryptInfo = multiEncryptInfo.getCloudEncryptInfo();
    Observable<SetPPPOEResponse> cloudObservable =
        cloudRepository.setPPPOETrans(cloudEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    int configed = ((SetPPPOERequest) ((QtecEncryptInfo) routerEncryptInfo).getData()).getConfiged();

    if (configed == 1) {

      if(multiEncryptInfo.getRouterDirectConnect()){
        result = Observable.concatDelayError(routerObservable, cloudObservable);
      }else {
        result = cloudObservable;
      }

      return  result;
    } else {
      return routerObservable;
    }
  }
}
