package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.data.exception.CloudNoSuchLockException;
import com.fernandocejas.android10.sample.data.exception.PasswordErrMoreTimesException;
import com.fernandocejas.android10.sample.data.exception.PasswordErrThreeTimesException;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.constant.AppConstant;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
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
public class AddLockPresenter implements Presenter {


  private static final String DEFAULT_LOCK_NAME = "三点安全智能锁";
  private final UseCase commitAddRouterInfoUseCase;
  private final UseCase getUsersOfLockUseCase;
  private final UseCase lockFactoryResetUseCase;
  private AddLockView addLockView;

  private String lockId;


  @Inject
  public AddLockPresenter(
      @Named(CloudUseCaseComm.COMMIT_ADD_ROUTER_INFO) UseCase commitAddRouterInfoUseCase,
      @Named(CloudUseCaseComm.GET_USERS_OF_LOCK) UseCase getUsersOfLockUseCase,
      @Named(CloudUseCaseComm.LOCK_FACTORY_RESET) UseCase lockFactoryResetUseCase) {
    this.commitAddRouterInfoUseCase = checkNotNull(commitAddRouterInfoUseCase, "commitAddRouterInfoUseCase cannot be null!");
    this.getUsersOfLockUseCase = checkNotNull(getUsersOfLockUseCase, "getUsersOfLockUseCase cannot be null!");
    this.lockFactoryResetUseCase = checkNotNull(lockFactoryResetUseCase, "lockFactoryResetUseCase cannot be null!");
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
    getUsersOfLockUseCase.unsubscribe();
    lockFactoryResetUseCase.unsubscribe();
  }

  public void setView(AddLockView addLockView) {
    this.addLockView = addLockView;
  }

  void addLockCloud(BleGetLockInfoResponse getLockInfoResponse, String mac, String bleName) {
    CommitAddRouterInfoRequest request = new CommitAddRouterInfoRequest();
    request.setDeviceType(AppConstant.DEVICE_TYPE_LOCK);
    request.setDeviceVersion(getLockInfoResponse.getVersion());
    request.setDeviceModel(getLockInfoResponse.getModel());
    request.setDeviceSerialNo(getLockInfoResponse.getDeviceId());
    request.setDeviceName(DEFAULT_LOCK_NAME);
    request.setMac(mac);
    request.setBluetoothName(bleName);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    addLockView.showLoading();

    commitAddRouterInfoUseCase.execute(
        EncryptInfoFactory.createEncryptInfo(request), new AppSubscriber<CommitAddRouterInfoResponse>(addLockView) {
          @Override
          protected void doNext(CommitAddRouterInfoResponse response) {
            addLockView.onAddLockSuccess(response);
          }
        });
  }

  void getUsersOfLock(String deviceId) {
    GetUsersOfLockRequest request = new GetUsersOfLockRequest();
    request.setDeviceSerialNo(deviceId);

    getUsersOfLockUseCase.execute(
        EncryptInfoFactory.createEncryptInfo(request), new AppSubscriber<GetUsersOfLockResponse<List<String>>>(addLockView) {

          @Override
          public void onError(Throwable e) {
            super.handleLoginInvalid(e);
          }

          @Override
          protected void doNext(GetUsersOfLockResponse<List<String>> response) {
            List<String> users = response.getUsers();
            StringBuilder hexUserIds = new StringBuilder();
            for (String user : users) {
              int userId;
              try {
                userId = Integer.parseInt(user);
              } catch (NumberFormatException e) {
                e.printStackTrace();
                userId = -1;
              }
              if (userId == -1) continue;
              hexUserIds.append(PrefConstant.getUserIdInHexString(userId));
            }

            System.out.println("hexUserIds = " + hexUserIds);
            addLockView.onGetUsersOfLockSuccess(hexUserIds.toString());

          }
        });
  }

  void lockFactoryReset(String lockId) {
    LockFactoryResetRequest request = new LockFactoryResetRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    addLockView.showLoading();

    lockFactoryResetUseCase.execute(EncryptInfoFactory.createEncryptInfo(request), new AppSubscriber<LockFactoryResetResponse>(addLockView) {
      @Override
      protected void doNext(LockFactoryResetResponse response) {
        addLockView.onFactoryResetSuccess(response);
      }

      @Override
      public void onError(Throwable e) {
        Exception exception = (Exception) handleCompositeException(e);
        if (exception instanceof CloudNoSuchLockException) {
          handleLoginInvalid(e);
          addLockView.onFactoryResetSuccess(null);
        } else {
          super.onError(e);
        }
      }
    });
  }

  String getLockId() {
    return lockId;
  }

  void setLockId(String lockId) {
    this.lockId = lockId;
  }


}
