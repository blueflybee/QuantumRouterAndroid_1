package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 意见反馈问题列表
 *      version: 1.0
 * </pre>
 */

public interface IGetQuestionListView extends LoadDataView {
    void openQuestionList(List<GetQuestionListResponse> response);
}
