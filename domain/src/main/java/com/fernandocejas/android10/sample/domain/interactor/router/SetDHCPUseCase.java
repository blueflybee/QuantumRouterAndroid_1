package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetDHCPRequest;
import com.qtec.router.model.rsp.SetDHCPResponse;

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
public class SetDHCPUseCase extends MultipleUseCase {
  Observable<SetDHCPResponse> result;
  @Inject
  public SetDHCPUseCase(RouterRepository routerRepository,
                        CloudRepository cloudRepository,
                        ThreadExecutor threadExecutor,
                        PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<SetDHCPResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    IRequest routerEncryptInfo = multiEncryptInfo.getRouterEncryptInfo();
    Observable<SetDHCPResponse> routerObservable =
        routerRepository.setDHCP(routerEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    IRequest cloudEncryptInfo = multiEncryptInfo.getCloudEncryptInfo();
    Observable<SetDHCPResponse> cloudObservable =
        cloudRepository.setDHCPTrans(cloudEncryptInfo)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());
    int configed = ((SetDHCPRequest) ((QtecEncryptInfo) routerEncryptInfo).getData()).getConfiged();

    if (configed == 1) {
      //return Observable.concatDelayError(routerObservable, cloudObservable);

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
