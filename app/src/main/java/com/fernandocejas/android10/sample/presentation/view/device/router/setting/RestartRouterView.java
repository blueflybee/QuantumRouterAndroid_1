package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetTimerTaskResponse;
import com.qtec.router.model.rsp.SetRouterTimerResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface RestartRouterView extends LoadDataView{
  void getTimerTaskSuccess(GetTimerTaskResponse response);

  void setRouterTimerSuccess(SetRouterTimerResponse response);

}
