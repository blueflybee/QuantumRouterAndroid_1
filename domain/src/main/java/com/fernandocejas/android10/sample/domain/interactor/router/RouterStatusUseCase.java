package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
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
public class RouterStatusUseCase extends MultipleUseCase {
  Observable<RouterStatusResponse<List<Status>>> result;
  @Inject
  public RouterStatusUseCase(RouterRepository routerRepository,
                             CloudRepository cloudRepository,
                             ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<RouterStatusResponse<List<Status>>> buildUseCaseObservable(final IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    Observable<RouterStatusResponse<List<Status>>> routerObservable =
        routerRepository.getRouterStatus(multiEncryptInfo.getRouterEncryptInfo())
            .map(new Func1<RouterStatusResponse<List<Status>>, RouterStatusResponse<List<Status>>>() {
              @Override
              public RouterStatusResponse<List<Status>> call(RouterStatusResponse<List<Status>> response) {

                Collections.sort(response.getStalist(), new Comparator<Status>() {
                  @Override
                  public int compare(Status status, Status status1) {
                    int result = status.getStastatus() - status1.getStastatus();
                    if (result < 0) {
                      result = -1;
                    } else if (result > 0) {
                      result = 1;
                    }
                    return result;
                  }
                });
                return response;
              }
            })
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler());

    Observable<RouterStatusResponse<List<Status>>> cloudObservable =
        cloudRepository.getRouterStatusTrans(multiEncryptInfo.getCloudEncryptInfo())
            .map(new Func1<RouterStatusResponse<List<Status>>, RouterStatusResponse<List<Status>>>() {
              @Override
              public RouterStatusResponse<List<Status>> call(RouterStatusResponse<List<Status>> response) {

                Collections.sort(response.getStalist(), new Comparator<Status>() {
                  @Override
                  public int compare(Status status, Status status1) {
                    int result = status.getStastatus() - status1.getStastatus();
                    if (result < 0) {
                      result = -1;
                    } else if (result > 0) {
                      result = 1;
                    }
                    return result;
                  }
                });
                return response;
              }
            })
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
