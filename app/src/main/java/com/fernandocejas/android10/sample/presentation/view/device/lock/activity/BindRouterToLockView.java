package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.BindRouterToLockResponse;
import com.qtec.router.model.rsp.QueryBindRouterToLockResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface BindRouterToLockView extends LoadDataView{
  void showNotifyBindSuccess(BindRouterToLockResponse response);

  void onQuery(QueryBindRouterToLockResponse response);
}
