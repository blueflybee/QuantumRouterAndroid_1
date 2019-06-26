package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.QueryCatLockRequest;
import com.qtec.mapp.model.req.UnbindRouterRequest;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author xiehao
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class UbindCatPresenter implements Presenter {

    private final UseCase unbindUseCase;

    private  UnbindCatView unbindView;

    @Inject
    public UbindCatPresenter(@Named(CloudUseCaseComm.UNBIND_ROUTER) UseCase unbindUseCase) {
        this.unbindUseCase = unbindUseCase;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        unbindUseCase.unsubscribe();
    }

    public void setView(UnbindCatView unbindView) {
        this.unbindView = unbindView;
    }

    public void unbindCat(String catId) {
        UnbindRouterRequest request = new UnbindRouterRequest();
        request.setDeviceSerialNo(catId);
        request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));

        QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
        encryptInfo.setData(request);

        unbindView.showLoading();

        unbindUseCase.execute(encryptInfo, new AppSubscriber<UnbindRouterResponse>(unbindView) {
            @Override
            public void onError(Throwable e) {
                super.handleLoginInvalid(e);
            }

            @Override
            protected void doNext(UnbindRouterResponse response) {
                //DeviceCacheManager.delete(unbindView.getContext(), deviceId);
                unbindView.unbindCatSuccess(response);
            }
        });
    }
}
