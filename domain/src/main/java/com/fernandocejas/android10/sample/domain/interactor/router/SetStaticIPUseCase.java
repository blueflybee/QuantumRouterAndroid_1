package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.req.SetStaticIPRequest;
import com.qtec.router.model.rsp.SetPPPOEResponse;
import com.qtec.router.model.rsp.SetStaticIPResponse;

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
public class SetStaticIPUseCase extends MultipleUseCase {
  Observable<SetStaticIPResponse> result;
  @Inject
  public SetStaticIPUseCase(RouterRepository routerRepository,
                            CloudRepository cloudRepository,
                            ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<SetStaticIPResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    IRequest routerEncryptInfo = multiEncryptInfo.getRouterEncryptInfo();
    Observable<SetStaticIPResponse> routerObservable =
        routerRepository.setStaticIP(routerEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    IRequest cloudEncryptInfo = multiEncryptInfo.getCloudEncryptInfo();
    Observable<SetStaticIPResponse> cloudObservable =
        cloudRepository.setStaticIPTrans(cloudEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    int configed = ((SetStaticIPRequest) ((QtecEncryptInfo) routerEncryptInfo).getData()).getConfiged();

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
