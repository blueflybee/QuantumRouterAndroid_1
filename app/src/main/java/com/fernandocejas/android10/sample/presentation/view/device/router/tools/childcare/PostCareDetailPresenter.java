package com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare;

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
import com.qtec.router.model.req.PostChildCareDetailRequest;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.req.PostChildCareDetailRequest;
import com.qtec.router.model.rsp.ChildCareListResponse;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;

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
public class PostCareDetailPresenter implements Presenter {

  private final UseCase postCareUse;
  private PostChildCareDetailView postChildCareDetailView;

  @Inject
  public PostCareDetailPresenter(@Named(RouterUseCaseComm.POST_CHILD_CARE_DETAIL) UseCase postCareUseView) {
    this.postCareUse= checkNotNull(postCareUseView, "postCareUsecannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    postCareUse.unsubscribe();
  }

  public void setView(PostChildCareDetailView postChildCareDetailView) {
    this.postChildCareDetailView = postChildCareDetailView;
  }

  public void postCareDetail(String routerID,PostChildCareDetailRequest bean) {
    //build router request
    PostChildCareDetailRequest routerData = new PostChildCareDetailRequest();
    routerData.setEnabled(bean.getEnabled());
    routerData.setStop_time(bean.getStop_time());
    routerData.setMacaddr(bean.getMacaddr());
    routerData.setWeekdays(bean.getWeekdays());
    routerData.setStart_time(bean.getStart_time());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_CHILD_CARE_LIST, RouterUrlPath.METHOD_POST);

    postChildCareDetailView.showLoading();
    postCareUse.execute(multiEncryptInfo,
        new AppSubscriber<PostChildCareDetailResponse>(postChildCareDetailView) {

          @Override
          protected void doNext(PostChildCareDetailResponse response) {
            if (response != null) {
              postChildCareDetailView.postCareDetail(response);
            }
          }
        });
  }

}
