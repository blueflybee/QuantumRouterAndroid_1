package com.fernandocejas.android10.sample.domain.interactor;

import com.fernandocejas.android10.sample.domain.executor.PostExecutionThread;
import com.fernandocejas.android10.sample.domain.executor.ThreadExecutor;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.domain.repository.CloudRepository;
import com.qtec.cateye.model.response.GetDoorBellRecordDetailResponse;
import com.qtec.cateye.model.response.SetAllDoorBellRecordReadResponse;

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
public class SetAllDoorBeelMsgIsReadUseCase extends UseCase {

    private final CloudRepository cloudRepository;

    @Inject
    public SetAllDoorBeelMsgIsReadUseCase(CloudRepository cloudRepository, ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.cloudRepository = cloudRepository;
    }

    @Override
    protected Observable<SetAllDoorBellRecordReadResponse> buildUseCaseObservable(IRequest param) {
        return cloudRepository.setAllDoorBeelMsgIsRead(param);
    }
}
