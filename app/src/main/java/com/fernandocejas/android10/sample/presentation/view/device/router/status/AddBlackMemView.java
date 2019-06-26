package com.fernandocejas.android10.sample.presentation.view.device.router.status;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.RemoveBlackMemResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 移除黑名单
 *     version: 1.0
 * </pre>
 */
public interface AddBlackMemView extends LoadDataView{
    void addBlackMem(RemoveBlackMemResponse response);
}
