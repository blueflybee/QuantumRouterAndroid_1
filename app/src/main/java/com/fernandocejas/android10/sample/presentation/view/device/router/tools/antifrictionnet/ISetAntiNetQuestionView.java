package com.fernandocejas.android10.sample.presentation.view.device.router.tools.antifrictionnet;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.SetAntiNetQuestionResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   : 设置问题
 *     version: 1.0
 * </pre>
 */
public interface ISetAntiNetQuestionView extends LoadDataView{
    void setAntiNetQuestion(SetAntiNetQuestionResponse response);
}
