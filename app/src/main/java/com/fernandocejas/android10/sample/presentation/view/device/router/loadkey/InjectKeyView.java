package com.fernandocejas.android10.sample.presentation.view.device.router.loadkey;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/07/13
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface InjectKeyView extends LoadDataView{
  void onKeyInjectStart();

  void onKeyInjectSuccess(String routerId);

  void onKeyInjectFail();

  void onKeyInjectProgress(int progress);

  void onKeyInjectCancel(String routerId);
}
