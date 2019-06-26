package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface LockInjectKeyView extends LoadDataView{
  void onKeyInjectStart();

  void onKeyInjectSuccess(String routerId);

  void onKeyInjectFail();

  void onKeyInjectProgress(int progress);

  void onKeyInjectCancel(String routerId);
}
