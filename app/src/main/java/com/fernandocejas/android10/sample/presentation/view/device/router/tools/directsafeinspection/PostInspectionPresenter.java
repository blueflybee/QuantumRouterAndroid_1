package com.fernandocejas.android10.sample.presentation.view.device.router.tools.directsafeinspection;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.PostInspectionRequest;
import com.qtec.router.model.rsp.PostInspectionResponse;

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
public class PostInspectionPresenter implements Presenter {

    private final UseCase safeUseCase;
    private PostInspectionView safeView;

    @Inject
    public PostInspectionPresenter(@Named(RouterUseCaseComm.POST_SAFE_INSPECTION) UseCase safeUseCase) {
        this.safeUseCase = checkNotNull(safeUseCase, "safeUseCase cannot be null!");
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        safeUseCase.unsubscribe();
    }

    public void setView(PostInspectionView safeView) {
        this.safeView = safeView;
    }

    public void safeInspection(String routerID) {
        //build router request
        PostInspectionRequest routerData = new PostInspectionRequest();

        QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
            routerID, routerData, RouterUrlPath.PATH_POST_SAFE_INSPECTION, RouterUrlPath.METHOD_GET);

        safeView.showLoading();
        safeUseCase.execute(multiEncryptInfo,
            new AppSubscriber<PostInspectionResponse>(safeView) {

                @Override
                protected void doNext(PostInspectionResponse response) {
                    if (response != null) {
                        safeView.safeInspection(response);
                    }
                }
            });
    }

}
