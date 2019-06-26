package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.router.model.req.SearchRouterRequest;
import com.qtec.router.model.rsp.SearchRouterResponse;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class SearchRouterPresenter implements Presenter {

    private final UseCase searchRouterUseCase;
    private AddRouterView addRouterView;

    @Inject
    public SearchRouterPresenter(
            @Named(RouterUseCaseComm.SEARCH_ROUTER) UseCase searchRouterUseCase) {
        this.searchRouterUseCase = checkNotNull(searchRouterUseCase, "searchRouterUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        searchRouterUseCase.unsubscribe();
    }

    public void setView(AddRouterView intelDeviceListView) {
        this.addRouterView = intelDeviceListView;
    }

    public void searchRouter(QtecEncryptInfo<SearchRouterRequest> request) {
        addRouterView.showLoading();
        searchRouterUseCase.execute(request, new AppSubscriber<SearchRouterResponse>(addRouterView) {
            @Override
            protected void doNext(SearchRouterResponse response) {
                addRouterView.showSearchSuccess(response);
            }
        });

    }

}