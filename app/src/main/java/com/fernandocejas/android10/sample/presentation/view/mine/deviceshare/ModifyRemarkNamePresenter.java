package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.GetMemRemarkNameRequest;
import com.qtec.mapp.model.rsp.GetMemRemarkNameResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class ModifyRemarkNamePresenter implements Presenter {
    private final UseCase modifyRemarkNameUseCase;
    private IModifyRemarkNameView modifyRemarkNameView;

    @Inject
    public ModifyRemarkNamePresenter(@Named(CloudUseCaseComm.SHARE_MEM_REMARK_NAME) UseCase modifyRemarkNameUseCase)
    {
        this.modifyRemarkNameUseCase = checkNotNull(modifyRemarkNameUseCase, "modifyRemarkNameUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        modifyRemarkNameUseCase.unsubscribe();
    }

    public void setView(IModifyRemarkNameView modifyRemarkNameView) {
        this.modifyRemarkNameView = modifyRemarkNameView;
    }

    public void modifyRemarkName(QtecEncryptInfo<GetMemRemarkNameRequest> request) {
        modifyRemarkNameView.showLoading();
        modifyRemarkNameUseCase.execute(request, new AppSubscriber<GetMemRemarkNameResponse>(modifyRemarkNameView) {
            @Override
            protected void doNext(GetMemRemarkNameResponse response) {
                modifyRemarkNameView.showMemRemarkName(response);
            }
        });

    }
}
