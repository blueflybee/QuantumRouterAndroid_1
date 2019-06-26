package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.AddFingerResponse;
import com.qtec.router.model.rsp.ModifyFingerNameResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 修改指纹名称
 *     version: 1.0
 * </pre>
 */
public interface IModifyFingerNameView extends LoadDataView{
    void modifyFingerName(ModifyFingerNameResponse response);
}
