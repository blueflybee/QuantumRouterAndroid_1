package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetMemRemarkNameResponse;
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
interface IModifyRemarkNameView extends LoadDataView {
    void showMemRemarkName(GetMemRemarkNameResponse response);
}
