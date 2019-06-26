package com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation.IPostSignalInfoView;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetBandSpeedRequest;
import com.qtec.router.model.req.SetSingalRegulationModeRequest;
import com.qtec.router.model.rsp.GetBandSpeedResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;

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
public class GetBandSpeedPresenter implements Presenter {

    private final UseCase bandSpeedUseCase;
    private IBandSpeedMeasureView bandSpeedView;

    @Inject
    public GetBandSpeedPresenter(@Named(RouterUseCaseComm.GET_BAND_SPEED) UseCase bandSpeedUseCase) {
        this.bandSpeedUseCase = checkNotNull(bandSpeedUseCase, "bandSpeedUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        bandSpeedUseCase.unsubscribe();
    }

    public void setView(IBandSpeedMeasureView bandSpeedView) {
        this.bandSpeedView = bandSpeedView;
    }

    public void getBandSpeed(String routerID) {
        //build router request
        GetBandSpeedRequest routerData = new GetBandSpeedRequest();

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_BAND_SPEED, RouterUrlPath.METHOD_GET);

        //bandSpeedView.showLoading();
        bandSpeedUseCase.execute(multiEncryptInfo,
                new AppSubscriber<GetBandSpeedResponse>(bandSpeedView) {

                    @Override
                    protected void doNext(GetBandSpeedResponse response) {
                        if (response != null) {
                            bandSpeedView.bandSpeedMeasure(response);
                        }
                    }
                });
    }

}
