package com.fernandocejas.android10.sample.presentation.view.device.litegateway.activity;


import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.CheckLiteVersionResponse;
import com.qtec.mapp.model.rsp.GetLiteUpdateResponse;
import com.qtec.mapp.model.rsp.UpdateLiteResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail :
 *     time   : 18-10-9
 *     desc   : UpdateLiteView
 *     version: 1.0
 * </pre>
 */
public interface UpdateLiteView extends LoadDataView {

  void showVersion(CheckLiteVersionResponse response);

  void onUpdate(UpdateLiteResponse response);

  void showUpdateStatus(GetLiteUpdateResponse response);

}