package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.QueryLockedCatResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 查询绑定的设备
 *      version: 1.0
 * </pre>
 */

public interface QueryLockedCatView extends LoadDataView {
    void queryLockedCat(QueryLockedCatResponse response);
}
