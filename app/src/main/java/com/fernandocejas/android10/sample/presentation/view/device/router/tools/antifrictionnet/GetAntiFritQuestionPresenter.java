package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.EnableAntiFritNetRequest;
import com.qtec.router.model.req.GetAntiFritQuestionRequest;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;

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
public class GetAntiFritQuestionPresenter implements Presenter {

    private final UseCase enableNetUseCase;
    private IGetAntiFritQuestionView enableNetView;

    @Inject
    public GetAntiFritQuestionPresenter(@Named(RouterUseCaseComm.GET_ANTI_QUESTION) UseCase enableNetUseCase) {
        this.enableNetUseCase = checkNotNull(enableNetUseCase, "enableNetUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        enableNetUseCase.unsubscribe();
    }

    public void setView(IGetAntiFritQuestionView enableNetView) {
        this.enableNetView = enableNetView;
    }

    public void getAntiFritQuestion(String routerID) {
        //build router request
        GetAntiFritQuestionRequest routerData = new GetAntiFritQuestionRequest();

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_ANTI_FRIT_QUESTION, RouterUrlPath.METHOD_GET);

        enableNetView.showLoading();
        enableNetUseCase.execute(multiEncryptInfo,
            new AppSubscriber<GetAntiFritQuestionResponse>(enableNetView) {

                @Override
                protected void doNext(GetAntiFritQuestionResponse response) {
                    if (response != null) {
                        enableNetView.getAntiFritQuestion(response);
                    }
                }
            });
    }

}
