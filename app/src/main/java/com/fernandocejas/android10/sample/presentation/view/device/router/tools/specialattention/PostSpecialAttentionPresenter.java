package com.fernandocejas.android10.sample.presentation.view.device.router.tools.specialattention;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.AddVpnRequest;
import com.qtec.router.model.req.PostSpecialAttentionRequest;
import com.qtec.router.model.req.RouterStatusRequest;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.PostSpecialAttentionResponse;
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
public class PostSpecialAttentionPresenter implements Presenter {

  private final UseCase attentionUseCase;
  private PostSpecialAttentionView attentionView;

  @Inject
  public PostSpecialAttentionPresenter(@Named(RouterUseCaseComm.POST_SPECIAL_ATTENTION) UseCase attentionUseCase) {
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

  public void setView(PostSpecialAttentionView attentionView) {
    this.attentionView = attentionView;
  }

  public void postSpecialAttention(String routerID,PostSpecialAttentionRequest bean) {
    PostSpecialAttentionRequest routerData = new PostSpecialAttentionRequest();
    routerData.setSpecialcare(bean.getSpecialcare());

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_POST_SPECIAL_ATTENTION, RouterUrlPath.METHOD_PUT);

    attentionView.showLoading();
    attentionUseCase.execute(multiEncryptInfo,
        new AppSubscriber<PostSpecialAttentionResponse>(attentionView) {

          @Override
          protected void doNext(PostSpecialAttentionResponse response) {
            if (response != null) {
              attentionView.postSpecialAttention(response);
            }
          }
        });
  }

}
