package com.fernandocejas.android10.sample.presentation.view.device.router.tools.manageDevice;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.IntelDevInfoModifyResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IntelDevInfoModifyView extends LoadDataView {

    void showDevNameEmp();

    void showModifySuccess(IntelDevInfoModifyResponse response);
}
