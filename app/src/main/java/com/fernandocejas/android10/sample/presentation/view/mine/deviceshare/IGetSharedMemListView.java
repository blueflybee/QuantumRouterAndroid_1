package com.fernandocejas.android10.sample.presentation.view.mine.deviceshare;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetQuestionListResponse;
import com.qtec.mapp.model.rsp.GetShareMemListResponse;

import java.util.List;

/**
 * <pre>
 *      author: xiehao
 *      e-mail: xieh@qtec.cn
 *      time: 2017/06/12
 *      desc: 共享成员列表
 *      version: 1.0
 * </pre>
 */

public interface IGetSharedMemListView extends LoadDataView {
    void getSharedMemList(List<GetShareMemListResponse> response);
}
