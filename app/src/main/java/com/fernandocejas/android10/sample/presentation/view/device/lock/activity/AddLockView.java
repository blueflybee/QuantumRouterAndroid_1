package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;
import com.qtec.mapp.model.rsp.LockFactoryResetResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface AddLockView extends LoadDataView{
  void onAddLockSuccess(CommitAddRouterInfoResponse response);

  void onGetUsersOfLockSuccess(String userIds);

  void onFactoryResetSuccess(LockFactoryResetResponse response);
}
