package com.fernandocejas.android10.sample.domain.interactor.router;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;
import com.qtec.router.model.rsp.AddRouterVerifyResponse;
import com.qtec.router.model.rsp.SearchIntelDevResponse;

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
public class AddRouterVerifyUseCase extends MultipleUseCase {

  @Inject
  public AddRouterVerifyUseCase(RouterRepository routerRepository,
                                CloudRepository cloudRepository,
                                ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {

    super(threadExecutor, postExecutionThread, cloudRepository, routerRepository);
  }


  @Override
  protected Observable<AddRouterVerifyResponse> buildUseCaseObservable(IRequest request) {
    QtecMultiEncryptInfo multiEncryptInfo = (QtecMultiEncryptInfo) request;

    IRequest routerEncryptInfo = multiEncryptInfo.getRouterEncryptInfo();

    Observable<AddRouterVerifyResponse> routerObservable = routerRepository.addRouterVerify(routerEncryptInfo)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler());

    IRequest cloudEncryptInfo = multiEncryptInfo.getCloudEncryptInfo();
    Observable<AddRouterVerifyResponse> cloudObservable = cloudRepository.addRouterVerifyTrans(cloudEncryptInfo)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(postExecutionThread.getScheduler());

    return routerObservable;
  }

}
