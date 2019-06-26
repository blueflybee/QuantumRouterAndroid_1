package com.fernandocejas.android10.sample.presentation.view.device.router.tools.signalregulation;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.PostSignalRegulationResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface IPostSignalInfoView extends LoadDataView{
    void postSignalRelationInfo(PostSignalRegulationResponse response);
}
