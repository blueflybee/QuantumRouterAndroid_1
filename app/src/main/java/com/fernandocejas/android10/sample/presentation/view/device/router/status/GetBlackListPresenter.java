package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetBlackListRequest;
import com.qtec.router.model.rsp.GetBlackListResponse;

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
public class GetBlackListPresenter implements Presenter {

    private final UseCase blackListUseCase;
    private GetBlackListView blackListView;

    @Inject
    public GetBlackListPresenter(@Named(RouterUseCaseComm.GET_BLACK_LIST) UseCase blackListUseCase) {
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

    public void setView(GetBlackListView blackListView) {
        this.blackListView = blackListView;
    }

    public void getBlackList(String routerID) {
        //build router request
       GetBlackListRequest routerData = new GetBlackListRequest();

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_GET_BLACK_LIST, RouterUrlPath.METHOD_GET);

        blackListView.showLoading();
        blackListUseCase.execute(multiEncryptInfo,
            new AppSubscriber<List<GetBlackListResponse>>(blackListView) {

                @Override
                protected void doNext(List<GetBlackListResponse> response) {
                    if (response != null) {
                        blackListView.getBlackList(response);
                    }
                }
            });
    }

}
