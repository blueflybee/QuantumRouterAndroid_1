package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.IntelDevInfoModifyRequest;
import com.qtec.mapp.model.rsp.IntelDevInfoModifyResponse;
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
public class ModifyLockNamePresenter implements Presenter {
    private final UseCase modifyLockNameUseCase;
    private IModifyLockNameView modifyLockNameView;

    @Inject
    public ModifyLockNamePresenter(
        @Named(CloudUseCaseComm.INTEL_DEVICE_MODIFY) UseCase modifyLockNameUseCase)
    {
        this.modifyLockNameUseCase = checkNotNull(modifyLockNameUseCase, "modifyLockNameUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        modifyLockNameUseCase.unsubscribe();
    }

    public void setView(IModifyLockNameView modifyLockNameView) {
        this.modifyLockNameView = modifyLockNameView;
    }

}
