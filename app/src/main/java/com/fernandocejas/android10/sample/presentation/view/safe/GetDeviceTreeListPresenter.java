package com.fernandocejas.android10.sample.presentation.view.safe;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.qtec.mapp.model.req.GetDevTreeRequest;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.model.core.QtecEncryptInfo;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author xiehao
 * @name LoginPresenter
 * @package com.fernandocejas.android10.sample.presentation.presenter
 * @date 15-9-10
 */
@PerActivity
public class GetDeviceTreeListPresenter implements Presenter {

  private final UseCase routerListUseCase;

  private IGetDeviceTreeListView routerListView;

  @Inject
  public GetDeviceTreeListPresenter(@Named(CloudUseCaseComm.DEV_TREE) UseCase routerListUseCase) {
    this.routerListUseCase = routerListUseCase;
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    routerListUseCase.unsubscribe();
  }

  public void setView(IGetDeviceTreeListView routerListView) {
    this.routerListView = routerListView;
  }

  public void getRouterList() {
    GetDevTreeRequest request = new GetDevTreeRequest();
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    request.setDeviceType("");
    QtecEncryptInfo encryptInfo = new QtecEncryptInfo();
    encryptInfo.setData(request);
    routerListView.showLoading();
    routerListUseCase.execute(encryptInfo, new AppSubscriber<List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>>>(routerListView) {
      @Override
      protected void doNext(List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> routerListResponse) {
        routerListView.getRouterList(routerListResponse);
      }
    });
  }
}
