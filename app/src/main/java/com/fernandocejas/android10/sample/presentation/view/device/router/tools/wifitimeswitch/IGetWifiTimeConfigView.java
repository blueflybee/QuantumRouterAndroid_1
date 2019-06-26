package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wifitimeswitch;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.GetWifiTimeConfigResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface IGetWifiTimeConfigView extends LoadDataView{
    void getWifiTimeConfig(GetWifiTimeConfigResponse<List<GetWifiTimeConfigResponse.WifiTimeConfig>> response);
}
