package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.AddFingerResponse;
import com.qtec.router.model.rsp.GetLockStatusResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 添加指纹信息
 *     version: 1.0
 * </pre>
 */
public interface IAddFingerView extends LoadDataView{
    void getFingerInfo(AddFingerResponse response);
}
