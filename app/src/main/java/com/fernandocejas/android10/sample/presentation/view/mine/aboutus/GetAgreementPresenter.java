package com.fernandocejas.android10.sample.presentation.view.mine.aboutus;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.params.IRequest;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.rsp.GetAgreementResponse;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author shaojun
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetAgreementPresenter implements Presenter {

    private final UseCase agreementUseCase;
    private final UseCase secretUseCase;

    private IGetAgreementView agreementView;

    @Inject
    public GetAgreementPresenter(@Named(CloudUseCaseComm.USER_AGREEMENT) UseCase agreementUseCase,
                                 @Named(CloudUseCaseComm.USER_SECRET_AGREEMENT) UseCase secretUseCase) {
        this.agreementUseCase = agreementUseCase;
        this.secretUseCase = secretUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        agreementUseCase.unsubscribe();
        secretUseCase.unsubscribe();
    }

    public void setView(IGetAgreementView agreementView) {
        this.agreementView = agreementView;
    }

    public void getAgreement(IRequest request) {
        agreementView.showLoading();
        agreementUseCase.execute(request, new AppSubscriber<GetAgreementResponse>(agreementView) {
            @Override
            protected void doNext(GetAgreementResponse agreementResponse) {
                agreementView.openUserAgreement(agreementResponse);
            }
        });
    }

    public void getSerectAgreement(IRequest request) {
        agreementView.showLoading();
        secretUseCase.execute(request, new AppSubscriber<GetAgreementResponse>(agreementView) {
            @Override
            protected void doNext(GetAgreementResponse agreementResponse) {
                agreementView.openUserSerectAgreement(agreementResponse);
            }
        });
    }
}
