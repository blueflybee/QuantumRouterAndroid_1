package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.GetRouterGroupsResponse;

import java.util.List;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface RouterGroupView extends LoadDataView{
  void showRouterGroups(List<GetRouterGroupsResponse> response);

  void showModifyRouterGroupSuccess();

  void showCreateRouterGroupSuccess();

}
