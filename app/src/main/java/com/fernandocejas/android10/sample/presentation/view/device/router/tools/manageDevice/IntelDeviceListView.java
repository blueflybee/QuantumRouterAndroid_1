package com.fernandocejas.android10.sample.presentation.view.device.router.tools.manageDevice;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.IntelDeviceListResponse;

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
interface IntelDeviceListView extends LoadDataView {
  void showIntelDeviceList(List<IntelDeviceListResponse> response);

  void showNoLock();

}
