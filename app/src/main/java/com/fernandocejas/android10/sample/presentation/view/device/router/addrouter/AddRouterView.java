package com.fernandocejas.android10.sample.presentation.view.device.router.addrouter;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.router.model.rsp.SearchRouterResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/06/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface AddRouterView extends LoadDataView {
  void showSearchSuccess(SearchRouterResponse response);

  void showSearchFailed(Throwable e);
}
