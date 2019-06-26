package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import android.text.TextUtils;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
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
import com.qtec.router.model.req.FirstGetKeyRequest;
import com.qtec.router.model.req.GetSambaAccountRequest;
import com.qtec.router.model.req.PostChildCareDetailRequest;
import com.qtec.router.model.req.SearchRouterRequest;
import com.qtec.router.model.rsp.FirstGetKeyResponse;
import com.qtec.router.model.rsp.GetSambaAccountResponse;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;
import com.qtec.router.model.rsp.SearchRouterResponse;

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
public class GetSambaAccountPresenter implements Presenter {

    private final UseCase sambaAccountUseCase;
    private IGetSambaAccountView sambaAccountView;

    @Inject
    public GetSambaAccountPresenter(@Named(RouterUseCaseComm.GET_SAMBA_ACCOUNT) UseCase sambaAccountUseCase) {
        this.sambaAccountUseCase = checkNotNull(sambaAccountUseCase, "sambaAccountUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        sambaAccountUseCase.unsubscribe();
    }

    public void setView(IGetSambaAccountView sambaAccountView) {
        this.sambaAccountView = sambaAccountView;
    }

    public void getSambaAccount() {
        //build router request
        GetSambaAccountRequest routerData = new GetSambaAccountRequest();

        QtecEncryptInfo<GetSambaAccountRequest> encryptInfo = new QtecEncryptInfo<>();
        encryptInfo.setData(routerData);

        QtecMultiEncryptInfo multiEncryptInfo = null;

        multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            GlobleConstant.getgDeviceId(), routerData, RouterUrlPath.PATH_GET_SAM_PWD, RouterUrlPath.METHOD_GET);

        sambaAccountView.showLoading();
        sambaAccountUseCase.execute(multiEncryptInfo,
            new AppSubscriber<GetSambaAccountResponse>(sambaAccountView) {

                @Override
                protected void doNext(GetSambaAccountResponse response) {
                    if (response != null) {
                        sambaAccountView.getSambaAccount(response);
                    }
                }
            });
    }

}
