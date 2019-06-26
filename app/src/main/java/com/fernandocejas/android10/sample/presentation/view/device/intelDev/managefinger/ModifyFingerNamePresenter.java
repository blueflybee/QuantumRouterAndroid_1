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
import com.qtec.router.model.req.ModifyFingerNameRequest;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;

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
public class ModifyFingerNamePresenter implements Presenter {

    private final UseCase modifyFingerNameUseCase;
    private IModifyFingerNameView modifyFingerNameView;

    @Inject
    public ModifyFingerNamePresenter(@Named(RouterUseCaseComm.MODIFY_FINGER_NAME) UseCase modifyFingerNameUseCase) {
        this.modifyFingerNameUseCase = checkNotNull(modifyFingerNameUseCase, "modifyFingerNameUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        modifyFingerNameUseCase.unsubscribe();
    }

    public void setView(IModifyFingerNameView modifyFingerNameView) {
        this.modifyFingerNameView = modifyFingerNameView;
    }

    public void postModifyFingerName(String routerID,String lockID,String fingerID,String name) {
        //build router request
        ModifyFingerNameRequest routerData = new ModifyFingerNameRequest();
        routerData.setDevid(lockID);
        routerData.setFingerprintid(fingerID);
        routerData.setName(name);
        routerData.setUsrid(PrefConstant.getUserUniqueKey("0"));
        QtecEncryptInfo<ModifyFingerNameRequest> routerRequest = new QtecEncryptInfo<>();
        routerRequest.setRequestUrl(RouterUrlPath.PATH_MODIFY_FINGER_NAME);
        routerRequest.setMethod(RouterUrlPath.METHOD_POST);
        routerRequest.setData(routerData);

        //build transmit
        TransmitRequest<QtecEncryptInfo<ModifyFingerNameRequest>> transmit = new TransmitRequest<>();
        transmit.setRouterSerialNo(routerID);
        transmit.setEncryptInfo(routerRequest);

        //build cloud EncryptInfo
        QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
        cloudEncryptInfo.setData(transmit);

        //build router EncryptInfo
        QtecEncryptInfo<ModifyFingerNameRequest> routerEncryptInfo = new QtecEncryptInfo<>();
        routerEncryptInfo.setData(new ModifyFingerNameRequest());

        QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
        multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
        multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

        modifyFingerNameView.showLoading();
        modifyFingerNameUseCase.execute(multiEncryptInfo,
                new AppSubscriber<ModifyFingerNameResponse>(modifyFingerNameView) {

                    @Override
                    protected void doNext(ModifyFingerNameResponse response) {
                        if (response != null) {
                            modifyFingerNameView.modifyFingerName(response);
                        }
                    }
                });
    }

}
