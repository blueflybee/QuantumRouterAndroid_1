package com.fernandocejas.android10.sample.presentation.view.device.intelDev.managefinger;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.AddFingerResponse;
import com.qtec.router.model.rsp.QueryFingerInfoResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface IQueryFingerView extends LoadDataView{
    void showFingerInfo(QueryFingerInfoResponse<List<QueryFingerInfoResponse.FingerInfo>> response);
}
