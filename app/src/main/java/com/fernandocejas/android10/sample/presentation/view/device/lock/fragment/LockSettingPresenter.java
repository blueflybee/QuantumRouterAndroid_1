package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import com.fernandocejas.android10.sample.data.constant.GlobleConstant;
import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.domain.interactor.cloud.GetLockVolume;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.DeviceCacheManager;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.AdjustLockVolumeRequest;
import com.qtec.mapp.model.req.GetLockVolumeRequest;
import com.qtec.mapp.model.req.UnbindLockOfAdminRequest;
import com.qtec.mapp.model.req.UnbindRouterRequest;
import com.qtec.mapp.model.rsp.AdjustLockVolumeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetLockVolumeResponse;
import com.qtec.mapp.model.rsp.UnbindLockOfAdminResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.model.core.QtecEncryptInfo;

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
public class LockSettingPresenter implements Presenter {

  private final UseCase unbindRouterUseCase;
  private final UseCase unbindLockOfAdminUseCase;
  private final UseCase getLockVolume;
  private final UseCase adjustLockVolume;
  private LockSettingView mLockSettingView;

  @Inject
  public LockSettingPresenter(@Named(CloudUseCaseComm.UNBIND_ROUTER) UseCase unbindRouterUseCase,
                              @Named(CloudUseCaseComm.UNBIND_LOCK_OF_ADMIN) UseCase unbindLockOfAdminUseCase,
                              @Named(CloudUseCaseComm.GET_LOCK_VOLUME) UseCase getLockVolume,
                              @Named(CloudUseCaseComm.ADJUST_LOCK_VOLUME) UseCase adjustLockVolume
  ) {
    this.unbindRouterUseCase = checkNotNull(unbindRouterUseCase, "unbindRouterUseCase cannot be null!");
    this.unbindLockOfAdminUseCase = checkNotNull(unbindLockOfAdminUseCase, "unbindLockOfAdminUseCase cannot be null!");
    this.getLockVolume = checkNotNull(getLockVolume, "getLockVolume cannot be null!");
    this.adjustLockVolume = checkNotNull(adjustLockVolume, "adjustLockVolume cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    unbindRouterUseCase.unsubscribe();
    unbindLockOfAdminUseCase.unsubscribe();
    getLockVolume.unsubscribe();
    adjustLockVolume.unsubscribe();
  }

  public void setView(LockSettingView netModeView) {
    this.mLockSettingView = netModeView;
  }


  void unbindLock(String lockId) {
    UnbindRouterRequest request = new UnbindRouterRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockSettingView.showLoading();
    unbindRouterUseCase.execute(encryptInfo, new AppSubscriber<UnbindRouterResponse>(mLockSettingView) {

      @Override
      protected void doNext(UnbindRouterResponse response) {
        DeviceCacheManager.delete(mLockSettingView.getContext(), request.getDeviceSerialNo());
        mLockSettingView.unbindLockSuccess(response, "");
      }
    });

  }

  void unbindLockOfAdmin(String lockId, String nextAdminUserId) {
    UnbindLockOfAdminRequest request = new UnbindLockOfAdminRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    request.setIsUnBind("1");
    request.setGeneralUserUniqueKey(nextAdminUserId);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockSettingView.showLoading();
    unbindLockOfAdminUseCase.execute(encryptInfo, new AppSubscriber<UnbindLockOfAdminResponse>(mLockSettingView) {

      @Override
      protected void doNext(UnbindLockOfAdminResponse response) {
        DeviceCacheManager.delete(mLockSettingView.getContext(), request.getDeviceSerialNo());
        mLockSettingView.unbindLockOfAdminSuccess(response, "");
      }
    });

  }


  public void unbindLock(GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> device) {
    UnbindRouterRequest request = new UnbindRouterRequest();
    request.setDeviceSerialNo(device.getDeviceSerialNo());
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockSettingView.showLoading();
    unbindRouterUseCase.execute(encryptInfo, new AppSubscriber<UnbindRouterResponse>(mLockSettingView) {

      @Override
      protected void doNext(UnbindRouterResponse response) {
        DeviceCacheManager.delete(mLockSettingView.getContext(), device.getDeviceSerialNo());
        mLockSettingView.unbindLockSuccess(response, device.getMac());
      }
    });

  }

  public void unbindLockOfAdmin(GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>> device, String nextAdminUserId) {
    UnbindLockOfAdminRequest request = new UnbindLockOfAdminRequest();
    request.setDeviceSerialNo(device.getDeviceSerialNo());
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    request.setIsUnBind("1");
    request.setGeneralUserUniqueKey(nextAdminUserId);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockSettingView.showLoading();
    unbindLockOfAdminUseCase.execute(encryptInfo, new AppSubscriber<UnbindLockOfAdminResponse>(mLockSettingView) {

      @Override
      protected void doNext(UnbindLockOfAdminResponse response) {
        DeviceCacheManager.delete(mLockSettingView.getContext(), device.getDeviceSerialNo());
        mLockSettingView.unbindLockOfAdminSuccess(response, device.getMac());
      }
    });

  }

  void getLockVolume(String lockId) {
    GetLockVolumeRequest request = new GetLockVolumeRequest();
    request.setDeviceSerialNo(lockId);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);
    mLockSettingView.showLoading();
    getLockVolume.execute(encryptInfo, new AppSubscriber<GetLockVolumeResponse>(mLockSettingView) {

      @Override
      protected void doNext(GetLockVolumeResponse response) {
        mLockSettingView.showLockVolume(response.getVolume());
      }
    });

  }

  void adjustLockVolume(String lockId, String volume) {
    AdjustLockVolumeRequest request = new AdjustLockVolumeRequest();
    request.setDeviceSerialNo(lockId);
    request.setVolume(volume);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);
    mLockSettingView.showLoading();
    adjustLockVolume.execute(encryptInfo, new AppSubscriber<AdjustLockVolumeResponse>(mLockSettingView) {

      @Override
      protected void doNext(AdjustLockVolumeResponse response) {
        mLockSettingView.onAdjustLockVolume(response);
      }
    });

  }
}
