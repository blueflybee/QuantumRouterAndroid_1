package com.fernandocejas.android10.sample.presentation.view.device.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetDevTreeResponse;
import com.qtec.mapp.model.rsp.GetDevTreeResponse.DeviceBean;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface QDevView extends LoadDataView {
  void showDevTree(List<GetDevTreeResponse<List<DeviceBean>>> response);

  void showNoDevice();

  void showNoDeviceGuide();

  void showDeviceGuide();
}
