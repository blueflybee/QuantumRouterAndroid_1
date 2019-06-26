package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.fernandocejas.android10.sample.domain.repository.RouterRepository;
import com.qtec.mapp.model.rsp.IntelDeviceDetailResponse;
import com.qtec.mapp.model.rsp.IntelDeviceListResponse;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class IntelDeviceDetailUseCase extends UseCase {

    private final CloudRepository routerRepository;

    @Inject
    public IntelDeviceDetailUseCase(CloudRepository routerRepository, ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routerRepository = routerRepository;
    }


    @Override protected Observable<IntelDeviceDetailResponse> buildUseCaseObservable(IRequest request) {
        return routerRepository.getIntelDeviceDetail(request);
    }
}
