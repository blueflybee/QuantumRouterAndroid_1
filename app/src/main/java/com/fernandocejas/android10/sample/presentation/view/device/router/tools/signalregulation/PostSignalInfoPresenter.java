package com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.router.model.req.GetSambaAccountRequest;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetVpnListRequest;
import com.qtec.router.model.req.SetSingalRegulationModeRequest;
import com.qtec.router.model.rsp.GetVpnListResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;

import java.util.List;

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
public class PostSignalInfoPresenter implements Presenter {

    private final UseCase signalReguaUseCase;
    private IPostSignalInfoView postSignalInfoView;

    @Inject
    public PostSignalInfoPresenter(@Named(RouterUseCaseComm.SET_SIGNAL_REGULATION) UseCase signalReguaUseCase) {
        this.signalReguaUseCase = checkNotNull(signalReguaUseCase, "signalReguaUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        signalReguaUseCase.unsubscribe();
    }

    public void setView(IPostSignalInfoView postSignalInfoView) {
        this.postSignalInfoView = postSignalInfoView;
    }

    public void setSignalRegulation(String routerID,int mode) {
        //build router request
        SetSingalRegulationModeRequest routerData = new SetSingalRegulationModeRequest();
        routerData.setMode(mode);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_SET_SIGNAL_REGULATION, RouterUrlPath.METHOD_POST);

        postSignalInfoView.showLoading();
        signalReguaUseCase.execute(multiEncryptInfo,
            new AppSubscriber<PostSignalRegulationResponse>(postSignalInfoView) {

                @Override
                protected void doNext(PostSignalRegulationResponse response) {
                    if (response != null) {
                        postSignalInfoView.postSignalRelationInfo(response);
                    }
                }
            });
    }

}
