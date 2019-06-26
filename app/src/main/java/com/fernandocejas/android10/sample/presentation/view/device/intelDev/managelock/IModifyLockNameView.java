package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managelock;

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
interface IModifyLockNameView extends LoadDataView {
    void showModifySuccess(IntelDevInfoModifyResponse response);
}
