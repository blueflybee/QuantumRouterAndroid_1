package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.navigation.Navigator;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.fernandocejas.android10.sample.presentation.view.device.lock.service.LockFirmwareDownloadService;
import com.qtec.mapp.model.req.CheckLockVersionRequest;
import com.qtec.mapp.model.req.UpdateLockVersionRequest;
import com.qtec.mapp.model.rsp.CheckLockVersionResponse;
import com.qtec.mapp.model.rsp.UpdateLockVersionResponse;
import com.qtec.model.core.QtecEncryptInfo;

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
public class LockCheckVersionPresenter implements Presenter {

  private final UseCase checkLockVersionUseCase;
  private final UseCase updateLockVersionUseCase;
  private LockCheckVersionView mLockCheckVersionView;

  private CheckLockVersionResponse mVersionResponse;

  private String mCurrentVersion;
  private String mNewVersion;

  @Inject
  public LockCheckVersionPresenter(@Named(CloudUseCaseComm.CHECK_LOCK_VERSION) UseCase checkLockVersionUseCase,
                                   @Named(CloudUseCaseComm.UPDATE_LOCK_VERSION) UseCase updateLockVersionUseCase) {
    this.checkLockVersionUseCase = checkNotNull(checkLockVersionUseCase, "checkLockVersionUseCase cannot be null!");
    this.updateLockVersionUseCase = checkNotNull(updateLockVersionUseCase, "updateLockVersionUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }


  @Override
  public void destroy() {
    checkLockVersionUseCase.unsubscribe();
    updateLockVersionUseCase.unsubscribe();
  }

  public void setView(LockCheckVersionView versionInfoView) {
    this.mLockCheckVersionView = versionInfoView;
  }

  public void checkVersion(String currentVersion, String deviceModel) {
    setCurrentVersion(currentVersion);
    CheckLockVersionRequest request = new CheckLockVersionRequest();
    request.setDeviceModel(deviceModel);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);
    mLockCheckVersionView.showLoading();
    checkLockVersionUseCase.execute(encryptInfo, new AppSubscriber<CheckLockVersionResponse>(mLockCheckVersionView) {

      @Override
      public void onError(Throwable e) {
        mLockCheckVersionView.showFirmwareVersionCheckFail();
        super.onError(e);
      }

      @Override
      protected void doNext(CheckLockVersionResponse response) {
        mVersionResponse = response;
        String newVersion = response.getVersionNo();
        setNewVersion(newVersion);
        if (TextUtils.isEmpty(currentVersion) || TextUtils.isEmpty(newVersion)) {
          mLockCheckVersionView.showIsLastedVersion(currentVersion);
          return;
        }
        int result = currentVersion.compareToIgnoreCase(newVersion);
        if (result >= 0) {
          mLockCheckVersionView.showIsLastedVersion(currentVersion);
        } else {
          mLockCheckVersionView.showHasNewVersion(currentVersion, newVersion);
        }
      }
    });
  }

  public void updateVersion(String deviceSerialNo) {
    UpdateLockVersionRequest request = new UpdateLockVersionRequest();
    request.setDeviceSerialNo(deviceSerialNo);
    request.setDeviceType(AppConstant.DEVICE_TYPE_LOCK);
    request.setDeviceVersion(getNewVersion());
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);
//    mLockCheckVersionView.showLoading();
    updateLockVersionUseCase.execute(encryptInfo, new AppSubscriber<UpdateLockVersionResponse>(mLockCheckVersionView) {

//      @Override
//      public void onError(Throwable e) {
//        mLockCheckVersionView.showFirmwareVersionCheckFail();
//        super.onError(e);
//      }

      @Override
      protected void doNext(UpdateLockVersionResponse response) {
      }
    });
  }

  public void update() {
    if (mVersionResponse == null) {
      ToastUtils.showShort("文件无法下载");
      return;
    }
    String downloadUrl = mVersionResponse.getDownloadUrl();
    if (TextUtils.isEmpty(downloadUrl)) {
      ToastUtils.showShort("文件不存在");
      return;
    }

    mLockCheckVersionView.showLoading();

    Intent intent = new Intent(mLockCheckVersionView.getContext(), LockFirmwareDownloadService.class);
    intent.putExtra(Navigator.EXTRA_LOCK_FIRMWARE_DOWNLOAD_URL, downloadUrl);
    intent.putExtra(Navigator.EXTRA_LOCK_UPDATE_FIRMWARE_MESSENGER, new Messenger(mHandler));
    mLockCheckVersionView.getContext().startService(intent);
//    ToastUtils.showShort("请在通知栏查看下载进度");
  }

  public String getCurrentVersion() {
    return mCurrentVersion;
  }

  public void setCurrentVersion(String currentVersion) {
    mCurrentVersion = currentVersion;
  }

  public String getNewVersion() {
    return mNewVersion;
  }

  public void setNewVersion(String newVersion) {
    mNewVersion = newVersion;
  }

  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {
    // 接收结果，刷新界面
    public void handleMessage(Message msg) {
      mLockCheckVersionView.hideLoading();
      switch (msg.what) {
        case LockFirmwareDownloadService.MSG_DOWNLOAD_SUCCESS:
          String filePath = (String) msg.obj;
          if (TextUtils.isEmpty(filePath)) ToastUtils.showShort("文件下载失败");
          mLockCheckVersionView.startUpdateFirmware(filePath);
          break;

        case LockFirmwareDownloadService.MSG_DOWNLOAD_FAILED:
          ToastUtils.showShort("文件下载失败");
          break;

        default:
          break;
      }
    }
  };

}
