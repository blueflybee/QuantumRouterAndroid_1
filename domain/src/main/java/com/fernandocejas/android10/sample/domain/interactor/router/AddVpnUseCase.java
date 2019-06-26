package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.AddVpnResponse;

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
public class AddVpnUseCase extends MultipleUseCase {
  Observable<AddVpnResponse> result;
  @Inject
  public AddVpnUseCase(RouterRepository routerRepository,
                       CloudRepository cloudRepository,
                       ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<AddVpnResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    Observable<AddVpnResponse> routerObservable =
        routerRepository.addVpn(multiEncryptInfo.getRouterEncryptInfo())
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    Observable<AddVpnResponse> cloudObservable =
        cloudRepository.addVpnTrans(multiEncryptInfo.getCloudEncryptInfo())
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

   // return Observable.concatDelayError(routerObservable, cloudObservable);
    if(multiEncryptInfo.getRouterDirectConnect()){
      result = Observable.concatDelayError(routerObservable, cloudObservable);
    }else {
      result = cloudObservable;
    }

    return  result;
//    return Observable.mergeDelayError(routerObservable, cloudObservable).first();
  }

}
