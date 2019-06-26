package com.fernandocejas.android10.sample.presentation.view.device.router.tools.bandspeedmeasure;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetBandSpeedRequest;
import com.qtec.router.model.req.OpenBandSpeedRequest;
import com.qtec.router.model.req.SetSingalRegulationModeRequest;
import com.qtec.router.model.rsp.GetBandSpeedResponse;
import com.qtec.router.model.rsp.OpenBandSpeedResponse;

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
public class OpenBandSpeedPresenter implements Presenter {

    private final UseCase openSpeedUseCase;
    private IOpenSpeedMeasureView openBandSpeedView;

    @Inject
    public OpenBandSpeedPresenter(@Named(RouterUseCaseComm.OPEN_BAND_SPEED) UseCase openSpeedUseCase) {
        this.openSpeedUseCase = checkNotNull(openSpeedUseCase, "openSpeedUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        openSpeedUseCase.unsubscribe();
    }

    public void setView(IOpenSpeedMeasureView openBandSpeedView) {
        this.openBandSpeedView = openBandSpeedView;
    }

    public void openBandSpeed(String routerID,int action) {
        //build router request
        OpenBandSpeedRequest routerData = new OpenBandSpeedRequest();
        routerData.setAction(action);

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_BAND_SPEED, RouterUrlPath.METHOD_POST);

        openBandSpeedView.showLoading();
        openSpeedUseCase.execute(multiEncryptInfo,
            new AppSubscriber<OpenBandSpeedResponse>(openBandSpeedView) {

                @Override
                protected void doNext(OpenBandSpeedResponse response) {
                    if (response != null) {
                        openBandSpeedView.openSpeedMeasure(response);
                    }
                }
            });
    }

}
