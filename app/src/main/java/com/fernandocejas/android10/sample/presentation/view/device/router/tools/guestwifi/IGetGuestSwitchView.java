package com.fernandocejas.android10.sample.presentation.view.device.router.tools.guestwifi;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetGuestWifiSwitchResponse;
import com.qtec.router.model.rsp.GetSignalRegulationResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface IGetGuestSwitchView extends LoadDataView{
    void getGuestWifiSwitch(GetGuestWifiSwitchResponse response);
}
