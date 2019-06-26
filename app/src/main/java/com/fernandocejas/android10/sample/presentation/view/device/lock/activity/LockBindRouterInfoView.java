package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.CloudUnbindRouterLockResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;
import com.qtec.router.model.rsp.UnbindRouterToLockResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface LockBindRouterInfoView extends LoadDataView{
  void showNotifyUnbindSuccess(UnbindRouterToLockResponse response);

  void showUnbindFail(QueryBindRouterToLockResponse response);

  void showUnbindSuccess(QueryBindRouterToLockResponse response);

  void showCloudUnbindSuccess(CloudUnbindRouterLockResponse response);
}
