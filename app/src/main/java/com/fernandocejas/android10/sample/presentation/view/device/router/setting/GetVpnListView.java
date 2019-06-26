package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.AddVpnResponse;
import com.qtec.router.model.rsp.GetVpnListResponse;

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
public interface GetVpnListView extends LoadDataView{
    void getVpnList(GetVpnListResponse<List<GetVpnListResponse.VpnBean>> responses);
}
