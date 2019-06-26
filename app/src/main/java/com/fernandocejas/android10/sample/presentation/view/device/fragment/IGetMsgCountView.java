package com.fernandocejas.android10.sample.presentation.view.device.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface IGetMsgCountView extends LoadDataView {
    void getMsgCount(GetMsgCountResponse response);
}
