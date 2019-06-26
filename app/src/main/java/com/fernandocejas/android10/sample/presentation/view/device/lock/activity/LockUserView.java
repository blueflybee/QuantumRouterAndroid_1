package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.DeleteLockUserResponse;
import com.qtec.mapp.model.rsp.GetLockUsersResponse;
import com.qtec.mapp.model.rsp.GetUserRoleResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface LockUserView extends LoadDataView{
  void showUserRole(GetUserRoleResponse response);

  void showLockUsers(List<GetLockUsersResponse> response);

  void onDeleteLockUserSuccess(DeleteLockUserResponse response);
}
