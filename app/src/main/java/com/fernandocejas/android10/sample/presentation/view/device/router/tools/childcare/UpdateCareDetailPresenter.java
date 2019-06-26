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
import com.qtec.router.model.req.UpdateChildCareDetailRequest;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.req.UpdateChildCareDetailRequest;
import com.qtec.router.model.rsp.PostChildCareDetailResponse;

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
public class UpdateCareDetailPresenter implements Presenter {

  private final UseCase postCareUseCase;
  private UpdateChildCareDetailView updateChildCareDetailView;

  @Inject
  public UpdateCareDetailPresenter(@Named(RouterUseCaseComm.POST_CHILD_CARE_DETAIL) UseCase postCareUseCase) {
    this.postCareUseCase = checkNotNull(postCareUseCase, "postCareUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    postCareUseCase.unsubscribe();
  }

  public void setView(UpdateChildCareDetailView updateChildCareDetailView) {
    this.updateChildCareDetailView = updateChildCareDetailView;
  }

  public void updateCareDetail(String routerID,UpdateChildCareDetailRequest bean) {
    //build router request
    UpdateChildCareDetailRequest routerData = new UpdateChildCareDetailRequest();
    routerData.setNewenabled(bean.getNewenabled());
    routerData.setNewmacaddr(bean.getNewmacaddr());
    routerData.setNewstarttime(bean.getNewstarttime());
    routerData.setNewstoptime(bean.getNewstoptime());
    routerData.setNewweekdays(bean.getNewweekdays());

    routerData.setOldenabled(bean.getOldenabled());
    routerData.setOldweekdays(bean.getOldweekdays());
    routerData.setOldstoptime(bean.getOldstoptime());
    routerData.setOldmacaddr(bean.getOldmacaddr());
    routerData.setOldstarttime(bean.getOldstarttime());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_CHILD_CARE_LIST, RouterUrlPath.METHOD_PUT);

    /*QtecEncryptInfo<UpdateChildCareDetailRequest> routerRequest = new QtecEncryptInfo<>();
    routerRequest.setRequestUrl(RouterUrlPath.PATH_GET_CHILD_CARE_LIST);
    routerRequest.setMethod(RouterUrlPath.METHOD_PUT);
    routerRequest.setData(routerData);

    //build transmit
    TransmitRequest<QtecEncryptInfo<UpdateChildCareDetailRequest>> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(routerID);
    transmit.setEncryptInfo(routerRequest);

    //build cloud EncryptInfo
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    //build router EncryptInfo
    QtecEncryptInfo<UpdateChildCareDetailRequest> routerEncryptInfo = new QtecEncryptInfo<>();
    routerEncryptInfo.setData(new UpdateChildCareDetailRequest());

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);*/

    updateChildCareDetailView.showLoading();
    postCareUseCase.execute(multiEncryptInfo,
        new AppSubscriber<PostChildCareDetailResponse>(updateChildCareDetailView) {

          @Override
          protected void doNext(PostChildCareDetailResponse response) {
            if (response != null) {
              updateChildCareDetailView.updateCareDetail(response);
            }
          }
        });
  }

}
