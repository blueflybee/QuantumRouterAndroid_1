package com.fernandocejas.android10.sample.presentation.view.device.cateye;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.CommitAddRouterInfoResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface AddCatView extends LoadDataView{
  void onAddCatSuccess(CommitAddRouterInfoResponse response);
  void onAddCatFailed();
}
