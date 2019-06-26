package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.DeleteVpnResponse;
import com.qtec.router.model.rsp.ModifyVpnResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface ModifyVpnView extends LoadDataView{
    void modifyVpn(ModifyVpnResponse response);
}
