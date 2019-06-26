package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;
import com.qtec.router.model.rsp.FactoryResetResponse;
import com.qtec.router.model.rsp.RestartRouterResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RouterSettingView extends LoadDataView{
  void restartRouterSuccess(RestartRouterResponse response);

  void factoryResetSuccess(FactoryResetResponse response);

  void unbindRouterSuccess(UnbindRouterResponse response, String deviceId);
}
