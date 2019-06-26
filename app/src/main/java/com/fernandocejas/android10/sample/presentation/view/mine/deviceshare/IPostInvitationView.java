package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetAgreementResponse;
import com.qtec.mapp.model.rsp.PostInvitationResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IPostInvitationView extends LoadDataView {
    void postInvitation(PostInvitationResponse response);
}
