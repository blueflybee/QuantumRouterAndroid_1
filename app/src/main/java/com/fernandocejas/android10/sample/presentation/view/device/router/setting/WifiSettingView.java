package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.ConfigWifiResponse;
import com.qtec.router.model.rsp.GetWifiConfigResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface WifiSettingView extends LoadDataView{
  void getWifiConfigSuccess(GetWifiConfigResponse response);

  void configWifiSuccess(ConfigWifiResponse response);

}
