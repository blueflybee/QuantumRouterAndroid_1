package com.fernandocejas.android10.sample.presentation.view.mine.aboutus;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetAgreementResponse;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc:
 *      version: 1.0
 * </pre>
 */

public interface IGetAgreementView extends LoadDataView {
    void openUserAgreement(GetAgreementResponse response);
    void openUserSerectAgreement(GetAgreementResponse response); //隐私协议
}
