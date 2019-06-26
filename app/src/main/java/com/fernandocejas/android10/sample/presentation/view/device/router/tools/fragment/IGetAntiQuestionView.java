package com.fernandocejas.android10.sample.presentation.view.device.router.tools.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.GetAntiFritQuestionResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 开启防蹭网
 *     version: 1.0
 * </pre>
 */
public interface IGetAntiQuestionView extends LoadDataView{
    void getAntiFritQuestion(GetAntiFritQuestionResponse response);
}
