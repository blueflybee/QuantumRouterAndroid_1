package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetMsgCountResponse;
import com.qtec.mapp.model.rsp.SetMsgReadResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface ISetMsgReadedView extends LoadDataView {
    void setMsgRead(SetMsgReadResponse response);
}
