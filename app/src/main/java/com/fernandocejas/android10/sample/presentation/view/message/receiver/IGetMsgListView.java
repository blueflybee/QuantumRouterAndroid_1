package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetMsgDetailResponse;
import com.qtec.mapp.model.rsp.GetMsgListResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IGetMsgListView extends LoadDataView {
    void getMsgList(List<GetMsgListResponse<GetMsgListResponse.messageContent>> response);
}
