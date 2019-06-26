package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.DeleteFingerResponse;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;

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
public class DeleteFingerInfoUseCase extends MultipleUseCase {
  Observable<DeleteFingerResponse> result;
  @Inject
  public DeleteFingerInfoUseCase(RouterRepository routerRepository,
                                 CloudRepository cloudRepository,
                                 ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<DeleteFingerResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    Observable<DeleteFingerResponse> routerObservable =
        routerRepository.deleteFingerInfo(multiEncryptInfo.getRouterEncryptInfo())
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    Observable<DeleteFingerResponse> cloudObservable =
        cloudRepository.deleteFingerInfoTrans(multiEncryptInfo.getCloudEncryptInfo())
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
