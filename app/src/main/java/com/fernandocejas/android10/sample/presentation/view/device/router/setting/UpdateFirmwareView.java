package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.CheckFirmwareResponse;
import com.qtec.router.model.rsp.GetFirmwareUpdateStatusResponse;
import com.qtec.router.model.rsp.UpdateFirmwareResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface UpdateFirmwareView extends LoadDataView {
  void updateFirmwareSuccess(UpdateFirmwareResponse response);

  void showFirmwareVersion(CheckFirmwareResponse response);

  void showFirmwareUpdateStatus(GetFirmwareUpdateStatusResponse response);

  void showFirmwareUpdateSuccess();

  void showFirmwareUpdateFailed(GetFirmwareUpdateStatusResponse response);
}
