package com.fernandocejas.android10.sample.presentation.view.mine.advicefeedback;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetUpdateFeedBackResponse;
import com.qtec.mapp.model.rsp.GetfeedBackResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IUpdateFeedBackView extends LoadDataView {
    void updateFeedback(GetUpdateFeedBackResponse response);
}
