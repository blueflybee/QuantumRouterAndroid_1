package com.fernandocejas.android10.sample.presentation.view.safe;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;

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

public interface IGetDeviceTreeListView extends LoadDataView {
    void getRouterList(List<GetDevTreeResponse<List<GetDevTreeResponse.DeviceBean>>> response);
}
