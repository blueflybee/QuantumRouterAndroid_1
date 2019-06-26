package com.fernandocejas.android10.sample.presentation.view.main;

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
public interface AddRouterView_NetChange extends LoadDataView {
  void showSearchSuccess(SearchRouterResponse response);

  void showSearchFailed(Throwable e);
}
