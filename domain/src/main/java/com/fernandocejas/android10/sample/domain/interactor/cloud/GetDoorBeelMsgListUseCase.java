package com.fernandocejas.android10.sample.domain.interactor.cloud;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;

import java.util.List;

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
public class GetDoorBeelMsgListUseCase extends UseCase {

    private final CloudRepository cloudRepository;

    @Inject
    public GetDoorBeelMsgListUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.cloudRepository = cloudRepository;
    }

    @Override
    protected Observable<GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>>> buildUseCaseObservable(IRequest param) {
        return cloudRepository.getDoorBeelMsgList(param);
    }
}
