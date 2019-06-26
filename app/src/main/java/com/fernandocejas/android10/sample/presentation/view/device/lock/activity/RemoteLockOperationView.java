package com.fernandocejas.android10.sample.presentation.view.device.lock.activity;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.lock.model.core.BlePkg;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/09/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface RemoteLockOperationView extends LoadDataView{
  void showRemoteLockOptFailed(String cmdType);

  void showRemoteLockOptSuccess(BlePkg blePkg, String cmdType);

  void onRefreshComplete();

  void onRefreshStart();

  void onRefreshError();
}
