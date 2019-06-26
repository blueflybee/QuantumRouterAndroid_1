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
import com.qtec.router.model.req.DeleteFingerRequest;
import com.qtec.router.model.rsp.DeleteFingerResponse;

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
public class DeleteFingerPresenter implements Presenter {

    private final UseCase deleteFingerUseCase;
    private IDeleteFingerView deleteFingerView;

    @Inject
    public DeleteFingerPresenter(@Named(RouterUseCaseComm.DELETE_FINGER_INFO) UseCase deleteFingerUseCase) {
        this.deleteFingerUseCase = checkNotNull(deleteFingerUseCase, "deleteFingerUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        deleteFingerUseCase.unsubscribe();
    }

    public void setView(IDeleteFingerView deleteFingerView) {
        this.deleteFingerView = deleteFingerView;
    }

    public void postDeleteFingerInfo(String routerID,String lockID,String fingerID) {
//build router request
        DeleteFingerRequest routerData = new DeleteFingerRequest();
        routerData.setDevid(lockID);
        routerData.setUsrid(PrefConstant.getUserUniqueKey("0"));
        routerData.setFingerprintid(fingerID);
        QtecEncryptInfo<DeleteFingerRequest> routerRequest = new QtecEncryptInfo<>();
        routerRequest.setRequestUrl(RouterUrlPath.PATH_DELETE_FINGER);
        routerRequest.setMethod(RouterUrlPath.METHOD_POST);
        routerRequest.setData(routerData);

        //build transmit
        TransmitRequest<QtecEncryptInfo<DeleteFingerRequest>> transmit = new TransmitRequest<>();
        transmit.setRouterSerialNo(routerID);
        transmit.setEncryptInfo(routerRequest);

        //build cloud EncryptInfo
        QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
        cloudEncryptInfo.setData(transmit);

        //build router EncryptInfo
        QtecEncryptInfo<DeleteFingerRequest> routerEncryptInfo = new QtecEncryptInfo<>();
        routerEncryptInfo.setData(new DeleteFingerRequest());

        QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
        multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
        multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

        deleteFingerView.showLoading();
        deleteFingerUseCase.execute(multiEncryptInfo,
                new AppSubscriber<DeleteFingerResponse>(deleteFingerView) {

                    @Override
                    protected void doNext(DeleteFingerResponse response) {
                        if (response != null) {
                            deleteFingerView.deleteFingerInfo(response);
                        }
                    }
                });
    }

}
