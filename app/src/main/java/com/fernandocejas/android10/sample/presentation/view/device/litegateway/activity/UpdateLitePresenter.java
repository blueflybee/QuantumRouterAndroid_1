package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.cloud.CheckLiteVersion;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetLiteUpdate;
import com.fernandocejas.android10.sample.domain.interactor.cloud.UpdateLite;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.google.common.base.Preconditions;
import com.qtec.mapp.model.req.CheckLiteVersionRequest;
import com.qtec.mapp.model.req.GetLiteUpdateRequest;
import com.qtec.mapp.model.req.UpdateLiteRequest;
import com.qtec.mapp.model.rsp.CheckLiteVersionResponse;
import com.qtec.mapp.model.rsp.GetLiteUpdateResponse;
import com.qtec.mapp.model.rsp.UpdateLiteResponse;
import com.qtec.model.core.QtecEncryptInfo;

import javax.inject.Inject;
import javax.inject.Named;


/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-9
 *     desc   : presenter
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class UpdateLitePresenter implements Presenter {


  private final CheckLiteVersion mCheckLiteVersion;
  private final UpdateLite mUpdateLite;
  private final GetLiteUpdate mGetLiteUpdate;
  private UpdateLiteView mView;

  @Inject
  public UpdateLitePresenter(@Named(CloudUseCaseComm.CHECK_LITE_VERSION) CheckLiteVersion pCheckLiteVersion,
                             @Named(CloudUseCaseComm.UPDATE_LITE) UpdateLite pUpdateLite,
                             @Named(CloudUseCaseComm.GET_LITE_UPDATE) GetLiteUpdate pGetLiteUpdate) {
    this.mCheckLiteVersion = Preconditions.checkNotNull(pCheckLiteVersion, "pCheckLiteVersion is null");
    this.mUpdateLite = Preconditions.checkNotNull(pUpdateLite, "pUpdateLite is null");
    this.mGetLiteUpdate = Preconditions.checkNotNull(pGetLiteUpdate, "pGetLiteUpdate is null");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    mCheckLiteVersion.unsubscribe();
    mUpdateLite.unsubscribe();
    mGetLiteUpdate.unsubscribe();
  }

  public void setView(UpdateLiteView view) {
    this.mView = view;
  }

  void checkVersion(String routerId) {
    CheckLiteVersionRequest request = new CheckLiteVersionRequest();
    request.setRouterSerialNo(routerId);
    QtecEncryptInfo<CheckLiteVersionRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    mView.showLoading();
    mCheckLiteVersion.execute(encryptInfo, new AppSubscriber<CheckLiteVersionResponse>(mView) {
      @Override
      protected void doNext(CheckLiteVersionResponse response) {
        mView.showVersion(response);
      }
    });
  }

  void updateLite(String routerId, String url) {
    UpdateLiteRequest request = new UpdateLiteRequest();
    request.setRouterSerialNo(routerId);
    request.setDownloadUrl(url);
    QtecEncryptInfo<UpdateLiteRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    mView.showLoading();
    mUpdateLite.execute(encryptInfo, new AppSubscriber<UpdateLiteResponse>(mView) {
      @Override
      protected void doNext(UpdateLiteResponse response) {
        mView.onUpdate(response);
      }
    });
  }

  void getLiteUpdate(String routerId) {
    GetLiteUpdateRequest request = new GetLiteUpdateRequest();
    request.setRouterSerialNo(routerId);
    QtecEncryptInfo<GetLiteUpdateRequest> encryptInfo = new QtecEncryptInfo<>();
    encryptInfo.setData(request);
    mView.showLoading();
    mGetLiteUpdate.execute(encryptInfo, new AppSubscriber<GetLiteUpdateResponse>(mView) {
      @Override
      protected void doNext(GetLiteUpdateResponse response) {
        mView.showUpdateStatus(response);
        if ("0".equals(response.getUpgradeStatus())) {
          mGetLiteUpdate.unsubscribe();
        }
      }
    });
  }
}
