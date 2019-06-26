package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;

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
public class SetMsgReadPresenter implements Presenter {
    private final UseCase msgUseCase;
    private ISetMsgReadedView msgReadView;

    @Inject
    public SetMsgReadPresenter(@Named(CloudUseCaseComm.SET_MSG_READ) UseCase msgUseCase)
    {
        this.msgUseCase = checkNotNull(msgUseCase, "msgUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        msgUseCase.unsubscribe();
    }

    public void setView(ISetMsgReadedView msgReadView) {
        this.msgReadView = msgReadView;
    }

}
