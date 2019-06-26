package com.fernandocejas.android10.sample.presentation.view.device.router.tools.wirelessrelay;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetConnectedWifiResponse;
import com.qtec.router.model.rsp.GetWirelessListResponse;

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
public interface GetConnectedWifiView extends LoadDataView{
    void getConnectedWifi(GetConnectedWifiResponse response);
}
