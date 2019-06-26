package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.GetFirmwareUpdateStatusResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
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
public class GetFirmwareUpdateStatusUseCase extends MultipleUseCase {
  Observable<GetFirmwareUpdateStatusResponse> result;

  @Inject
  public GetFirmwareUpdateStatusUseCase(RouterRepository routerRepository,
                                        CloudRepository cloudRepository,
                                        ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<GetFirmwareUpdateStatusResponse> buildUseCaseObservable(IRequest request) {
    final QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;


    return Observable.interval(4, TimeUnit.SECONDS)
        .flatMap(new Func1<Long, Observable<GetFirmwareUpdateStatusResponse>>() {
          @Override
          public Observable<GetFirmwareUpdateStatusResponse> call(Long aLong) {

            IRequest routerEncryptInfo = multiEncryptInfo.getRouterEncryptInfo();
            Observable<GetFirmwareUpdateStatusResponse> routerObservable =
                routerRepository.getFirmwareUpdateStatus(routerEncryptInfo)
                    .subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(postExecutionThread.getScheduler());

            IRequest cloudEncryptInfo = multiEncryptInfo.getCloudEncryptInfo();
            Observable<GetFirmwareUpdateStatusResponse> cloudObservable =
                cloudRepository.getFirmwareUpdateStatusTrans(cloudEncryptInfo)
                    .subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(postExecutionThread.getScheduler());

            // return Observable.concatDelayError(routerObservable, cloudObservable);

            if (multiEncryptInfo.getRouterDirectConnect()) {
              result = Observable.concatDelayError(routerObservable, cloudObservable);
            } else {
              result = cloudObservable;
            }

            return result;
          }
        });


//    return Observable.mergeDelayError(routerObservable, cloudObservable).first();
//    return routerObservable;
//    return cloudObservable;
  }
}
