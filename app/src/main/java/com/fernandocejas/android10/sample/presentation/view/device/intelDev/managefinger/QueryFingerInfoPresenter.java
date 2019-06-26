package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.TransmitRequest;
import com.qtec.model.core.QtecEncryptInfo;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.QueryFingerInfoRequest;
import com.qtec.router.model.rsp.QueryFingerInfoResponse;

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
public class QueryFingerInfoPresenter implements Presenter {

  private final UseCase queryFingerUseCase;
  private IQueryFingerView queryFingerView;

  @Inject
  public QueryFingerInfoPresenter(@Named(RouterUseCaseComm.QUERY_FINGER_INFO) UseCase queryFingerUseCase) {
    this.queryFingerUseCase = checkNotNull(queryFingerUseCase, "queryFingerUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    queryFingerUseCase.unsubscribe();
  }

  public void setView(IQueryFingerView queryFingerView) {
    this.queryFingerView = queryFingerView;
  }

  public void queryFingerInfo(String routerID, String lockID) {
//build router request
    QueryFingerInfoRequest routerData = new QueryFingerInfoRequest();
    routerData.setDevid(lockID);
    //routerData.setDevid("dev2");
    routerData.setUsrid(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo<QueryFingerInfoRequest> routerRequest = new QtecEncryptInfo<>();
    routerRequest.setRequestUrl(RouterUrlPath.PATH_QUERY_FINGER_INFO);
    routerRequest.setMethod(RouterUrlPath.METHOD_POST);
    routerRequest.setData(routerData);

    //build transmit
    TransmitRequest<QtecEncryptInfo<QueryFingerInfoRequest>> transmit = new TransmitRequest<>();
    transmit.setRouterSerialNo(routerID);
    //transmit.setRouterSerialNo("C61702000754");
    transmit.setEncryptInfo(routerRequest);

    //build cloud EncryptInfo
    QtecEncryptInfo<TransmitRequest> cloudEncryptInfo = new QtecEncryptInfo<>();
    cloudEncryptInfo.setData(transmit);

    //build router EncryptInfo
    QtecEncryptInfo<QueryFingerInfoRequest> routerEncryptInfo = new QtecEncryptInfo<>();
    routerEncryptInfo.setData(new QueryFingerInfoRequest());

    QtecMultiEncryptInfo multiEncryptInfo = new QtecMultiEncryptInfo();
    multiEncryptInfo.setCloudEncryptInfo(cloudEncryptInfo);
    multiEncryptInfo.setRouterEncryptInfo(routerEncryptInfo);

    queryFingerView.showLoading();
    queryFingerUseCase.execute(multiEncryptInfo,
        new AppSubscriber<QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>>>(queryFingerView) {
          @Override
          protected void doNext(QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>> response) {
            queryFingerView.showFingerInfo(response);
          }
        });
  }

}
