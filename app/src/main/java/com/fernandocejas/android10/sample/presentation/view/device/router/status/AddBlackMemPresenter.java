package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.RemoveBlackMemRequest;
import com.qtec.router.model.rsp.RemoveBlackMemResponse;

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
public class AddBlackMemPresenter implements Presenter {

    private final UseCase blackListUseCase;
    private AddBlackMemView blackListView;

    @Inject
    public AddBlackMemPresenter(@Named(RouterUseCaseComm.REMOVE_BLACK_MEM) UseCase blackListUseCase) {
        this.blackListUseCase = checkNotNull(blackListUseCase, "blackListUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        blackListUseCase.unsubscribe();
    }

    public void setView(AddBlackMemView blackListView) {
        this.blackListView = blackListView;
    }

    public void addMem(String routerID,RemoveBlackMemRequest bean) {
        //build router request
       RemoveBlackMemRequest routerData = new RemoveBlackMemRequest();
      routerData.setMacaddr(bean.getMacaddr());
      routerData.setEnabled(bean.getEnabled());
      routerData.setName(bean.getName());

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_REMOVE_BLACK_MEM, RouterUrlPath.METHOD_POST);

        blackListView.showLoading();
        blackListUseCase.execute(multiEncryptInfo,
            new AppSubscriber<RemoveBlackMemResponse>(blackListView) {

                @Override
                protected void doNext(RemoveBlackMemResponse response) {
                    if (response != null) {
                        blackListView.addBlackMem(response);
                    }
                }
            });
    }

}
