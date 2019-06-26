package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.mapp.model.rsp.ConnectLockResponse;
import com.qtec.mapp.model.rsp.UnbindCatLockResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * 登录用例
 *
 * @author shaojun
 * @name LoginUseCase
 * @package com.fernandocejas.android10.sample.domain.interactor
 * @date 15-9-9
 */
public class UnbindCatLockUseCase extends UseCase {

    private final CloudRepository cloudRepository;

    @Inject
    public UnbindCatLockUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.cloudRepository = cloudRepository;
    }

    @Override
    protected Observable<UnbindCatLockResponse> buildUseCaseObservable(IRequest param) {
        return cloudRepository.unbindCatLock(param);
    }
}
