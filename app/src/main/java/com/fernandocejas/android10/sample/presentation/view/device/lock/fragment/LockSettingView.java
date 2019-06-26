package com.fernandocejas.android10.sample.presentation.view.device.lock.fragment;

import com.fernandocejas.android10.sample.presentation.view.LoadDataView;
import com.qtec.mapp.model.rsp.AdjustLockVolumeResponse;
import com.qtec.mapp.model.rsp.UnbindLockOfAdminResponse;
import com.qtec.mapp.model.rsp.UnbindRouterResponse;

/**
 * <pre>
 *     author : shaojun
 *     e-mail : wusj@qtec.cn
 *     time   : 2017/08/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public interface LockSettingView extends LoadDataView{
  void unbindLockSuccess(UnbindRouterResponse response, String mac);

  void unbindLockOfAdminSuccess(UnbindLockOfAdminResponse response, String mac);

  void showLockVolume(String volume);

  void onAdjustLockVolume(AdjustLockVolumeResponse response);
}
