package com.fernandocejas.android10.sample.presentation.view.message.receiver;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.DealInvitationResponse;
import com.qtec.mapp.model.rsp.GetMsgDetailResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IDealInvitationView extends LoadDataView {
    void dealInvitation(DealInvitationResponse response);
}
