package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.IntelDeviceDetailResponse;
import com.qtec.mapp.model.rsp.UnbindIntelDevResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IGetLockDetailView extends LoadDataView{

    void showLockDeviceDetail(IntelDeviceDetailResponse response);

}
