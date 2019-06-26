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
import com.qtec.router.model.req.PostQosInfoRequest;
import com.qtec.router.model.rsp.GetQosInfoResponse;
import com.qtec.router.model.rsp.PostQosInfoResponse;

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
public class PostQosInfoPresenter implements Presenter {

  private final UseCase qosUseCase;
  private PostQosInfoView qosView;

  @Inject
  public PostQosInfoPresenter(@Named(RouterUseCaseComm.POST_QOS_INFO) UseCase qosUseCase) {
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

  public void setView(PostQosInfoView qosView) {
    this.qosView = qosView;
  }

  public void postQosInfo(String routerID,PostQosInfoRequest bean) {
    PostQosInfoRequest routerData = new PostQosInfoRequest();
    routerData.setEnabled(bean.getEnabled());
    routerData.setQosmode(bean.getQosmode());
    routerData.setUpload(bean.getUpload());
    routerData.setDownload(bean.getDownload());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_QOS_INFO, RouterUrlPath.METHOD_PUT);

    qosView.showLoading();
    qosUseCase.execute(multiEncryptInfo,
        new AppSubscriber<PostQosInfoResponse>(qosView) {

          @Override
          protected void doNext(PostQosInfoResponse response) {
            if (response != null) {
              qosView.postQosInfo(response);
            }
          }
        });
  }

}
