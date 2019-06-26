package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetPassagewayModeResponse;
import com.qtec.mapp.model.rsp.GetUnlockModeResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/12/21
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface PassagewayModeView extends LoadDataView {

  void showPassageway(GetPassagewayModeResponse response);

  void showModifyPassagewaySuccess();

}
