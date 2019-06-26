package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.data.net.RouterUrlPath;
import com.fernandocejas.android10.sample.domain.constant.RouterUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.model.core.QtecMultiEncryptInfo;
import com.qtec.router.model.req.CheckFirmwareRequest;
import com.qtec.router.model.req.GetFirmwareUpdateStatusRequest;
import com.qtec.router.model.req.UpdateFirmwareRequest;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.GetFirmwareUpdateStatusResponse;
import com.qtec.router.model.rsp.UpdateFirmwareResponse;

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
class UpdateFirmwarePresenter implements Presenter {

  private final UseCase getFirmwareUpdateStatusUseCase;
  private final UseCase updateFirmwareUseCase;
  private final UseCase checkFirmwareUseCase;
  private UpdateFirmwareView mUpdateFirmwareView;

  @Inject
  public UpdateFirmwarePresenter(@Named(RouterUseCaseComm.UPDATE_FIRMWARE) UseCase updateFirmwareUseCase,
                                 @Named(RouterUseCaseComm.CHECK_FIRMWARE) UseCase checkFirmwareUseCase,
                                 @Named(RouterUseCaseComm.GET_FIRMWARE_UPDATE_STATUS) UseCase getFirmwareUpdateStatusUseCase) {
    this.updateFirmwareUseCase = checkNotNull(updateFirmwareUseCase, "updateFirmwareUseCase cannot be null!");
    this.checkFirmwareUseCase = checkNotNull(checkFirmwareUseCase, "checkFirmwareUseCase cannot be null!");
    this.getFirmwareUpdateStatusUseCase = checkNotNull(getFirmwareUpdateStatusUseCase, "getFirmwareUpdateStatusUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    updateFirmwareUseCase.unsubscribe();
    checkFirmwareUseCase.unsubscribe();
    getFirmwareUpdateStatusUseCase.unsubscribe();
  }

  public void setView(UpdateFirmwareView updateFirmwareView) {
    this.mUpdateFirmwareView = updateFirmwareView;
  }


  void update() {
    UpdateFirmwareRequest request = new UpdateFirmwareRequest();
    request.setKeepconfig(1);
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        request, RouterUrlPath.PATH_UPDATE_FIRMWARE, RouterUrlPath.METHOD_POST);

    mUpdateFirmwareView.showLoading();
    updateFirmwareUseCase.execute(multiEncryptInfo, new AppSubscriber<UpdateFirmwareResponse>(mUpdateFirmwareView) {

      @Override
      protected void doNext(UpdateFirmwareResponse response) {
        mUpdateFirmwareView.updateFirmwareSuccess(response);
      }
    });

  }

  void checkUpdate() {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new CheckFirmwareRequest(), RouterUrlPath.PATH_CHECK_FIRMWARE, RouterUrlPath.METHOD_POST);

    mUpdateFirmwareView.showLoading();
    checkFirmwareUseCase.execute(multiEncryptInfo, new AppSubscriber<CheckFirmwareResponse>(mUpdateFirmwareView) {

      @Override
      protected void doNext(CheckFirmwareResponse response) {
        mUpdateFirmwareView.showFirmwareVersion(response);
      }
    });

  }

  void getUpdateStatus() {
    QtecMultiEncryptInfo multiEncryptInfo = EncryptInfoFactory.createMultiEncryptInfo(
        new GetFirmwareUpdateStatusRequest(), RouterUrlPath.PATH_GET_FIRMWARE_UPDATE_STATUS, RouterUrlPath.METHOD_POST);

    mUpdateFirmwareView.showLoading();
    getFirmwareUpdateStatusUseCase.execute(multiEncryptInfo, new AppSubscriber<GetFirmwareUpdateStatusResponse>(mUpdateFirmwareView) {

      @Override
      public void onError(Throwable e) {
        //可能是网关重启导致异常，该现象出现说明固件升级成功
//        if (e instanceof CompositeException) {
          getFirmwareUpdateStatusUseCase.unsubscribe();
          mUpdateFirmwareView.showFirmwareUpdateSuccess();
//        }
        super.onError(e);
      }

      @Override
      protected void doNext(GetFirmwareUpdateStatusResponse response) {
        String status = response.getStatus();
        if (AppConstant.IMG_UPLOADING_FAILED.equals(status) || AppConstant.IMG_CHECK_FAILED.equals(status)) {
          getFirmwareUpdateStatusUseCase.unsubscribe();
          mUpdateFirmwareView.showFirmwareUpdateFailed(response);
        } else {
          mUpdateFirmwareView.showFirmwareUpdateStatus(response);
        }

      }
    });

  }

}
