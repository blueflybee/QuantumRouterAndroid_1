package com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.GetSpecialAttentionRequest;
import com.qtec.router.model.req.PostSpecialAttentionRequest;
import com.qtec.router.model.rsp.GetSpecialAttentionResponse;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;

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
public class GetSpecialAttentionPresenter implements Presenter {

  private final UseCase attentionUseCase;
  private GetSpecialAttentionView attentionView;

  @Inject
  public GetSpecialAttentionPresenter(@Named(RouterUseCaseComm.GET_SPECIAL_ATTENTION) UseCase attentionUseCase) {
    this.attentionUseCase = checkNotNull(attentionUseCase, "attentionUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    attentionUseCase.unsubscribe();
  }

  public void setView(GetSpecialAttentionView attentionView) {
    this.attentionView = attentionView;
  }

  public void getSpecialAttention(String routerID) {
    GetSpecialAttentionRequest routerData = new GetSpecialAttentionRequest();

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_GET_SPECIAL_ATTENTION, RouterUrlPath.METHOD_GET);

    attentionView.showLoading();
    attentionUseCase.execute(multiEncryptInfo,
        new AppSubscriber<GetSpecialAttentionResponse>(attentionView) {

          @Override
          protected void doNext(GetSpecialAttentionResponse response) {
            attentionView.getSpecialAttention(response);
          }
        });
  }

}
