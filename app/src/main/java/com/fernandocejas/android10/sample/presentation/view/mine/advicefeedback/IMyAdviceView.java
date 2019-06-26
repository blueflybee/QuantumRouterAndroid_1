package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetMyAdviceResponse;
import com.qtec.mapp.model.rsp.GetQuestionDetailResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 问题详情
 *      version: 1.0
 * </pre>
 */

public interface IMyAdviceView extends LoadDataView {
    void openMyAdvice(List<GetMyAdviceResponse> response);
}
