package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.SetAntiNetQuestionRequest;
import com.qtec.router.model.rsp.SetAntiNetQuestionResponse;

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
public class SetAntiNetQuestionPresenter implements Presenter {

  private final UseCase setQuestionUseCase;
  private ISetAntiNetQuestionView setQuestionView;

  @Inject
  public SetAntiNetQuestionPresenter(@Named(RouterUseCaseComm.SET_ANTIWIFI_QUESTION) UseCase setQuestionUseCase) {
    this.setQuestionUseCase = checkNotNull(setQuestionUseCase, "setQuestionUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    setQuestionUseCase.unsubscribe();
  }

  public void setView(ISetAntiNetQuestionView setQuestionView) {
    this.setQuestionView = setQuestionView;
  }

  public void setAntiQuestion(String routerID,String question, String answer) {
    //build router request
    SetAntiNetQuestionRequest routerData = new SetAntiNetQuestionRequest();

    routerData.setQuestion(question);
    routerData.setAnswer(answer);

    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        routerID, routerData, RouterUrlPath.PATH_SET_ANTI_QUESTION, RouterUrlPath.METHOD_POST);
    
    setQuestionView.showLoading();
    setQuestionUseCase.execute(multiEncryptInfo,
        new AppSubscriber<SetAntiNetQuestionResponse>(setQuestionView) {

          @Override
          protected void doNext(SetAntiNetQuestionResponse response) {
            if (response != null) {
              setQuestionView.setAntiNetQuestion(response);
            }
          }
        });
  }

}
