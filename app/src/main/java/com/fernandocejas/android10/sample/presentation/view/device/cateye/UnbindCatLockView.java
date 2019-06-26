package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.QueryCatLockResponse;
import com.qtec.mapp.model.rsp.UnbindCatLockResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 门锁列表
 *      version: 1.0
 * </pre>
 */

public interface UnbindCatLockView extends LoadDataView {
    void unbindCayLock(UnbindCatLockResponse response);
}
