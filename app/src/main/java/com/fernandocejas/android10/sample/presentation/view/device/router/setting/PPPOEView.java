package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.SetPPPOEResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface PPPOEView extends LoadDataView{
  void showSetPPPOESuccess(SetPPPOEResponse response);
}
