package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.cateye.model.response.GetDoorBellRecordListResponse;
import com.qtec.mapp.model.rsp.GetUnlockInfoListResponse;

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

public interface GetDoorBellRecordListView extends LoadDataView {
    void getDoorBellRecordList(GetDoorBellRecordListResponse<List<GetDoorBellRecordListResponse.MsgList>> response);
}
