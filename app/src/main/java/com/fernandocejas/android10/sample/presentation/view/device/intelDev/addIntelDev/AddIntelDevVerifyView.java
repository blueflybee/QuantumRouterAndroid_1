package com.fernandocejas.android10.sample.presentation.view.device.intelDev.addIntelDev;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.AddIntelDevVerifyResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface AddIntelDevVerifyView extends LoadDataView{
    void showAdminPwdEmp();

    void onAddSuccess(AddIntelDevVerifyResponse response);

}
