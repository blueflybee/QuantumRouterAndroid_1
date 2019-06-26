package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.data.constant.PrefConstant;
import com.fernandocejas.android10.sample.domain.constant.CloudUseCaseComm;
import com.fernandocejas.android10.sample.domain.interactor.UseCase;
import com.fernandocejas.android10.sample.presentation.interactor.AppSubscriber;
import com.fernandocejas.android10.sample.presentation.internal.di.PerActivity;
import com.fernandocejas.android10.sample.presentation.presenter.Presenter;
import com.fernandocejas.android10.sample.presentation.utils.EncryptInfoFactory;
import com.qtec.mapp.model.req.DeleteLockUserRequest;
import com.qtec.mapp.model.req.GetLockUsersRequest;
import com.qtec.mapp.model.req.GetUserRoleRequest;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;
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
public class LockUserPresenter implements Presenter {

  private final UseCase getUserRoleUseCase;
  private final UseCase getLockUsersUseCase;
  private final UseCase deleteLockUserUseCase;
  private LockUserView mLockUserView;

  @Inject
  public LockUserPresenter(@Named(CloudUseCaseComm.GET_USER_ROLE) UseCase getUserRoleUseCase,
                           @Named(CloudUseCaseComm.GET_LOCK_USERS)UseCase getLockUsersUseCase,
                           @Named(CloudUseCaseComm.DELETE_LOCK_USER)UseCase deleteLockUserUseCase) {
    this.getUserRoleUseCase = checkNotNull(getUserRoleUseCase, "getUserRoleUseCase cannot be null!");
    this.getLockUsersUseCase = checkNotNull(getLockUsersUseCase, "getLockUsersUseCase cannot be null!");
    this.deleteLockUserUseCase = checkNotNull(deleteLockUserUseCase, "deleteLockUserUseCase cannot be null!");
  }

  @Override
  public void resume() {
  }

  @Override
  public void pause() {

  }

  @Override
  public void destroy() {
    getUserRoleUseCase.unsubscribe();
    getLockUsersUseCase.unsubscribe();
    deleteLockUserUseCase.unsubscribe();
  }

  public void setView(LockUserView lockUserView) {
    this.mLockUserView = lockUserView;
  }


  public void getUserRole(String deviceId) {
    GetUserRoleRequest request = new GetUserRoleRequest();
    request.setDeviceSerialNo(deviceId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockUserView.showLoading();
    getUserRoleUseCase.execute(encryptInfo, new AppSubscriber<GetUserRoleResponse>(mLockUserView) {

      @Override
      protected void doNext(GetUserRoleResponse response) {
        mLockUserView.showUserRole(response);
      }
    });

  }

  public void getLockUsers(String deviceId) {
    GetLockUsersRequest request = new GetLockUsersRequest();
    request.setDeviceSerialNo(deviceId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockUserView.showLoading();
    getLockUsersUseCase.execute(encryptInfo, new AppSubscriber<List<GetLockUsersResponse>>(mLockUserView) {

      @Override
      protected void doNext(List<GetLockUsersResponse> response) {
        mLockUserView.showLockUsers(response);
      }
    });

  }

  void deleteLockUser(String lockId, String userId) {
    DeleteLockUserRequest request = new DeleteLockUserRequest();
    request.setDeviceSerialNo(lockId);
    request.setUserUniqueKey(PrefConstant.getUserUniqueKey("0"));
    request.setGeneralUserUniqueKey(userId);
    QtecEncryptInfo encryptInfo = EncryptInfoFactory.createEncryptInfo(request);

    mLockUserView.showLoading();
    deleteLockUserUseCase.execute(encryptInfo, new AppSubscriber<DeleteLockUserResponse>(mLockUserView) {

      @Override
      protected void doNext(DeleteLockUserResponse response) {
        mLockUserView.onDeleteLockUserSuccess(response);
      }
    });

  }
}
