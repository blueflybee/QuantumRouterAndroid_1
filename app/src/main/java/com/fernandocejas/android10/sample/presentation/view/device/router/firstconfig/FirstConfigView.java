package com.fernandocejas.android10.sample.presentation.view.device.router.firstconfig;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.FirstConfigResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface FirstConfigView extends LoadDataView{
  void showFirstConfigSuccess(FirstConfigResponse response);
}
