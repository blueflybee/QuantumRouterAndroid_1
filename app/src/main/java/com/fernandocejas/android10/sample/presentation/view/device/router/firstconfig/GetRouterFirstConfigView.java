package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetRouterFirstConfigResponse;
import com.qtec.router.model.rsp.SetDHCPResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface GetRouterFirstConfigView extends LoadDataView{

  void startFirstConfig(GetRouterFirstConfigResponse response);
}
