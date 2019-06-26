package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.EnableAntiFritNetResponse;
import com.qtec.router.model.rsp.GetSambaAccountResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 开启防蹭网
 *     version: 1.0
 * </pre>
 */
public interface IEnableAntiFritNetView extends LoadDataView{
    void getAntiFritNetStatus(EnableAntiFritNetResponse response);
}
