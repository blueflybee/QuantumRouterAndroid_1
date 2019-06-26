package com.fernandocejas.android10.sample.presentation.view.device.router.tools.intelligentband;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetQosInfoRequest;
import com.qtec.router.model.rsp.GetQosInfoResponse;

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
public class GetQosInfoPresenter implements Presenter {

  private final UseCase qosUseCase;
  private GetQosInfoView qosView;

  @Inject
  public GetQosInfoPresenter(@Named(RouterUseCaseComm.GET_QOS_INFO) UseCase qosUseCase) {
    this.qosUseCase = checkNotNull(qosUseCase, "qosUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    qosUseCase.unsubscribe();
  }

  public void setView(GetQosInfoView qosView) {
    this.qosView = qosView;
  }

  public void getQosInfo(String routerID) {
    GetQosInfoRequest routerData = new GetQosInfoRequest();

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_QOS_INFO, RouterUrlPath.METHOD_GET);

    qosView.showLoading();
    qosUseCase.execute(multiEncryptInfo,
        new AppSubscriber<GetQosInfoResponse>(qosView) {

          @Override
          protected void doNext(GetQosInfoResponse response) {
            if (response != null) {
              qosView.getQosInfo(response);
            }
          }
        });
  }

}
