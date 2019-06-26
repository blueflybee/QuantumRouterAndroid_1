package com.fernandocejas.android10.sample.presentation.view.device.router.tools.childcare;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.RouterStatusResponse;
import com.qtec.router.model.rsp.RouterStatusResponse.Status;

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
interface RouterDeviceListView extends LoadDataView {
  void showRouterStatus(RouterStatusResponse<List<Status>> response);
}
