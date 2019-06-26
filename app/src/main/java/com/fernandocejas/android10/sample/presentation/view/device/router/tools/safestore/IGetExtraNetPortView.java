package com.fernandocejas.android10.sample.presentation.view.device.router.tools.safestore;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetDeviceCountResponse;
import com.qtec.mapp.model.rsp.GetExtraNetPortResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IGetExtraNetPortView extends LoadDataView {
    void getExtraNetPort(GetExtraNetPortResponse response);
    void getExtraNetPortFailed();
}
