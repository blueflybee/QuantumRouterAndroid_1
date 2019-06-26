package com.fernandocejas.android10.sample.presentation.view.device.router.setting;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.fernandocejas.android10.sample.presentation.view.device.router.data.Router;
import com.qtec.mapp.model.rsp.GetRouterInfoCloudResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RouterInfoView extends LoadDataView{
  void showRouterInfo(Router.BaseInfo baseInfo);

  void showRouterInfoCloud(GetRouterInfoCloudResponse baseInfo);

}
