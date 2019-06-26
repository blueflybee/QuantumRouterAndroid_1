package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.exception.CloudNoSuchLockException;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.litegateway.data.LiteGateway;
import com.qtec.lock.model.rsp.BleGetLockInfoResponse;
import com.qtec.mapp.model.req.CommitAddRouterInfoRequest;
import com.qtec.mapp.model.req.GetUsersOfLockRequest;
import com.qtec.mapp.model.req.LockFactoryResetRequest;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.GetUsersOfLockResponse;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
@PerActivity
public class AddLiteGwPresenter implements Presenter {


  private static final String DEFAULT_LOCK_NAME = "三点LITE网关";
  private final UseCase commitAddRouterInfoUseCase;
  private AddGwView mAddGwView;

  private String lockId;


  @Inject
  public AddLiteGwPresenter(
      @Named(CloudUseCaseComm.COMMIT_ADD_ROUTER_INFO) UseCase commitAddRouterInfoUseCase) {
    this.commitAddRouterInfoUseCase = checkNotNull(commitAddRouterInfoUseCase, "commitAddRouterInfoUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {
  }

  @Override
  public void destroy() {
    commitAddRouterInfoUseCase.unsubscribe();
  }

  public void setView(AddGwView addGwView) {
    this.mAddGwView = addGwView;
  }

  void addGw(LiteGateway liteGw) {
    CommitAddRouterInfoRequest request = new CommitAddRouterInfoRequest();
    request.setDeviceType(liteGw.getDeviceType());
    request.setDeviceSerialNo(liteGw.getId());
    request.setDeviceName(DEFAULT_LOCK_NAME);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    mAddGwView.showLoading();

    commitAddRouterInfoUseCase.execute(
        EncryptInfoFactory.createEncryptInfo(request), new AppSubscriber<CommitAddRouterInfoResponse>(mAddGwView) {
          @Override
          protected void doNext(CommitAddRouterInfoResponse response) {
            mAddGwView.onAddGwSuccess(response);
          }
        });
  }

}
