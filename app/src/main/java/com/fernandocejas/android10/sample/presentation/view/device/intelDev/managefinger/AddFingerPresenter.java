package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddFingerRequest;
import com.qtec.router.model.rsp.AddFingerResponse;

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
public class AddFingerPresenter implements Presenter {

    private final UseCase addFingerUseCase;
    private IAddFingerView addFingerView;

    @Inject
    public AddFingerPresenter(@Named(RouterUseCaseComm.ADD_FINGER_INFO) UseCase addFingerUseCase) {
        this.addFingerUseCase = checkNotNull(addFingerUseCase, "addFingerUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        addFingerUseCase.unsubscribe();
    }

    public void setView(IAddFingerView addFingerView) {
        this.addFingerView = addFingerView;
    }

    public void postFingerInfo(String routerID,String lockID) {
//build router request
        AddFingerRequest routerData = new AddFingerRequest();
        routerData.setDevid(lockID);
        //routerData.setDevid("dev2");
        routerData.setUsrid(PrefConstant.getUserUniqueKey("0"));
        QtecEncryptInfo<AddFingerRequest> routerRequest = new QtecEncryptInfo<>();
        routerRequest.setRequestUrl(RouterUrlPath.PATH_ADD_FINGER);
        routerRequest.setMethod(RouterUrlPath.METHOD_POST);
        routerRequest.setData(routerData);

        //build transmit
        TransmitRequest<QtecEncryptInfo<AddFingerRequest>> transmit = new TransmitRequest<>();
        transmit.setRouterSerialNo(routerID);
       //transmit.setRouterSerialNo("C61702000754");
        transmit.setEncryptInfo(routerRequest);

        //build cloud EncryptInfo
        QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
        cloudEncryptInfo.setData(transmit);

        //build router EncryptInfo
        QtecEncryptInfo<AddFingerRequest> routerEncryptInfo = new QtecEncryptInfo<>();
        routerEncryptInfo.setData(new AddFingerRequest());

        QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
        multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
        multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

        addFingerView.showLoading();
        addFingerUseCase.execute(multiEncryptInfo,
                new AppSubscriber<AddFingerResponse>(addFingerView) {

                    @Override
                    protected void doNext(AddFingerResponse response) {
                        if (response != null) {
                            addFingerView.getFingerInfo(response);
                        }
                    }
                });
    }

}
