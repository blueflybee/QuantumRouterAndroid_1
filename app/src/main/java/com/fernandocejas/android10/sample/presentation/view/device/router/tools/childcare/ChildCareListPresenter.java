package com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.ChildCareListRequest;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.rsp.ChildCareListResponse;
import com.qtec.router.model.rsp.RouterStatusResponse;

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
public class ChildCareListPresenter implements Presenter {

  private final UseCase childCareListUseCase;
  private ChildCareListView childCareListView;

  @Inject
  public ChildCareListPresenter(@Named(RouterUseCaseComm.CHILD_CARE_LIST) UseCase childCareListUseCase) {
    this.childCareListUseCase = checkNotNull(childCareListUseCase, "childCareListUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    childCareListUseCase.unsubscribe();
  }

  public void setView(ChildCareListView childCareListView) {
    this.childCareListView = childCareListView;
  }

  public void getRouterStatus(String routerId) {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerId, new RouterStatusRequest(), RouterUrlPath.PATH_GET_CHILD_CARE_LIST, RouterUrlPath.METHOD_GET);
    childCareListView.showLoading();
    childCareListUseCase.execute(multiEncryptInfo,
        new AppSubscriber<List<ChildCareListResponse>>(childCareListView) {

          @Override
          protected void doNext(List<ChildCareListResponse> response) {
            childCareListView.showChildCareList(response);
          }
        });
  }

}
