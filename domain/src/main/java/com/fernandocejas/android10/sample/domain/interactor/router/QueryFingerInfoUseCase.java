package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.QueryFingerInfoResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 登录用例
 *
 * @author shaojun
 * @name LoginUseCase
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-9
 */
public class QueryFingerInfoUseCase extends MultipleUseCase {
  Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> result;
  @Inject
  public QueryFingerInfoUseCase(RouterRepository routerRepository,
                                CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> routerObservable =
        routerRepository.queryFingerInfo(multiEncryptInfo.getRouterEncryptInfo())
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    Observable<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>> cloudObservable =
        cloudRepository.queryFingerInfoTrans(multiEncryptInfo.getCloudEncryptInfo())
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
